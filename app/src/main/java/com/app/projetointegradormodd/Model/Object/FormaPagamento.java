package com.app.projetointegradormodd.Model.Object;

import java.io.Serializable;

public class FormaPagamento implements Serializable {

    private String id;
    private String idRemetente;
    private String nomeTitular;
    private String cpfTitular;
    private String numeroCartao;
    private String validade;
    private String cvv;

    public FormaPagamento(String id, String idRemetente, String nomeTitular, String cpfTitular, String numeroCartao,
                          String validade, String cvv) {
        super();
        this.id = id;
        this.idRemetente = idRemetente;
        this.nomeTitular = nomeTitular;
        this.cpfTitular = cpfTitular;
        this.numeroCartao = numeroCartao;
        this.validade = validade;
        this.cvv = cvv;
    }

    public FormaPagamento() {
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
    public String getNomeTitular() {
        return nomeTitular;
    }
    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }
    public String getCpfTitular() {
        return cpfTitular;
    }
    public void setCpfTitular(String cpfTitular) {
        this.cpfTitular = cpfTitular;
    }
    public String getNumeroCartao() {
        return numeroCartao;
    }
    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }
    public String getValidade() {
        return validade;
    }
    public void setValidade(String validade) {
        this.validade = validade;
    }
    public String getCvv() {
        return cvv;
    }
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
