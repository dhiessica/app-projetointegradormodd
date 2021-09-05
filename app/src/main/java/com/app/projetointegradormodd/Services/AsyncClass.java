package com.app.projetointegradormodd.Services;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
//import org.apache.commons.compress.utils.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncClass extends AsyncTask<String,Void,String> {

    public interface ConexaoListner{
        void onConexaoFinalizada(JSONObject object);
    }

    private ConexaoListner conexaoListner;

    public AsyncClass(ConexaoListner conexaoListner) {
        this.conexaoListner = conexaoListner;
    }

    @Override
    protected String doInBackground(String... strings) {

        //Recebe parÂmetros para fazer a conexão
        String urlString = strings[0];
        String method = strings[1];
        String header = strings[2];

        try {
            //Objeto url ecebe a string parametrizando
            URL url = new URL(urlString);

            //Iicia configuração da conexão
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("id", header);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);

            //Verifica se é algum metodo que envia dados e configura
            if (method.equals("POST") || method.equals("PUT")){

                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod(method);
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);
                String body = strings[3];

                //Insere a variavel com os dads do request body
                try(OutputStream os = urlConnection.getOutputStream()) {
                    byte[] input = body.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

            }else if(method.equals("GET")){
                //OBS: MetoDo get teve que ser chamado como POST para corrigir erro de requisição
                urlConnection.setRequestMethod("POST");
            }else{
                urlConnection.setRequestMethod(method);
            }

            //Abre a conexão com a url encima de um protocolo http
            urlConnection.connect();

            //Verifica o retorno da requisição
            if (urlConnection.getResponseCode() == 200){
                //Pega a resposta da requisição
                InputStream inputStream = urlConnection.getInputStream();

                //Passa o retorno para string
                return IOUtils.toString(inputStream,"UTF-8");

            }else{
                return null;
            }


        } catch (Exception e) {
            Log.e("ASYNC CLASS ERRO : ",""+e);
            return null;
        }
    }

    @Override
    public void onPostExecute(String s) {
        super.onPostExecute(s);

        try {

            //Tenta converter o objeto direto
            JSONObject convertedObject = new JSONObject(s);

            //Chama a interface
            conexaoListner.onConexaoFinalizada(convertedObject);

        } catch (Exception e) {

            try{
                //Pega a resposta e insere em uma string formatada pra json
                String json = "{ 'response': '"+s+"' }";

                //Converte a string em json
                JSONObject convertedObject = new JSONObject(json);

                //Chama a interface
                conexaoListner.onConexaoFinalizada(convertedObject);

            }catch(Exception ex){conexaoListner.onConexaoFinalizada(null);}
        }

    }
}