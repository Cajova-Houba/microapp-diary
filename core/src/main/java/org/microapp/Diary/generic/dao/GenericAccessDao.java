package org.microapp.Diary.generic.dao;

import java.io.Serializable;
import java.util.List;

import org.microapp.Diary.generic.model.BaseAccessObject;


/**
 * Use this interface when creating DAOs for entities which implement BaseAccessObject.
 * 
 * This interface also has same methods as GenericDao interface. Just extending GenericDao interface
 * caused problems with overriding so I made a new class.
 * @author Zdenda
 *
 * @param <T>
 * @param <PK>
 */
public interface GenericAccessDao<T extends BaseAccessObject, PK extends Serializable> extends GenericDao<T, PK>{

	/*
	 * ========================
	 * MICROAPPLICATION METHODS
	 * ======================== 
	 */
	
	/**
	 * Returns a List of object which personId field matches provided parameter.
	 * @param personId
	 * @return
	 */
	public List<T> getAllForPerson(PK personId);
	
	
	/*
	 * ===============================
	 * END OF MICROAPPLICATION METHODS
	 * =============================== 
	 */
	
}
