package com.app.projetointegradormodd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //Variáreis de layout
    private Toolbar toolbar;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutSenha;
    private Button buttonEntrar;

    //Variáreis de dados
    private String email;
    private String senha;

    //Variaveis do Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Incilializa o Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Referencia e insere ações do layout
        setUpLayout();
    }


    public void setUpToolbars(){
        //Toolbar superior
        toolbar = findViewById(R.id.toolbar_superior);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

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
        //Referencia as variaveis de layot
        setUpToolbars();
        textInputLayoutEmail = findViewById(R.id.text_input_layout_email_login);
        textInputLayoutSenha = findViewById(R.id.text_input_layout_senha_login);
        buttonEntrar = findViewById(R.id.button_entrar_login);

        //Ação do botão
        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });

    }

    public void coletarDadosFormulario(){
        //Coletando dados da tela e passando para variáveis de dados
        email = textInputLayoutEmail.getEditText().getText().toString();
        senha = textInputLayoutSenha.getEditText().getText().toString();
    }

    public void validation(){
        coletarDadosFormulario();

        //Valida os campos
        if (email.isEmpty()){
            textInputLayoutEmail.setError("Favor inserir o email");
            return;
        }else if (senha.isEmpty()){
            textInputLayoutSenha.setError("Favor inserir a senha");
            return;
        }

        loginFirebase();

    }

    private void loginFirebase() {
        //Efetua o login com auxilio do Autentication
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Redireciona para home tutor se o login for um sucesso
                            Intent home = new Intent(LoginActivity.this, HomeTutorActivity.class);
                            startActivity(home);

                        } else {
                            // Mostra mensagem ao usuario caso ocorra algum erro
                            Toast.makeText(LoginActivity.this, "Erro ao tentar efetuar o login. Verifique seus dados e tente novamente.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
