
package com.icommerce.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
    /**
     * Storing all valid tokens which are expired manual by user operation.
     */
    // TODO change to use distributed cache (Hazelcast) when need to support clustering.
    private final Set<String> blackList = Collections.synchronizedSet(new HashSet<>());

    /**
     * Define expired time (in minutes) for a JWT token.
     */
    @Value("${security.jwt.validity-time}")
    private long jwtValidityTime;

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.refresh-time}")
    private long refreshExpireTime;

    /**
     * Extract username from jwt token.
     *
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extract expiration date from jwt token
     *
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Extract all {@link Claims} from input token.
     *
     * @param token
     * @param claimsResolver
     * @param <T>
     * @return
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generate token for input user.
     *
     * @param username
     * @return jwt access token.
     */
    public String generateAccessToken(String username, Map<String, Object> claims) {
        logDebug("[Security] Generate new token for {}", username);
        if (claims == null) {
            claims = new HashMap<>();
        }
        claims.put("scope", "access");
        return doGenerateToken(claims, username, jwtValidityTime);
    }

    /**
     * Generate token for input user.
     *
     * @param username
     * @return jwt refresh token.
     */
    public String generateRefreshToken(String username) {
        logDebug("[Security] Generate new refresh token for {}", username);
        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", "refresh");
        return doGenerateToken(claims, username, refreshExpireTime);
    }

    /**
     * Determine whether or not input token is valid.
     * Note: a valid token can return false in validation if it is set into black list.
     *
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return !blackList.contains(token)
                && username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    /**
     * Expired a valid token by setting it into black list.
     *
     * @param token
     */
    public void expiredToken(String token) {
        final String username = getUsernameFromToken(token);

        logDebug("[Security] add {}'s token {} into black list", username, truncateToken(token));
        blackList.add(token);
    }

    @Scheduled(fixedDelayString = "${security.jwt.validity-time}", initialDelay = 1000)
    public void cleanupBlackList() {
        int size = blackList.size();
        LOGGER.info("[Security] black list size: {}", size);
        Iterator<String> items = blackList.iterator();
        while (items.hasNext()) {
            String token = items.next();
            if (isTokenExpired(token)) {
                items.remove();
            }
        }
        LOGGER.info("[Security] removed items: {}", (size - blackList.size()));
    }


    private String doGenerateToken(Map<String, Object> claims, String subject, Long validTime) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (validTime)))
                // Use HMAC-SHA-512
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        //for retrieveing any information from token we will need the secret key
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Determine whether or not input token is expired
     *
     * @param token
     * @return true if token is expired, otherwise false.
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String truncateToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException("Token must not be null!");
        }
        return token.substring(0, 10);
    }

    private void logDebug(String message, Object... params) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(message, params);
        }
    }
}
