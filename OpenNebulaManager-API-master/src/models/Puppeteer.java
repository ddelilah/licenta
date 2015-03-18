/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author oneadmin
 */


//do we really need it??? if not, what happens with getDatacenter from the Factory?
public class Puppeteer {

    private String username;
    private String password;
    private String datastoreImgPath;
    private String ip;
    private int port;

    public Puppeteer(String username, String password, String datastoreImgPath, String ip, int port) {
        this.username = username;
        this.password = password;
        this.datastoreImgPath = datastoreImgPath;
        this.ip = ip;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
    }

    public void setUsername(String hostname) {
        this.username = hostname;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDatastoreImgPath(String datastoreImgPath) {
        this.datastoreImgPath = datastoreImgPath;
    }

    public String getDatastoreImgPath() {
        return datastoreImgPath;
    }

    public String getImgPath() {
        return username + "@" + ip + ":" + datastoreImgPath;
    }
}
