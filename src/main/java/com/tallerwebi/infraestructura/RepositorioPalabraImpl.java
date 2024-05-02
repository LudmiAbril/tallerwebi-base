package com.tallerwebi.infraestructura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tallerwebi.dominio.Palabra;
import com.tallerwebi.dominio.RepositorioPalabra;

import java.util.Random;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@Repository("repositorioPalabra")
public class RepositorioPalabraImpl implements RepositorioPalabra {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPalabraImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Palabra obtenerUnaPalabraAleatoriaNoAdivinada() {
        final Session session = sessionFactory.getCurrentSession();
        List<Palabra> palabras = session.createCriteria(Palabra.class)
                .add(Restrictions.eq("adivinada", false))
                .list();
        if (!palabras.isEmpty()) {
            return palabras.get(new Random().nextInt(palabras.size()));
        }
        return null;
    }

}