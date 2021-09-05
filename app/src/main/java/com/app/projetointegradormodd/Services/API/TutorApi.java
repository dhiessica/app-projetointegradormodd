package com.app.projetointegradormodd.Services.API;

import android.util.Log;
import com.app.projetointegradormodd.Model.Object.Tutor;
import com.app.projetointegradormodd.Services.AsyncClass;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TutorApi {

    private String baseUrl = "https://rest-projetointegradormodd.rj.r.appspot.com/";

    private ConexaoListner conexaoListner;

    public interface ConexaoListner{
        void onAsyncData(Date response);
        void onAsyncObject(Tutor response);
    }

    public TutorApi(ConexaoListner conexaoListner) {
        this.conexaoListner = conexaoListner;
    }

    public void create(String id, Tutor tutor){
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
        String json = gson.toJson(tutor);

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"createTutor","POST",id,json);
    }

    public void update(String id, Tutor tutor){

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
        String json = gson.toJson(tutor);

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"updateTutor","PUT",id,json);

    }

    public void get(String id){

        //Instância a classe assincrona esperando a resposta da interface
        AsyncClass classeAssincrona = new AsyncClass(new AsyncClass.ConexaoListner() {
            @Override
            public void onConexaoFinalizada(JSONObject object) {

                try{
                    //Testa se o objeto possui tutor
                    JSONObject data = object.getJSONObject("tutor");

                    //Cria um objeto tutor e atribui os valores coletados do json
                    /*Tutor tutor = new Tutor();

                    tutor.setId(data.getString("id"));
                    tutor.setNome(data.getString("nome"));
                    tutor.setEmail(data.getString("email"));
                    tutor.setTelefone(data.getString("telefone"));

                     */

                    Gson gson = new Gson();
                    Tutor tutor = gson.fromJson(String.valueOf(data), Tutor.class);
                    conexaoListner.onAsyncObject(tutor);

                    Log.e(getClass().getName()+" : GET",tutor.toString());

                }catch(Exception e){
                    Log.e(getClass().getName()+" - GET : ERRO",e.toString());
                }
            }
        });

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"getTutor","GET",id);

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
        classeAssincrona.execute(baseUrl+"deleteTutor","DELETE",id);

    }
}
