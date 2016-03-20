package org.microapp.Diary.generic.service.impl;

import java.io.Serializable;
import java.util.List;

import org.microapp.Diary.generic.dao.GenericAccessDao;
import org.microapp.Diary.generic.model.BaseAccessObject;
import org.microapp.Diary.generic.service.GenericAccessManager;

/**
 * Basically the same class as GenericManagerImpl, but it also implements the getAllForPerson() method.
 * @author Zdenda
 *
 * @param <T>
 * @param <PK>
 */
public class GenericAccessManagerImpl<T extends BaseAccessObject, PK extends Serializable> extends GenericManagerImpl<T, PK> 
		implements GenericAccessManager<T, PK> {

    /**
     * GenericDao instance, set by constructor of child classes
     */
    protected GenericAccessDao<T, PK> dao;


    public GenericAccessManagerImpl() {
    }

    public GenericAccessManagerImpl(GenericAccessDao<T, PK> genericDao) {
    	super(genericDao);
        this.dao = genericDao;
    }

    /**
     * {@inheritDoc}
     */
	public List<T> getAllForPerson(PK personId) {
		// TODO Auto-generated method stub
		return this.dao.getAllForPerson(personId);
	}
}