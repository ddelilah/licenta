package app.GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.LiveGraph.LiveGraph;
import org.LiveGraph.dataCache.DataCache;
import org.LiveGraph.dataFile.write.DataStreamWriter;
import org.LiveGraph.dataFile.write.DataStreamWriterFactory;
import org.LiveGraph.gui.MainWindow;
import org.LiveGraph.plot.Plotter;
import org.LiveGraph.settings.DataFileSettings;
import org.LiveGraph.settings.DataSeriesSettings;
import org.LiveGraph.settings.DataSeriesSettings.ScaleMode;
import org.LiveGraph.settings.GraphSettings;
import org.LiveGraph.settings.GraphSettings.VGridType;

public class Charts {
	
	
	public static final String DEMO_DIR = System.getProperty("user.dir");

	private DataStreamWriter out;

	public Charts(){
		
		out = DataStreamWriterFactory.createDataWriter(DEMO_DIR,"Chart");
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
		DataSeriesSettings dss = new DataSeriesSettings();
		
		MainWindow mw = new MainWindow();
		//mw.hide();
		
	
		dfs.setDataFile(DEMO_DIR+"/Chart.15"+"."+mm+"."+dd+"-"+hh+"."+min+"."+ss+".dat");

		dfs.setUpdateFrequency(1000);
		dfs.save("startup.lgdfs");		
		
		dss.load("startup.lgdfs");
		dss.setScaleMode(0, ScaleMode.Scale_SetVal);
		dss.setParameter(0, 0.01);
		dss.setColour(0, Color.MAGENTA);
		dss.setScaleMode(1, ScaleMode.Scale_SetVal);
		dss.setParameter(1, 0.01);
		dss.setColour(1, Color.ORANGE);
		dss.save("dss.lgdss");
		
		gfs.load("startup.lgdfs");
		gfs.setHighlightDataPoints(true);
		gfs.setVGridType(VGridType.VGrid_XAUnitAligned);
		gfs.setVGridSize(1.0);
		gfs.save("gfs.lggfs");
	
		
		LiveGraph app = LiveGraph.application();
		
		app.exec(new String[] {"-dfs", "startup.lgdfs", "-dss","dss.lgdss", "-gs","gfs.lggfs"});
				
		
		dfss.setUpdateFrequency(1000);
		dfss.save("startupAirflow.lgdfs");

		out.setSeparator(";");
		out.writeFileInfo("Chart");
		out.addDataSeries("PowerConsumption");
		out.addDataSeries("Cooling Power Consumption");
		out.addDataSeries("Nb of deployed VMs");
		
		app.setDisplayDataFileSettingsWindow(false);
		app.setDisplayGraphSettingsWindow(false);
		
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
	
	
	public void finishChartExecution(){
		 out.close();
//		 outAirflow.close();
	}
}
