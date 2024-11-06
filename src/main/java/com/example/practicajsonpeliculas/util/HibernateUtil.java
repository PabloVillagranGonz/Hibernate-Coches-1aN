package com.example.practicajsonpeliculas.util;

import com.example.practicajsonpeliculas.modelo.Multas;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {

	static SessionFactory factory = null;
	static {
		Configuration cfg = new Configuration();
		cfg.configure("configuration/hibernate.cfg.xml");
		cfg.addAnnotatedClass(Multas.class);

		factory = cfg.buildSessionFactory();
	}
	
	public static SessionFactory getSessionFactory() {
		return factory;
	}
	
	public static Session getSession() {
		return factory.openSession();
	}
}
