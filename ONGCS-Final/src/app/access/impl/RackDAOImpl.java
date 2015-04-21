package app.access.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import app.access.RackDAO;
import app.hibernate.SessionFactoryUtil;
import app.model.Rack;

public class RackDAOImpl extends GenericDAOImpl implements RackDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Rack> getAllRacks() {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<Rack> racks = new ArrayList<Rack>();
	    try {
	      tx = session.beginTransaction();
	      racks = session.createQuery("select h from Rack as h").list();
	      tx.commit();
	    } catch (RuntimeException e) {
	      if (tx != null && tx.isActive()) {
	        try {
	          tx.rollback();
	        } catch (HibernateException e1) {
	          System.out.println("Error for getAllRacks()");
	        }
	        throw e;
	      }
	    }
	    
	    return racks;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Rack getRackById(int rackId) {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<Rack> racks = new ArrayList<Rack>();
	    Rack identified = null;
	    try {
	      tx = session.beginTransaction();
	      racks = session.createQuery("select h from Rack as h").list();
	      for (Rack r : racks) {
	    	  if(r.getRackId() == rackId) {
	    		  identified = r;
	    	  }	 
	      }
	      tx.commit();
	    } catch (RuntimeException e) {
	      if (tx != null && tx.isActive()) {
	        try {
	          tx.rollback();
	        } catch (HibernateException e1) {
	          System.out.println("Error for getRackById(int rackId)");
	        }
	        throw e;
	      }
	    }
	    
	    return identified;
	}
	
	public String mergeSessionsForRack(Rack r) {
		Session session = SessionFactoryUtil.getInstance().openSession();
		Query query = session
				.createQuery("from Rack r where r.rackId=:rack_id");
		List<Rack> queryList = query.setParameter("rack_id",
				r.getRackId()).list();
		session.close();
		Session session2 = SessionFactoryUtil.getInstance().openSession();
		try {
			if (queryList.size() > 0) {
				session2.beginTransaction();
				Rack rack = (Rack) session2.get(
						Rack.class, new Integer(213));
				session2.merge(r);
				// session2.update(vm);
			} else {
				session2.beginTransaction();
				session2.save(r);
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
