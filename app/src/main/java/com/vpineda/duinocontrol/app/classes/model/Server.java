package com.vpineda.duinocontrol.app.classes.model;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.util.Collections;
import java.util.UUID;

/**
 * Created by Victor on 2/3/2015.
 */
public class Server {
    private UUID uuid;
    private String name;
    private URI uri;

    /**
     * =====================================
     * ===========CONSTRUCTORS==============
     * =====================================
     */

    public Server (UUID uuid, String name, URI uri){
        this.uuid = uuid;
        this.name = name;
        this.uri = uri;
    }
    public Server (String name, URI uri){
        this(UUID.randomUUID(), name, uri);
    }

    //TODO: send JSON HTTP request

    /**
     * send a message to the server
     * @param message a message that will be sent to this server
     * @return the message of the server in JSON object
     * @throws JSONException
     */
    public JSONObject sendCommand(JSONObject message) throws JSONException{
//        HttpClient httpClient = new DefaultHttpClient();
//        // setup the message that you are going to send
//        HttpPost httpPost = new HttpPost(uri);
//        httpPost.setHeader("Content-type", "application/json");
//
//        InputStream input;
//        String result = null;
//        try {
//            httpPost.setEntity(new StringEntity(message.toString()));
//            HttpResponse response = httpClient.execute(httpPost);
//            input = response.getEntity().getContent();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 8);
//            StringBuilder sb = new StringBuilder();
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line);
//            }
//            result = sb.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new JSONObject(result);
        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity se = null;
        try {
            se = new StringEntity(message.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.setTimeout(3000);
            client.post(null, "http://reddit.com", se, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    try {
                        String str = new String(responseBody, "UTF-8");
                        System.out.println(str);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * =====================================
     * ========GETTERS AND SETTERS==========
     * =====================================
     */

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
