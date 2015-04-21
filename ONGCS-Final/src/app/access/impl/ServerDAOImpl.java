package app.access.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import app.access.ServerDAO;
import app.hibernate.SessionFactoryUtil;
import app.model.Server;

public class ServerDAOImpl extends GenericDAOImpl implements ServerDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<Server> getAllServers() {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<Server> servers = new ArrayList<Server>();
	    try {
	      tx = session.beginTransaction();
	      servers = session.createQuery("select h from Server as h").list();
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
	    
	    return servers;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Server> getAllServersByState(String state) {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<Server> servers = new ArrayList<Server>();
	    try {
	      tx = session.beginTransaction();
	      servers = session.createQuery("select h from Server as h where state='"+state+"'").list();
	      tx.commit();
	    } catch (RuntimeException e) {
	      if (tx != null && tx.isActive()) {
	        try {
	          tx.rollback();
	        } catch (HibernateException e1) {
	          System.out.println("Error for getAllServersByState()");
	        }
	        throw e;
	      }
	    }
	    
	    return servers;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Server getServerById(int serverId) {
		Transaction tx = null;
	    Session session = SessionFactoryUtil.getInstance().getCurrentSession();
	    List<Server> servers = new ArrayList<Server>();
	    Server identified = null;
	    try {
	      tx = session.beginTransaction();
	      servers = session.createQuery("select h from Server as h").list();
	      for (Server s : servers) {
	    	  if(s.getServerId() == serverId) {
	    		  identified = s;
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
	
	public String mergeSessionsForServer(Server s) {
		Session session = SessionFactoryUtil.getInstance().openSession();
		Query query = session
				.createQuery("from Server s where s.serverId=:server_id");
		List<Server> queryList = query.setParameter("server_id",
				s.getServerId()).list();
		session.close();
		Session session2 = SessionFactoryUtil.getInstance().openSession();
		try {
			if (queryList.size() > 0) {
				session2.beginTransaction();
				Server srv = (Server) session2.get(
						Server.class, new Integer(213));
				session2.merge(s);
				// session2.update(vm);
			} else {
				session2.beginTransaction();
				session2.save(s);
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
