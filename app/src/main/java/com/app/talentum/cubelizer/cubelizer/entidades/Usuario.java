package com.app.talentum.cubelizer.cubelizer.entidades;

/**
 * Created by Daboom on 28/11/2016.
 */

public class Usuario {
    String usuario;
    String password;
    public Usuario(){
        super();
    }
    public Usuario(String user, String pass){
        this.usuario = user;
        this.password = pass;

    }
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
