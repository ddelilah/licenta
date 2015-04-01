package app.access.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import app.access.CpuDAO;
import app.hibernate.SessionFactoryUtil;
import app.model.CPU;

public class CpuDAOImpl extends GenericDAOImpl implements CpuDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<CPU> getAllCPUs() {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<CPU> CPUs = new ArrayList<CPU>();
	    try {
	      tx = session.beginTransaction();
	      CPUs = session.createQuery("select h from CPU as h").list();
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
	    
	    return CPUs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CPU getCPUById(int cpuId) {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<CPU> CPUs = new ArrayList<CPU>();
	    CPU identified = null;
	    try {
	      tx = session.beginTransaction();
	      CPUs = session.createQuery("select h from CPU as h").list();
	      for (CPU c : CPUs) {
	    	  if(c.getCpuId() == cpuId) {
	    		  identified = c;
	    	  }	 
	      }
	      tx.commit();
	    } catch (RuntimeException e) {
	      if (tx != null && tx.isActive()) {
	        try {
	          tx.rollback();
	        } catch (HibernateException e1) {
	          System.out.println("Error for getCPUById(int cpuId)");
	        }
	        throw e;
	      }
	    }
	    
	    return identified;
	}

}
