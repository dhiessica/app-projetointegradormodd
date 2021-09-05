package com.app.projetointegradormodd.Model.Object;

import java.io.Serializable;

public class Atividade implements Serializable {

    private String id;
    private String idAnimal;
    private String titulo;
    private String categoria;
    private String horario;
    private String dataInicial;
    private String dataFinal;
    private String observacoes;

    public Atividade(String id, String idAnimal, String titulo, String categoria, String horario, String dataInicial,
                     String dataFinal, String observacoes) {
        super();
        this.id = id;
        this.idAnimal = idAnimal;
        this.titulo = titulo;
        this.categoria = categoria;
        this.horario = horario;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.observacoes = observacoes;
    }

    public Atividade() {
        super();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIdAnimal() {
        return idAnimal;
    }
    public void setIdAnimal(String idAnimal) {
        this.idAnimal = idAnimal;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getHorario() {
        return horario;
    }
    public void setHorario(String horario) {
        this.horario = horario;
    }
    public String getDataInicial() {
        return dataInicial;
    }
    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }
    public String getDataFinal() {
        return dataFinal;
    }
    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }
    public String getObservacoes() {
        return observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
