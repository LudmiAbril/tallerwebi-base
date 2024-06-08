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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.isNotNull;

import org.apache.commons.codec.digest.DigestUtils;

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

    @Test
    public void queLaContraseñaDelUsuarioSeGuardeEncriptada() {
        Usuario user = new Usuario();
        user.setId(1L);
        user.setEmail("user@gmail.com");
        user.setNombre("usuario");
        user.setConfig(new ConfiguracionesJuego());
        user.setPassword("pass");
        String passEncriptada = DigestUtils.sha512Hex(user.getPassword());

        repositorioUsuario.guardar(user);
        Usuario userGuardado = sessionFactory.getCurrentSession().get(Usuario.class, user.getId());
        String passGuardada = userGuardado.getPassword();
        assertThat(passGuardada, equalTo(passEncriptada));
    }

    @Test
    public void queSePuedaBuscarUnUsuarioPorSuEmail() {
        String email = "buscar@gmail.com";
        Usuario user = crearObjetoUsuario("user", email, "123");
        repositorioUsuario.guardar(user);

        Usuario usuarioEncontrado = repositorioUsuario.buscar(email);
        assertThat(usuarioEncontrado, is(notNullValue()));
        assertThat(usuarioEncontrado, equalTo(user));
    }

    @Test
    public void queSeNoSePuedaBuscarUnUsuarioPorSuEmailSiNoSeEncuentraEnLaBase() {
        String email = "buscar@gmail.com";
        Usuario user = crearObjetoUsuario("user", "otro-email@gmail.com", "123");
        repositorioUsuario.guardar(user);

        Usuario usuarioEncontrado = repositorioUsuario.buscar(email);
        assertThat(usuarioEncontrado, is(nullValue()));
    }

    private Usuario crearObjetoUsuario(String nombre, String email, String pass) {
        Usuario user = new Usuario();
        user.setNombre(nombre);
        user.setEmail(email);
        user.setPassword(pass);
        return user;
    }

}
