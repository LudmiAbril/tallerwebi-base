package com.tallerwebi.infraestructura;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioPartida")
@Transactional
public class RepositorioPartidaImpl implements RepositorioPartida {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPartidaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Partida partida) throws PartidaConPuntajeNegativoException, IllegalArgumentException, BingoBotEsNullException {
        if (partida == null || partida.getJuego() == null) {
            throw new IllegalArgumentException();
        } else if (partida instanceof PartidaBlackJack && (((PartidaBlackJack) partida).getPuntaje()) < 0) {
            throw new PartidaConPuntajeNegativoException();
        } else if (partida instanceof PartidaBingo && ((PartidaBingo) partida).getSeHizoBingoBot() == null) {
            throw new BingoBotEsNullException();
        }
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
        } else if (id == null) {
            throw new PartidaDeUsuarioNoEncontradaException();
        } else if (juego == null) {
            throw new PartidaDeUsuarioNoEncontradaException();
        } else {
            return partidas;
        }

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

    @Override
    public List<Partida> obtenerPartidasPorFechaRango(Long usuarioId, Juego juego, LocalDateTime of,
            LocalDateTime of2) throws PartidaDeUsuarioNoEncontradaException {
        String hql = "FROM Partida WHERE juego = :juego AND idJugador = :userId AND fechaYhora BETWEEN :fechaInicio AND :fechaFin ORDER BY fechaYhora DESC";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("juego", juego);
        query.setParameter("userId", usuarioId);
        LocalDateTime fechaInicio = of;
        query.setParameter("fechaInicio", fechaInicio);
        LocalDateTime fechaFin = of2;
        query.setParameter("fechaFin", fechaFin);

        List<Partida> partidas = query.getResultList();

        if (partidas.isEmpty()) {
            throw new PartidaDeUsuarioNoEncontradaException();
        }

        return partidas;
    }

    @Override
    public List<PartidaBingo> generarRankingDePartidasDeBingo(Long userId) throws NoHayPartidasDeBingoException {
        String hql = "SELECT DISTINCT pb FROM PartidaBingo pb LEFT JOIN FETCH pb.casillerosMarcados WHERE pb.juego = :juego AND pb.idJugador = :userId ORDER BY pb.fechaYhora DESC";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("juego", Juego.BINGO);
        query.setParameter("userId", userId);
        List<PartidaBingo> partidasBingo = query.getResultList();
        if (partidasBingo.isEmpty()) {
            throw new NoHayPartidasDeBingoException();
        }
        return partidasBingo;
    }

    @Override
    public List<Compra> obtenerCompras(Long id, Juego juego) throws NoHayCompras {
        String hql = "FROM Compra c WHERE c.user.id = :userId AND c.juego = :juego";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("userId", id);
        query.setParameter("juego", juego);

        List<Compra> compras = query.getResultList(); // Utiliza list() en lugar de getResultList()

        if (compras.isEmpty()) {
            throw new NoHayCompras();
        }

        return compras;
    }

    @Override
    public void guardarCompra(Compra compra) throws NoSePudoGuardarLaCompraException {
        if (compra == null) {
            throw new NoSePudoGuardarLaCompraException();
        }

        this.sessionFactory.getCurrentSession().save(compra);
    }

    /*@Override
    public List<PartidaMayorMenor> generarRankingDePartidasMayorMenor(Long userId) throws PartidasDelJuegoNoEncontradasException {
        String hql = "SELECT DISTINCT pmm FROM PartidaMayorMenor pmm LEFT JOIN FETCH pmm.aciertos WHERE pmm.juego = :juego AND pmm.idJugador = :userId ORDER BY pmm.fechaYhora DESC";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("juego", Juego.MAYOR_MENOR);
        query.setParameter("userId", userId);
        List<PartidaMayorMenor> partidasMayorMenor = query.getResultList();
        if (partidasMayorMenor.isEmpty()) {
            throw new PartidasDelJuegoNoEncontradasException();
        }
        return partidasMayorMenor;
    }*/

}
