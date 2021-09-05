package com.app.projetointegradormodd.Model.Object;

import java.io.Serializable;

public class Servico implements Serializable {

    private String id;
    private String idLoja;
    private String nome;
    private String descricao;
    private String categoria;
    private Double valor;

    public Servico(String id, String idLoja, String nome, String descricao, String categoria, Double valor) {
        super();
        this.id = id;
        this.idLoja = idLoja;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.valor = valor;
    }

    public Servico() {
        super();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIdLoja() {
        return idLoja;
    }
    public void setIdLoja(String idLoja) {
        this.idLoja = idLoja;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public Double getValor() {
        return valor;
    }
    public void setValor(Double valor) {
        this.valor = valor;
    }
}
