package com.carwoosh.utils;

import java.time.LocalDateTime;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carwoosh.constant.ConstantStrings;



public class SafeFieldUtils {
	private static final Logger logger = LoggerFactory.getLogger(SafeFieldUtils.class);

	@SuppressWarnings("unchecked")
	public static <T> T readField(Object target, String fieldName, boolean forceAccess, Class<T> clazz) {
		Object o = null;
		try {
			if (FieldUtils.getField(target.getClass(), fieldName, forceAccess) != null) {
				o = FieldUtils.readField(target, fieldName, forceAccess);
			}
		} catch (IllegalAccessException e) {
			//change_vishal
			logger.error("Notification :: "+"SafeFieldUtils_readField :: "+"Exception :: "+ ConstantStrings.DTF.format(LocalDateTime.now()) + " :: IllegalAccess access field " + fieldName + " on target " + target.getClass().getName());
	
		} catch (IllegalArgumentException e) {
			
			//change_vishal
			logger.warn("Notification :: "+"SafeFieldUtils_readField :: "+"Exception :: "+ ConstantStrings.DTF.format(LocalDateTime.now()) + " :: IllegalAccess access field " + fieldName + " on target " + target.getClass().getName());
			
		}
		if (o != null && clazz.isAssignableFrom(o.getClass())) {
			return (T) o;
		} else {
			return null;
		}
	}
}
