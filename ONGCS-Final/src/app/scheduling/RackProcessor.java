package app.scheduling;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.model.*;

public class RackProcessor implements Comparator<Rack> {

	private List<Rack> rackList;
	
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
	
	/*public static void main(String []args){
		
		Rack rack1= new Rack();
		rack1.setUtilization(79);
		Rack rack2= new Rack();
		rack2.setUtilization(50);
		Rack rack3= new Rack();
		rack3.setUtilization(99);
		Rack rack4= new Rack();
		rack4.setUtilization(1);
		List<Rack> rList = new ArrayList<Rack>();
		rList.add(rack1);
		rList.add(rack2);		
		rList.add(rack3);
		rList.add(rack4);
		
		Collections.sort(rList, new RackScheduling());
	
		for(Rack r: rList)
			System.out.println(r.getUtilization()+ " ");
	}*/

	
}