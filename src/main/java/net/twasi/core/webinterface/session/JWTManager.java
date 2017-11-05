package net.twasi.core.webinterface.session;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import net.twasi.core.config.Config;
import net.twasi.core.database.models.User;
import net.twasi.core.logger.TwasiLogger;

import java.util.Date;

public class JWTManager {

    public String createNewToken(User user) {
        String token = null;

        try {
            Algorithm algorithm = Algorithm.HMAC256(Config.catalog.auth.secret);

            token = JWT.create()
                    .withIssuer("Twasi JWT Service")
                    .withIssuedAt(new Date())
                    .withClaim("name", user.getTwitchAccount().getUserName())
                    .withClaim("twitchid", user.getTwitchAccount().getTwitchId())
                    //.withArrayClaim("array", new Integer[]{1, 2, 3})
                    .sign(algorithm);
        } catch (Exception e) {
            TwasiLogger.log.error(e);
        }
        return token;
    }

}
