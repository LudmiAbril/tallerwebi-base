package com.tallerwebi.dominio;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.tallerwebi.config.HibernateTestConfig;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HibernateTestConfig.class })
public class RepositorioUsuarioTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioUsuarioImpl repositorioUsuario;

    @BeforeEach
    public void init() {
        this.repositorioUsuario = new RepositorioUsuarioImpl(sessionFactory);
    }

    @Test
    public void queSePuedaGuardarUnUsuarioEnLaPlataforma() {
        Usuario user = new Usuario();
        user.setId(1L);
        user.setEmail("user@gmail.com");
        user.setNombre("usuario");
        user.setConfig(new ConfiguracionesJuego());
        user.setPassword("pass");

        repositorioUsuario.guardar(user);
        assertThat(sessionFactory.getCurrentSession().contains(user), equalTo(true));
    }
}
