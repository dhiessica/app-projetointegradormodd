package com.app.projetointegradormodd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projetointegradormodd.Model.Object.Endereco;
import com.app.projetointegradormodd.Model.Object.FormaPagamento;
import com.app.projetointegradormodd.Services.API.EnderecoApi;
import com.app.projetointegradormodd.Services.API.FormaPagamentoApi;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FormEnderecoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Variáreis de layout
    private Toolbar toolbar;
    private TextInputLayout textInputLayoutNomeRua;
    private TextInputLayout textInputLayoutCep;
    private TextInputLayout textInputLayoutNumero;
    private Spinner spinnerEstado;
    private TextInputLayout textInputLayoutCidade;
    private Button buttonSalvar;

    //Variáreis de dados
    private Endereco endereco = new Endereco();
    private Boolean update = false;
    private String enderecoString;
    private String action;
    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_endereco);

        getDataPreviousActivity();

        //Inciializa o Firebase Auth e pega o usuario logado
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Faz requisição para a api
        getDataInitialize();

        setUpToolbars();
        initializeLayout();

    }

    public void getDataPreviousActivity(){
        //Recebe dados da tela anterior
        Bundle extras = getIntent().getExtras();
        if(extras != null){

            //Atribuindo dados aos objetos
            action = getIntent().getStringExtra("action");
        }
    }

    public void getDataInitialize(){
        EnderecoApi enderecoApi = new EnderecoApi(new EnderecoApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {

            }

            @Override
            public void onAsyncData(Date response) {

            }

            @Override
            public void onAsyncObject(Endereco response) {

                try{
                    //Pega a resposta em uma lista
                    Endereco valido = response;

                    //Verifica se é válido
                    if(valido != null){

                        endereco = valido;
                        update = true;
                    }
                    //Inicializa o layout
                    initializeLayout();
                    if (update) {
                        setDataInitialize();
                    }
                }catch (Exception e){
                    Log.e("ERRO", "FormEnderecoActivity:failure", e);
                    Toast.makeText(FormEnderecoActivity.this, "Erro ao tentar acessar dados de endereço.", Toast.LENGTH_SHORT).show();

                    //Inicializa o layout
                    initializeLayout();
                    if (update) {
                        setDataInitialize();
                    }
                }
            }
        });

        enderecoApi.get(currentUser.getUid());
    }

    public void initializeLayout(){
        //Referenciando itens de layout
        setUpToolbars();
        textInputLayoutNomeRua = findViewById(R.id.text_input_layout_nome_rua);
        textInputLayoutCep = findViewById(R.id.text_input_layout_cep);
        textInputLayoutNumero = findViewById(R.id.text_input_layout_numero);
        spinnerEstado = findViewById(R.id.spinner_estado_form_endereco);
        textInputLayoutCidade = findViewById(R.id.text_input_layout_cidade);
        buttonSalvar = findViewById(R.id.button_salvar_endereco);

        //Ação do botão e selecionadores
        spinnerEstado.setOnItemSelectedListener(this);
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(FormEnderecoActivity.this);
                coletaValidaDados();
            }
        });

    }

    public void setDataInitialize(){

        textInputLayoutNomeRua.getEditText().setText(endereco.getRua());
        textInputLayoutCep.getEditText().setText(endereco.getCep());
        textInputLayoutNumero.getEditText().setText(String.valueOf(endereco.getNumero()));
        textInputLayoutCidade.getEditText().setText(endereco.getCidade());

        //busca a categoria no select
        for (int i = 0; i < spinnerEstado.getCount(); i++) {
            if (spinnerEstado.getItemAtPosition(i).equals(endereco.getUf())) {
                spinnerEstado.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        enderecoString = adapterView.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void coletaValidaDados(){

        try {
            endereco.setIdRemetente(currentUser.getUid());
            endereco.setRua(textInputLayoutNomeRua.getEditText().getText().toString());
            endereco.setCep(textInputLayoutCep.getEditText().getText().toString());
            endereco.setNumero( Integer.parseInt(textInputLayoutNumero.getEditText().getText().toString()));
            endereco.setUf(enderecoString);
            endereco.setCidade(textInputLayoutCidade.getEditText().getText().toString());
        }catch (Exception e){
            Toast.makeText(this, "Verifique se todos os campos estão preenchidos", Toast.LENGTH_LONG).show();
        }


        if (endereco.getRua().isEmpty()){
            textInputLayoutNomeRua.setError("Inserir o nome da rua");
            return;
        }
        else if(endereco.getCep().isEmpty()){
            textInputLayoutCep.setError("Inserir o CEP da rua");
            return;
        }
        else if(textInputLayoutNumero.getEditText().getText().toString() == null){
            textInputLayoutNumero.setError("Inserir o numero da casa");
            return;
        }
        else if(endereco.getUf().isEmpty() || endereco.getUf().equals("Selecione")){
            Toast.makeText(this, "Inserir a uf do estado", Toast.LENGTH_LONG).show();
            return;
        }
        else if(endereco.getCidade().isEmpty()){
            textInputLayoutCidade.setError("Inserir a cidade onde reside");
            return;
        }else{

            if (update) {
                updateEndereco();
            } else if (!update) {
                cadastrarEndereco();
            }
        }

    }

    public void updateEndereco(){

        EnderecoApi enderecoApi = new EnderecoApi(new EnderecoApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {

            }

            @Override
            public void onAsyncData(Date response) {
                //Verifica se  retorno é uma data
                try{
                    Date valido = response;

                    if(valido != null){
                        //Informa que o pet foi excluido
                        Toast.makeText(FormEnderecoActivity.this,
                                "Seu endereço foi atualizado com sucesso!",
                                Toast.LENGTH_SHORT).show();


                    }

                }catch (Exception e){
                    Log.e("ERRO", "FormEnderecoActivity:failure", e);
                    Toast.makeText(FormEnderecoActivity.this,
                            "Ops! Tivemos algum problema ao tentar atualizar seu endereço.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAsyncObject(Endereco response) {

            }
        });

        enderecoApi.update(currentUser.getUid(),endereco);
    }

    public void cadastrarEndereco(){
        EnderecoApi enderecoApi = new EnderecoApi(new EnderecoApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {
                //Verifica se  retorno é uma data
                try{
                    String valido = response;

                    if(valido != null) {
                        //Informa que o pet foi excluido
                        Toast.makeText(FormEnderecoActivity.this,
                                "Endereço cadastrado com sucesso",
                                Toast.LENGTH_SHORT).show();

                        if (action != null) {


                            if (action.equals("create")) {
                                //Envia dados do item selecionado para a proxima tela
                                Intent intent = new Intent(FormEnderecoActivity.this, CarrinhoActivity.class);

                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    }

                }catch (Exception e){
                    Log.e("ERRO", "FormFormasPagamentoActivity:failure", e);
                    Toast.makeText(FormEnderecoActivity.this,
                            "Ops! Houve algum problema ao tentar cadastrar seu endereço",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAsyncData(Date response) {

            }

            @Override
            public void onAsyncObject(Endereco response) {

            }
        });

        enderecoApi.create(currentUser.getUid(),endereco);
    }

    public void setUpToolbars(){
        //Toolbar superior
        toolbar = findViewById(R.id.toolbar_superior);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText(getResources().getString(R.string.nome_tela_endereco));

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

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
