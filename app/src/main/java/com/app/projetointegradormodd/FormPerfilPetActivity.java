package com.app.projetointegradormodd;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Model.Object.Atividade;
import com.app.projetointegradormodd.Model.Object.Tutor;
import com.app.projetointegradormodd.Services.API.AnimalApi;
import com.app.projetointegradormodd.Services.API.AtividadeApi;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FormPerfilPetActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    //Variáreis de layout
    private Toolbar toolbar;
    private TextInputLayout textInputLayoutNomePet;
    private TextView textInputLayoutDataNascimento;
    private Spinner spinnerEspecie;
    private TextInputLayout textInputLayoutRaca;
    private Spinner spinnerGenero;
    private Spinner spinnerPelagem;
    private TextInputLayout textInputLayoutCor;
    private Spinner spinnerPorte;
    private TextInputLayout textInputLayoutPeso;
    private TextInputLayout textInputLayoutObs;
    private Button buttonCriar;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    //Variáreis de dados
    private Animal animal = new Animal();
    private Animal previousAnimal;
    private String action = null;
    private String especie = null;
    private String genero = null;
    private String pelagem = null;
    private String porte = null;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_perfil_pet);

        //Inciializa o Firebase Auth e pega o usuario logado
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Recebe dados da tela anterior
        recebeDados();

        //Referencia e insere ações do layout
        initializeLayout();

        //Escuta a seleção da data
        dateDialogListener();

        if (action.equals(getResources().getString(R.string.action_editar_perfil_pet))){
            previousAnimal = (Animal) getIntent().getSerializableExtra("animal");
            setDataInitialize();
        }

    }

    public void recebeDados(){
        //Recebe dados da tela anterior
        try {

            Bundle extras = getIntent().getExtras();
            if(extras != null){

                //Atribuindo dados aos objetos
                animal = new Animal();
                animal = (Animal) getIntent().getSerializableExtra("animal");
                action = getIntent().getStringExtra("action");

            }
        }catch(Exception e){
            Log.e("FormPerfilPetActivity",e.toString());
        }
    }

    private void initializeLayout() {
        //Referenciando itens de layout
        setUpToolbars();
        textInputLayoutNomePet = findViewById(R.id.text_input_layout_nome_pet);
        textInputLayoutDataNascimento = findViewById(R.id.text_input_layout_data_nascimento);
        spinnerEspecie = findViewById(R.id.spinner_especie_form_criar_pet);
        textInputLayoutRaca = findViewById(R.id.text_input_layout_raca);
        spinnerGenero = findViewById(R.id.spinner_genero_form_criar_pet);
        spinnerPelagem = findViewById(R.id.spinner_pelagem_form_criar_pet);
        textInputLayoutCor = findViewById(R.id.text_input_layout_cor);
        spinnerPorte = findViewById(R.id.spinner_porte_form_criar_pet);
        textInputLayoutPeso = findViewById(R.id.text_input_layout_peso);
        textInputLayoutObs = findViewById(R.id.text_input_layout_obs);
        buttonCriar = findViewById(R.id.button_criar_pet);

        //Ação do botão e selecionadores
        spinnerEspecie.setOnItemSelectedListener(this);
        spinnerGenero.setOnItemSelectedListener(this);
        spinnerPelagem.setOnItemSelectedListener(this);
        spinnerPorte.setOnItemSelectedListener(this);
        textInputLayoutDataNascimento.setOnClickListener(this);

        buttonCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(FormPerfilPetActivity.this);
                validation();
            }
        });
    }

    public void setDataInitialize(){

        textInputLayoutNomePet.getEditText().setText(animal.getNome());
        textInputLayoutDataNascimento.setText(animal.getDataNascimento());
        textInputLayoutRaca.getEditText().setText(animal.getRaca());
        textInputLayoutCor.getEditText().setText(animal.getCor());
        textInputLayoutPeso.getEditText().setText(animal.getPeso());
        textInputLayoutObs.getEditText().setText(animal.getObservacoes());

        //busca a categoria no select
        for (int i = 0; i < spinnerEspecie.getCount(); i++) {
            if (spinnerEspecie.getItemAtPosition(i).equals(animal.getEspecie())) {
                spinnerEspecie.setSelection(i);
                break;
            }
        }

        //busca a categoria no select
        for (int i = 0; i < spinnerGenero.getCount(); i++) {
            if (spinnerGenero.getItemAtPosition(i).equals(animal.getGenero())) {
                spinnerGenero.setSelection(i);
                break;
            }
        }

        //busca a categoria no select
        for (int i = 0; i < spinnerPelagem.getCount(); i++) {
            if (spinnerPelagem.getItemAtPosition(i).equals(animal.getPelagem())) {
                spinnerPelagem.setSelection(i);
                break;
            }
        }

        //busca a categoria no select
        for (int i = 0; i < spinnerPorte.getCount(); i++) {
            if (spinnerPorte.getItemAtPosition(i).equals(animal.getPorte())) {
                spinnerPorte.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spinner_especie_form_criar_pet){
            especie = adapterView.getSelectedItem().toString();
        }
        else if (adapterView.getId() == R.id.spinner_genero_form_criar_pet){
            genero = adapterView.getSelectedItem().toString();
        }
        else if (adapterView.getId() == R.id.spinner_pelagem_form_criar_pet){
            pelagem = adapterView.getSelectedItem().toString();
        }
        else if (adapterView.getId() == R.id.spinner_porte_form_criar_pet){
            porte = adapterView.getSelectedItem().toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void validation(){
        animal = new Animal();
        animal.setNome(textInputLayoutNomePet.getEditText().getText().toString());
        animal.setDataNascimento(textInputLayoutDataNascimento.getText().toString());
        animal.setEspecie(especie);
        animal.setRaca(textInputLayoutRaca.getEditText().getText().toString());
        animal.setGenero(genero);
        animal.setPelagem(pelagem);
        animal.setCor(textInputLayoutCor.getEditText().getText().toString());
        animal.setPorte(porte);
        animal.setPeso(textInputLayoutPeso.getEditText().getText().toString());
        animal.setObservacoes(textInputLayoutObs.getEditText().getText().toString());

        if (animal.getNome().isEmpty()){
            textInputLayoutNomePet.setError("Inserir o nome do animal");
            return;
        }
        else if(animal.getDataNascimento().isEmpty() || animal.getDataNascimento().equals("Selecione")){
            textInputLayoutDataNascimento.setError("Inserir a data de nascimento do animal");
            return;
        }
        else if(animal.getEspecie() ==null || animal.getEspecie().equals("Selecione")){
            Log.e("especie",animal.getEspecie());
            Toast.makeText(this,"Inserir a especie do animal",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(animal.getRaca().isEmpty() ){
            textInputLayoutRaca.setError("Inserir a raça do animal");
            return;
        }
        else if(animal.getGenero() ==null|| animal.getGenero().equals("Selecione")){
            Log.e("genero",animal.getGenero());
            Toast.makeText(this,"Inserir o gênero do animal",Toast.LENGTH_SHORT).show();

            return;
        }
        else if(animal.getPelagem()==null || animal.getPelagem().equals("Selecione")){
            Log.e("pelagem",animal.getPelagem());
            Toast.makeText(this,"Inserir o tipo de pelagem do animal",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(animal.getCor().isEmpty()){
            textInputLayoutCor.setError("Inserir a cor do animal");
            return;
        }
        else if(animal.getPorte()==null || animal.getPorte().equals("Selecione")){
            Log.e("porte",animal.getPorte());
            Toast.makeText(FormPerfilPetActivity.this,"Inserir o porte do animal",Toast.LENGTH_SHORT).show();
            return;

        }
        else if(animal.getPeso().isEmpty()){
            textInputLayoutPeso.setError("Inserir o peso do animal");
            return;
        }
        else {
            animal.setIdTutor(currentUser.getUid());

            if (action.equals(getResources().getString(R.string.action_editar_perfil_pet))) {
                animal.setId(previousAnimal.getId());
                updateAnimal();

            } else if (action.equals(getResources().getString(R.string.action_cadastro_perfil_pet))) {
                cadastrarAnimal();

            }
        }
    }

    public void setUpToolbars(){
        //Toolbar superior
        toolbar = findViewById(R.id.toolbar_superior);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText(action);

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

    public void dateDialogListener(){

        //Coleta e insere as informações do dialog na caixa de texto
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                String data = day + "/" + month + "/" + year;
                textInputLayoutDataNascimento.setText(data);

            }
        };


    }

    public void dateSelector(){

        //Pega a data atual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;

        //Configura a janela de seleção da data a apartir da data atual
        datePickerDialog = new DatePickerDialog(
                FormPerfilPetActivity.this,
                mDateSetListener,
                year,month,day);

        datePickerDialog.show();

    }

    @Override
    public void onClick(View view) {

        if (view == buttonCriar){
            hideKeyboard(FormPerfilPetActivity.this);
            validation();

        }
        else if (view == textInputLayoutDataNascimento){
            dateSelector();
        }

    }

    public void cadastrarAnimal(){

        AnimalApi animalApi = new AnimalApi(new AnimalApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {
                //Verifica se  retorno é uma data
                try{
                    String valido = response;

                    if(valido != null){

                        //Informa que o pet foi excluido
                        Toast.makeText(FormPerfilPetActivity.this,
                                "Pet cadastrado com sucesso",
                                Toast.LENGTH_SHORT).show();

                        //Envia dados do item selecionado para a proxima tela
                        Intent intent = new Intent(FormPerfilPetActivity.this, MenuPerfilPetActivity.class);
                        intent.putExtra("animal_create", animal);

                        setResult(RESULT_OK, intent);
                        finish();
                    }

                }catch (Exception e){
                    Log.e("ERRO", "FormPerfilPetActivity:failure", e);
                    Toast.makeText(FormPerfilPetActivity.this,
                            "Ops! Houve algum problema ao tentar cadastrar o perfil do pet",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAsyncData(Date response) {

            }

            @Override
            public void onAsyncObject(Animal response) {

            }

            @Override
            public void onAsyncObjectList(ArrayList<Animal> response) {

            }
        });

        animalApi.create(currentUser.getUid(), animal);
    }

    private void updateAnimal() {
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
                        Toast.makeText(FormPerfilPetActivity.this,
                                "Dados do pet atualizados com sucesso!",
                                Toast.LENGTH_SHORT).show();

                        //Envia dados do item selecionado para a proxima tela
                        Intent intent = new Intent(FormPerfilPetActivity.this, MenuPerfilPetActivity.class);
                        intent.putExtra("animal_update", animal);

                        setResult(RESULT_OK, intent);
                        finish();

                    }

                }catch (Exception e){
                    Log.e("ERRO", "FormPerfilPetActivity:failure", e);
                    Toast.makeText(FormPerfilPetActivity.this,
                            "Ops!Houve algum problema ao tentar atualizar os dados do pet.",
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

        animalApi.update(animal.getId(),animal);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
