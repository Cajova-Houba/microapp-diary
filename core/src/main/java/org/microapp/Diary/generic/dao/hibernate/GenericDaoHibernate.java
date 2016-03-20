package org.microapp.Diary.generic.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import org.microapp.Diary.generic.dao.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateTemplate;
/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * <p/>
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *      &lt;bean id="fooDao" class="org.appfuse.dao.hibernate.GenericDaoHibernate"&gt;
 *          &lt;constructor-arg value="org.appfuse.model.Foo"/&gt;
 *      &lt;/bean&gt;
 * </pre>
 *
 * @param <T>  a type variable
 * @param <PK> the primary key for that type
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 *         Updated by jgarcia: update hibernate3 to hibernate4
 * @author jgarcia (update: added full text search + reindexing)
 */
public class GenericDaoHibernate<T, PK extends Serializable> implements GenericDao<T, PK> {

	/**
     * Log variable for all child classes.
     */
	protected static final Logger log = LogManager.getLogger(GenericDaoHibernate.class);
    private Class<T> persistentClass;
    private HibernateTemplate hibernateTemplate;
    private SessionFactory sessionFactory;

    /**
     * Constructor that takes in a class to see which type of entity to persist.
     * Use this constructor when subclassing.
     *
     * @param persistentClass the class type you'd like to persist
     */
    /**
     * Constructor that takes in a class to see which type of entity to persist.
     * Use this constructor when subclassing.
     *
     * @param persistentClass the class type you'd like to persist
     */
    public GenericDaoHibernate(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    /**
     * Constructor that takes in a class and sessionFactory for easy creation of
     * DAO.
     *
     * @param persistentClass the class type you'd like to persist
     * @param sessionFactory the pre-configured Hibernate SessionFactory
     */
    public GenericDaoHibernate(final Class<T> persistentClass, SessionFactory sessionFactory) {
        this.persistentClass = persistentClass;
        this.sessionFactory = sessionFactory;
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    public HibernateTemplate getHibernateTemplate() {
        return this.hibernateTemplate;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    @Autowired
    @Required
    public void setSessionFactory(@Qualifier("sessionFactoryDiary")SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return hibernateTemplate.loadAll(this.persistentClass);
    }

    public Class<T> getPersistentClass() {
    	return this.persistentClass;
    }
    
    /**
     * {@inheritDoc}
     */
//    public List<T> search(String searchTerm) throws SearchException {
//        Session sess = getSession();
//        FullTextSession txtSession = Search.getFullTextSession(sess);
//
//        org.apache.lucene.search.Query qry;
//        try {
//            qry = HibernateSearchTools.generateQuery(searchTerm, this.persistentClass, sess, defaultAnalyzer);
//        } catch (ParseException ex) {
//            throw new SearchException(ex);
//        }
//        org.hibernate.search.FullTextQuery hibQuery = txtSession.createFullTextQuery(qry,
//                this.persistentClass);
//        return hibQuery.list();
//    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAllDistinct() {
        Collection result = new LinkedHashSet(getAll());
        return new ArrayList(result);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T get(PK id) {
        T entity = (T) hibernateTemplate.get(this.persistentClass, id);

        if (entity == null) {
            log.warn("Uh oh, '" + this.persistentClass + "' object with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(this.persistentClass, id);
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public boolean exists(PK id) {
        T entity = (T) hibernateTemplate.get(this.persistentClass, id);
        return entity != null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T save(T object) {
        return (T) hibernateTemplate.merge(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(PK id) {
    	hibernateTemplate.delete(this.get(id));
    }

    /**
     * {@inheritDoc}
     */
    public void reindex() {
        HibernateSearchTools.reindex(persistentClass, getSessionFactory().getCurrentSession());
    }


    /**
     * {@inheritDoc}
     */
    public void reindexAll(boolean async) {
        HibernateSearchTools.reindexAll(async, getSessionFactory().getCurrentSession());
    }

	public void remove(T object) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List find(String query, Map<Object, Object> parameters) {
		Session session = getSessionFactory().openSession();
		Query q = session.createQuery(query);
		
		for(Object key : parameters.keySet()) {
			log.debug("Parameter: "+key+","+parameters.get(key));
			
			if(key instanceof String) {
				q.setParameter((String)key, parameters.get(key));
			} else if(key instanceof Integer) {
				q.setParameter((Integer)key, parameters.get(key));
			}
			
		}
		
		log.debug(q.getQueryString());
		List result = q.list();
		
		session.close();
		
		return result;
	}
}

