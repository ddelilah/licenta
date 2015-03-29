package app.main;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import app.constants.RackState;
import app.constants.ServerState;
import app.constants.VMState;
import app.model.CPU;
import app.model.HDD;
import app.model.RAM;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class Main {

	 private static final String PERSISTENCE_UNIT_NAME = "components";
	 private static EntityManagerFactory factory;

	
	public static void main(String[] args) {
		  factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		    EntityManager em = factory.createEntityManager();
		    // read the existing entries and write to console
		    Query q = em.createQuery("select t from CPU t");
		    List<CPU> todoList = q.getResultList();
		    for (CPU todo : todoList) {
		      System.out.println(todo);
		    }
		    System.out.println("Size: " + todoList.size());

		    // create new todo
		    em.getTransaction().begin();
		    RAM todo = new RAM();
		    todo.setName("ram2");
		    todo.setCapacity(99);
		    em.persist(todo);
		    
		    CPU cpua = new CPU();
		    cpua.setFrequency(1000);
		    cpua.setCpu_utilization(42);
		    cpua.setName("cpu2");
		    cpua.setNr_cores(4);
		    em.persist(cpua);
		    
		    HDD hdda = new HDD();
		    hdda.setCapacity(120);
		    hdda.setName("hdd2");
		    em.persist(hdda);
		    
//		 			Query q1 = em.createQuery("select m from VirtualMachine m");
//		 			Query q2 = em.createQuery("select n from Rack n");
//		 			Query q3 = em.createQuery("select p from Server p");
//		 			// Persons should be empty
//		 	
//		 			// do we have entries?
//		 			boolean createNewEntries1 = (q1.getResultList().size() == 0);
//		 			boolean createNewEntries2 = (q2.getResultList().size() == 0);
//		 			boolean createNewEntries3 = (q3.getResultList().size() == 0);
//		 	
//		 			Query cpu = em.createQuery("select c from CPU c");
//		 			Query ram = em.createQuery("select d from RAM d");
//		 			Query hdd = em.createQuery("select e from HDD e");
//		 	
//		 			Rack r = new Rack();
//		 			Server s = new Server();
//		 	
//		 			// RACK
//		 			if (createNewEntries2) {
//		 				System.out.println(q2.getResultList().size());
//		 				if(q2.getResultList().size() == 0) {
//		 					r.setCapacity(45);
//			 				r.setCoolingValue(23);
//			 				r.setName("rack1");
//			 				r.setPowerValue(543);
//			 				r.setRackId(1);
//			 				r.setServers(null);
//			 				r.setState(RackState.OFF);
//			 				r.setUtilization(20);
//			 				
//			 				em.persist(r);
//		 				}
//		 	
//		 				
//		 			}
//		 	
//		 			// /SERVER
//		 			if (createNewEntries3) {
//		 				System.out.println(q3.getResultList().size());
//		 				if(q3.getResultList().size() == 0) {
//		 					s.setCoolingValue(56);
//			 				s.setIdleEnergy(23);
//			 				s.setIdServer(1);
//			 				s.setName("server1");
//			 				s.setUtilization(54);
//			 				s.setPowerValue(22);
//			 				s.setCpu((CPU) cpu.getResultList().get(1));
//			 				s.setRam((RAM) ram.getResultList().get(1));
//			 				s.setHdd((HDD) hdd.getResultList().get(1));
//			 				s.setState(ServerState.OFF);
//			 				s.setCorrespondingVMs(null);
//			 				s.setRack(r);
//			 				
//			 				em.persist(s);
//		 					
//		 				}
//		 	
//		 			}
//		 	
//		 			// VM
//		 			if (createNewEntries1) {
//		 				System.out.println(q1.getResultList().size());
//		 				if(q1.getResultList().size() == 0) {
//		 					List<VirtualMachine> correspVms = new ArrayList<VirtualMachine>();
//			 				for (int i = 0; i < 40; i++) {
//			 					
//			 					VirtualMachine vm = new VirtualMachine();
//			 					vm.setCpu((CPU) cpu.getResultList().get(0));
//			 					vm.setHdd((HDD) hdd.getResultList().get(0));
//			 					vm.setRam((RAM) ram.getResultList().get(0));
//			 					vm.setIdVm(1);
//			 					vm.setName("vm"+i);
//			 					vm.setPowerValue(34);
//			 					vm.setState(VMState.SHUT_DOWN);
//			 					vm.setServer(s);
//			 					em.persist(vm);
//			 					
//			 					correspVms.add(vm);
//			 				}
//			 					// now persists the family person relationship
//			 					s.setCorrespondingVMs(correspVms);
//			 					em.persist(s);
//			 				}
//			    
//		 				}
		 			
	
		 				
		 				
		    
		    em.getTransaction().commit();
		    
		    Query qr = em.createQuery("select v from VirtualMachine v");
 			
				// We should have 40 Vms in the database
				System.out.println("Number of vms in the database:" + qr.getResultList().size());
				for(int i = 0; i < qr.getResultList().size(); i++) {
					System.out.println((VirtualMachine)qr.getResultList().get(i));
					VirtualMachine m = (VirtualMachine)qr.getResultList().get(i);
					System.out.println(m.getName());
				}
				
				Query qy = em.createQuery("select s from Server s");
				
						// We should have one server with 40 vms
						System.out.println("Number of servers in the db: " + qy.getResultList().size());
						System.out.println(((Server) qy.getSingleResult()).getCorrespondingVMs().size());


		    em.close();
		    
		  }
}
