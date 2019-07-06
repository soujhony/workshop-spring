package com.jhonystein.pedidex.model;

public class Session {

    private final String usuario;
    private final String token;

    public Session(String usuario, String token) {
        this.usuario = usuario;
        this.token = token;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getToken() {
        return token;
    }
    
}
