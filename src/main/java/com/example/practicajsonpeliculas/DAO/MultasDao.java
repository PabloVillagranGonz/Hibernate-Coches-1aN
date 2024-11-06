package com.example.practicajsonpeliculas.DAO;

import com.example.practicajsonpeliculas.modelo.Multas;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MultasDao {

    public void guardarMulta(Multas multa, Session session) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(multa);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
        }
    }

    public void eliminarMultas (int id_multa, Session session) {
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            Multas multas = session.get(Multas.class, id_multa);
            session.delete(multas);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println(e.getMessage());
        }
    }

    public void updateMulta(Multas multas, Session session) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(multas);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
        }
    }
}