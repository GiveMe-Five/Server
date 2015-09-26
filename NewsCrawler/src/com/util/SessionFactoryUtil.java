package com.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryUtil {

	public static SessionFactory sf = null;
	
	public static SessionFactory getSessionFactory(){
		if(sf!=null){
			return sf;
		}
		Configuration config = new Configuration().configure();
		sf = config.buildSessionFactory();
		return sf;
	}
	
}
