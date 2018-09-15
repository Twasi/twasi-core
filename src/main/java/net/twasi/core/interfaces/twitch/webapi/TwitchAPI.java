package net.twasi.core.interfaces.twitch.webapi;

import com.google.gson.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.TwitchClientBuilder;
import me.philippheuer.twitch4j.auth.model.twitch.Authorize;
import me.philippheuer.twitch4j.model.Token;
import net.twasi.core.database.models.AccessToken;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.repositories.UserRepository;
import net.twasi.core.interfaces.twitch.webapi.dto.TokenInfoDTO;
import net.twasi.core.interfaces.twitch.webapi.dto.UserInfoDTO;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.IService;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.DatabaseService;
import net.twasi.core.services.providers.config.ConfigService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TwitchAPI implements IService {
    private TwitchClient client;

    private String clientId;
    private String clientSecret;

    private HttpClient httpclient = HttpClients.createDefault();

    public TwitchAPI() {
        clientId = ServiceRegistry.get(ConfigService.class).getCatalog().twitch.clientId;
        clientSecret = ServiceRegistry.get(ConfigService.class).getCatalog().twitch.clientSecret;

        client = TwitchClientBuilder.init()
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .build();
    }

    public String getAuthURL() {
        return "https://api.twitch.tv/kraken/oauth2/authorize" +
                "?client_id=" + client.getClientId() +
                "&redirect_uri=" + ServiceRegistry.get(ConfigService.class).getCatalog().twitch.redirectUri +
                "&response_type=code" +
                "&scope=channel_editor+user_read";
    }

    public AccessToken getToken(String code) {
        Optional<Authorize> optionalAuth = client.getKrakenEndpoint().getOAuthToken("authorization_code", ServiceRegistry.get(ConfigService.class).getCatalog().twitch.redirectUri, code);

        if (!optionalAuth.isPresent()) {
            TwasiLogger.log.info("Cannot authorize: Code validation failed.");
            return null;
        }

        Authorize auth = optionalAuth.get();
        return new AccessToken(auth.getAccessToken(), auth.getRefreshToken(), auth.getExpiresIn(), auth.getScope().toArray(new String[0]));
    }

    public AccessToken refreshToken(AccessToken old) {
        /* DefaultHttpClient httpclient = new DefaultHttpClient();

        try {
            HttpGet httpget = new HttpGet("https://id.twitch.tv/oauth2/token");

            httpget.setHeader("Accept", "application/vnd.twitchtv.v5+json");
            httpget.setHeader("Authorization", "OAuth " + token.getAccessToken());
            /* --data - urlencode
                    ? grant_type = refresh_token
                    & refresh_token =<your refresh token >
                    & client_id =<your client ID >
                    & client_secret =<your client secret >

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);

            return new Gson().fromJson(responseBody, TokenInfoDTO.class);
            return null;
            catch (IOException e) {
                e
            }
        }*/

        HttpPost post = new HttpPost("https://api.twitch.tv/kraken/oauth2/token");

        post.setHeader("Accept", "application/vnd.twitchtv.v5+json");
        // We don't need to add the oauth token here since it's expired

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "refresh_token"));
        params.add(new BasicNameValuePair("refresh_token", old.getRefreshToken()));
        params.add(new BasicNameValuePair("client_id", clientId));
        params.add(new BasicNameValuePair("client_secret", clientSecret));
        try {
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            HttpResponse response = httpclient.execute(post);
            HttpEntity entity = response.getEntity();

            InputStream inputStream = entity.getContent();
            String content = IOUtils.toString(inputStream, "UTF-8");

            JsonObject json = new JsonParser().parse(content).getAsJsonObject();

            JsonArray scopesJson = json.get("scope").getAsJsonArray();
            List<String> scopes = new ArrayList<>();

            for (JsonElement e : scopesJson) {
                scopes.add(e.getAsString());
            }

            AccessToken newToken = new AccessToken(json.get("access_token").getAsString(), json.get("refresh_token").getAsString(), json.get("expires_in").getAsLong(), scopes.toArray(new String[0]));
            return newToken;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TwitchAccount getTwitchAccountByToken(AccessToken token) {
        Token tokenValidation = client.getKrakenEndpoint().getToken(token.toCredential());

        if (!tokenValidation.getValid()) {
            TwasiLogger.log.info("Invalid token found.");
            token.refresh();
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
        }
        return null;
    }

    private UserInfoDTO getUserInfo(AccessToken token) {
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
