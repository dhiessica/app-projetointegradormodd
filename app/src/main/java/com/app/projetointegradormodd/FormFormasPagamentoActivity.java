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
import android.widget.TextView;
import android.widget.Toast;

import com.app.projetointegradormodd.Model.Object.FormaPagamento;
import com.app.projetointegradormodd.Services.API.FormaPagamentoApi;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FormFormasPagamentoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Variáreis de layout
    private Toolbar toolbar;
    private TextInputLayout textInputLayoutNumeroCartao;
    private TextInputLayout textInputLayoutValidade;
    private TextInputLayout textInputLayoutCvv;
    private TextInputLayout textInputLayoutNomeTitular;
    private TextInputLayout textInputLayoutCpfTitular;
    private Button buttonSalvar;

    //Variáreis de dados
    private FormaPagamento formaPagamento = new FormaPagamento();
    private Boolean update = false;
    private String action;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_formas_pagamentos);

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
            if (action.equals("create")){
                update = false;
            }
        }
    }

    public void getDataInitialize(){
        FormaPagamentoApi formaPagamentoApi = new FormaPagamentoApi(new FormaPagamentoApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {

            }

            @Override
            public void onAsyncData(Date response) {

            }

            @Override
            public void onAsyncObject(FormaPagamento response) {
                try{
                    //Pega a resposta em uma lista
                    FormaPagamento valido = response;

                    //Verifica se é válido
                    if(valido != null){

                        formaPagamento = valido;
                        update = true;
                       // action="update";
                    }
                    //else {
                       // action = "create";
                       // update = false;

                   // }

                    //Inicializa o layout
                    initializeLayout();
                    if (update) {
                        setDataInitialize();
                    }
                }catch (Exception e){
                    Log.e("ERRO", "getformapagamento:failure", e);
                    Toast.makeText(FormFormasPagamentoActivity.this, "Erro ao tentar acessar dados da forma de pagamento.", Toast.LENGTH_SHORT).show();

                    //Inicializa o layout
                    initializeLayout();
                    if (update) {
                        setDataInitialize();
                    }
                }
            }
        });

        formaPagamentoApi.get(currentUser.getUid());
    }

    public void initializeLayout(){
        //Referencia as variaveis de layot
        setUpToolbars();
        textInputLayoutNumeroCartao = findViewById(R.id.text_input_layout_numero_cartao);
        textInputLayoutValidade = findViewById(R.id.text_input_layout_validade);
        textInputLayoutCvv = findViewById(R.id.text_input_layout_cvv);
        textInputLayoutNomeTitular= findViewById(R.id.text_input_layout_nome_titular);
        textInputLayoutCpfTitular= findViewById(R.id.text_input_layout_cpf);
        buttonSalvar = findViewById(R.id.button_salvar_forma_pagamento);

        //Ação do botão
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(FormFormasPagamentoActivity.this);
                validation();
            }
        });

    }

    public void setDataInitialize(){
        textInputLayoutNumeroCartao.getEditText().setText(formaPagamento.getNumeroCartao());
        textInputLayoutValidade.getEditText().setText(formaPagamento.getValidade());
        textInputLayoutCvv.getEditText().setText(formaPagamento.getCvv());
        textInputLayoutNomeTitular.getEditText().setText(formaPagamento.getNomeTitular());
        textInputLayoutCpfTitular.getEditText().setText(formaPagamento.getCpfTitular());
    }

    public void coletarDadosFormulario(){
        //Coletando dados da tela e passando para variáveis de dados
        formaPagamento.setNumeroCartao(textInputLayoutNumeroCartao.getEditText().getText().toString());
        formaPagamento.setValidade(textInputLayoutValidade.getEditText().getText().toString());
        formaPagamento.setCvv(textInputLayoutCvv.getEditText().getText().toString());
        formaPagamento.setNomeTitular(textInputLayoutNomeTitular.getEditText().getText().toString());
        formaPagamento.setCpfTitular(textInputLayoutCpfTitular.getEditText().getText().toString());
        formaPagamento.setIdRemetente(currentUser.getUid());
    }

    public void validation(){
        coletarDadosFormulario();

        //Valida os campos
        if (formaPagamento.getNumeroCartao().isEmpty()){
            textInputLayoutNumeroCartao.setError("Inserir o número do cartao");
            return;
        }else if (formaPagamento.getValidade().isEmpty()){
            textInputLayoutValidade.setError("Inserir a validade do cartão");
            return;
        }else if (formaPagamento.getCvv().isEmpty()){
            textInputLayoutCvv.setError("Inserir o código do verso do cartão");
            return;
        }else if (formaPagamento.getNomeTitular().isEmpty()){
            textInputLayoutNomeTitular.setError("Inserir o nome do titular do cartão");
            return;
        }else if (formaPagamento.getCpfTitular().isEmpty()){
            textInputLayoutCpfTitular.setError("Inserir o cpf do titular do cartão");
            return;
        }else {

            if (update) {
                updateFormaPagamento();
            } else if (!update) {
                cadastrarFormaPagamento();
            }
        }
    }

    public void updateFormaPagamento(){

        FormaPagamentoApi formaPagamentoApi = new FormaPagamentoApi(new FormaPagamentoApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {

            }

            @Override
            public void onAsyncData(Date response) {
                //Verifica se  retorno é uma string
                try{
                    Date valido = response;

                    if(valido != null){
                        //Informa que o pet foi excluido
                        Toast.makeText(FormFormasPagamentoActivity.this,
                                "Sua forma de pagamento foi atualizada com sucesso!",
                                Toast.LENGTH_SHORT).show();

                    }

                }catch (Exception e){
                    Log.e("ERRO", "FormFormasPagamentoActivity:failure", e);
                    Toast.makeText(FormFormasPagamentoActivity.this,
                            "Ops! Tivemos algum problema ao tentar atualizar sua forma de pagamento.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAsyncObject(FormaPagamento response) {

            }
        });

        formaPagamentoApi.update(currentUser.getUid(),formaPagamento);
    }

    public void cadastrarFormaPagamento(){
        FormaPagamentoApi formaPagamentoApi = new FormaPagamentoApi(new FormaPagamentoApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {
                //Verifica se  retorno é uma data
                try{
                    String valido = response;

                    if(valido != null) {
                        //Informa que o pet foi excluido
                        Toast.makeText(FormFormasPagamentoActivity.this,
                                "Forma de pagamento cadastrada com sucesso",
                                Toast.LENGTH_SHORT).show();

                        if (action != null) {


                            if (action.equals("create")) {
                                //Envia dados do item selecionado para a proxima tela
                                Intent intent = new Intent(FormFormasPagamentoActivity.this, CarrinhoActivity.class);

                                setResult(RESULT_OK, intent);
                                finish();
                            }

                        }
                    }

                }catch (Exception e){
                    Log.e("ERRO", "FormFormasPagamentoActivity:failure", e);
                    Toast.makeText(FormFormasPagamentoActivity.this,
                            "Ops! Houve algum problema ao tentar cadastrar sua forma de pagamento",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAsyncData(Date response) {

            }

            @Override
            public void onAsyncObject(FormaPagamento response) {

            }
        });

        formaPagamentoApi.create(currentUser.getUid(),formaPagamento);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void setUpToolbars(){
        //Toolbar superior
        toolbar = findViewById(R.id.toolbar_superior);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText(getResources().getString(R.string.nome_tela_forma_pagamento));

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
