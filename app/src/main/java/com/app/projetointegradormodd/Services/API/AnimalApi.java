package com.app.projetointegradormodd.Services.API;

import android.util.Log;
import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Services.AsyncClass;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AnimalApi {

    private String baseUrl = "https://rest-projetointegradormodd.rj.r.appspot.com/";

    private ConexaoListner conexaoListner;

    public interface ConexaoListner{
        void onAsyncId(String response);
        void onAsyncData(Date response);
        void onAsyncObject(Animal response);
        void onAsyncObjectList(ArrayList<Animal> response);
    }

    public AnimalApi(ConexaoListner conexaoListner) {
        this.conexaoListner = conexaoListner;
    }

    public void create(String id, Animal animal){
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
        String json = gson.toJson(animal);

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"createAnimal","POST",id,json);
    }

    public void update(String id, Animal animal){

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
        String json = gson.toJson(animal);

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"updateAnimal","PUT",id,json);

    }

    public void getList(String id){

        //Instância a classe assincrona esperando a resposta da interface
        AsyncClass classeAssincrona = new AsyncClass(new AsyncClass.ConexaoListner() {
            @Override
            public void onConexaoFinalizada(JSONObject object) {

                try{
                    //Array que vai retornar a lista de objetos
                    ArrayList<Animal> list = new ArrayList<>();

                    //Testa se o objeto possui uma lista de animais
                    JSONArray jsonArray = object.getJSONArray("animais");

                    //Coleta todos os objetos do json Array
                    for (int i=0; i< jsonArray.length(); i++){

                        //Coleta o JSON object do json array
                        JSONObject item = jsonArray.getJSONObject(i);

                        //coleta o objeto do json e adiciona na lista
                        Gson gson = new Gson();
                        Animal animal = gson.fromJson(String.valueOf(item), Animal.class);
                        list.add(animal);

                    }

                    //Envia o retorno pra interface
                    conexaoListner.onAsyncObjectList(list);

                }catch(Exception e){
                    Log.e(getClass().getName()+" - GET : ERRO",e.toString());
                }
            }
        });

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"getAnimalList","GET",id);
    }

    public void get(String id){

        //Instância a classe assincrona esperando a resposta da interface
        AsyncClass classeAssincrona = new AsyncClass(new AsyncClass.ConexaoListner() {
            @Override
            public void onConexaoFinalizada(JSONObject object) {

                try{
                    //Testa se o objeto possui tutor
                    JSONObject data = object.getJSONObject("tutor");

                    Gson gson = new Gson();
                    Animal animal = gson.fromJson(String.valueOf(data), Animal.class);
                    conexaoListner.onAsyncObject(animal);


                }catch(Exception e){
                    Log.e(getClass().getName()+" - GET : ERRO",e.toString());
                }
            }
        });

        //Chama a classe assincrona passando os parâmetros para execução do request
        classeAssincrona.execute(baseUrl+"getAnimal","GET",id);
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
        classeAssincrona.execute(baseUrl+"deleteAnimal","DELETE",id);

    }
}
