package app.GUI;

import java.awt.Color;
import java.util.Calendar;

import org.LiveGraph.LiveGraph;
import org.LiveGraph.dataFile.write.DataStreamWriter;
import org.LiveGraph.dataFile.write.DataStreamWriterFactory;
import org.LiveGraph.settings.DataFileSettings;
import org.LiveGraph.settings.DataSeriesSettings;
import org.LiveGraph.settings.GraphSettings;
import org.LiveGraph.settings.DataSeriesSettings.ScaleMode;
import org.LiveGraph.settings.GraphSettings.VGridType;

public class ChartAirflow {

	
	public static final String DEMO_DIR = System.getProperty("user.dir");
	private DataStreamWriter outAirflow ;

	public ChartAirflow(){
		
		outAirflow = DataStreamWriterFactory.createDataWriter(DEMO_DIR,"Airflow %");
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) +1; 
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		int sec = now.get(Calendar.SECOND);
		
		String yy ="15";
		String mm = "0"+Integer.toString(month);
		String dd,hh,min,ss;
		if (day<10)
			 dd = "0"+Integer.toString(day);
		else
			dd = Integer.toString(day);
		if (hour<10)
			 hh = "0"+Integer.toString(hour);
		else
			hh = Integer.toString(hour);
		if (minute<10)
			 min = "0"+Integer.toString(minute);
		else
			min = Integer.toString(minute);
		if (sec<10)
			 ss = "0"+Integer.toString(sec);
		else
			ss = Integer.toString(sec);
		
		DataFileSettings dfss = new DataFileSettings();
		DataSeriesSettings dss = new DataSeriesSettings();
		
		dfss.setDataFile(DEMO_DIR+"/Airflow %.15"+"."+mm+"."+dd+"-"+hh+"."+min+"."+ss+".dat");

		dfss.setUpdateFrequency(1000);
		dfss.save("startupAirflow2.lgdfs");
		
		dss.load("startupAirflow2.lgdfs");
		dss.setColour(0, Color.BLACK);
		dss.setColour(1, Color.BLUE);
		dss.setColour(2, Color.RED);
				
		dss.save("dssAirflow.lgdss");
		LiveGraph appp = LiveGraph.application();
		appp.exec(new String[] {"-dfs", "startupAirflow2.lgdfs","-dss", "dssAirflow.lgdss"});
		outAirflow.setSeparator(";");
		outAirflow.writeFileInfo("Airflow2");

		outAirflow.addDataSeries("HACS volumetric airflow");		
		outAirflow.addDataSeries("CACS volumetric airflow");
		outAirflow.addDataSeries("Parallel volumetric airflow 0.1 loss");
		outAirflow.addDataSeries("Parallel volumetric airflow 0.2 loss");
		outAirflow.addDataSeries("Parallel volumetric airflow 0.3 loss");
		outAirflow.addDataSeries("Parallel volumetric airflow 0.4 loss");
		outAirflow.addDataSeries("Parallel volumetric airflow 0.5 loss");
		
	}
	
	
	
	public void updatChartAirflow(float hacsAirflow , float cacsAirflow, float parAirflow01, float parAirflow02, float parAirflow03, float parAirflow04, float parAirflow05){
		
		outAirflow.setDataValue(hacsAirflow);
		outAirflow.setDataValue(cacsAirflow);
		outAirflow.setDataValue(parAirflow01);
		outAirflow.setDataValue(parAirflow02);
		outAirflow.setDataValue(parAirflow03);
		outAirflow.setDataValue(parAirflow04);
		outAirflow.setDataValue(parAirflow05);
			 // Write dataset to disk:
		outAirflow.writeDataSet();
		      
		      // Check for IOErrors:      
		      if (outAirflow.hadIOException()) {
		    	  outAirflow.getIOException().printStackTrace();
		    	  outAirflow.resetIOException();
		      }
		      // Pause:
//		      Thread.yield();
//		        try { Thread.sleep(3000); } catch (InterruptedException e) {}
//		        Thread.yield();		

	}
	
	public void finishChartExecution(){
		 outAirflow.close();
	}
}
