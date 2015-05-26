package app.GUI;

import java.awt.Component;
import java.awt.Panel;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.LiveGraph.LiveGraph;
import org.LiveGraph.dataFile.write.DataStreamWriter;
import org.LiveGraph.dataFile.write.DataStreamWriterFactory;
import org.LiveGraph.settings.DataFileSettings;
import org.LiveGraph.settings.DataSeriesSettings;
import org.LiveGraph.settings.GraphSettings;

public class Charts extends JFrame{
	
	public JFrame frame=new JFrame("Data Center");
	private JLabel label = new JLabel();
	private Panel p;

	public static final String DEMO_DIR = System.getProperty("user.dir");

	private DataStreamWriter out;
	private DataStreamWriter outAirflow ;

	public Charts(){
		
		out = DataStreamWriterFactory.createDataWriter(DEMO_DIR,"Chart");
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
		
		GraphSettings gfs = new GraphSettings();
		DataFileSettings dfss = new DataFileSettings();
		DataFileSettings dfs = new DataFileSettings();

		dfs.setDataFile(DEMO_DIR+"/Chart.15"+"."+mm+"."+dd+"-"+hh+"."+min+"."+ss+".dat");
		dfss.setDataFile(DEMO_DIR+"/Airflow %.15"+"."+mm+"."+dd+"-"+hh+"."+min+"."+ss+".dat");

		dfs.setUpdateFrequency(1000);
		dfs.save("startup.lgdfs");
		LiveGraph app = LiveGraph.application();
		app.exec(new String[] {"-dfs", "startup.lgdfs"});
				
		dfss.setUpdateFrequency(1000);
		dfss.save("startupAirflow.lgdfs");
		LiveGraph appp = LiveGraph.application();
		appp.exec(new String[] {"-dfs", "startupAirflow.lgdfs"});

		// Set a values separator:
		out.setSeparator(";");
		outAirflow.setSeparator(";");
		// Add a file description line:
		out.writeFileInfo("Chart");
		outAirflow.writeFileInfo("Airflow %");
		// Set-up the data series:
		out.addDataSeries("PowerConsumption");
		out.addDataSeries("Cooling Power Consumption");
		out.addDataSeries("Nb of deployed VMs");
		outAirflow.addDataSeries("HACS volumetric airflow");		
		outAirflow.addDataSeries("CACS volumetric airflow");
		outAirflow.addDataSeries("Parallel volumetric airflow 0.1 loss");
		outAirflow.addDataSeries("Parallel volumetric airflow 0.2 loss");
		outAirflow.addDataSeries("Parallel volumetric airflow 0.3 loss");
		outAirflow.addDataSeries("Parallel volumetric airflow 0.4 loss");
		outAirflow.addDataSeries("Parallel volumetric airflow 0.5 loss");
		
	//	frame.add(dfs);
		frame.setVisible(true);
	}
	
	public void updateChartPowerConsumption(float powerConsumption,  float coolingPowerConsumption, int nbVMs){
		
			out.setDataValue(powerConsumption);
			out.setDataValue(coolingPowerConsumption);
			out.setDataValue(nbVMs);

		 // Write dataset to disk:
	      out.writeDataSet();
	      
	      // Check for IOErrors:      
	      if (out.hadIOException()) {
	        out.getIOException().printStackTrace();
	        out.resetIOException();
	      }
//	      // Pause:
//	      Thread.yield();
//	        try { Thread.sleep(3000); } catch (InterruptedException e) {}
//	        Thread.yield();		
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
		 out.close();
		 outAirflow.close();
	}
}
