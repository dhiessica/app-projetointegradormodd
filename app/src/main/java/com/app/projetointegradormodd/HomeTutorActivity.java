package com.app.projetointegradormodd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projetointegradormodd.Adapter.HomeLojaPageViewAdapter;
import com.app.projetointegradormodd.Adapter.HomeTutorPageViewAdapter;
import com.app.projetointegradormodd.Interfaces.IDataCallback;
import com.app.projetointegradormodd.Interfaces.ISearch;
import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Model.Object.Endereco;
import com.app.projetointegradormodd.Model.Object.FormaPagamento;
import com.app.projetointegradormodd.Model.Object.Pedido;
import com.app.projetointegradormodd.Model.Object.Produto;
import com.app.projetointegradormodd.Model.Object.Servico;
import com.app.projetointegradormodd.Model.Object.Tutor;
import com.app.projetointegradormodd.Services.API.PedidoApi;
import com.app.projetointegradormodd.Services.API.TutorApi;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeTutorActivity extends AppCompatActivity implements View.OnClickListener, TabLayout.BaseOnTabSelectedListener {

    //Variaveis para referênciar o layout
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton imageButtonHomePet;
    private ImageButton imageButtonHomeLoja;
    private ImageButton imageButtonHomeTutor;
    private ImageButton imageButtonEngrenagem;
    private HomeTutorPageViewAdapter adapter;

    //Variáveis de dados
    private ArrayList<Pedido> pedidosAnteriores = new ArrayList<>();
    private ArrayList<Pedido> pedidosAndamento = new ArrayList<>();

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tutor);

        //Incializa o Firebase Auth e pega o usuario logado
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Inicializa o layout
        initializeLayout();
        
        //Inicializa pegando dados da API e configurando/referenciando o layout
        getDataInitialize();
        setUpLayout();

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

    public void getDataInitialize(){

        //Trata a resposta da classe intermediária da chamada da Api
        PedidoApi pedidoApi = new PedidoApi(new PedidoApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {

            }

            @Override
            public void onAsyncData(Date response) {

            }

            @Override
            public void onAsyncObject(Pedido response) {

            }

            @Override
            public void onAsyncObjectList(ArrayList<Pedido> response) {
                try{
                    //Pega a resposta em uma lista
                    ArrayList<Pedido> valido = response;

                    //Verifica se é válido
                    if(valido != null){

                        //Separa os em andamento dos concluídos
                        for (int i=0; i<valido.size(); i++){

                            Pedido pedido = valido.get(i);

                            if (pedido.getStatus().equals(getResources().getString(R.string.hint_pendente))){
                                pedidosAndamento.add(pedido);
                            }else if(pedido.getStatus().equals(getResources().getString(R.string.hint_confirmado))){
                                pedidosAnteriores.add(pedido);
                            }
                        }
                    }
                    //Referencia e insere ações do layout
                    setUpLayout();
                }catch (Exception e){
                    Log.e("ERRO", "getpedidoslist:failure", e);
                    Toast.makeText(HomeTutorActivity.this, "Erro ao tentar coletar lista.", Toast.LENGTH_SHORT).show();

                    //Referencia e insere ações do layout
                    setUpLayout();
                }
            }
        });

        //Chama método get da classe intermediária
        pedidoApi.getList(currentUser.getUid());

    }

    private void initializeLayout() {

        //Referencia as variaveis de layot
        setUpToolbars();

        //Adicionando tabs
        tabLayout = findViewById(R.id.tab_layout_home_tutor);

        tabLayout.addTab(tabLayout.newTab().setText("COMPRAS ANTERIORES"));
        tabLayout.addTab(tabLayout.newTab().setText("COMPRAS EM ANDAMENTO"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Inicializando ViewPager
        viewPager = findViewById(R.id.view_pager_home_tutor);

        //Adicionando onTabSelectedListener para deslizar as views
        tabLayout.setOnTabSelectedListener(this);

    }

    public void setUpToolbars(){
        //Toolbar superior
        toolbar = findViewById(R.id.toolbar_superior);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText(getResources().getString(R.string.nome_tela_home_tutor));

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Toolbar inferior
        imageButtonHomePet = findViewById(R.id.image_button_home_pet_action_bar);
        imageButtonHomeLoja = findViewById(R.id.image_button_home_loja_action_bar);
        imageButtonHomeTutor = findViewById(R.id.image_button_home_tutor_action_bar);
        imageButtonEngrenagem = findViewById(R.id.image_button_engrenagem_tutor);

        imageButtonEngrenagem.setVisibility(View.VISIBLE);

        //Ação do botão e selecionadores
        imageButtonHomePet.setOnClickListener(this);
        imageButtonHomeLoja.setOnClickListener(this);
        imageButtonHomeTutor.setOnClickListener(this);
        imageButtonEngrenagem.setOnClickListener(this);
    }

    public void setUpLayout(){

        //Tenta inserir dados no layout
        try {

            //Criando PagerAdapter
            adapter = new HomeTutorPageViewAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),pedidosAnteriores,pedidosAndamento);

            //Adicionando Adapter para o pageView
            viewPager.setAdapter(adapter);


        }catch (Exception e){
            Toast.makeText(HomeTutorActivity.this, getResources().getString(R.string.hint_sem_registros), Toast.LENGTH_LONG).show();

        }

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
            finish();
        }
        else if (view == imageButtonHomeLoja){
            Intent intent = new Intent(this, HomeLojaActivity.class);
            startActivity(intent);
            finish();
        }
        else if (view == imageButtonEngrenagem){
            Intent intent = new Intent(this, MenuPerfilTutorActivity.class);
            startActivity(intent);
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
