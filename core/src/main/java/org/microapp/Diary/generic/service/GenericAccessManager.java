package org.microapp.Diary.generic.service;

import java.io.Serializable;
import java.util.List;

import org.microapp.Diary.generic.model.BaseAccessObject;

public interface GenericAccessManager<T extends BaseAccessObject, PK extends Serializable> {

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
	
	 /**
     * Generic method used to get all objects of a particular type. This
     * is the same as lookup up all rows in a table.
     * @return List of populated objects
     */
    List<T> getAll();

    /**
     * Generic method to get an object based on class and identifier. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
     *
     * @param id the identifier (primary key) of the object to get
     * @return a populated object
     * @see org.springframework.orm.ObjectRetrievalFailureException
     */
    T get(PK id);

    /**
     * Checks for existence of an object of type T using the id arg.
     * @param id the identifier (primary key) of the object to get
     * @return - true if it exists, false if it doesn't
     */
    boolean exists(PK id);

    /**
     * Generic method to save an object - handles both update and insert.
     * @param object the object to save
     * @return the updated object
     */
    T save(T object);

    /**
     * Generic method to delete an object
     * @param object the object to remove
     */
    void remove(T object);

    /**
     * Generic method to delete an object based on class and id
     * @param id the identifier (primary key) of the object to remove
     */
    void remove(PK id);
}
