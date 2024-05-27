package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;

public interface ServicioUsuario {
    Usuario consultarUsuario(String email, String password);

    void registrar(Usuario usuario) throws UsuarioExistente;

    void actualizarConfiguracionesDePartida(Usuario usuario);

}
