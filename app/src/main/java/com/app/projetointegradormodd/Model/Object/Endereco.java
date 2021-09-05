package com.app.projetointegradormodd.Model.Object;

import java.io.Serializable;

public class Endereco implements Serializable {

    private String id;
    private String idRemetente;
    private String rua;
    private int numero;
    private String cep;
    private String cidade;
    private String uf;

    public Endereco(String id, String idRemetente, String rua, int numero, String cep, String cidade, String uf) {
        super();
        this.id = id;
        this.idRemetente = idRemetente;
        this.rua = rua;
        this.numero = numero;
        this.cep = cep;
        this.cidade = cidade;
        this.uf = uf;
    }

    public Endereco() {
        super();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIdRemetente() {
        return idRemetente;
    }
    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }
    public String getRua() {
        return rua;
    }
    public void setRua(String rua) {
        this.rua = rua;
    }
    public int getNumero() {
        return numero;
    }
    public void setNumero(int numero) {
        this.numero = numero;
    }
    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        this.cep = cep;
    }
    public String getCidade() {
        return cidade;
    }
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    public String getUf() {
        return uf;
    }
    public void setUf(String uf) {
        this.uf = uf;
    }
}
