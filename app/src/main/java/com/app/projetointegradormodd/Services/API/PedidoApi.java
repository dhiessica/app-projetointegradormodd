package com.app.projetointegradormodd.Services.API;

import android.util.Log;

import com.app.projetointegradormodd.Model.Object.Atividade;
import com.app.projetointegradormodd.Model.Object.Pedido;
import com.app.projetointegradormodd.Model.Object.Produto;
import com.app.projetointegradormodd.Services.AsyncClass;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PedidoApi {

    private String baseUrl = "https://rest-projetointegradormodd.rj.r.appspot.com/";

    private ConexaoListner conexaoListner;

    public interface ConexaoListner{
        void onAsyncId(String response);
        void onAsyncData(Date response);
        void onAsyncObject(Pedido response);
        void onAsyncObjectList(ArrayList<Pedido> response);
    }

    public PedidoApi(ConexaoListner conexaoListner) {
        this.conexaoListner = conexaoListner;
    }

    public void create(String id, Pedido pedido){
        //Instância a classe assincrona esperando a resposta da interface
        AsyncClass classeAssincrona = new AsyncClass(new AsyncClass.ConexaoListner() {
            @Override
            public void onConexaoFinalizada(JSONObject object) {
                try{
                    //Testa se o objeto possui response com a data
                    String stringData = object.getString("response");

                    if (stringData != null){
                        conexaoListner.onAsyncId(stringData);
                    }

                    Log.e(getClass().getName()+" - CREATE",stringData);

                }catch(Exception e){
                    Log.e(getClass().getName()+" - CREATE : ERRO",e.toString());
                }
            }
        });

        //Cria um objeto json
        Gson gson = new Gson();
        //Converte o objeto para json e passa para uma string
        String json = gson.toJson(pedido);

        Log.e("json",json);

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"createPedido","POST",id,json);
    }

    public void update(String id, Pedido pedido){

        //Instância a classe assincrona esperando a resposta da interface
        AsyncClass classeAssincrona = new AsyncClass(new AsyncClass.ConexaoListner() {
            @Override
            public void onConexaoFinalizada(JSONObject object) {

                Log.e(getClass().getName()+" : UPDATE",object.toString());

                try{
                    //Testa se o objeto possui response com a data
                    String stringData = object.getString("response");

                    //Formato de data
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                    //Converte a string para data
                    Date data = dateFormat.parse(stringData);
                    conexaoListner.onAsyncData(data);

                }catch(Exception e){
                    Log.e(getClass().getName()+" - UPDATE : ERRO",e.toString());
                }

            }
        });

        //Cria um objeto json
        Gson gson = new Gson();
        //Converte o objeto para json e passa para uma string
        String json = gson.toJson(pedido);

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"updatePedido","PUT",id,json);

    }

    public void getList(String id){

        //Instância a classe assincrona esperando a resposta da interface
        AsyncClass classeAssincrona = new AsyncClass(new AsyncClass.ConexaoListner() {
            @Override
            public void onConexaoFinalizada(JSONObject object) {

                try{
                    //Array que vai retornar a lista de objetos
                    ArrayList<Pedido> list = new ArrayList<>();

                    //Testa se o objeto possui uma lista de atividades
                    JSONArray jsonArray = object.getJSONArray("pedidos");

                    //Coleta todos os objetos do json Array
                    for (int i=0; i< jsonArray.length(); i++){

                        //Coleta o JSON object do json array
                        JSONObject item = jsonArray.getJSONObject(i);

                        //coleta o objeto do json e adiciona na lista
                        Gson gson = new Gson();
                        Pedido pedido = gson.fromJson(String.valueOf(item), Pedido.class);
                        list.add(pedido);

                    }

                    //Envia o retorno pra interface
                    conexaoListner.onAsyncObjectList(list);

                }catch(Exception e){
                    Log.e(getClass().getName()+" - GET : ERRO",e.toString());
                }
            }
        });

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"getPedidoList","GET",id);
    }

    public void get(String id){

        //Instância a classe assincrona esperando a resposta da interface
        AsyncClass classeAssincrona = new AsyncClass(new AsyncClass.ConexaoListner() {
            @Override
            public void onConexaoFinalizada(JSONObject object) {

                try{
                    //Testa se o objeto possui tutor
                    JSONObject data = object.getJSONObject("pedido");

                    Gson gson = new Gson();
                    Pedido pedido = gson.fromJson(String.valueOf(data), Pedido.class);
                    conexaoListner.onAsyncObject(pedido);


                }catch(Exception e){
                    Log.e(getClass().getName()+" - GET : ERRO",e.toString());
                }
            }
        });

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"getPedido","GET",id);
    }

    public void delete(String id){

        //Instância a classe assincrona esperando a resposta da interface
        AsyncClass classeAssincrona = new AsyncClass(new AsyncClass.ConexaoListner() {
            @Override
            public void onConexaoFinalizada(JSONObject object) {

                Log.e(getClass().getName()+" : DELETE",object.toString());

                try{
                    //Testa se o objeto possui response com a data
                    String stringData = object.getString("response");

                    //Formato de data
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                    //Converte a string para data
                    Date data = dateFormat.parse(stringData);
                    conexaoListner.onAsyncData(data);

                }catch(Exception e){
                    Log.e(getClass().getName()+" - DELETE : ERRO",e.toString());
                }

            }
        });

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"deletePedido","DELETE",id);

    }
}
