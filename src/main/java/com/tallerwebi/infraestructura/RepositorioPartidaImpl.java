package com.tallerwebi.infraestructura;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tallerwebi.dominio.Juego;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.RepositorioPartida;

@Repository("repositorioPartida")
public class RepositorioPartidaImpl implements RepositorioPartida {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPartidaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Partida partida) {
        this.sessionFactory.getCurrentSession().save(partida);
    }

    @Override
    public List<Partida> listarPartidasPorJuego(Juego juego) {
        String hql = "FROM Partida WHERE juego = :juego";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("juego", juego);

        return query.getResultList();
    }
    

}
