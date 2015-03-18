package logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import config.Configurations;

public class CloudLogger {
	private static CloudLogger instance = null;
    private final File log;
    
    protected CloudLogger() {
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateTime = dateFormat.format(new Date());        
        String filePath = Configurations.LogPath + dateTime + ".txt";         
        log = new File(filePath);        
    }
    
    public static CloudLogger getInstance() {
        if (instance == null)
        {
            instance = new CloudLogger();
        }
        return instance;
    }
    
    public void LogInfo(String message) {
        try {
            FileWriter fileWriter = new FileWriter(log);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append(message + "\n");
            System.out.println(message);
            bufferedWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
