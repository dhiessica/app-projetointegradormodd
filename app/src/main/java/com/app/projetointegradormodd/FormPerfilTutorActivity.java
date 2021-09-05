package com.app.projetointegradormodd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projetointegradormodd.Model.Object.Atividade;
import com.app.projetointegradormodd.Model.Object.Produto;
import com.app.projetointegradormodd.Model.Object.Tutor;
import com.app.projetointegradormodd.Services.API.TutorApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class FormPerfilTutorActivity extends AppCompatActivity {

    //Variáreis de layout
    private Toolbar toolbar;
    private TextInputLayout textInputLayoutNomeCompleto;
    private TextInputLayout textInputLayoutTelefone;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutSenha;
    private TextInputLayout textInputLayoutConfirmarSenha;
    private Button buttonSalvar;

    //Variáreis de dados
    private String nomeCompleto = "";
    private String telefone = "";
    private String email = "";
    private String senha;
    private String confirmaSenha;
    private Tutor tutor;
    private String action;


    //Instância classe conexão com Firestore
    private TutorApi tutorApi;

    //Variaveis do Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_perfil_tutor);

        //Incializa o Firebase Auth e pega o usuario logado
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Recebe dados da tela anterior
        recebeDados();

        //Referencia e insere ações do layout
        initializeLayout();


    }

    public void recebeDados(){
        //Recebe dados da tela anterior
        try {

            Bundle extras = getIntent().getExtras();
            if(extras != null){

                //Atribuindo dados aos objetos
                tutor = new Tutor();
                tutor = (Tutor) getIntent().getSerializableExtra("tutor");
                action = getIntent().getStringExtra("action");

            }
        }catch(Exception e){
            Log.e("FormPerfilTutorActivity",e.toString());
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

    public void initializeLayout(){
        //Referencia as variaveis de layot
        setUpToolbars();
        textInputLayoutNomeCompleto = findViewById(R.id.text_input_layout_nome_completo_cadastro);
        textInputLayoutTelefone = findViewById(R.id.text_input_layout_telefone_cadastro);
        textInputLayoutEmail = findViewById(R.id.text_input_layout_email_cadastro);
        textInputLayoutSenha= findViewById(R.id.text_input_layout_senha_cadastro);
        textInputLayoutConfirmarSenha= findViewById(R.id.text_input_layout_confirmar_senha_cadastro);
        buttonSalvar = findViewById(R.id.button_cadastrar_cadastro);

        if (action.equals(getResources().getString(R.string.action_editar_perfil_tutor))){
            textInputLayoutNomeCompleto.getEditText().setText(tutor.getNome());
            textInputLayoutEmail.getEditText().setText(tutor.getEmail());
            textInputLayoutTelefone.getEditText().setText(tutor.getTelefone());
            textInputLayoutEmail.setEnabled(false);
            textInputLayoutSenha.setEnabled(false);
            textInputLayoutConfirmarSenha.setEnabled(false);
        }

        //Ação do botão
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(FormPerfilTutorActivity.this);
                validation();
            }
        });

    }


    public void validation(){
        nomeCompleto = textInputLayoutNomeCompleto.getEditText().getText().toString();
        telefone = textInputLayoutTelefone.getEditText().getText().toString();
        email = textInputLayoutEmail.getEditText().getText().toString();
        senha = textInputLayoutSenha.getEditText().getText().toString();
        email = textInputLayoutEmail.getEditText().getText().toString();
        confirmaSenha = textInputLayoutConfirmarSenha.getEditText().getText().toString();

        //Valida os campos
        if (nomeCompleto.isEmpty()){
            textInputLayoutNomeCompleto.setError("Insira o nome completo");
            return;
        }else if (telefone.isEmpty()){
            textInputLayoutTelefone.setError("Insira o telefone");
            return;
        }else if (email.isEmpty()){
            textInputLayoutEmail.setError("Insira o email");
            return;
        }if(textInputLayoutSenha.isEnabled()){
            if (senha.isEmpty()){
                textInputLayoutSenha.setError("Insira a senha");
                return;
            }else if (confirmaSenha.isEmpty()){
                textInputLayoutConfirmarSenha.setError("Confirme a senha");
                return;
            }

        }

        if (!senha.equals(confirmaSenha)){
            Toast.makeText(this, "As senhas não são iguais, verifique e tente novamente!", Toast.LENGTH_LONG).show();
        }else{

            tutor = new Tutor();
            tutor.setNome(nomeCompleto);
            tutor.setEmail(email);
            tutor.setTelefone(telefone);

            if (action.equals(getResources().getString(R.string.action_editar_perfil_tutor))){
                updateTutor(tutor);
            }
            else if(action.equals(getResources().getString(R.string.action_cadastro_perfil_tutor))){
                cadastrarTutor();
            }

        }
    }

    public void updateTutor(Tutor tutor){

        TutorApi tutorApi = new TutorApi(new TutorApi.ConexaoListner() {
            @Override
            public void onAsyncData(Date response) {
                //Verifica se  retorno é uma data
                try{
                    Date valido = response;

                    if(valido != null){
                        //Informa que o pet foi excluido
                        Toast.makeText(FormPerfilTutorActivity.this,
                                "Seus dados foram atualizados com sucesso!",
                                Toast.LENGTH_SHORT).show();

                    }

                }catch (Exception e){
                    Log.e("ERRO", "FormPerfilTutorActivity:failure", e);
                    Toast.makeText(FormPerfilTutorActivity.this,
                            "Ops! Tivemos algum problema ao tentar atualizar seus dados.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAsyncObject(Tutor response) {

            }
        });

        tutorApi.update(currentUser.getUid(),tutor);
    }

    public void cadastrarTutor(){
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SUCCESS", "createUserWithEmail:success");

                            //Pega a instancia do usuario do firebase criado
                            currentUser = mAuth.getCurrentUser();

                            //Cria um objeto tutor para enviar para a api do banco
                            tutor = new Tutor(currentUser.getUid(),nomeCompleto,email,telefone);

                            //Instância api intermediária pegando o retorno
                            tutorApi = new TutorApi(new TutorApi.ConexaoListner() {
                                @Override
                                public void onAsyncData(Date response) {
                                    //Verifica se  retorno da criação do usuário é uma data
                                    try{
                                        Date valido = response;

                                        if(valido != null){
                                            //Chama proxima activity
                                            callNextActivity();
                                        }

                                    }catch (Exception e){
                                        Log.e("ERRO", "createUserDatabase:failure", e);
                                        Toast.makeText(FormPerfilTutorActivity.this, "Erro ao efetuar o cadastro.", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onAsyncObject(Tutor response) {}
                            });

                            //Chama método create da classe intermediária pra salvar os dados do usuario
                            tutorApi.create(tutor.getId(), tutor);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("ERRO", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(FormPerfilTutorActivity.this, "Erro ao efetuar o cadastro.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void callNextActivity(){
        //Chama a proxima activity passando os dados do usuário cadastrado
        Intent intent = new Intent(FormPerfilTutorActivity.this, HomeTutorActivity.class);
        startActivity(intent);
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
