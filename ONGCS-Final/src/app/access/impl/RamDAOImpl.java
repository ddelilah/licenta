package app.access.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import app.access.RamDAO;
import app.hibernate.SessionFactoryUtil;
import app.model.RAM;

public class RamDAOImpl extends GenericDAOImpl implements RamDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<RAM> getAllRAMs() {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<RAM> rams = new ArrayList<RAM>();
	    try {
	      tx = session.beginTransaction();
	      rams = session.createQuery("select h from RAM as h").list();
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
	    
	    return rams;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RAM getRAMById(int ramId) {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<RAM> rams = new ArrayList<RAM>();
	    RAM identified = null;
	    try {
	      tx = session.beginTransaction();
	      rams = session.createQuery("select h from RAM as h").list();
	      for (RAM r : rams) {
	    	  if(r.getRamId() == ramId) {
	    		  identified = r;
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

}
