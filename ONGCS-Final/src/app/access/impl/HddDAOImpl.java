package app.access.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import app.access.HddDAO;
import app.hibernate.SessionFactoryUtil;
import app.model.HDD;

public class HddDAOImpl extends GenericDAOImpl implements HddDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<HDD> getAllHDDs() {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<HDD> HDDs = new ArrayList<HDD>();
	    try {
	      tx = session.beginTransaction();
	      HDDs = session.createQuery("select h from HDD as h").list();
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
	    
	    return HDDs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HDD getHDDById(int hddId) {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<HDD> HDDs = new ArrayList<HDD>();
	    HDD identified = null;
	    try {
	      tx = session.beginTransaction();
	      HDDs = session.createQuery("select h from HDD as h").list();
	      for (HDD h : HDDs) {
	    	  if(h.getHddId() == hddId) {
	    		  identified = h;
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
