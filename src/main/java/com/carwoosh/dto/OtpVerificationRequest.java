package com.carwoosh.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OtpVerificationRequest {
	private String txnId;
	private String otp;
}
