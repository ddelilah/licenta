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
	        }
	        throw e;
	      }
	    }
	    
	    return VMs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VirtualMachine> getAllVMsByState(String state) {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<VirtualMachine> VMs = new ArrayList<VirtualMachine>();
	    try {
	      tx = session.beginTransaction();
	      VMs = session.createQuery("select h from VirtualMachine as h where state='"+state+"'").list();
	      tx.commit();
	    } catch (RuntimeException e) {
	      if (tx != null && tx.isActive()) {
	        try {
	          tx.rollback();
	        } catch (HibernateException e1) {
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
	        }
	        throw e;
	      }
	    }
	    return identified;

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
	
	public String mergeSessionsForVirtualMachine(VirtualMachine vm) {
		Session session = SessionFactoryUtil.getInstance().openSession();
		Query query = session
				.createQuery("from VirtualMachine vm where vm.vmId=:vm_id");
		List<VirtualMachine> queryList = query.setParameter("vm_id",
				vm.getVmId()).list();
		session.close();
		Session session2 = SessionFactoryUtil.getInstance().openSession();
		try {
			if (queryList.size() > 0) {
				session2.beginTransaction();
				VirtualMachine v = (VirtualMachine) session2.get(
						VirtualMachine.class, new Integer(213));
				session2.merge(vm);
				// session2.update(vm);
			} else {
				session2.beginTransaction();
				session2.save(vm);
			}
		} catch (HibernateException e) {
			session2.getTransaction().rollback();
			System.out
					.println("Getting Exception : " + e.getLocalizedMessage());
		} finally {
			session2.getTransaction().commit();
			session2.close();
		}

		return "Successfully data updated into table";

	}

	
}
