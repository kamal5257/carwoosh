package com.carwoosh.services;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.carwoosh.base.BaseResponse;
import com.carwoosh.constant.APPServiceCode;
import com.carwoosh.dto.ForgotPasswordDto;
import com.carwoosh.dto.OtpVerificationRequest;
import com.carwoosh.dto.ResetPasswordRequest;
import com.carwoosh.dto.UserDTO;
import com.carwoosh.dto.UserLoginDTO;
import com.carwoosh.entity.AuthUserTokens;
import com.carwoosh.entity.OtpHistory;
import com.carwoosh.entity.User;
import com.carwoosh.entity.Vehicle;
import com.carwoosh.model.AuthenticationResponseModel;
import com.carwoosh.model.EmailDetails;
import com.carwoosh.repository.OtpHistoryRepo;
import com.carwoosh.repository.TokenRepository;
import com.carwoosh.repository.UserRepository;
import com.carwoosh.utils.AppEncryptionUtil;
import com.carwoosh.utils.EmailUtil;
import com.carwoosh.utils.JwtUtil;
import com.carwoosh.utils.StringUtils;
import com.carwoosh.utils.Validation;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {
	final static Logger logger = LogManager.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailService userDetailService;

	@Autowired
	private TokenRepository tokenRepository;
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	EmailUtil emailUtil;

	@Autowired
	OtpHistoryRepo otpHistoryRepo;
	@Autowired
	Validation validation;

	private final String UPLOAD_DIR = "E://ReactJS Projects/carwoosh2/public/images/profile/";

	public BaseResponse<Object> getUserDetails(String token) {
		System.out.println("INSIDE --- GET USER METHODD");
		BaseResponse<Object> response = new BaseResponse<>();
		String username = jwtUtil.extractUsername(token);
		User userOpt = userRepository.findByUserName(username);
		if (userOpt == null) {
			throw new RuntimeException("Invalid token or user not found.");
		}

		// ✅ 2. Fetch user from DB
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

		// ✅ 3. Prepare response
		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put("id", userOpt.getId());
		userDetails.put("name", userOpt.getFullName());
		userDetails.put("email", userOpt.getEmail());
		userDetails.put("role", userOpt.getRole().name());
		userDetails.put("profilePic", userOpt.getProfileImageUrl());
		userDetails.put("mobileNumber", userOpt.getMobileNumber());
		
		// ✅ Build absolute image URL
	    if (userOpt.getProfileImageUrl() != null && userOpt.getProfileImageUrl().startsWith("/uploads")) {
	        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
	        userOpt.setProfileImageUrl(baseUrl + userOpt.getProfileImageUrl());
	    }

		// ✅ Include vehicle info (if available)
		List<Vehicle> vehicles = userOpt.getVehicles();
		userDetails.put("vehicles", vehicles);
		response.setData(userDetails);
		return response;
	}

	public APPServiceCode registerNewUser(UserDTO userDTO) {
		APPServiceCode serviceCode = null;

		try {
			serviceCode = validation.validateUserDto(userDTO);
			if (!StringUtils.isValidObj(serviceCode)) {

				// Create a new user
				User user = new User();
				System.out.println("USERRRRR DTTOOO : " + userDTO);
				user.setEmail(userDTO.getEmail());
				user.setUsername(userDTO.getEmail());
				user.setFullName(userDTO.getFullName());
				user.setMobileNumber(userDTO.getMobileNo());
//				user.setRole();
				user.setPassword(AppEncryptionUtil.encrypt(userDTO.getPassword()));
				userRepository.save(user);
				serviceCode = APPServiceCode.APP_001;
			}
		} catch (Exception e) {
			System.out.println("CHEKKKK " + e);
			serviceCode = APPServiceCode.APP_999;
		}
		return serviceCode;

	}

	public BaseResponse<Object> loginUser(UserLoginDTO request) {
		{
			APPServiceCode code = APPServiceCode.APP_998;
			BaseResponse<Object> response = new BaseResponse<Object>();
			try {
				System.out.println("INSIDE LOGIN USER");
				byte[] decoded = Base64.decodeBase64(request.getPassword());
				String reqPwd = new String(decoded, StandardCharsets.UTF_8);
				System.out.println("INSIDE LOGIN USER " + reqPwd + " ----   " + request.getUsername() + "---- "
						+ AppEncryptionUtil.encrypt(reqPwd));
				this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
						AppEncryptionUtil.encrypt(reqPwd)));
				final UserDetails userDetails = userDetailService.loadUserByUsername(request.getUsername());
				System.out.println("INSIDE LOGIN USER" + userDetails.getUsername());
				if (!StringUtils.isValidObj(userDetails)) {
					code = APPServiceCode.APP_998;
					System.out.println("ROOHHH");
				}
				final AuthenticationResponseModel jwt = jwtUtil.generateToken(userDetails);
				System.out.println("INSIDE LOGIN USER " + jwt);
				if (jwt != null) {
					AuthUserTokens userToken = new AuthUserTokens();
					userToken.setUserId(request.getUsername());
					userToken.setToken(jwt.getAccessToken());
					tokenRepository.save(userToken);
					List<Object> responseObject = new ArrayList<>();
//					responseObject.add(jwt);
					Map<String, Object> jwtObj = new HashMap<>();
					jwtObj.put("token", jwt);
					response.setData(jwtObj);
				}
				code = APPServiceCode.APP_001;
			} catch (Exception e) {
				logger.error(e.getCause(), e);
			} finally {
				response.setStatusCode(code.getStatusCode());
				response.setMessage(code.getStatusDesc());
			}
			return response;
		}
	}

	public BaseResponse<Object> forgotPassword(ForgotPasswordDto request) {
		BaseResponse<Object> response = new BaseResponse<Object>();
		APPServiceCode code = APPServiceCode.APP_999;
		try {
			User user = userRepository.findByUserName(request.getUsername());
			System.out.println("USER_DETAILS :::: " + user + " ___ " + request.getUsername());
			if (StringUtils.isValidObj(user)) {
				int otp = new Random().nextInt(900000) + 100000;
				int leftLimit = 48; // numeral '0'
				int rightLimit = 122; // letter 'z'
				int targetStringLength = 20;
				Random random = new Random();
				String txnId = random.ints(leftLimit, rightLimit + 1)
						.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
						.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
				logger.info(otp + " : " + txnId);
				OtpHistory otpHistory = new OtpHistory();
				otpHistory.setOtp(otp + "");
				otpHistory.setTxnId(txnId);
				otpHistory.setReceiverDetail(user.getEmail());
				otpHistory = otpHistoryRepo.save(otpHistory);
				if (StringUtils.isValidObj(otpHistory.getHistoryId()) && otpHistory.getHistoryId() > 0) {
					EmailDetails detail = new EmailDetails();
					detail.setMsgBody("Your OTP is: " + otp);
					detail.setSubject("Forgot Password");
					detail.setRecipient(user.getEmail());
					System.out.println("EMAILLL ::: \n\n " + detail);
					Boolean email = emailUtil.sendSimpleMail(detail);
					if (email) {
						response.setTxnId(txnId);
						code = APPServiceCode.APP_001;
					}
				}
			} else {
				code = APPServiceCode.APP_908;
			}
		} catch (Exception e) {
			logger.error(e.getCause(), e);
		} finally {
			response.setStatusCode(code.getStatusCode());
			response.setMessage(code.getStatusDesc());
		}
		return response;
	}

	// 1. Verify OTP
	public BaseResponse<Object> verifyOtp(OtpVerificationRequest request) {
		BaseResponse<Object> response = new BaseResponse<>();
		APPServiceCode code = APPServiceCode.APP_999;

		OtpHistory record = otpHistoryRepo.findByTxnIdAndOtp(request.getTxnId(), request.getOtp());
		if (record != null) {
			// (Optional) Check OTP expiry
			code = APPServiceCode.APP_001;
			response.setTxnId(record.getTxnId());
		} else {
			code = APPServiceCode.APP_904; // Invalid OTP
		}

		response.setStatusCode(code.getStatusCode());
		response.setMessage(code.getStatusDesc());

		return response;
	}

	// 2. Reset Password after OTP verification
	public BaseResponse<Object> resetPassword(ResetPasswordRequest request) {
		BaseResponse<Object> response = new BaseResponse<>();
		APPServiceCode code = APPServiceCode.APP_999;

		OtpHistory record = otpHistoryRepo.findByTxnId(request.getTxnId());
		if (record != null) {
			User user = userRepository.findByUserName(request.getUsername());
			System.out.println("RESTTTTT ----  " + request.getNewPassword() + " -- " + request.getUsername());
			if (user != null && user.getEmail().equalsIgnoreCase(record.getReceiverDetail())) {
				user.setPassword(AppEncryptionUtil.encrypt(request.getNewPassword()));
				userRepository.save(user);
				code = APPServiceCode.APP_001;
			} else {
				code = APPServiceCode.APP_908;
			}
		} else {
			code = APPServiceCode.APP_904;
		}

		response.setStatusCode(code.getStatusCode());
		response.setMessage(code.getStatusDesc());
		return response;
	}

	public BaseResponse<Object> getUserFromToken(String token) {
		BaseResponse<Object> response = new BaseResponse<>();
		String username = jwtUtil.extractUsername(token);
		User userOpt = userRepository.findByUserName(username);

		if (!StringUtils.isValidObj(userOpt))
			return null;
		userOpt.setPassword(null);
		UserDetails userDetails = userDetailService.loadUserByUsername(username);
		if (!jwtUtil.validateToken(token, userDetails))
			return null;

		List<Object> userData = new ArrayList<>();
		userData.add(userOpt);
		Map<String, Object> userMap = new HashMap<>();
		userMap.put("user", userOpt);
		userMap.put("org", null);
		userMap.put("projects", null);
		userMap.put("invitation", null);
		userMap.put("plan", null);
		response.setData(userMap);
		return response;
	}

	public BaseResponse<Object> uploadProfilePicture(HttpServletRequest request, MultipartFile file) throws IOException {
	    logger.info("INSIDE SERVICE -->> UPLOAD PIC");
	    BaseResponse<Object> response = new BaseResponse<>();

	    String authHeader = request.getHeader("Authorization");
	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        response.setStatusCode("APP_909");
	        response.setMessage("Missing or invalid token");
	        return response;
	    }

	    String token = authHeader.substring(7);
	    String username = jwtUtil.extractUsername(token);
	    User user = userRepository.findByUserName(username);

	    if (file.isEmpty()) {
	        response.setStatusCode("APP_902");
	        response.setMessage("File is empty");
	        return response;
	    }

	    // ✅ Create uploads folder inside project root
	    String uploadDirPath = "uploads";
	    File uploadDir = new File(uploadDirPath);
	    if (!uploadDir.exists()) uploadDir.mkdirs();

	    // ✅ Preserve extension (important for browser display)
	    String originalFileName = file.getOriginalFilename();
	    String extension = "";
	    if (originalFileName != null && originalFileName.contains(".")) {
	        extension = originalFileName.substring(originalFileName.lastIndexOf("."));
	    }

	    String fileName = "user_" + user.getId() + "_" + System.currentTimeMillis() + extension;
	    Path filePath = Paths.get(uploadDirPath, fileName);

	    // ✅ Save file
	    Files.write(filePath, file.getBytes());

	    // ✅ Save relative URL
	    String relativeUrl = "/uploads/" + fileName;
	    user.setProfileImageUrl(relativeUrl);
	    userRepository.save(user);

	    response.setStatusCode(APPServiceCode.APP_001.getStatusCode());
	    response.setMessage(APPServiceCode.APP_001.getStatusDesc());	    

	    return response;
	}



	public BaseResponse<Object> updateProfilePicture(HttpServletRequest request, MultipartFile file)
			throws IOException {
		BaseResponse<Object> response = new BaseResponse<>();
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			response.setStatusCode("APP_909");
			response.setMessage("Missing or invalid token");
		}

		String token = authHeader.substring(7);
		String username = jwtUtil.extractUsername(token);

		User user = userRepository.findByUserName(username);

		if (user.getProfileImageUrl() != null) {
			File oldFile = new File(user.getProfileImageUrl());
			if (oldFile.exists())
				oldFile.delete();
		}

		return uploadProfilePicture(request, file);
	}

	public APPServiceCode deleteProfilePic(HttpServletRequest request) {
		APPServiceCode serviceCode = null;
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			serviceCode = APPServiceCode.APP_909;
		}

		String token = authHeader.substring(7);
		String username = jwtUtil.extractUsername(token);

		User user = userRepository.findByUserName(username);

		if (user.getProfileImageUrl() != null) {
			File oldFile = new File(user.getProfileImageUrl());
			if (oldFile.exists())
				oldFile.delete();
			user.setProfileImageUrl(null);
			userRepository.save(user);
			serviceCode = APPServiceCode.APP_001;
		}

		return serviceCode;
	}

}
