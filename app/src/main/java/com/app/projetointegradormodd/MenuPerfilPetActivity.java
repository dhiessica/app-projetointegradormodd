package com.app.projetointegradormodd;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projetointegradormodd.Adapter.MenuPerfilPetRecyclerViewAdapter;
import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Services.API.AnimalApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;

public class MenuPerfilPetActivity extends AppCompatActivity implements View.OnClickListener {

    //Variáreis de layout
    private Toolbar toolbar;
    private ImageButton imageButtonHomePet;
    private ImageButton imageButtonHomeLoja;
    private ImageButton imageButtonHomeTutor;
    private Button buttonAdicionarPet;
    private RecyclerView mRecyclerView = null;
    private MenuPerfilPetRecyclerViewAdapter mRecyclerAdapter = null;
    private LinearLayout noData;
    private TextView noDataText;


    //Variáveis de dados
    private ArrayList<Animal> animais = new ArrayList<>();

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_perfil_pet);

        //Inciializa o Firebase Auth e pega o usuario logado
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Inicializa o layout
        initializeLayout();

        //Inicializa pegando dados da API e configurando/referenciando o layout
        getDataInitialize();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 7 && resultCode == RESULT_OK) {
            Animal animal = (Animal) data.getSerializableExtra("animal_create");

            animais.add(animal);
            setUpLayout();


        }
        else if (requestCode == 3 && resultCode == RESULT_OK) {
            Animal animal = (Animal) data.getSerializableExtra("animal_update");

            for (int i = 0; i< animais.size();i++){
                if (animais.get(i).getId().equals(animal.getId())){

                    animais.set(i,animal);

                }
            }
            setUpLayout();

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
                    Toast.makeText(MenuPerfilPetActivity.this, "Erro ao tentar coletar lista.", Toast.LENGTH_SHORT).show();

                    //Referencia e insere ações do layout
                    setUpLayout();
                }
            }
        });


        //Chama método get da classe intermediária
        animalApi.getList(currentUser.getUid());

    }

    public void initializeLayout(){
        //Referencia as variaveis de layot
        setUpToolbars();
        imageButtonHomePet = findViewById(R.id.image_button_home_pet_action_bar);
        imageButtonHomeLoja = findViewById(R.id.image_button_home_loja_action_bar);
        imageButtonHomeTutor = findViewById(R.id.image_button_home_tutor_action_bar);
        buttonAdicionarPet = findViewById(R.id.button_adicionar_carrinho_select_pet);
        mRecyclerView = findViewById(R.id.recycler_view_animais_select_pet);

        //Ação do botão e selecionadores
        imageButtonHomePet.setOnClickListener(this);
        imageButtonHomeLoja.setOnClickListener(this);
        imageButtonHomeTutor.setOnClickListener(this);
        buttonAdicionarPet.setOnClickListener(this);

    }

    public void setUpToolbars(){
        //Toolbar superior
        toolbar = findViewById(R.id.toolbar_superior);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText(getResources().getString(R.string.nome_tela_meus_pets));

        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    public void setUpLayout(){

        noData = findViewById(R.id.container_no_data);
        noDataText = findViewById(R.id.text_no_data);
        if (animais.size() == 0){
            noData.setVisibility(View.VISIBLE);
            noDataText.setText("Ops! Você ainda não cadastrou um pet.");
        }else{
            noData.setVisibility(View.INVISIBLE);
        }

        //Tenta inserir dados no layout
        try {

            //Criando e configurando adapter no recyclerview
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerAdapter = new MenuPerfilPetRecyclerViewAdapter(this, animais, MenuPerfilPetActivity.this, new MenuPerfilPetRecyclerViewAdapter.PetRecyclerViewListner() {
                @Override
                public void onExcludeClick(Animal animal) {
                    atualizaAnimal(animal);
                }
            });
            mRecyclerView.setAdapter(mRecyclerAdapter);
            mRecyclerAdapter.notifyDataSetChanged();



        }catch (Exception e){
            Log.e("errolistas",e.toString());
            Toast.makeText(MenuPerfilPetActivity.this, getResources().getString(R.string.hint_sem_registros), Toast.LENGTH_LONG).show();

        }

    }

    public void atualizaAnimal(final Animal animal){

        AnimalApi animalApi = new AnimalApi(new AnimalApi.ConexaoListner() {
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
                        Toast.makeText(MenuPerfilPetActivity.this,
                                getResources().getString(R.string.toast_exclui_pet_ok),
                                Toast.LENGTH_SHORT).show();

                        animais.remove(animal);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }

                }catch (Exception e){
                    Log.e("ERRO", "AnimalYesCancelDialogFragment:failure", e);
                    Toast.makeText(MenuPerfilPetActivity.this,
                            getResources().getString(R.string.toast_exclui_pet_notok),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAsyncObject(Animal response) {

            }

            @Override
            public void onAsyncObjectList(ArrayList<Animal> response) {

            }
        });

        animalApi.delete(animal.getId());
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
        else if (view == imageButtonHomeTutor){
            Intent intent = new Intent(this, HomeTutorActivity.class);
            startActivity(intent);
            finish();

        }
        else if (view == buttonAdicionarPet){
            Intent intent = new Intent(this, FormPerfilPetActivity.class);
            intent.putExtra("action",getResources().getString(R.string.action_cadastro_perfil_pet));
            startActivityForResult(intent,7);

        }
    }
}
