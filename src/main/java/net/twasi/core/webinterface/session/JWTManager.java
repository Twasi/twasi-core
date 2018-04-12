package net.twasi.core.webinterface.session;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import net.twasi.core.config.Config;
import net.twasi.core.database.Database;
import net.twasi.core.database.models.User;
import net.twasi.core.database.store.UserStore;
import net.twasi.core.logger.TwasiLogger;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Date;

public class JWTManager {

    public String createNewToken(User user) {
        String token = null;

        try {
            String secret = generateNewSecret();
            Algorithm algorithm = Algorithm.HMAC256(secret);

            token = JWT.create()
                    .withIssuer(Config.getCatalog().auth.issuer)
                    .withIssuedAt(new Date())
                    .withClaim("name", user.getTwitchAccount().getUserName())
                    .withClaim("twitchid", user.getTwitchAccount().getTwitchId())
                    .withClaim("rank", user.getRank())
                    .sign(algorithm);

            user.setJWTSecret(secret);
            Database.getStore().save(user);
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
            String userId = decodedJWT.getClaim("twitchid").asString();
            return UserStore.getById(userId);
        }
    }

    public boolean isValidToken(String jwt) {
        String userId;

        // First read out User of token. This has to be done before verification
        // because every user has another secret.
        try {
            DecodedJWT decodedJWT = JWT.decode(jwt);
            userId = decodedJWT.getClaim("twitchid").asString();
        } catch (JWTDecodeException exception) {
            TwasiLogger.log.info(exception);
            return false;
        }

        User user = UserStore.getById(userId);

        if (user == null) {
            return false;
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(user.getJWTSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(Config.getCatalog().auth.issuer)
                    .build(); //Reusable verifier instance
            DecodedJWT decodedJWT = verifier.verify(jwt);
            return true;
        } catch (UnsupportedEncodingException exception) {
            TwasiLogger.log.error(exception);
            return false;
        } catch (JWTVerificationException exception) {
            TwasiLogger.log.debug(exception);
            return false;
        }
    }

}
