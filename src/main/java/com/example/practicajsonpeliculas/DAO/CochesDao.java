package com.example.practicajsonpeliculas.DAO;


import com.example.practicajsonpeliculas.modelo.Coche;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class CochesDao {

    public void guardarCoche(Coche coche, Session session) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(coche);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
        }
    }

    public void eliminarCoche (int id, Session session) {
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            Coche coche = session.get(Coche.class, id);
            session.delete(coche);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
        }
    }

    public void updateCoche(Coche coche, Session session)   {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(coche);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
        }
    }
}