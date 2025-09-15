package com.carwoosh.controller;

import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.carwoosh.base.BaseResponse;
import com.carwoosh.constant.APPServiceCode;
import com.carwoosh.dto.ForgotPasswordDto;
import com.carwoosh.dto.OtpVerificationRequest;
import com.carwoosh.dto.ResetPasswordRequest;
import com.carwoosh.dto.UserDTO;
import com.carwoosh.dto.UserLoginDTO;
import com.carwoosh.services.UserService;
import com.carwoosh.utils.SSLUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

	private final static String AUTHENTICATE = "/authenticate";
	private final static String FORGOT_PASSWORD = "/forgot-password";
	private final static String REGISTER = "/register";
	private final static String GET_USER_DETAILS = "/user-details";
	private final static String RESET_PASSWORD = "/reset-password";
	private final static String VERIFY_OTP = "/verify-otp";
	private final static String UPLOAD_PROFILE = "/upload-profile";
	private final static String UPDATE_PROFILE = "/update-profile";
	private final static String DELETE_PROFILE = "/delete-profile";
	final static Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(value = REGISTER, method = RequestMethod.POST)
	public BaseResponse<Object> createUser(@RequestBody UserDTO userDTO) {
		BaseResponse<Object> response = new BaseResponse<>();
		APPServiceCode serviceCode = userService.registerNewUser(userDTO);
		response.setMessage(serviceCode.getStatusDesc());
		response.setStatusCode(serviceCode.getStatusCode());
		return response;
	}

	@RequestMapping(value = AUTHENTICATE, method = { RequestMethod.POST })
	public BaseResponse<Object> authenticate(@RequestHeader Map httpHeaders, @RequestBody @Valid UserLoginDTO request,
			BindingResult inBindingResult) {
		APPServiceCode code = null;
		BaseResponse<Object> result = new BaseResponse<Object>();
		try {
			if (inBindingResult.hasErrors()) {
				FieldError fieldError = inBindingResult.getFieldErrors().get(0);
				logger.info("Error Code:" + fieldError.getDefaultMessage());
				code = APPServiceCode.valueOf(fieldError.getDefaultMessage());
				result.setStatusCode(code.getStatusCode());
				result.setMessage(code.getStatusDesc());
			} else {
				result = userService.loginUser(request);
				logger.info(
						"INSIDE:::_USER_CONTROLLER -- " + request.getPassword() + "   --   " + request.getUsername());
			}
		} catch (Exception e) {
			logger.error("Error in deactivate project controller", e);
			code = APPServiceCode.APP_999;
			result.setStatusCode(code.getStatusCode());
			result.setMessage(code.getStatusDesc());
		}
		return result;
	}

	@RequestMapping(value = FORGOT_PASSWORD, method = { RequestMethod.POST })
	public BaseResponse<Object> forgotPassword(@RequestHeader Map httpHeaders,
			@RequestBody @Valid ForgotPasswordDto request, BindingResult inBindingResult) {
		APPServiceCode code = null;
		BaseResponse<Object> result = new BaseResponse<Object>();
		SSLUtil.disableSSLCertificateChecking();
		try {
			if (inBindingResult.hasErrors()) {
				FieldError fieldError = inBindingResult.getFieldErrors().get(0);
				logger.info("Error Code:" + fieldError.getDefaultMessage());
				code = APPServiceCode.valueOf(fieldError.getDefaultMessage());
				result.setStatusCode(code.getStatusCode());
				result.setMessage(code.getStatusDesc());
			} else {
				result = userService.forgotPassword(request);
			}
		} catch (Exception e) {
			logger.error("Error in deactivate project controller", e);
			code = APPServiceCode.APP_999;
			result.setStatusCode(code.getStatusCode());
			result.setMessage(code.getStatusDesc());
		}
		return result;
	}

	@RequestMapping(value = VERIFY_OTP, method = { RequestMethod.POST })
	public BaseResponse<Object> verifyOtp(@RequestBody OtpVerificationRequest request) {
		return userService.verifyOtp(request);
	}

	@RequestMapping(value = RESET_PASSWORD, method = { RequestMethod.POST })
	public BaseResponse<Object> resetPassword(@RequestBody ResetPasswordRequest request) {
		return userService.resetPassword(request);
	}

	@RequestMapping(value = GET_USER_DETAILS, method = { RequestMethod.POST })
	public BaseResponse<Object> getUser(HttpServletRequest request) {
		logger.info("INSIDE CONTROLLER >> GET USER DETAILS");
		BaseResponse<Object> result = new BaseResponse<Object>();
		APPServiceCode code = null;

		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			code = APPServiceCode.APP_009;
		}

		try {
			String token = authHeader.substring(7);
			BaseResponse<Object> response = userService.getUserDetails(token);

			if (response == null || response.getData() == null) {
				code = APPServiceCode.APP_910;
			}

			result = userService.getUserDetails(token);
			code = APPServiceCode.APP_001;
			result.setMessage(code.getStatusDesc());
			result.setStatusCode(code.getStatusCode());

		} catch (Exception e) {
			logger.error("Error fetching user details", e);
			code = APPServiceCode.APP_999;
		}

		if (result == null) {
			code = APPServiceCode.APP_910;
		}

		return result;
	}

	@RequestMapping(value = UPLOAD_PROFILE, method = { RequestMethod.POST }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BaseResponse<Object> uploadProfile(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
		APPServiceCode code = null;
		BaseResponse<Object> result = new BaseResponse<Object>();
		try {

			result = userService.uploadProfilePicture(request, file);

		} catch (Exception e) {
			logger.error("Error in deactivate project controller", e);
			code = APPServiceCode.APP_999;
			result.setStatusCode(code.getStatusCode());
			result.setMessage(code.getStatusDesc());
		}
		return result;
	}
	
	@RequestMapping(value = UPDATE_PROFILE, method = { RequestMethod.POST }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BaseResponse<Object> updateProfile(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
		APPServiceCode code = null;
		BaseResponse<Object> result = new BaseResponse<Object>();
		try {

			result = userService.updateProfilePicture(request, file);

		} catch (Exception e) {
			logger.error("Error in deactivate project controller", e);
			code = APPServiceCode.APP_999;
			result.setStatusCode(code.getStatusCode());
			result.setMessage(code.getStatusDesc());
		}
		return result;
	}

	@RequestMapping(value = DELETE_PROFILE, method = { RequestMethod.POST })
	public BaseResponse<Object> updateProfile(HttpServletRequest request) {
		APPServiceCode code = null;
		BaseResponse<Object> result = new BaseResponse<Object>();
		try {

			code = userService.deleteProfilePic(request);

		} catch (Exception e) {
			logger.error("Error in deactivate project controller", e);
			code = APPServiceCode.APP_999;}
		finally {
			result.setStatusCode(code.getStatusCode());
			result.setMessage(code.getStatusDesc());
		}
		return result;
	}
}

@Getter
class AuthRequest {
	private String username;
	private String password;
}

@Getter
@Setter
class AuthResponse {
	private String token;

	public AuthResponse(String token) {
		this.token = token;
	}
}
