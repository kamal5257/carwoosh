package com.carwoosh.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.carwoosh.entity.AuthUserTokens;
import com.carwoosh.repository.TokenRepository;
import com.carwoosh.services.MyUserDetailService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private MyUserDetailService myUserDetailService;
	
	@Autowired
	private JwtUtil jwtUtilToken;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain fltr)
			throws ServletException, IOException {
		// get jwt
		// Bearer
		//validate
		
		String requestTokenHeader = request.getHeader("Authorization");
		String username=null;
		String jwtToken=null;
		
		if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer "))
		{
			jwtToken= requestTokenHeader.substring(7);
			
			AuthUserTokens userTokens = this.tokenRepository.getByToken(jwtToken);
			try {
					if(userTokens!=null)
					{
						username = this.jwtUtilToken.extractUsername(jwtToken);
					}
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
			System.out.println("JWT ---->> FILTER  --->>> "+request.getHeader("Authorization")+"\n USRNAMEE------>>>>>  "+username);
			
			UserDetails userDetails = this.myUserDetailService.loadUserByUsername(username);
			logger.error("INSIDE SERVICE -->> UPLOAD PIC" + userDetails);
			//security
			if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
			{
				UsernamePasswordAuthenticationToken userNamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				userNamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(userNamePasswordAuthenticationToken);
			}
			else
			{
				System.out.println("Token is not validated...");
			}
		}
		else
		{
			
		}
		
		fltr.doFilter(request, response);
	}

}
