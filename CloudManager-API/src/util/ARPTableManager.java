/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;


public class ARPTableManager {

    private File arpTableFile;
    private Map<String, String> macAddressesMapping;

    public ARPTableManager(String arpTableFileLocation) {
        macAddressesMapping = new HashMap<String, String>();

        //read file contents
        arpTableFile = new File(arpTableFileLocation);
        if (arpTableFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(arpTableFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    if(line.equals("IP=MAC")){
                        continue;
                    }
                    //accepted format:  serverIP = serverMAC  
                    String[] data = line.split("=");
                    macAddressesMapping.put(data[0].trim(), data[1].trim());
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ARPTableManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ARPTableManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PatternSyntaxException syntaxException) {
                 Logger.getLogger(ARPTableManager.class.getName()).log(Level.SEVERE, "Invalid arp table file syntax. Accepted syntax: IP=MAC", syntaxException); 
            } catch(NullPointerException nullPointerException){
                Logger.getLogger(ARPTableManager.class.getName()).log(Level.SEVERE, "Invalid arp table file syntax. Accepted syntax: IP=MAC", nullPointerException); 
            }
        }
    }
    
    public String getMAC(String ip){
        return macAddressesMapping.get(ip);
    }
    
    public boolean hasMAC(String ip){
        return macAddressesMapping.containsKey(ip);
    }
    
    public void addMAC(String ip, String mac){
        macAddressesMapping.put(ip, mac);
    }
    
    public void writeArpTable(){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(arpTableFile));
            bufferedWriter.write("IP=MAC");
            bufferedWriter.newLine();
            for(Entry<String,String> entry : macAddressesMapping.entrySet()){
                bufferedWriter.write(entry.getKey() + "=" + entry.getValue());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(ARPTableManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

