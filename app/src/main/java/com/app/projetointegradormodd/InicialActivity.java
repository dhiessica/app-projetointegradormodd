package com.app.projetointegradormodd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.projetointegradormodd.Adapter.InicialCardAdapter;
import com.app.projetointegradormodd.Model.Layout.InicialCardModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class InicialActivity extends AppCompatActivity implements View.OnClickListener {

    //Variáreis de layout
    private ViewPager viewPager;
    private LinearLayout linearLayoutSliderDotsPanel;
    private ImageView[] imageViewsDots;
    private Button buttonEntrar;
    private TextView buttonCadastrar;

    //Variáreis de dados
    private InicialCardAdapter adapter;
    private List<InicialCardModel> listaCards;
    private int dotCount;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        //Inciializa o Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Preenche a lista de cards
        listarCards();

        //Referencia e insere ações do layout
        setUpLayout();
    }

    @Override
    public void onStart() {
        super.onStart();
        //Verifica se ja existe um usuário logado e muda pra tel ahome caso existir
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            Intent home = new Intent(this, HomeTutorActivity.class);
            startActivity(home);
        }
    }

    public void setUpLayout(){
        //Insere pontos no layout que identificam o card no swipe do pageview
        dotsSetUp();

        //Variaveis para referênciar o layout
        buttonEntrar = findViewById(R.id.button_entrar_inicial);
        buttonCadastrar = findViewById(R.id.button_cadastrar_inicial);

        //Ação dos botões
        buttonEntrar.setOnClickListener(this);
        buttonCadastrar.setOnClickListener(this);
    }

    public void listarCards(){
        //Dados dos cards
        listaCards = new ArrayList<>();
        listaCards.add(new InicialCardModel("Cadastre atividades do seu pet!", "Cadastre atividades e visualize agendamentos do seu pet."));
        listaCards.add(new InicialCardModel("Compre produtos!", "Compre produtos e receba na sua casa, tudo direto do seu celular."));
        listaCards.add(new InicialCardModel("Agende serviços!","Quer marcar o banho e tosa do eu animalzinho? Faça tudo em apenas alguns passos."));

        adapter = new InicialCardAdapter(listaCards,this);
    }

    public void dotsSetUp (){

        //Referenciando view pager para paginar a tela inicial com os cards
        viewPager = findViewById(R.id.view_pager_inicial);
        viewPager.setAdapter(adapter);

        //Pontos identificando a posição do card
        linearLayoutSliderDotsPanel = findViewById(R.id.linear_layout_slider_dots_inicial);

        dotCount = adapter.getCount();
        imageViewsDots = new ImageView[dotCount];

        for (int i = 0; i< dotCount; i++){
            imageViewsDots[i] = new ImageView(this);
            imageViewsDots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.dot_nonactive));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(16,8,16,8);
            linearLayoutSliderDotsPanel.addView(imageViewsDots[i],params);

        }

        imageViewsDots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dot_active));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {}

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i< dotCount; i++){
                    imageViewsDots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.dot_nonactive));
                }
                imageViewsDots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.dot_active));

            }

            @Override
            public void onPageScrollStateChanged(int i) {}
        });
    }

    @Override
    public void onClick(View view) {
        if (view == buttonEntrar){
            //Inicia outra activity
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);

        }else if(view == buttonCadastrar){
            Intent cadastrar = new Intent(this, FormPerfilTutorActivity.class);
            cadastrar.putExtra("action",getResources().getString(R.string.action_cadastro_perfil_tutor));
            startActivity(cadastrar);

        }

    }
}
