package app.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.constants.RackState;
import app.constants.ServerState;
import app.constants.VMState;
import app.hibernate.SessionFactoryUtil;
import app.model.CPU;
import app.model.HDD;
import app.model.RAM;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class Main {

	final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		CPU cpu = new CPU();

		cpu.setCpu_utilization(88);
		cpu.setFrequency(88);
		cpu.setName("generic");
		cpu.setNr_cores(3);
		createInstance(cpu);
		
	
	    RAM ram = new RAM();
	    ram.setName("ram2");
	    ram.setCapacity(99);
	    createInstance(ram);
	    
	       
	    HDD hdd = new HDD();
	    hdd.setCapacity(120);
	    hdd.setName("hdd2");
	    createInstance(hdd);
		
	    Rack r = new Rack();
		Server s = new Server();
	
			// RACK
				r.setCapacity(45);
 				r.setCoolingValue(23);
 				r.setName("rack1");
 				r.setPowerValue(543);
 				r.setRackId(1);
 				r.setServers(null);
 				r.setState(RackState.OFF);
 				r.setUtilization(20);
 				
 				createInstance(r);
				
 				
	 			// /SERVER
	 					s.setCoolingValue(56);
		 				s.setIdleEnergy(23);
		 				s.setIdServer(1);
		 				s.setName("server1");
		 				s.setUtilization(54);
		 				s.setPowerValue(22);
		 				s.setCpu(cpu);
		 				s.setRam(ram);
		 				s.setHdd(hdd);
		 				s.setState(ServerState.OFF);
		 				s.setCorrespondingVMs(null);
		 				s.setRack(r);
		 				
		 			createInstance(s);
		 			

		 			// VM
		 				List<VirtualMachine> correspVms = new ArrayList<VirtualMachine>();
			 			for (int i = 0; i < 40; i++) {
			 				
			 					VirtualMachine vm = new VirtualMachine();
			 					vm.setCpu(cpu);
			 					vm.setHdd(hdd);
			 					vm.setRam(ram);
			 					vm.setIdVm(1);
			 					vm.setName("vm"+i);
			 					vm.setPowerValue(34);
			 					vm.setState(VMState.SHUT_DOWN);
			 					vm.setServer(s);
			 					createInstance(vm);
			 					
			 					correspVms.add(vm);
			 				}
			 					// now persists the family person relationship
			 					s.setCorrespondingVMs(correspVms);
			 					createInstance(s);
			    
		 			
	
		
		//listCpu();

	}

	private static void listCpu() {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    try {
	      tx = session.beginTransaction();
	      List cpus = session.createQuery("select h from CPU as h").list();
	      for (Iterator iter = cpus.iterator(); iter.hasNext();) {
	        CPU element = (CPU) iter.next();
	        System.out.println(element);
	      }
	      tx.commit();
	    } catch (RuntimeException e) {
	      if (tx != null && tx.isActive()) {
	        try {
	// Second try catch as the rollback could fail as well
	          tx.rollback();
	        } catch (HibernateException e1) {
	          System.out.println("Error");
	        }
	// throw again the first exception
	        throw e;
	      }


	    }
	}
	
	  private static void createInstance(Object o) {
		    Transaction tx = null;
		    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		    try {
		      tx = session.beginTransaction();
		      session.save(o);
		      tx.commit();
		    } catch (RuntimeException e) {
		      if (tx != null && tx.isActive()) {
		        try {
		// Second try catch as the rollback could fail as well
		          tx.rollback();
		        } catch (HibernateException e1) {
		          logger.debug("Error rolling back transaction");
		        }
		// throw again the first exception
		        throw e;
		      }
		    }
		  }

}
