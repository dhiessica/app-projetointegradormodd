package com.app.projetointegradormodd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projetointegradormodd.Model.Object.Tutor;
import com.app.projetointegradormodd.Services.API.TutorApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class MenuPerfilTutorActivity extends AppCompatActivity implements View.OnClickListener {

    //Variaveis de layout
    private LinearLayout linearLayoutEditarPerfilTutor;
    private LinearLayout linearLayoutEditarFormaPagamento;
    private LinearLayout linearLayoutEditarEndereco;
    private LinearLayout linearLayoutEditarSair;
    private ImageButton imageButtonHomePet;
    private ImageButton imageButtonHomeLoja;
    private ImageButton imageButtonHomeTutor;
    private TextView textViewNomeTutor;
    
    //Variáveis de dados
    private Tutor tutor;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_perfil_tutor);

        //Inciializa o Firebase Auth e pega o usuario logado
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Faz requisição para a api
        getDataInitialize();


    }
    
    public void getDataInitialize(){
        TutorApi tutorApi = new TutorApi(new TutorApi.ConexaoListner() {
            @Override
            public void onAsyncData(Date response) {
                
            }

            @Override
            public void onAsyncObject(Tutor response) {
                try{
                    //Pega a resposta em uma lista
                    Tutor valido = response;

                    //Verifica se é válido
                    if(valido != null){

                        tutor = valido;
                    }
                    //Inicializa o layout
                    initializeLayout();
                    setDataInitialize();
                }catch (Exception e){
                    Log.e("ERRO", "gettutor:failure", e);
                    Toast.makeText(MenuPerfilTutorActivity.this, "Erro ao tentar acessar dados do tutor.", Toast.LENGTH_SHORT).show();

                    //Inicializa o layout
                    initializeLayout();
                    setDataInitialize();
                }
            }
        });

        tutorApi.get(currentUser.getUid());
    }

    public void setDataInitialize(){
        textViewNomeTutor.setText(tutor.getNome());
    }

    public void initializeLayout(){
        //Refenrecia layout
        linearLayoutEditarPerfilTutor = findViewById(R.id.linear_layout_editar_perfil_tutor_menu);
        linearLayoutEditarFormaPagamento = findViewById(R.id.linear_layout_editar_forma_pagamento_tutor_menu);
        linearLayoutEditarEndereco = findViewById(R.id.linear_layout_editar_endereco_tutor_menu);
        linearLayoutEditarSair = findViewById(R.id.linear_layout_sair_tutor_menu);
        textViewNomeTutor = findViewById(R.id.text_view_nome_tutor_menu_perfil_tutor);
        //Toolbar inferior
        imageButtonHomePet = findViewById(R.id.image_button_home_pet_action_bar);
        imageButtonHomeLoja = findViewById(R.id.image_button_home_loja_action_bar);
        imageButtonHomeTutor = findViewById(R.id.image_button_home_tutor_action_bar);

        linearLayoutEditarPerfilTutor.setOnClickListener(this);
        linearLayoutEditarFormaPagamento.setOnClickListener(this);
        linearLayoutEditarEndereco.setOnClickListener(this);
        linearLayoutEditarSair.setOnClickListener(this);
        imageButtonHomePet.setOnClickListener(this);
        imageButtonHomeLoja.setOnClickListener(this);
        imageButtonHomeTutor.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == linearLayoutEditarPerfilTutor){
            Intent intent = new Intent(MenuPerfilTutorActivity.this,FormPerfilTutorActivity.class);
            intent.putExtra("tutor",tutor);
            intent.putExtra("action",getResources().getString(R.string.action_editar_perfil_tutor));
            startActivity(intent);

        }
        else if (view == linearLayoutEditarFormaPagamento){
            Intent intent = new Intent(MenuPerfilTutorActivity.this,FormFormasPagamentoActivity.class);

            startActivity(intent);

        }
        else if (view == linearLayoutEditarEndereco){
            Intent intent = new Intent(MenuPerfilTutorActivity.this,FormEnderecoActivity.class);
            startActivity(intent);

        }
        else if (view == linearLayoutEditarSair){
            mAuth.signOut();
            currentUser = mAuth.getCurrentUser();

            if (currentUser == null){
                finishAffinity();
                Intent inicial = new Intent(this, InicialActivity.class);
                startActivity(inicial);
            }
        }
        else if (view == imageButtonHomeTutor){
            finish();
        }
        else if (view == imageButtonHomePet){
            Intent intent = new Intent(this, HomePetActivity.class);
            startActivity(intent);
            finish();
        }
        else if (view == imageButtonHomeLoja){
            Intent intent = new Intent(this, HomeLojaActivity.class);
            startActivity(intent);
            finish();
        }

    }

}
