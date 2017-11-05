package net.twasi.core.interfaces.twitch.webapi;

import com.google.gson.Gson;
import net.twasi.core.config.Config;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.webinterface.dto.auth.AccessTokenDTO;
import net.twasi.core.webinterface.dto.auth.TokenInfoDTO;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class TwitchAPI {

    public String getAuthURL() {
        return "https://api.twitch.tv/kraken/oauth2/authorize" +
                "?client_id=" + Config.catalog.twitch.clientId +
                "&redirect_uri=" + Config.catalog.twitch.redirectUri +
                "&response_type=code" +
                "&scope=channel_editor";
    }

    public AccessTokenDTO getToken(String code) {
        HttpClient httpclient = new DefaultHttpClient();
        AccessTokenDTO token = null;
        try {
            HttpPost httppost = new HttpPost("https://api.twitch.tv/kraken/oauth2/token" +
                    "?client_id=" + Config.catalog.twitch.clientId +
                    "&client_secret=" + Config.catalog.twitch.clientSecret +
                    "&code=" + code +
                    "&grant_type=authorization_code" +
                    "&redirect_uri=" + Config.catalog.twitch.redirectUri);

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
        }
        return token;
    }

    public TwitchAccount getTwitchAccountByToken(AccessTokenDTO token) {
        HttpClient httpclient = new DefaultHttpClient();
        TwitchAccount acc = null;
        try {
            HttpGet httpget = new HttpGet("https://api.twitch.tv/kraken");

            httpget.setHeader("Accept", "application/vnd.twitchtv.v5+json");
            httpget.setHeader("Authorization", "OAuth " + token.getAccessToken());

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);

            TokenInfoDTO info = new Gson().fromJson(responseBody, TokenInfoDTO.class);

            acc = new TwitchAccount(info.token.userName, token.toModel(), info.token.userId);
        } catch (IOException e) {
            TwasiLogger.log.error(e);
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
        return acc;
    }

}
