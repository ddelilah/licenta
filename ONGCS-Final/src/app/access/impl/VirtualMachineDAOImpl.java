package app.access.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import app.access.VirtualMachineDAO;
import app.hibernate.SessionFactoryUtil;
import app.model.VirtualMachine;

public class VirtualMachineDAOImpl extends GenericDAOImpl implements VirtualMachineDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<VirtualMachine> getAllVMs() {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<VirtualMachine> VMs = new ArrayList<VirtualMachine>();
	    try {
	      tx = session.beginTransaction();
	      VMs = session.createQuery("select h from VirtualMachine as h").list();
	      tx.commit();
	    } catch (RuntimeException e) {
	      if (tx != null && tx.isActive()) {
	        try {
	          tx.rollback();
	        } catch (HibernateException e1) {
	          System.out.println("Error for getAllServers()");
	        }
	        throw e;
	      }
	    }
	    
	    return VMs;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public VirtualMachine getVirtualMachineById(int virtualMachineId) {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<VirtualMachine> VMs = new ArrayList<VirtualMachine>();
	    VirtualMachine identified = null;
	    try {
	      tx = session.beginTransaction();
	      VMs = session.createQuery("select h from VirtualMachine as h").list();
	      for (VirtualMachine v : VMs) {
	    	  if(v.getVmId() == virtualMachineId) {
	    		  identified = v;
	    	  }	 
	      }
	      tx.commit();
	    } catch (RuntimeException e) {
	      if (tx != null && tx.isActive()) {
	        try {
	          tx.rollback();
	        } catch (HibernateException e1) {
	          System.out.println("Error for getServerById(int serverId)");
	        }
	        throw e;
	      }
	    }
	    return identified;

	}
	    public void deleteVirtualMachineById(int virtualMachineId) {
			Transaction tx = null;
		    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		    List<VirtualMachine> VMs = new ArrayList<VirtualMachine>();
		    VirtualMachine identified = null;
		    try {
		      tx = session.beginTransaction();
		   //  session.createQuery("delete from VirtualMachine where vm_id="+virtualMachineId).list();
		     String hql = "delete VirtualMachine where vmId = :vmId";
		     Query q = session.createQuery(hql).setParameter("vmId", virtualMachineId);
		     q.executeUpdate();
		      tx.commit();
		    } catch (RuntimeException e) {
		      if (tx != null && tx.isActive()) {
		        try {
		          tx.rollback();
		        } catch (HibernateException e1) {
		          System.out.println("Error for getServerById(int serverId)");
		        }
		        throw e;
		      }
		    }
	    
	}
	public void deleteInstance(VirtualMachine o) {
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			session.delete(o);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					logger.debug("Error rolling back transaction");
				}
				throw e;
			}
		}

	}
	

}
