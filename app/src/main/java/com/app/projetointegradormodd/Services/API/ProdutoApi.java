package com.app.projetointegradormodd.Services.API;

import android.util.Log;

import com.app.projetointegradormodd.Model.Object.Atividade;
import com.app.projetointegradormodd.Model.Object.Produto;
import com.app.projetointegradormodd.Services.AsyncClass;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProdutoApi {

    private String baseUrl = "https://rest-projetointegradormodd.rj.r.appspot.com/";

    private ConexaoListner conexaoListner;

    public interface ConexaoListner{
        void onAsyncData(Date response);
        void onAsyncObject(Produto response);
        void onAsyncObjectList(ArrayList<Produto> response);
    }

    public ProdutoApi(ConexaoListner conexaoListner) {
        this.conexaoListner = conexaoListner;
    }

    public void create(String id, Produto produto){
        //Instância a classe assincrona esperando a resposta da interface
        AsyncClass classeAssincrona = new AsyncClass(new AsyncClass.ConexaoListner() {
            @Override
            public void onConexaoFinalizada(JSONObject object) {
                try{
                    //Testa se o objeto possui response com a data
                    String stringData = object.getString("response");

                    //Formato de data
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

                    //Converte a string para data
                    Date data = dateFormat.parse(stringData);
                    conexaoListner.onAsyncData(data);

                    Log.e(getClass().getName()+" - CREATE",data.toString());

                }catch(Exception e){
                    Log.e(getClass().getName()+" - CREATE : ERRO",e.toString());
                }
            }
        });

        //Cria um objeto json
        Gson gson = new Gson();
        //Converte o objeto para json e passa para uma string
        String json = gson.toJson(produto);

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"createProduto","POST",id,json);
    }

    public void update(String id, Atividade atividade){

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
        String json = gson.toJson(atividade);

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"updateProduto","PUT",id,json);

    }

    public void getList(String id){

        //Instância a classe assincrona esperando a resposta da interface
        AsyncClass classeAssincrona = new AsyncClass(new AsyncClass.ConexaoListner() {
            @Override
            public void onConexaoFinalizada(JSONObject object) {

                try{
                    //Array que vai retornar a lista de objetos
                    ArrayList<Produto> list = new ArrayList<>();

                    //Testa se o objeto possui uma lista de atividades
                    JSONArray jsonArray = object.getJSONArray("produtos");

                    //Coleta todos os objetos do json Array
                    for (int i=0; i< jsonArray.length(); i++){

                        //Coleta o JSON object do json array
                        JSONObject item = jsonArray.getJSONObject(i);

                        //coleta o objeto do json e adiciona na lista
                        Gson gson = new Gson();
                        Produto produto = gson.fromJson(String.valueOf(item), Produto.class);
                        list.add(produto);

                    }

                    //Envia o retorno pra interface
                    conexaoListner.onAsyncObjectList(list);

                }catch(Exception e){
                    Log.e(getClass().getName()+" - GET : ERRO",e.toString());
                }
            }
        });

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"getProdutoList","GET",id);
    }

    public void get(String id){

        //Instância a classe assincrona esperando a resposta da interface
        AsyncClass classeAssincrona = new AsyncClass(new AsyncClass.ConexaoListner() {
            @Override
            public void onConexaoFinalizada(JSONObject object) {

                try{
                    //Testa se o objeto possui tutor
                    JSONObject data = object.getJSONObject("produto");

                    Gson gson = new Gson();
                    Produto produto = gson.fromJson(String.valueOf(data), Produto.class);
                    conexaoListner.onAsyncObject(produto);


                }catch(Exception e){
                    Log.e(getClass().getName()+" - GET : ERRO",e.toString());
                }
            }
        });

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"getProduto","GET",id);
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
        classeAssincrona.execute(baseUrl+"deleteProduto","DELETE",id);

    }
}
