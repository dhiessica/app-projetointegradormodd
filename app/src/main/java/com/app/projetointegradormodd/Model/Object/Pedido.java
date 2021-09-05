package com.app.projetointegradormodd.Model.Object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Pedido implements Serializable {

    private String id;
    private String status;
    private String idTutor;
    private String idLoja;
    private ArrayList<Produto> produtos;
    private Endereco enderecoEntrega;
    private FormaPagamento formaPagamento;
    private String data;
    private String hora;
    private String previsaoEntrega;
    private String valorTransporte;
    private String valorTotal;

    public Pedido(String id, String status, String idTutor, String idLoja, ArrayList<Produto> produtos, Endereco enderecoEntrega,
                  FormaPagamento formaPagamento, String data, String hora, String previsaoEntrega, String valorTransporte,
                  String valorTotal) {
        this.id = id;
        this.status = status;
        this.idTutor = idTutor;
        this.idLoja = idLoja;
        this.produtos = produtos;
        this.enderecoEntrega = enderecoEntrega;
        this.formaPagamento = formaPagamento;
        this.data = data;
        this.hora = hora;
        this.previsaoEntrega = previsaoEntrega;
        this.valorTransporte = valorTransporte;
        this.valorTotal = valorTotal;
    }

    public Pedido() {
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
    public String getIdTutor() {
        return idTutor;
    }
    public void setIdTutor(String idTutor) {
        this.idTutor = idTutor;
    }

    public String getIdLoja() {
        return idLoja;
    }

    public void setIdLoja(String idLoja) {
        this.idLoja = idLoja;
    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }
    public void setProdutos(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }
    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }
    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }
    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }
    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }
    public String getPrevisaoEntrega() {
        return previsaoEntrega;
    }
    public void setPrevisaoEntrega(String previsaoEntrega) {
        this.previsaoEntrega = previsaoEntrega;
    }
    public String getValorTransporte() {
        return valorTransporte;
    }
    public void setValorTransporte(String valorTransporte) {
        this.valorTransporte = valorTransporte;
    }
    public String getValorTotal() {
        return valorTotal;
    }
    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }
}
