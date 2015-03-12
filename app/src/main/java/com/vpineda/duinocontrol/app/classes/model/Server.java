package com.vpineda.duinocontrol.app.classes.model;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.vpineda.duinocontrol.app.factories.ServerFactory;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.UUID;

/**
 * Created by Victor on 2/3/2015.
 */
public class Server{
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

    public void sendCommand(JSONObject message, ServerFactory.OnResponseListener mListener) throws UnsupportedEncodingException {
        ServerFactory.getInstance().sendCommand(message,mListener,uri);
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
