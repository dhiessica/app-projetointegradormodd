package com.app.projetointegradormodd.Model.Object;

import java.io.Serializable;

public class Loja implements Serializable {

    private String id;
    private String nome;
    private String telefone;
    private String email;

    public Loja(String id, String nome, String telefone,String email) {
        super();
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    public Loja() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
