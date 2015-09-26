package com.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.util.SessionFactoryUtil;

/**
 * Dao层接口的实现类 许多人习惯根据不多的业务逻辑定义不同的DAO层接口，如UserDAO,CommentDAO等等，
 * 这样往往使得编码量十分庞大，而且带来了维护的困难，因此，抽取此DAO层接口，收录大部分 DAO层必须的方法，以供Service层调用。
 * 
 * @author 
 * @version 1.0
 * @param <T>
 *            范型，指实体类
 * @param <PK>
 *            范型，指实体类主键的数据类型，如Integer,Long
 * @see dream.dao.EntityDAO
 */
@SuppressWarnings({"unchecked" })
public class EntityDAO<T>{

	/**
	 * 保存实体
	 * 
	 * @param t
	 *            实体对象
	 */
	public void save(T t) {
		Session session = SessionFactoryUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.save(t);
		tx.commit();
		session.close();
	}

	/**
	 * 更新实体
	 * 
	 * @param t
	 *            实体对象
	 */
	public void update(T t) {
		Session session = SessionFactoryUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.update(t);
		tx.commit();
		session.close();
	}

	/**
	 * 保存实体 包括添加和修改
	 * 
	 * @param t
	 *            实体对象
	 */
	public void saveOrUpdate(T t) {
		Session session = SessionFactoryUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(t);
		tx.commit();
		session.close();
	}


	/**
	 * 删除实体
	 * 
	 * @param t
	 *            实体对象
	 */
	public void delete(T t) {
		Session session = SessionFactoryUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		session.delete(t);
		tx.commit();
		session.close();
	}

	/**
	 * 删除实体
	 * 
	 * @param entityClass
	 *            实体类名
	 * @param id
	 *            实体的ID
	 */
	public void delete(Class<T> entityClass, int id) {
		Session session = SessionFactoryUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		T t = (T) session.get(entityClass, id);
		session.delete(t);
		tx.commit();
		session.close();
	}

	/**
	 * 单查实体
	 * 
	 * @param entityClass
	 *            实体类名
	 * @param id
	 *            实体的ID
	 * @return 实体对象
	 */

	public T get(Class<T> entityClass, int id) {
		Session session = SessionFactoryUtil.getSessionFactory().openSession();
		T t = (T) session.get(entityClass, id);
		return t;
	}

	/**
	 * 查询全部记录列表
	 * 
	 * @param entityClass
	 *            实体类名
	 * @param propertyName
	 *            排序的参照属性
	 * @param isAsc
	 *            排序方式
	 * @param criterions
	 *            查询条件,可为0项或任意多项目
	 * @return 记录List集
	 */
	public List<T> findAll(final Class<T> entityClass,
			final String propertyName, final boolean isAsc,
			final List<Criterion> criterions) {
		int firstResult = 0;
		int maxResults = 0; // 设置为0,则表示查询全部记录
		return findByCriteria(entityClass, propertyName, isAsc, firstResult,
				maxResults, criterions);
	}

	/**
	 * 查询列表
	 * 
	 * @param entityClass
	 *            实体类名
	 * @param propertyName
	 *            排序的参照属性
	 * @param isAsc
	 *            排序方式,true表示升序,false表示降序,当propertyName赋值为null时,此参数无效
	 * @param firstResult
	 *            开始记录序号
	 * @param maxResults
	 *            最大记录数
	 * @param criterions
	 *            查询条件,可有0项或任意多项目
	 * @return 记录List集
	 */

	public List<T> findByCriteria(final Class<T> entityClass,
			final String propertyName, final boolean isAsc,
			final int firstResult, final int maxResults,
			final List<Criterion> criterions) {
		Session session = SessionFactoryUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(entityClass);
		// 按属性条件查询
		if(criterions!=null)
			for (int i = 0; i < criterions.size(); i++) {
				criteria.add(criterions.get(i));
			}
		// 按某个属性排序
		if (null != propertyName) {
			if (isAsc) {
				criteria.addOrder(Order.asc(propertyName));
			} else {
				criteria.addOrder(Order.desc(propertyName));
			}
		}
		// 用于分页查询
		if (maxResults != 0) {
			criteria.setFirstResult(firstResult);
			criteria.setMaxResults(maxResults);
		}
		List<T> list = criteria.list();
		session.close();
		return list;
	}

	/**
	 * 查询总记录数
	 * 
	 * @param entityClass
	 *            实体类名
	 * @param criterions
	 *            查询条件,可有0项或任意多项
	 * @return 总记录数
	 */

	public int findCountsByCriteria(final Class<T> entityClass,
			final List<Criterion> criterions) {
		Session session = SessionFactoryUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(entityClass);
		// 按属性条件查询
		if(criterions!=null)
			for (int i = 0; i < criterions.size(); i++) {
				criteria.add(criterions.get(i));
			}
		int totalCounts = ((Number) criteria.setProjection(
				Projections.rowCount()).uniqueResult())
				.intValue();
		session.close();
		return totalCounts;
	}


	/**
	 * 根据属性值查询列表
	 * 
	 * @param entityClass
	 *            实体类名
	 * @param propertyName
	 *            属性名
	 * @param value
	 *            属性值
	 * @return List列表
	 */
	public List<T> findByProperty(Class<T> entityClass, String propertyName,
			Object value) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		Criterion criterion = Restrictions.eq(propertyName, value);
		criterions.add(criterion);
		List<T> list = findAll(entityClass, null, true, criterions);
		return list;
	}

	/**
	 * 根据属性值列表查询列表
	 * 
	 * @param entityClass
	 *            实体类名
	 * @param properties
	 *            属性表
	 * @return List列表
	 */
	public List<T> findByProperties(final Class<T> entityClass,
			final Map<String, Object> properties) {
		Session session = SessionFactoryUtil.getSessionFactory().openSession();
		Set<String> keys = properties.keySet();
		Criteria criteria = session.createCriteria(entityClass);
		Iterator<String> iter = keys.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			Criterion criterion = Restrictions.eq(key,
					properties.get(key));
			criteria.add(criterion);
		}
		List<T> list = criteria.list();
		session.close();
		return list;
	}

	/**
	 * 根据属性值查询单个对象
	 * 
	 * @param entityClass
	 *            实体类名
	 * @param propertyName
	 *            属性名
	 * @param value
	 *            属性值
	 * @return 实体对象
	 */

	public T findUniqueByProperty(final Class<T> entityClass,
			final String propertyName, final Object value) {
		Session session = SessionFactoryUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(entityClass).add(
				Restrictions.eq(propertyName, value));
		T t = (T) criteria.uniqueResult();
		session.close();
		return t;
	}

	/**
	 * 根据属性值列表查询单个对象
	 * 
	 * @param entityClass
	 *            实体类名
	 * @param properties
	 *            属性表
	 * @return 实体对象
	 */

	public T findUniqueByProperties(final Class<T> entityClass,
			final Map<String, Object> properties) {
		Set<String> keys = properties.keySet();
		Session session = SessionFactoryUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(entityClass);
		Iterator<String> iter = keys.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			Criterion criterion = Restrictions.eq(key,
					properties.get(key));
			criteria.add(criterion);
		}
		T t = (T) criteria.uniqueResult();
		session.close();
		return t;
	}

	/**
	 * 根据属性值查询实体是否存在
	 * 
	 * @param entityClass
	 *            实体类名
	 * @param properties
	 *            属性表
	 * @return 存在则返回true,不存在则返回false
	 */

	public boolean isPropertiesExist(final Class<T> entityClass,
			final Map<String, Object> properties) {
		Set<String> keys = properties.keySet();
		Session session = SessionFactoryUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(entityClass);
		Iterator<String> iter = keys.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			Criterion criterion = Restrictions.eq(key,
					properties.get(key));
			criteria.add(criterion);
		}
		boolean isEmpty = criteria.list().isEmpty();
		session.close();
		return !isEmpty;
	}

	/**
	 * 根据属性值列表查询实体是否存在
	 * 
	 * @param entityClass
	 *            实体类名
	 * @param propertyName
	 *            参照的属性名
	 * @param value
	 *            属性值
	 * @return 存在则返回true,不存在则返回false
	 */

	public boolean isPropertyExist(final Class<T> entityClass,
			final String propertyName, final Object value) {
		Session session = SessionFactoryUtil.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(entityClass)
				.add(Restrictions.eq(propertyName, value));
		boolean isEmpty = criteria.list().isEmpty();
		session.close();
		return !isEmpty;
	}

}