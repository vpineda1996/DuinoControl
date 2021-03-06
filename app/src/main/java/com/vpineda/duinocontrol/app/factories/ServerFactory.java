package com.vpineda.duinocontrol.app.factories;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;

/**
 * Created by vpineda1996 on 2015-03-10.
 */
public class ServerFactory {
    public static ServerFactory instance = null;

    private OnResponseListener mResponseListener;
    private AsyncHttpClient client;

    public static ServerFactory getInstance(){
        if(instance == null){
            instance = new ServerFactory();
        }
        return instance;
    }

    protected ServerFactory(){}

    /**
     * Listener (so we we can interact with the toggle once we've had a response)
     */
    public interface OnResponseListener {
        public void onServerResponseSuccess(JSONObject response);
        public void onServerResponseFailure(Exception e, byte[] response);
    }

    /**
     * send a message to the server
     * @param message a message that will be sent to this server
     * @param mListener listener for the server's response
     * @throws java.io.UnsupportedEncodingException whenever the request sent to the HttpClient is not valid
     */
    public void sendCommand(JSONObject message, OnResponseListener mListener, URI uri) throws UnsupportedEncodingException {
        if (client == null){
            client = new AsyncHttpClient();
        }
        this.mResponseListener = mListener;
        // Create the new client from which we will send the information
        StringEntity se = null;
        // Convert the jsonMessage to string and then to a StringEntity to be able to send it via the
        // AsyncHttpClient
        se = new StringEntity(message.toString());
        // Configure the response
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.setMaxRetriesAndTimeout(0, 1000);
        // If a request hasn't been responded ignore it and restart the connection again
        client.cancelAllRequests(true);
        // Send the message
        client.post(null, uri.toString(), se, "application/json", new AsyncHttpResponseHandler() {
            /**
             * this method will be called when we get a 20x response from the server
             * @param statusCode the status code of 20x as a int
             * @param headers headers of the website (if using DuinoServer that will be
             *                application/json
             * @param responseBody the response from the server as bytes (we need to transform it to
             *                     whatever we like)
             */
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    // Read the bytes and transform them into UTF-8 encoding
                    String str = null;
                    str = new String(responseBody, "UTF-8");
                    // Display message
                    // TODO Maybe check if header is correct or respond differently with different headers
                    JSONObject response = new JSONObject(str);
                    // Set listener
                    mResponseListener.onServerResponseSuccess(response);
                } catch (Exception e) {
                    // Set failure listener
                    mResponseListener.onServerResponseFailure(e, responseBody);
                }
            }

            /**
             * Called when the response is 40x :0
             * @param statusCode 40x
             * @param headers headers of the response
             * @param responseBody response body by bytes that we dont know if they are bytes
             * @param error the error given by the HttpClient
             */
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO change the exception to an actual new class exception
                mResponseListener.onServerResponseFailure(new Exception("Couldn't connect to the server"), responseBody);
            }
        });
    }

}
