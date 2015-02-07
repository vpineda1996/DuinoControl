package com.vpineda.duinocontrol.app.classes.model;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.UUID;

/**
 * Created by Victor on 2/3/2015.
 */
public class Server {
    private UUID uuid;
    private String name;
    private URI uri;

    /* =====================================
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
    /*
     * REQUIRES: a valid JSONbject
     * EFFECTS: sends your desired JSON to the required server
     */
    public JSONObject sendJSONMessage(JSONObject message) throws JSONException {
        HttpClient httpClient = new DefaultHttpClient();
        // setup the message that you are going to send
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-type", "application/json");

        InputStream input = null;
        String result = null;
        try {
            httpPost.setEntity(new StringEntity(message.toString()));
            HttpResponse response = httpClient.execute(httpPost);
            input = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (Exception e) {}
        }
        return new JSONObject(result);
    }

    /*
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
