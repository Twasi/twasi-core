package net.twasi.core.webinterface.session;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import net.twasi.core.config.Config;
import net.twasi.core.database.Database;
import net.twasi.core.database.models.User;
import net.twasi.core.logger.TwasiLogger;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
import java.util.Date;

public class JWTManager {

    public String createNewToken(User user) {
        String token = null;

        try {
            String secret = generateNewSecret();
            Algorithm algorithm = Algorithm.HMAC256(secret);

            token = JWT.create()
                    .withIssuer("Twasi JWT Service")
                    .withIssuedAt(new Date())
                    .withClaim("name", user.getTwitchAccount().getUserName())
                    .withClaim("twitchid", user.getTwitchAccount().getTwitchId())
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

        StringBuilder sb = new StringBuilder( 32 );
        for( int i = 0; i < 32; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        String randomString = sb.toString();

        return DatatypeConverter.printBase64Binary(randomString.getBytes());
    }

}
