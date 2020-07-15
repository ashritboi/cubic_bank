package com.rab3tech.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {
	
	public static String genRandomAlphaNum() {
		// pseudo code
		char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder((100000 + rnd.nextInt(900000)) + "-");
		for (int i = 0; i < 5; i++)
		    sb.append(chars[rnd.nextInt(chars.length)]);
		return sb.toString();
	}
	

	public static String generateCustomerAccount() {
	    Random r = new Random(System.currentTimeMillis());
	    return "00"+(1000000000 + r.nextInt(2000000000))+"";
	}

	public static void main(String[] args) {
		System.out.println(generateCustomerAccount());
	}


	public static void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

	public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
