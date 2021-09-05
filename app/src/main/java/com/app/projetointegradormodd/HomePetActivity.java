package com.app.projetointegradormodd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projetointegradormodd.Adapter.HomePetPageViewAdapter;
import com.app.projetointegradormodd.Model.Object.Agendamento;
import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Model.Object.Atividade;
import com.app.projetointegradormodd.Services.API.AgendamentoApi;
import com.app.projetointegradormodd.Services.API.AtividadeApi;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class HomePetActivity extends AppCompatActivity implements View.OnClickListener, TabLayout.BaseOnTabSelectedListener {

    //Variaveis para referênciar o layout
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton imageButtonHomePet;
    private ImageButton imageButtonHomeLoja;
    private ImageButton imageButtonHomeTutor;
    private HomePetPageViewAdapter adapter;
    private TextView mTitle;

    //Variáveis de dados
    private Animal currentAnimal;
    private ArrayList<Atividade> atividades = new ArrayList<>();
    private ArrayList<Agendamento> agendamentos = new ArrayList<>();

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pet);

        //Incializa o Firebase Auth e pega o usuario logado
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Chama a activity pra selecionar o pet
        if (currentAnimal == null){
            Intent intent = new Intent(this, MenuPerfilPetActivity.class);
            startActivityForResult(intent,1);
        }

        //Inicializa o layout
        initializeLayout();

    }

    public void getDataInitialize(){

        //Trata a resposta da classe intermediária da chamada da Api
        AtividadeApi atividadeApi = new AtividadeApi(new AtividadeApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {

            }

            @Override
            public void onAsyncData(Date response) {

            }

            @Override
            public void onAsyncObject(Atividade response) {

            }

            @Override
            public void onAsyncObjectList(ArrayList<Atividade> response) {
                try{
                    //Pega a resposta em uma lista
                    ArrayList<Atividade> valido = response;

                    //Verifica se é válido
                    if(valido != null){

                        //Atribui à lista do layout
                        atividades = valido;

                    }

                    //Chama metodos para buscar os outros dados no banco
                    getDataInitializeAgendamentos();

                }catch (Exception e){
                    Log.e("ERRO", "getatividadeslist:failure", e);
                    Toast.makeText(HomePetActivity.this, "Erro ao tentar coletar lista.", Toast.LENGTH_SHORT).show();

                    //Chama metodos para buscar os outros dados no banco
                    getDataInitializeAgendamentos();
                }
            }
        });

        //Chama método get da classe intermediária
        atividadeApi.getList(currentAnimal.getId());

    }

    public void getDataInitializeAgendamentos(){

        //Trata a resposta da classe intermediária da chamada da Api
        AgendamentoApi agendamentoApi = new AgendamentoApi(new AgendamentoApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {

            }

            @Override
            public void onAsyncData(Date response) {

            }

            @Override
            public void onAsyncObject(Agendamento response) {

            }

            @Override
            public void onAsyncObjectList(ArrayList<Agendamento> response) {

                try{
                    //Pega a resposta em uma lista
                    ArrayList<Agendamento> valido = response;

                    //Verifica se é válido
                    if(valido != null){

                        //Atribui à lista do layout
                        agendamentos = valido;

                    }

                    //Referencia e insere ações do layout
                    setUpLayout();

                }catch (Exception e){
                    Log.e("ERRO", "getagendamentoslist:failure", e);
                    Toast.makeText(HomePetActivity.this, "Erro ao tentar coletar lista.", Toast.LENGTH_SHORT).show();

                    //Referencia e insere ações do layout
                    setUpLayout();
                }
            }
        });

        //Chama método get da classe intermediária
        agendamentoApi.getList(currentAnimal.getId());

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null){
            Intent inicial = new Intent(this, InicialActivity.class);
            startActivity(inicial);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK){
            currentAnimal = (Animal) data.getSerializableExtra("animal_selecionado");

            //Inicializa pegando dados da API e configurando/referenciando o layout
            getDataInitialize();

            mTitle.setText(getResources().getString(R.string.nome_tela_home_pet) );

            setUpLayout();

        }
        else if (requestCode == 2 && resultCode == RESULT_OK) {
            Atividade atividade = (Atividade) data.getSerializableExtra("atividade_create");
            currentAnimal = (Animal) data.getSerializableExtra("animal_create");

            criaAtividade(atividade, currentAnimal);


        }
        else if (requestCode == 3 && resultCode == RESULT_OK) {
            Atividade atividade = (Atividade) data.getSerializableExtra("atividade_update");
            currentAnimal = (Animal) data.getSerializableExtra("animal_update");

            updateAtividade(atividade);

        }


    }

    private void initializeLayout() {

        //Referencia as variaveis de layot
        setUpToolbars();

        //Adicionando tabs
        tabLayout = findViewById(R.id.tab_layout_home_pet);

        tabLayout.addTab(tabLayout.newTab().setText("ATIVIDADES"));
        tabLayout.addTab(tabLayout.newTab().setText("AGENDAMENTOS"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Inicializando ViewPager
        viewPager = findViewById(R.id.view_pager_home_pet);

        //Adicionando onTabSelectedListener para deslizar as views
        tabLayout.setOnTabSelectedListener(this);

    }

    public void setUpLayout(){

        try {

            //Criando PagerAdapter
            adapter = new HomePetPageViewAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), atividades, agendamentos, currentAnimal, new HomePetPageViewAdapter.PageviewListner() {
                @Override
                public void onCreateAtividadeClick() {
                    Intent intent = new Intent(HomePetActivity.this, FormAtividadeActivity.class);
                    intent.putExtra("action",getResources().getString(R.string.nome_tela_criar_atividade));
                    intent.putExtra("animal",currentAnimal);
                    startActivityForResult(intent,2);
                }

                @Override
                public void onUpdateAtividadeClick(Atividade atividade) {
                    Intent intent = new Intent(HomePetActivity.this, FormAtividadeActivity.class);
                    intent.putExtra("action",getResources().getString(R.string.nome_tela_editar_atividade));
                    intent.putExtra("atividade",atividade);
                    intent.putExtra("animal",currentAnimal);
                    startActivityForResult(intent,3);

                }
            });

            //Adicionando Adapter para o pageView
            viewPager.setAdapter(adapter);

        }catch (Exception e){
            Toast.makeText(HomePetActivity.this, getResources().getString(R.string.hint_sem_registros), Toast.LENGTH_LONG).show();

        }

    }

    public void criaAtividade(final Atividade atividade, Animal animal){

        AtividadeApi atividadeApi = new AtividadeApi(new AtividadeApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {
                //Verifica se  retorno da criação do usuário é uma data
                try{
                    String valido = response;

                    if(valido != null){
                        //Informa que o pet foi excluido
                        Toast.makeText(HomePetActivity.this,
                                "Atividade criada com sucesso!",
                                Toast.LENGTH_SHORT).show();

                        atividades.add(atividade);
                        setUpLayout();
                    }

                }catch (Exception e){
                    Log.e("ERRO", "HomePetActivity:failure", e);
                    Toast.makeText(HomePetActivity.this,
                            "Ops! Tivemos algum problema ao tentar criar sua atividade",
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onAsyncData(Date response) {

            }

            @Override
            public void onAsyncObject(Atividade response) {

            }

            @Override
            public void onAsyncObjectList(ArrayList<Atividade> response) {

            }

        });

        atividadeApi.create(animal.getId(), atividade);
    }

    private void updateAtividade(final Atividade atividade) {
        AtividadeApi atividadeApi = new AtividadeApi(new AtividadeApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {

            }

            @Override
            public void onAsyncData(Date response) {
                //Verifica se  retorno da criação do usuário é uma data
                try{
                    Date valido = response;

                    if(valido != null){
                        //Informa que o pet foi excluido
                        Toast.makeText(HomePetActivity.this,
                                getResources().getString(R.string.toast_update_atividade_ok),
                                Toast.LENGTH_SHORT).show();

                        for (int i = 0; i< atividades.size();i++){
                            if (atividades.get(i).getId().equals(atividade.getId())){

                                atividades.set(i,atividade);

                            }
                        }
                        setUpLayout();

                    }

                }catch (Exception e){
                    Log.e("ERRO", "FormAtividadeActivity:failure", e);
                    Toast.makeText(HomePetActivity.this,
                            getResources().getString(R.string.toast_update_atividade_notok),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAsyncObject(Atividade response) {

            }

            @Override
            public void onAsyncObjectList(ArrayList<Atividade> response) {

            }
        });

        atividadeApi.update(atividade.getId(),atividade);
    }

    public void setUpToolbars(){
        //Toolbar superior
        toolbar = findViewById(R.id.toolbar_superior);
        setSupportActionBar(toolbar);
        mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText(getResources().getString(R.string.nome_tela_home_pet));

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Toolbar inferior
        imageButtonHomePet = findViewById(R.id.image_button_home_pet_action_bar);
        imageButtonHomeLoja = findViewById(R.id.image_button_home_loja_action_bar);
        imageButtonHomeTutor = findViewById(R.id.image_button_home_tutor_action_bar);

        //Ação do botão e selecionadores
        imageButtonHomePet.setOnClickListener(this);
        imageButtonHomeLoja.setOnClickListener(this);
        imageButtonHomeTutor.setOnClickListener(this);
    }

    public void sair(){
        mAuth.signOut();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null){
            Intent inicial = new Intent(this, InicialActivity.class);
            startActivity(inicial);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == imageButtonHomePet){

            Intent intent = new Intent(this, HomePetActivity.class);
            startActivity(intent);
            this.finish();

        }
        else if (view == imageButtonHomeLoja){
            Intent intent = new Intent(this, HomeLojaActivity.class);

            startActivity(intent);
            this.finish();
        }
        else if (view == imageButtonHomeTutor){
            Intent intent = new Intent(this, HomeTutorActivity.class);

            startActivity(intent);
            this.finish();

        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
