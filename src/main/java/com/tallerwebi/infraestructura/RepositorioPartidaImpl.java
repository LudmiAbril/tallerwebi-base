package com.tallerwebi.infraestructura;

import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tallerwebi.dominio.Juego;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.RepositorioPartida;
import com.tallerwebi.dominio.excepcion.PartidaDeUsuarioNoEncontradaException;
import com.tallerwebi.dominio.excepcion.PartidasDelJuegoNoEncontradasException;

@Repository("repositorioPartida")
@Transactional
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
    public List<Partida> listarPartidasPorJuego(Juego juego) throws PartidasDelJuegoNoEncontradasException {
        String hql = "FROM Partida WHERE juego = :juego ORDER BY puntaje DESC";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("juego", juego);

        List<Partida> partidas = query.getResultList();

        if (partidas.isEmpty()) {
            throw new PartidasDelJuegoNoEncontradasException();
        }

        return partidas;
    }

    @Override
    public List<Partida> obtenerPartidasUsuario(Long id, Juego juego) throws PartidaDeUsuarioNoEncontradaException {

        String hql = "FROM Partida WHERE juego = :juego AND idJugador = :user ORDER BY puntaje DESC";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("juego", juego);
        query.setParameter("user", id);

        List<Partida> partidas = query.getResultList();

        if (partidas.isEmpty()) {
            throw new PartidaDeUsuarioNoEncontradaException();
        }

        return partidas;
    }

    @Override
    public List<Partida> obtenerPartidasUsuarioPorFecha(Long id, Juego juego)
            throws PartidaDeUsuarioNoEncontradaException {
        String hql = "FROM Partida WHERE juego = :juego AND idJugador = :user ORDER BY fechaYhora DESC";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("juego", juego);
        query.setParameter("user", id);

        List<Partida> partidas = query.getResultList();

        if (partidas.isEmpty()) {
            throw new PartidaDeUsuarioNoEncontradaException();
        }

        return partidas;

    }

}
