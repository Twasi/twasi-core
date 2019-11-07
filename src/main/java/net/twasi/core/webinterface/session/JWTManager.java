package net.twasi.core.webinterface.session;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import net.twasi.core.database.models.User;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.config.ConfigService;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Date;

public class JWTManager {

    public String createNewToken(User user) {
        String token = null;

        user = ServiceRegistry.get(DataService.class).get(UserRepository.class).getByTwitchId(user.getTwitchAccount().getTwitchId());

        try {
            String secret;
            if (user.getJWTSecret() == null) {
                secret = generateNewSecret();
                user.setJWTSecret(secret);
                ServiceRegistry.get(DataService.class).get(UserRepository.class).commit(user);
            } else {
                secret = user.getJWTSecret();
            }

            Algorithm algorithm = Algorithm.HMAC256(secret);

            Date expiration = new Date();
            expiration.setTime(expiration.getTime() + 1000 * 60 * 60 * 24);

            token = JWT.create()
                    .withIssuer(ServiceRegistry.get(ConfigService.class).getCatalog().auth.issuer)
                    .withIssuedAt(new Date())
                    .withExpiresAt(expiration)
                    .withClaim("name", user.getTwitchAccount().getUserName())
                    .withClaim("twitchid", user.getTwitchAccount().getTwitchId())
                    .withClaim("rank", user.getRank().toString())
                    .withClaim("displayName", user.getTwitchAccount().getDisplayName())
                    .sign(algorithm);
        } catch (Exception e) {
            TwasiLogger.log.error(e);
        }
        return token;
    }

    public String generateNewSecret() {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        String randomString = sb.toString();

        return DatatypeConverter.printBase64Binary(randomString.getBytes());
    }

    public User getUserFromToken(String jwt) {
        if (!isValidToken(jwt)) {
            return null;
        } else {
            DecodedJWT decodedJWT = JWT.decode(jwt);
            String twitchId = decodedJWT.getClaim("twitchid").asString();
            return DataService.get().get(UserRepository.class).getByTwitchId(twitchId);
        }
    }

    public boolean isValidToken(String jwt) {
        String twitchId;

        if (jwt == null) {
            return false;
        }

        // First read out User of token. This has to be done before verification
        // because every user has another secret.
        try {
            DecodedJWT decodedJWT = JWT.decode(jwt);

            twitchId = decodedJWT.getClaim("twitchid").asString();
        } catch (JWTDecodeException exception) {
            return false;
        }

        User user = ServiceRegistry.get(DataService.class).get(UserRepository.class).getByTwitchId(twitchId);

        if (user == null) {
            return false;
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(user.getJWTSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ServiceRegistry.get(ConfigService.class).getCatalog().auth.issuer)
                    .build(); //Reusable verifier instance
            DecodedJWT decodedJWT = verifier.verify(jwt);

            // The token is okay, check expiration
            if (decodedJWT.getExpiresAt().getTime() < new Date().getTime()) {
                // Token expired
                return false;
            }

            return true;
        } catch (UnsupportedEncodingException | JWTVerificationException exception) {
            TwasiLogger.log.debug(exception);
            return false;
        }
    }

}
