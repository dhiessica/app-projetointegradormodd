package com.app.projetointegradormodd.Model.Object;

import java.io.Serializable;

public class Produto implements Serializable {


    private String id;
    private String idLoja;
    private String foto;
    private String nome;
    private String descricao;
    private String marca;
    private String categoria;
    private Double valor;
    private int quantidade;

    public Produto(String id, String idLoja, String foto, String nome, String descricao, String marca, String categoria,
                   Double valor, int quantidade) {
        super();
        this.id = id;
        this.idLoja = idLoja;
        this.foto = foto;
        this.nome = nome;
        this.descricao = descricao;
        this.marca = marca;
        this.categoria = categoria;
        this.valor = valor;
        this.quantidade = quantidade;
    }

    public Produto() {
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
    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
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
    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
