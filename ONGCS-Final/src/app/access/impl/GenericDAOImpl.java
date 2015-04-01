package app.access.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.access.GenericDAO;
import app.hibernate.SessionFactoryUtil;

public class GenericDAOImpl implements GenericDAO {

	final static Logger logger = LoggerFactory.getLogger(GenericDAOImpl.class);

	@Override
	public void createInstance(Object o) {
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			session.save(o);
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

	@Override
	public void deleteInstance(Object o) {
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

	@Override
	public void updateInstance(Object o) {
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			session.update(o);
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
