package net.twasi.core.interfaces.twitch.webapi;

import com.google.gson.Gson;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.TwitchClientBuilder;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.auth.model.twitch.Authorize;
import me.philippheuer.twitch4j.model.Token;
import me.philippheuer.twitch4j.model.User;
import net.twasi.core.config.Config;
import net.twasi.core.database.models.AccessToken;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.interfaces.twitch.webapi.dto.TokenInfoDTO;
import net.twasi.core.interfaces.twitch.webapi.dto.UserInfoDTO;
import net.twasi.core.logger.TwasiLogger;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Optional;

public class TwitchAPI {
    private TwitchClient client;

    public TwitchAPI() {
        client = TwitchClientBuilder.init()
                .withClientId(Config.getCatalog().twitch.clientId)
                .withClientSecret(Config.getCatalog().twitch.clientSecret)
                .build();
    }

    public String getAuthURL() {
        return "https://api.twitch.tv/kraken/oauth2/authorize" +
                "?client_id=" + client.getClientId() +
                "&redirect_uri=" + Config.getCatalog().twitch.redirectUri +
                "&response_type=code" +
                "&scope=channel_editor+user_read";
    }

    public AccessToken getToken(String code) {
        Optional<Authorize> optionalAuth = client.getKrakenEndpoint().getOAuthToken("authorization_code", Config.getCatalog().twitch.redirectUri, code);

        if (!optionalAuth.isPresent()) {
            TwasiLogger.log.info("Cannot authorize: Code validation failed.");
            return null;
        }

        Authorize auth = optionalAuth.get();
        return new AccessToken(auth.getAccessToken(), auth.getRefreshToken(), auth.getExpiresIn(), auth.getScope().toArray(new String[0]));
    }

    public TwitchAccount getTwitchAccountByToken(AccessToken token) {
        Token tokenValidation = client.getKrakenEndpoint().getToken(token.toCredential());

        if (!tokenValidation.getValid()) {
            TwasiLogger.log.info("Invalid token found.");
            return null;
        }

        UserInfoDTO userInfo = getUserInfo(token);
        if (userInfo == null) {
            TwasiLogger.log.info("Could not find user.");
            return null;
        }

        TokenInfoDTO tokenInfo = getKrakenUser(token);

        return TwitchAccount.fromUser(userInfo, tokenInfo, token);
    }

    private TokenInfoDTO getKrakenUser(AccessToken token) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        TwitchAccount acc = null;
        try {
            HttpGet httpget = new HttpGet("https://api.twitch.tv/kraken");

            httpget.setHeader("Accept", "application/vnd.twitchtv.v5+json");
            httpget.setHeader("Authorization", "OAuth " + token.getAccessToken());

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);

            return new Gson().fromJson(responseBody, TokenInfoDTO.class);
        } catch (IOException e) {
            TwasiLogger.log.error(e);
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
            httpclient.close();
        }
        return null;
    }

    private UserInfoDTO getUserInfo(AccessToken token) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        TwitchAccount acc = null;
        try {
            HttpGet httpget = new HttpGet("https://api.twitch.tv/kraken/user");

            httpget.setHeader("Accept", "application/vnd.twitchtv.v5+json");
            httpget.setHeader("Authorization", "OAuth " + token.getAccessToken());

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);

            return new Gson().fromJson(responseBody, UserInfoDTO.class);
        } catch (IOException e) {
            TwasiLogger.log.error(e);
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
            httpclient.close();
        }
        return null;
    }

    /* public String getAuthURL() {
        return "https://api.twitch.tv/kraken/oauth2/authorize" +
                "?client_id=" + Config.getCatalog().twitch.clientId +
                "&redirect_uri=" + Config.getCatalog().twitch.redirectUri +
                "&response_type=code" +
                "&scope=channel_editor+user_read";
    }

    public AccessTokenDTO getToken(String code) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        AccessTokenDTO token = null;
        try {
            HttpPost httppost = new HttpPost("https://api.twitch.tv/kraken/oauth2/token" +
                    "?client_id=" + Config.getCatalog().twitch.clientId +
                    "&client_secret=" + Config.getCatalog().twitch.clientSecret +
                    "&code=" + code +
                    "&grant_type=authorization_code" +
                    "&redirect_uri=" + Config.getCatalog().twitch.redirectUri);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httppost, responseHandler);

            token = new Gson().fromJson(responseBody, AccessTokenDTO.class);
        } catch (IOException e) {
            TwasiLogger.log.error(e);
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
            httpclient.close();
        }
        return token;
    }

    public TwitchAccount getTwitchAccountByToken(AccessTokenDTO token) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        TwitchAccount acc = null;
        try {
            HttpGet httpget = new HttpGet("https://api.twitch.tv/kraken");

            httpget.setHeader("Accept", "application/vnd.twitchtv.v5+json");
            httpget.setHeader("Authorization", "OAuth " + token.getAccessToken());

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);

            TokenInfoDTO info = new Gson().fromJson(responseBody, TokenInfoDTO.class);

            acc = new TwitchAccount(info.getToken().getUserName(), token.toModel(), info.getToken().getUserId(), new ArrayList<>());
            acc.setEmail(info.getToken().getEmail());
        } catch (IOException e) {
            TwasiLogger.log.error(e);
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
            httpclient.close();
        }
        return acc;
    }

    public void applyUserInfo(TwitchAccount account) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        TwitchAccount acc = null;
        try {
            HttpGet httpget = new HttpGet("https://api.twitch.tv/kraken/user");

            httpget.setHeader("Accept", "application/vnd.twitchtv.v5+json");
            httpget.setHeader("Authorization", "OAuth " + account.getToken().getAccessToken());

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);

            UserInfoDTO info = new Gson().fromJson(responseBody, UserInfoDTO.class);

            account.setEmail(info.getEmail());
            account.setAvatar(info.getLogo());
        } catch (IOException e) {
            TwasiLogger.log.error(e);
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
            httpclient.close();
        }
    }

    public TwitchAccount updateAccount(TwitchAccount old) {

    }*/

}
