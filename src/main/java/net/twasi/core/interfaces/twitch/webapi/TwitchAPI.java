package net.twasi.core.interfaces.twitch.webapi;

import net.twasi.core.config.Config;
import net.twasi.core.logger.TwasiLogger;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
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

    public String getAccessToken(String code) {
        HttpClient httpclient = new DefaultHttpClient();
        String response = null;
        try {
            HttpPost httppost = new HttpPost("https://api.twitch.tv/kraken/oauth2/token" +
                    "?client_id=" + Config.catalog.twitch.clientId +
                    "&client_secret=" + Config.catalog.twitch.clientSecret +
                    "&code=" + code +
                    "&grant_type=authorization_code" +
                    "&redirect_uri=" + Config.catalog.twitch.redirectUri);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httppost, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            System.out.println("----------------------------------------");
            response = responseBody;
        } catch (IOException e) {
            TwasiLogger.log.error(e);
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
        return response;
    }

}
