/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.TemplateModelON;

import org.opennebula.client.OneResponse;

import config.OpenNebulaConfigurationManager;

import services.ImageServiceImpl;
import services.TemplateServiceImpl;

/**
 * 
 * @author oneadmin
 */
public class TCPServer implements Runnable {

	static String clientSentence;
	static String capitalizedSentence;

	public static void main(String... args) {

		listen();
	}

	public static void listen() {

		TemplateModelON tm = null;
		Socket connectionSocket = null;
		boolean isListening = true;
		ServerSocket welcomeSocket = null;
		try {
			welcomeSocket = new ServerSocket(6789);
		} catch (IOException ex) {
			Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		while (isListening) {

			try {
				System.out.println("Listening on port"
						+ OpenNebulaConfigurationManager.getListeningPort());

				connectionSocket = welcomeSocket.accept();
				if (connectionSocket.isConnected()) {
					System.out.println("Accepted a connection from "
							+ connectionSocket.getRemoteSocketAddress()
									.toString());
					InputStream is = connectionSocket.getInputStream();
					ObjectInputStream obj = new ObjectInputStream(is);
					try {
						tm = (TemplateModelON) obj.readObject();
						System.out.println("\n" + tm.toString());

						ImageServiceImpl imageService = new ImageServiceImpl();
						TemplateServiceImpl templateService = new TemplateServiceImpl();

						List<OneResponse> oneResponses = imageService
								.allocateImages(tm);

						templateService.allocateTemplate(tm, oneResponses);
						if (!isListening) {
							connectionSocket.close();
						}

					} catch (ClassNotFoundException ex) {
						Logger.getLogger(TCPServer.class.getName()).log(
								Level.SEVERE, null, ex);
					}

				}
			} catch (IOException ex) {
				Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE,
						null, ex);
			}

		}

	}

	@Override
	public void run() {
		listen();
	}
}
