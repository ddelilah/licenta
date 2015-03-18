/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.DatacenterON;
import models.TemplateModel;

/**
 *
 * @author oneadmin
 */
public class TCPClient {

    public void restore(DatacenterON dataCenter, TemplateModel tm, List<String> imageNames) {
        System.out.println(
                "Trying to establish a connection with "
                + dataCenter.getPuppeteer().getIp()
                + " on port "
                + dataCenter.getPuppeteer().getPort());

        try {
            Socket clientSocket = new Socket(
                    dataCenter.getPuppeteer().getIp(),
                    dataCenter.getPuppeteer().getPort());
            if (clientSocket.isConnected()) {
                System.out.println("Connection established.");
                ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
                outToServer.writeObject(tm);
                clientSocket.close();                
            } else {
                System.out.println("Unable to establish a connection");
            }
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error occured while trying to connect");
        } finally {
            System.out.println("Connection terminated");
        }
    }

}
