package app.access.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import app.access.CoolingDAO;
import app.hibernate.SessionFactoryUtil;
import app.model.Cooling;

public class CoolingDAOImpl extends GenericDAOImpl implements CoolingDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Cooling> getAllCooling() {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<Cooling> cooling = new ArrayList<Cooling>();
	    try {
	      tx = session.beginTransaction();
	      cooling = session.createQuery("select h from Cooling as h").list();
	      tx.commit();
	    } catch (RuntimeException e) {
	      if (tx != null && tx.isActive()) {
	        try {
	          tx.rollback();
	        } catch (HibernateException e1) {
	          System.out.println("Error for getAllCPUs()");
	        }
	        throw e;
	      }
	    }
	    
	    return cooling;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Cooling getCoolingById(int coolingId) {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<Cooling> cooling = new ArrayList<Cooling>();
	    Cooling identified = null;
	    try {
	      tx = session.beginTransaction();
	      cooling = session.createQuery("select h from Cooling as h").list();
	      for (Cooling c : cooling) {
	    	  if(c.getCoolingId() == coolingId) {
	    		  identified = c;
	    	  }	 
	      }
	      tx.commit();
	    } catch (RuntimeException e) {
	      if (tx != null && tx.isActive()) {
	        try {
	          tx.rollback();
	        } catch (HibernateException e1) {
	          System.out.println("Error for getCoolingById(int coolingId)");
	        }
	        throw e;
	      }
	    }
	    
	    return identified;
	}
	
	

}
