package com.app.projetointegradormodd.Model.Object;

import java.io.Serializable;
import java.util.Date;

public class Animal implements Serializable {

    private String id;
    private String idTutor;
    private String nome;
    private String dataNascimento;
    private String especie;
    private String raca;
    private String genero;
    private String pelagem;
    private String cor;
    private String porte;
    private String peso;
    private String observacoes;

    public Animal(String id, String idTutor, String nome, String dataNascimento, String especie, String raca,
                  String genero, String pelagem, String cor, String porte, String peso, String observacoes) {
        super();
        this.id = id;
        this.idTutor = idTutor;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.especie = especie;
        this.raca = raca;
        this.genero = genero;
        this.pelagem = pelagem;
        this.cor = cor;
        this.porte = porte;
        this.peso = peso;
        this.observacoes = observacoes;
    }

    public Animal() {
        super();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIdTutor() {
        return idTutor;
    }
    public void setIdTutor(String idTutor) {
        this.idTutor = idTutor;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDataNascimento() {
        return dataNascimento;
    }
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    public String getEspecie() {
        return especie;
    }
    public void setEspecie(String especie) {
        this.especie = especie;
    }
    public String getRaca() {
        return raca;
    }
    public void setRaca(String raca) {
        this.raca = raca;
    }
    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public String getPelagem() {
        return pelagem;
    }
    public void setPelagem(String pelagem) {
        this.pelagem = pelagem;
    }
    public String getCor() {
        return cor;
    }
    public void setCor(String cor) {
        this.cor = cor;
    }
    public String getPorte() {
        return porte;
    }
    public void setPorte(String porte) {
        this.porte = porte;
    }
    public String getPeso() {
        return peso;
    }
    public void setPeso(String peso) {
        this.peso = peso;
    }
    public String getObservacoes() {
        return observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
