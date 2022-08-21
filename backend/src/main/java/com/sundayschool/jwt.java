package com.sundayschool;

import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class jwt {
	
	public String createToken() {
		
		try {
			HashMap<String, String> myMap = new HashMap<String, String>() {{
			    put("userID", "lakshya");
			    put("role", "admin");
			}};
		    Algorithm algorithm = Algorithm.HMAC256("secret");
		    String token = JWT.create()
		        .withIssuer("auth0")
		        .withPayload(myMap)
		        .sign(algorithm);
		    return token;
		} catch (JWTCreationException e){
		    e.printStackTrace();
		}
		return null;
	}
	
	public void verifyToken(String token) {
		
		
		try {
			String key = "ba52e2f2632ab31e115f806448ac3e5c5b307324eebbd141e1979dcc06c4d7328cb252618aac6aed6bfd581d8846730854545bdea007d1b76b5b6ef4a60ccd3f";
//			String key = "secret";
		    Algorithm algorithm = Algorithm.HMAC256(key); //use more secure key
		    JWTVerifier verifier = JWT.require(algorithm)
//		        .withIssuer("auth0")
		        .build(); //Reusable verifier instance
		    DecodedJWT jwt = verifier.verify(token);
		    System.out.println(jwt.getClaims().get("userId"));
		    
		} catch (JWTVerificationException exception){
		    exception.printStackTrace();
		    throw exception;
		}
		
	}

}
