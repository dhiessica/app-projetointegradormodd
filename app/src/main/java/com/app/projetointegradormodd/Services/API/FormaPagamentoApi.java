package com.app.projetointegradormodd.Services.API;

import android.util.Log;

import com.app.projetointegradormodd.Model.Object.FormaPagamento;
import com.app.projetointegradormodd.Model.Object.Loja;
import com.app.projetointegradormodd.Services.AsyncClass;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormaPagamentoApi {

    private String baseUrl = "https://rest-projetointegradormodd.rj.r.appspot.com/";

    private ConexaoListner conexaoListner;

    public interface ConexaoListner{
        void onAsyncId(String response);
        void onAsyncData(Date response);
        void onAsyncObject(FormaPagamento response);
    }

    public FormaPagamentoApi(ConexaoListner conexaoListner) {
        this.conexaoListner = conexaoListner;
    }

    public void create(String id, FormaPagamento formaPagamento){
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
        String json = gson.toJson(formaPagamento);

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"createFormaPagamento","POST",id,json);
    }

    public void update(String id, FormaPagamento formaPagamento){

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

                    Log.e(getClass().getName()+" : UPDATE",object.toString());

                }catch(Exception e){
                    Log.e(getClass().getName()+" - UPDATE : ERRO",e.toString());
                }

            }
        });

        //Cria um objeto json
        Gson gson = new Gson();
        //Converte o objeto para json e passa para uma string
        String json = gson.toJson(formaPagamento);

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"updateFormaPagamento","PUT",id,json);

    }

    public void get(String id){

        //Instância a classe assincrona esperando a resposta da interface
        AsyncClass classeAssincrona = new AsyncClass(new AsyncClass.ConexaoListner() {
            @Override
            public void onConexaoFinalizada(JSONObject object) {

                try{
                    //Testa se o objeto possui loja
                    JSONObject data = object.getJSONObject("formaPagamento");

                    Gson gson = new Gson();
                    FormaPagamento formaPagamento = gson.fromJson(String.valueOf(data), FormaPagamento.class);
                    conexaoListner.onAsyncObject(formaPagamento);


                }catch(Exception e){
                    Log.e(getClass().getName()+" - GET : ERRO",e.toString());
                }
            }
        });

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"getFormaPagamento","GET",id);

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
        classeAssincrona.execute(baseUrl+"deleteFormaPagamento","DELETE",id);

    }
}
