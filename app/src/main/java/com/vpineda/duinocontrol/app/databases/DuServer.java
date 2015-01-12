package com.vpineda.duinocontrol.app.databases;

import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-01-10.
 */
public class DuServer {

    private UUID serverID;
    private String serverName, protocol, host, port, path;

    public DuServer(UUID id, String nameInput, String protocolInput, String hostInput, String portInput, String pathInput){
        this.serverID = id;
        this.serverName = nameInput;
        this.protocol = protocolInput;
        this.host = hostInput;
        this.port = portInput;
        this.path = pathInput;
    }

    public DuServer(String nameInput, String protocolInput, String hostInput, String portInput, String pathInput){
        this(UUID.randomUUID(), nameInput, protocolInput, hostInput, portInput, pathInput);
    }

    public void setServerID(UUID serverID) {
        this.serverID = serverID;

    }

    public void setServerName(String name) {
        this.serverName = name;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public UUID getServerID() {

        return serverID;
    }

    public String getServerName() {
        return serverName;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }
}
