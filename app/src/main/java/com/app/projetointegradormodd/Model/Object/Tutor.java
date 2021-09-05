package com.app.projetointegradormodd.Model.Object;

import java.io.Serializable;

public class Tutor implements Serializable {

    private String id;
    private String nome;
    private String email;
    private String telefone;

    public Tutor(String id, String nome, String email, String telefone) {
        super();
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public Tutor() {
        super();
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
