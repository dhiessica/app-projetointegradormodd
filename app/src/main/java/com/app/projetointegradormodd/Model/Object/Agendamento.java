package com.app.projetointegradormodd.Model.Object;

import java.io.Serializable;

public class Agendamento implements Serializable {


    private String id;
    private String status;
    private String idAnimal;
    private String idLoja;
    private Animal animal;
    private Servico servico;
    private Endereco endereco;
    private String data;
    private String horario;
    private String tipoTransporte;
    private Double valorTransporte;
    private Double valorTotal;

    public Agendamento(String id, String status, String idAnimal, String idLoja, Animal animal, Servico servico,
                       Endereco endereco, String data, String horario, String tipoTransporte, Double valorTransporte,
                       Double valorTotal) {
        super();
        this.id = id;
        this.status = status;
        this.idAnimal = idAnimal;
        this.idLoja = idLoja;
        this.animal = animal;
        this.servico = servico;
        this.endereco = endereco;
        this.data = data;
        this.horario = horario;
        this.tipoTransporte = tipoTransporte;
        this.valorTransporte = valorTransporte;
        this.valorTotal = valorTotal;
    }

    public Agendamento() {
        super();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getIdAnimal() {
        return idAnimal;
    }
    public void setIdAnimal(String idAnimal) {
        this.idAnimal = idAnimal;
    }
    public String getIdLoja() {
        return idLoja;
    }
    public void setIdLoja(String idLoja) {
        this.idLoja = idLoja;
    }
    public Animal getAnimal() {
        return animal;
    }
    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
    public Servico getServico() {
        return servico;
    }
    public void setServico(Servico servico) {
        this.servico = servico;
    }
    public Endereco getEndereco() {
        return endereco;
    }
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getHorario() {
        return horario;
    }
    public void setHorario(String horario) {
        this.horario = horario;
    }
    public String getTipoTransporte() {
        return tipoTransporte;
    }
    public void setTipoTransporte(String tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }
    public Double getValorTransporte() {
        return valorTransporte;
    }
    public void setValorTransporte(Double valorTransporte) {
        this.valorTransporte = valorTransporte;
    }
    public Double getValorTotal() {
        return valorTotal;
    }
    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
