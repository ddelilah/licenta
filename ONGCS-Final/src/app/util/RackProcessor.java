package app.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.access.RackDAO;
import app.access.impl.RackDAOImpl;
import app.model.*;

public class RackProcessor implements Comparator<Rack> {

	private List<Rack> rackList;
	
	private RackDAOImpl rackDAO = new RackDAOImpl();
	
	private static float LOWER_BOUND_RACK_UTILIZATION = 0.4f;
	
	public RackProcessor(List<Rack> rackList){
		this.rackList=rackList;
	}
	
	public RackProcessor() {
	}

	public List<Rack> getRackList(){
		return rackList;
	}
	
	public void setRackList(List<Rack> rackList){
		this.rackList=rackList;
	}

	@Override
	public int compare(Rack r1, Rack r2) {
		return r1.getUtilization() > r2.getUtilization() ? -1 : r1.getUtilization() == r2.getUtilization()? 0 : 1;
	}
	
	public List sortRackListDescending(){
		
		Collections.sort(rackList, new RackProcessor());
		return rackList;
	}
	
	public List<Rack> getNonUnderUtilizedRacks(List<Rack> allRacks) {
		List<Rack> nonUnderutilizedRacks = new ArrayList<Rack>();
		
		for(Rack r : allRacks) {
			if(r.getUtilization() > LOWER_BOUND_RACK_UTILIZATION) {
				nonUnderutilizedRacks.add(r);
			}
		}
		
		return nonUnderutilizedRacks;
		
	}
	
	public List<Rack> getUnderUtilizedRacks(List<Rack> allRacks) {
		List<Rack> underutilizedRacks = new ArrayList<Rack>();
		
		for(Rack r : allRacks) {
			if(r.getUtilization() < LOWER_BOUND_RACK_UTILIZATION) {
				underutilizedRacks.add(r);
			}
		}
		
		return underutilizedRacks;
		
	}
	
	
	
}