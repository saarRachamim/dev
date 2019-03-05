package app.network;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RequestHandler {
    HttpClient httpclient;
    HttpPost httpPost;
    String url;

    public RequestHandler(String url) {
        this.url = url;
        httpclient = HttpClients.createDefault();
    }

    public String postHttp(String relativePath, String[] headerKeys, String[] headerValues, String rawBodyParams) throws IOException, JSONException {
        httpPost = new HttpPost(this.url + relativePath + ".json");

        httpPost.setEntity(new StringEntity(rawBodyParams));

        for (int i = 0; i < headerKeys.length; i++)
            httpPost.setHeader(headerKeys[i], headerValues[i]);

        HttpResponse response = httpclient.execute(httpPost);
        String json_string = EntityUtils.toString(response.getEntity());
        JSONObject json = new JSONObject(json_string);
        String key = String.valueOf(json.get("name"));

        return key;
    }
}
