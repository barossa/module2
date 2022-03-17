package com.epam.esm.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.esm.dto.UserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JwtUtils {

    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";

    private static final int ACCESS_TOKEN_LIVE_TIME = 10;
    private static final int REFRESH_TOKEN_LIVE_TIME = 60;

    private static final String TOKEN_REGEX = "((?<=^Bearer )((?:\\.?(?:[A-Za-z0-9-_]+)){3}))$";

    private static final String SECRET = "qwerty_";

    private static final String ROLES_KEY = "roles";
    private static final String USER_ID_KEY = "user_id";

    private static final Pattern tokenPattern = Pattern.compile(TOKEN_REGEX);

    private JwtUtils() {
    }

    public static String matchToken(String header) {
        String token = null;
        if (header != null) {
            Matcher matcher = tokenPattern.matcher(header);
            if (matcher.find()) {
                token = header.substring(matcher.start(), matcher.end());
            }
        }
        return token;
    }

    public static String buildAccessToken(UserDto user) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_LIVE_TIME * 60 * 1000))
                .withClaim(USER_ID_KEY, user.getId())
                .withClaim(ROLES_KEY, new ArrayList<>(user.getRoles()))
                .sign(algorithm);
    }

    public static String buildRefreshToken(UserDto user) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_LIVE_TIME * 60 * 1000))
                .withClaim(USER_ID_KEY, user.getId())
                .sign(algorithm);
    }

    public static Map<String, String> buildTokens(UserDto user) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put(ACCESS_TOKEN_KEY, buildAccessToken(user));
        tokens.put(REFRESH_TOKEN_KEY, buildRefreshToken(user));
        return tokens;
    }

    public static JWTVerifier getVerifier() {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        return JWT.require(algorithm).build();
    }

    public static UsernamePasswordAuthenticationToken parseJwt(DecodedJWT token) {
        String username = token.getSubject();
        Integer userId = token.getClaim(USER_ID_KEY).as(Integer.class);
        Claim claim = token.getClaim(ROLES_KEY);
        List<String> roles = new ArrayList<>();
        if (!claim.isNull()) {
            roles = claim.asList(String.class);
        }
        UserDto user = new UserDto(userId, username, null, new HashSet<>(roles));
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }
}
