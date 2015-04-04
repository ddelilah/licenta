package app.access.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
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

}
