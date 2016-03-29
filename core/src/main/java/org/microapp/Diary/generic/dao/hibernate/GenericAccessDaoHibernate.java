package org.microapp.Diary.generic.dao.hibernate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.microapp.Diary.generic.dao.GenericAccessDao;
import org.microapp.Diary.generic.model.BaseAccessObject;

public class GenericAccessDaoHibernate<T extends BaseAccessObject, PK extends Serializable> extends GenericDaoHibernate<T, PK>
		implements GenericAccessDao<T, PK> {

	
	public GenericAccessDaoHibernate(final Class<T> persistentClass) {
		super(persistentClass);
	}

	public GenericAccessDaoHibernate(final Class<T> persistentClass, SessionFactory sessionFactory) {
		super(persistentClass, sessionFactory);
	} 
	 
	/**
     * {@inheritDoc}
     */
	public List<T> getAllForPerson(PK personId) {
		String query = "SELECT obj FROM "+getPersistentClass().getName()+ " obj WHERE "+BaseAccessObject.ACCESS_ID_COLUMN_NAME+"=?";
		Map<Object, Object> parameters = new HashMap<Object, Object>(1);
		parameters.put(0, personId);
		
		return find(query, parameters);
	}
}
