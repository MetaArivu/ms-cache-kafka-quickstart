/**
 * (C) Copyright 2021 Araf Karsh Hamid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.fusion.air.microservice.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Function;

import io.fusion.air.microservice.server.config.ServiceConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author arafkarsh
 *
 */
@Service
public final class JsonWebToken {
	
	private static String TOKEN = "<([1234567890SecretKey!!To??Encrypt##Data@12345%6790])>";

	public static final long EXPIRE_IN_ONE_MINS 		= 1000 * 60;
	public static final long EXPIRE_IN_FIVE_MINS 	= EXPIRE_IN_ONE_MINS * 5;
	public static final long EXPIRE_IN_TEN_MINS 		= EXPIRE_IN_ONE_MINS * 10;
	public static final long EXPIRE_IN_TWENTY_MINS 	= EXPIRE_IN_ONE_MINS * 20;
	public static final long EXPIRE_IN_THIRTY_MINS 	= EXPIRE_IN_ONE_MINS * 30;
	public static final long EXPIRE_IN_ONE_HOUR 		= EXPIRE_IN_ONE_MINS * 60;

	public static final long EXPIRE_IN_TWO_HOUR 		= EXPIRE_IN_ONE_HOUR * 2;
	public static final long EXPIRE_IN_THREE_HOUR 	= EXPIRE_IN_ONE_HOUR * 3;
	public static final long EXPIRE_IN_FIVE_HOUR 	= EXPIRE_IN_ONE_HOUR * 5;
	public static final long EXPIRE_IN_EIGHT_HOUR 	= EXPIRE_IN_ONE_HOUR * 8;
	public static final long EXPIRE_IN_ONE_DAY 		= EXPIRE_IN_ONE_HOUR * 24;

	public static final long EXPIRE_IN_TWO_DAYS 		= EXPIRE_IN_ONE_DAY * 2;
	public static final long EXPIRE_IN_ONE_WEEK 		= EXPIRE_IN_ONE_DAY * 7;
	public static final long EXPIRE_IN_TWO_WEEKS 	= EXPIRE_IN_ONE_DAY * 14;
	public static final long EXPIRE_IN_ONE_MONTH 	= EXPIRE_IN_ONE_DAY * 30;
	public static final long EXPIRE_IN_THREE_MONTHS	= EXPIRE_IN_ONE_DAY * 90;
	public static final long EXPIRE_IN_SIX_MONTHS 	= EXPIRE_IN_ONE_DAY * 180;
	public static final long EXPIRE_IN_ONE_YEAR 		= EXPIRE_IN_ONE_DAY * 365;

	public static final long EXPIRE_IN_TWO_YEARS 	= EXPIRE_IN_ONE_YEAR * 2;
	public static final long EXPIRE_IN_FIVE_YEARS 	= EXPIRE_IN_ONE_YEAR * 5;
	public static final long EXPIRE_IN_TEN_YEARS 	= EXPIRE_IN_ONE_YEAR * 10;

	@Autowired
	private ServiceConfiguration serviceConfig;

	private Key signingKey;
	private final SignatureAlgorithm algorithm;
	public final static SignatureAlgorithm defaultAlgo = SignatureAlgorithm.HS512;

	private final Map<String, Object> claimsToken;
	private final Map<String, Object> claimsRefreshToken;
	private String issuer;
	private String subject;
	private long tokenExpiry;
	private long tokenRefreshExpiry;

	/**
	 * Initialize the JWT with Default Algorithm
	 */
	public JsonWebToken() {
		this(defaultAlgo);
	}

	/**
	 * Initialize the JWT with the Signature Algorithm
	 * @param _algorithm
	 */
	public JsonWebToken(SignatureAlgorithm _algorithm) {
		algorithm 	= _algorithm;
		signingKey 	= new SecretKeySpec(getTokenKeyBytes(), algorithm.getJcaName());
		claimsToken = new HashMap<String, Object>();
		claimsRefreshToken = new HashMap<String, Object>();
		issuer		= "metarivu";
		subject 	= "jane.doe";
		tokenExpiry	= EXPIRE_IN_FIVE_MINS;
		tokenRefreshExpiry = EXPIRE_IN_TWENTY_MINS;
	}

	/**
	 * Set the Issuer
	 * @param _issuer
	 * @return
	 */
	public JsonWebToken setIssuer(String _issuer) {
		issuer = _issuer;
		return this;
	}

	/**
	 * Set the Subject
	 * @param _subject
	 * @return
	 */
	public JsonWebToken setSubject(String _subject)   {
		subject = _subject;
		return this;
	}

	/**
	 * Set the Token Expiry Time - MUST NOT BE GREATER THAN 30 MINS
	 * IF YES THEN SET EXPIRY TO 5 MINS
	 * @param _time
	 * @return
	 */
	public JsonWebToken setTokenExpiry(long _time)   {
		tokenExpiry = (_time > EXPIRE_IN_THIRTY_MINS) ? EXPIRE_IN_FIVE_MINS : _time;
		return this;
	}

	/**
	 * Set the Token Expiry Time
	 * @param _time
	 * @return
	 */
	public JsonWebToken setTokenRefreshExpiry(long _time)   {
		tokenRefreshExpiry = _time;
		return this;
	}

	/**
	 * Clear & Add All Claims for Token
	 * @param _claims
	 * @return
	 */
	public JsonWebToken addAllTokenClaims(Map<String, Object> _claims) {
		claimsToken.clear();
		claimsToken.putAll(_claims);
		String aud = (serviceConfig != null) ? serviceConfig.getServiceName() : "general";
		claimsToken.putIfAbsent("aud", aud);
		claimsToken.putIfAbsent("jti", UUID.randomUUID().toString());
		claimsToken.putIfAbsent("rol", "User");
		return this;
	}

	/**
	 * Clear & Add All Claims for Refresh Token
	 * @param _claims
	 * @return
	 */
	public JsonWebToken addAllRefreshTokenClaims(Map<String, Object> _claims) {
		claimsRefreshToken.clear();
		claimsRefreshToken.putAll(_claims);
		String aud = (serviceConfig != null) ? serviceConfig.getServiceName() : "general";
		claimsRefreshToken.putIfAbsent("aud", aud);
		claimsRefreshToken.putIfAbsent("jti", UUID.randomUUID().toString());
		claimsRefreshToken.putIfAbsent("rol", "User");
		return this;
	}

	/**
	 * Generate Authorize Bearer Token and Refresh Token
	 * Returns in a HashMap
	 * token = Authorization Token
	 * refresh = Refresh token to re-generate the Authorize Token
	 * API Usage
	 * HashMap<String,String> tokens = new JsonWebToken(SignatureAlgorithm.HS512)
	 * 									.setSubject("user")
	 * 									.setIssuer("company")
	 * 									.setTokenExpiry(JsonWebToken.EXPIRE_IN_FIVE_MINS)
	 * 									.setTokenRefreshExpiry(JsonWebToken.EXPIRE_IN_THIRTY_MINS)
	 * 									.addAllTokenClaims(Map<String,Object> claims)
	 * 									.addAllRefreshTokenClaims(Map<String,Object> claims)
	 * 									generateTokens()
	 * @return
	 */
	public HashMap<String,String>  generateTokens() {
		HashMap<String, String> tokens  = new HashMap<String, String>();
		String tokenAuth 	= generateToken(subject, issuer, tokenExpiry, claimsToken);
		String tokenRefresh = generateToken(subject, issuer, tokenRefreshExpiry, claimsRefreshToken);
		tokens.put("token", tokenAuth);
		tokens.put("refresh", tokenRefresh);
		return tokens;
	}

	/**
	 * Clear All Claims (Token and Refresh Token)
	 * @return
	 */
	public JsonWebToken clearAllClaims()  {
		claimsToken.clear();
		claimsRefreshToken.clear();
		return this;
	}

	/**
	 * Returns the Algorithm
	 * @return
	 */
	public SignatureAlgorithm getAlgorithm() {
		return algorithm;
	}

	/**
	 * Returns the Key
	 * @return
	 */
	public Key getKey() {
		return signingKey;
	}

	/**
	 * Returns Token Key - 
	 * In SpringBooT Context from ServiceConfiguration
	 * Else from Static TOKEN Key
	 * @return
	 */
	private String getTokenKey() {
		return (serviceConfig != null) ? serviceConfig.getTokenKey() : TOKEN;
	}

	/**
	 * Returns the Token Key in Bytes
	 * @return
	 */
	private byte[] getTokenKeyBytes() {
		return HashData.base64Encoder(getTokenKey()).getBytes();
	}

    /**
     * Generate Token for the User
     *  
	 * @param _userId
	 * @param _expiryTime
	 * @return
	 */
    public String generateToken(String _userId, long _expiryTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("aud", "general");
        claims.put("jti", UUID.randomUUID().toString());
        return generateToken(_userId,"metarivu",_expiryTime,claims);
    }

    /**
     * Generate Token with Claims
     *  
     * @param _userId
     * @param _expiryTime
     * @param _claims
     * @return
     */
    public String generateToken(String _userId, long _expiryTime, Map<String, Object> _claims) {
        return generateToken(_userId,"metarivu",_expiryTime, _claims);
    }
    
    /**
     * Generate Token with Claims
     * 
     * @param _userId
     * @param _expiryTime
     * @param _algo
     * @param _claims
     * @return
     */
    public String generateToken(String _userId, String _issuer, long _expiryTime, Map<String, Object> _claims) {
    	long currentTime = System.currentTimeMillis();
        return Jwts.builder()
        		.setClaims(_claims)
        		.setSubject(_userId)
				.setIssuer(_issuer)
        		.setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + _expiryTime))
				.signWith(algorithm, signingKey)
				// .signWith(getKey())
                // .signWith(_algo, HashData.base64Encoder(getTokenKey()))
                .compact();
    }

    /**
     * Validate User Id with Token
     * 
     * @param _userId
     * @param _token
     * @return
     */
    public boolean validateToken(String _userId, String _token) {
        return (!isTokenExpired(_token) &&
        		  getSubjectFromToken(_token).equals(_userId));
    }
    
    /**
     * Returns True if the Token is expired
     * 
     * @param _token
     * @return
     */
    public boolean isTokenExpired(String _token) {
        return getExpiryDateFromToken(_token).before(new Date());
    }
    
	/**
	 * Get the User / Subject from the Token
	 * 
	 * @param _token
	 * @return
	 */
    public String getSubjectFromToken(String _token) {
        return getClaimFromToken(_token, Claims::getSubject);
    }

    /**
     * Get the Expiry Date of the Token
     * 
     * @param _token
     * @return
     */
    public Date getExpiryDateFromToken(String _token) {
        return getClaimFromToken(_token, Claims::getExpiration);
    }
    
    /**
     * Token Should not be used before this Date.
     * 
     * @param _token
     * @return
     */
    public Date getNotBeforeDateFromToken(String _token) {
        return getClaimFromToken(_token, Claims::getNotBefore);
    }
    /**
     * Get the Token Issue Date
     * 
     * @param _token
     * @return
     */
    public Date getIssuedAtFromToken(String _token) {
        return getClaimFromToken(_token, Claims::getIssuedAt);
    }
    
    /**
     * Get the Issuer from the Token
     * 
     * @param _token
     * @return
     */
    public String getIssuerFromToken(String _token) {
        return getClaimFromToken(_token, Claims::getIssuer);
    }
    
    /**
     * Get the Audience from the Token
     * 
     * @param _token
     * @return
     */
    public String getAudienceFromToken(String _token) {
        return getClaimFromToken(_token, Claims::getAudience);
    }

	/**
	 * Returns the User Role
	 * @param token
	 * @return
	 */
	public String getUserRoleFromToken(String token) {
		Claims claims = getAllClaims(token);
		String role = (String) claims.get("role");
		return (role == null) ? "Public" : role;
	}
    
    /**
     * Return Payload as JSON String
     * 
     * @param _token
     * @return
     */
    public String getPayload(String _token) {
    	StringBuilder sb = new StringBuilder();
		Claims claims = getAllClaims(_token);
		int x=1;
		int size=claims.size();
		sb.append("{");
		for(Entry<String, Object> claim : claims.entrySet()) {
			if(claim != null) {
				sb.append("\""+claim.getKey()+"\": \"").append(claim.getValue());
				sb.append("\"");
				if(x<size) {
					sb.append(",");
				}
			}
			x++;
		}
		sb.append("}");
    	return sb.toString();
    }

    /**
     * Get a Claim from the Token based on the Claim Type
     * 
     * @param <T>
     * @param _token
     * @param _claimsResolver
     * @return
     */
    public <T> T getClaimFromToken(String _token, 
    		Function<Claims, T> _claimsResolver) {
        return _claimsResolver.apply(getAllClaims(_token));
    }
    
    /**
     * Return All Claims for the Token
     * 
     * @param _token
     * @return
     */
    public Claims getAllClaims(String _token) {
		return Jwts
        		.parser()
				// .signWith(signingKey)
        		.setSigningKey(signingKey)
				// .signWith(algorithm, signingKey)
				.parseClaimsJws(_token)
        		.getBody();
    }
	/**
	 * Print Token Stats
	 * @param token
	 */
	public static void tokenStats(String token) {
		tokenStats(token, true, true);
	}

    /**
     * Print Token Stats
	 * @param token
	 * @param showClaims
	 */
	public static void tokenStats(String token,  boolean showClaims) {
		tokenStats(token, showClaims, false);
	}

	/**
	 * Print Token Stats
	 * @param token
	 * @param showClaims
	 * @param showPayload
	 */
    public static void tokenStats(String token, boolean showClaims, boolean showPayload) {
		JsonWebToken jwt = new JsonWebToken();
		System.out.println("-------------- aaa.bbb.ccc -------------------");
		System.out.println(token);
		System.out.println("-------------- ----------- -------------------");
		System.out.println("Subject  = "+jwt.getSubjectFromToken(token));
		System.out.println("Audience = "+jwt.getAudienceFromToken(token));
		System.out.println("Issuer   = "+jwt.getIssuerFromToken(token));
		System.out.println("IssuedAt = "+jwt.getIssuedAtFromToken(token));
		System.out.println("Expiry   = "+jwt.getExpiryDateFromToken(token));
		System.out.println("Expired  = "+jwt.isTokenExpired(token));
		System.out.println("----------------------------------------------");
		if(showClaims) {
			Claims claims = jwt.getAllClaims(token);
			int x = 1;
			for (Entry<String, Object> o : claims.entrySet()) {
				System.out.println(x + "> " + o);
				x++;
			}
		}
		if(showPayload) {
			System.out.println("----------------------------------------------");
			System.out.println("Payload=" + jwt.getPayload(token));
			System.out.println("----------------------------------------------");
		}

    }
    
    /**
     * Returns the Expiry in Days
     * 
     * @param _time
     * @return
     */
    public static double getDays(long _time) {
    	return _time / (1000 * 60 * 60 * 24);
    }

	/**
	 * Returns the Expiry in Hours
	 * @param _time
	 * @return
	 */
	public static double getHours(long _time) {
		return _time / (1000 * 60 * 60);
	}

	/**
	 * Returns the Expiry in Minutes
	 * @param _time
	 * @return
	 */
	public static double getMins(long _time) {
		return _time / (1000 * 60);
	}

	/**
	 * Returns Token Claims Set for Generating the Token
	 * @return
	 */
	public Map<String, Object> getClaimsToken() {
		return claimsToken;
	}

	/**
	 * Returns the Token Claims set for Generating the Refresh Token
	 * @return
	 */
	public Map<String, Object> getClaimsRefreshToken() {
		return claimsRefreshToken;
	}

	/**
	 * Get the Issuer Set for Generating Token / Refresher Token
	 * @return
	 */
	public String getIssuer() {
		return issuer;
	}

	/**
	 * Get the Subject Set for Generating Token / Refresher Token
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Only for Testing from Command Line
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Default Algo is HS512 = Hmac with SHA-512
		JsonWebToken jwt = new JsonWebToken(SignatureAlgorithm.HS512);

		Map<String, Object> claims = new HashMap<>();
		claims.put("aud", "microservices");
		claims.put("jti", UUID.randomUUID().toString());
		claims.put("did", "device id");
		claims.put("rol", "user");
		String subject	 = "jane.doe";
		long expiry		 = JsonWebToken.EXPIRE_IN_ONE_HOUR;

		String token1	 = jwt.generateToken(subject, "companyName", expiry, claims);
		System.out.println("Expiry Time in Days:Hours:Mins "+getDays(expiry) +":"+getHours(expiry)+":"+getMins(expiry));
		tokenStats(token1);
		if(jwt.validateToken(subject, token1)) {
			System.out.println(">>> Token is Valid");
		}
	}
}
