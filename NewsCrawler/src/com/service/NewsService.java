package com.service;

import java.util.List;

import com.bean.NewsBean;
import com.dao.EntityDAO;

public class NewsService {

	private EntityDAO<NewsBean> entityDAO = new EntityDAO<NewsBean>();
	
	public void add(NewsBean nb){
		entityDAO.save(nb);
	}
	
	public NewsBean get(int id){
		return entityDAO.get(NewsBean.class, id);
	}
	
	public List<NewsBean> getNewsList(int start, int limit){
		return entityDAO.findByCriteria(NewsBean.class, "id", false, start, limit, null);
	}
	
}
