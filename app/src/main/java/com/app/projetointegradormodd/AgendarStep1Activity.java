package com.app.projetointegradormodd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projetointegradormodd.Adapter.AgendarStep1Adapter;
import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Model.Object.Servico;
import com.app.projetointegradormodd.Services.API.AnimalApi;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AgendarStep1Activity extends AppCompatActivity implements View.OnClickListener {

    //Variáreis de layout
    private Toolbar toolbar;
    private RecyclerView recyclerViewAnimais;
    private Button buttonContinuar;
    private LinearLayout noData;
    private TextView noDataText;

    //Variáreis de dados
    private Servico servico;
    private ArrayList<Animal> animais = new ArrayList<>();
    private AgendarStep1Adapter animalAdapter;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_step1);

        //Inciializa o Firebase Auth e pega o usuario logado
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Recebe dados da tela anterior
        recebeDados();

        //Inicializa o layout
        initializeLayout();

        //Inicializa pegando dados da API e configurando/referenciando o layout
        getDataInitialize();

        setUpLayout();


    }

    public void recebeDados(){

        //Recebe dados da tela se serviços
        Bundle extras = getIntent().getExtras();
        if(extras != null){

            //Atribuindo dados ao objeto
            servico = new Servico();
            servico = (Servico) getIntent().getSerializableExtra("servico_item_click");

        }
    }

    public void getDataInitialize(){

        //Trata a resposta da classe intermediária da chamada da Api
        AnimalApi animalApi = new AnimalApi(new AnimalApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {

            }

            @Override
            public void onAsyncData(Date response) {

            }

            @Override
            public void onAsyncObject(Animal response) {

            }

            @Override
            public void onAsyncObjectList(ArrayList<Animal> response) {
                try{
                    //Pega a resposta em uma lista
                    ArrayList<Animal> valido = response;

                    //Verifica se é válido
                    if(valido != null){

                        //Atribui o array list da resposta para o utilizado na aplicação
                        animais = valido;
                    }
                    //Referencia e insere ações do layout
                    setUpLayout();
                }catch (Exception e){
                    Log.e("ERRO", "getpedidoslist:failure", e);
                    Toast.makeText(AgendarStep1Activity.this, "Ops! Acho que você não possui pets cadastrados.", Toast.LENGTH_SHORT).show();

                    //Referencia e insere ações do layout
                    setUpLayout();
                }
            }
        });


        //Chama método get da classe intermediária
        animalApi.getList(currentUser.getUid());

    }

    private void initializeLayout() {
        setUpToolbars();
        //Referencia as variaveis de layot
        buttonContinuar = findViewById(R.id.button_continuar_agendar_step1);
        recyclerViewAnimais = findViewById(R.id.recycler_view_animais_agendar_step1);
    }

    public void setUpToolbars(){
        //Toolbar superior
        toolbar = findViewById(R.id.toolbar_superior);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText(getResources().getString(R.string.nome_tela_agendar_serviço));

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //Mostrar seta de voltar na toolbar
        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //ACTION BAR ACTIONS QUANDO SELECIONADOS

        //Ação da seta de voltar na toolbar
        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUpLayout(){
        noData = findViewById(R.id.container_no_data);
        noDataText = findViewById(R.id.text_no_data);
        if (animais.size() == 0 || animais == null){
            noData.setVisibility(View.VISIBLE);
            noDataText.setText("Ops! Você ainda não cadastrou um pet.");
        }else{
            noData.setVisibility(View.INVISIBLE);
        }
        recyclerViewAnimais.setLayoutManager(new LinearLayoutManager(this));
        animalAdapter = new AgendarStep1Adapter(this,animais);
        recyclerViewAnimais.setAdapter(animalAdapter);


        //Ação do botão
        buttonContinuar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == buttonContinuar){
            if (animais.size()>0) {
                animalAdapter.sentDataNextStep(servico);
            }

        }
    }
}
