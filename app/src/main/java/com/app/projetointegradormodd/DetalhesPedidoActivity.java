package com.app.projetointegradormodd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.projetointegradormodd.Adapter.DetalhesPedidosRecyclerViewAdapter;
import com.app.projetointegradormodd.Model.Object.Endereco;
import com.app.projetointegradormodd.Model.Object.Pedido;

public class DetalhesPedidoActivity extends AppCompatActivity implements View.OnClickListener {

    //Variáreis de layout
    private Toolbar toolbar;
    private ImageButton imageButtonHomePet;
    private ImageButton imageButtonHomeLoja;
    private ImageButton imageButtonHomeTutor;
    private RelativeLayout relativeLayoutBackgroundStatus;
    private TextView textViewTextStatus;
    private TextView textViewHorarioEntrega;
    private TextView textViewData;
    private TextView textViewEndereco;
    private TextView textViewValorEntrega;
    private TextView textViewValorTotal;
    private RecyclerView mRecyclerView;
    private DetalhesPedidosRecyclerViewAdapter mRecyclerAdapter;

    //Variáreis de dados
    private Pedido pedido = new Pedido();
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pedido);

        //Coleta dados do intent da outra tela
        recebeDados();

        //Referencia e insere ações do layout
        setUpLayout();
    }

    public void recebeDados(){

        //Recebe dados da tela colaboradores (colaboradores recylerview adapter) onde foi clicado
        Bundle extras = getIntent().getExtras();
        if(extras != null){

            //Atribuindo dados aos objetos
            pedido = (Pedido) getIntent().getSerializableExtra("pedido");
            action = getIntent().getStringExtra("action");

        }
    }

    public void insereDados(){
        //Insere dados sem necessidade de validações
        Endereco endereco = pedido.getEnderecoEntrega();

        textViewHorarioEntrega.setText(pedido.getHora()+" - "+pedido.getPrevisaoEntrega());
        textViewEndereco.setText(endereco.getRua() + ", " + endereco.getNumero() + ", " + endereco.getCep() +
                " - " + endereco.getCidade() + " " + endereco.getUf());

        textViewData.setText(pedido.getData());
        textViewValorEntrega.setText("R$ "+pedido.getValorTransporte());
        textViewValorTotal.setText("R$ "+pedido.getValorTotal());

        //Valida status
        if (pedido.getStatus().equals(getResources().getString(R.string.hint_confirmado))){
            relativeLayoutBackgroundStatus.setBackground(getDrawable(R.drawable.status_confirmado));
            textViewTextStatus.setTextColor(getResources().getColor(R.color.colorStatusConfirmadoText));
            textViewTextStatus.setText(pedido.getStatus());
        }
        else if (pedido.getStatus().equals(getResources().getString(R.string.hint_pendente))){
            relativeLayoutBackgroundStatus.setBackground(getDrawable(R.drawable.status_pendente));
            textViewTextStatus.setTextColor(getResources().getColor(R.color.colorStatusPendenteText));
            textViewTextStatus.setText(pedido.getStatus());
        }


    }

    public void setUpLayout(){
        //Referencia as variaveis de layot
        setUpToolbars();
        imageButtonHomePet = findViewById(R.id.image_button_home_pet_action_bar);
        imageButtonHomeLoja = findViewById(R.id.image_button_home_loja_action_bar);
        imageButtonHomeTutor = findViewById(R.id.image_button_home_tutor_action_bar);
        textViewHorarioEntrega = findViewById(R.id.text_view_horario_entrega_detalhes_pedido);
        textViewTextStatus = findViewById(R.id.text_status_text_detalhes_pedido);
        textViewData = findViewById(R.id.text_view_data_detalhes_pedido);
        textViewEndereco = findViewById(R.id.text_view_endereco_detalhes_pedido);
        textViewData = findViewById(R.id.text_view_data_detalhes_pedido);
        textViewValorEntrega = findViewById(R.id.text_view_valor_total_transporte_detalhes_pedido);
        textViewValorTotal = findViewById(R.id.text_view_valor_total_detalhes_pedido);
        mRecyclerView = findViewById(R.id.recycler_view_produtos_detalhes_pedido);
        relativeLayoutBackgroundStatus = findViewById(R.id.relative_layout_status_background_detalhes_pedido);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerAdapter = new DetalhesPedidosRecyclerViewAdapter(this, pedido.getProdutos());
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.notifyDataSetChanged();

        insereDados();

        //Ação do botão e selecionadores
        imageButtonHomePet.setOnClickListener(this);
        imageButtonHomeLoja.setOnClickListener(this);
        imageButtonHomeTutor.setOnClickListener(this);

    }

    public void setUpToolbars(){
        //Toolbar superior
        toolbar = findViewById(R.id.toolbar_superior);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText(getResources().getString(R.string.nome_tela_detalhes_pedido));

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

        if (action.equals(getResources().getString(R.string.nome_tela_editar_atividade))){
        }

        //Ação da seta de voltar na toolbar
        if (item.getItemId() == android.R.id.home){
            if (action.equals(getResources().getString(R.string.action_enviar_pedido))){

                Intent intent = new Intent(DetalhesPedidoActivity.this,HomeLojaActivity.class);
                startActivity(intent);
                finish();

            }else if (action.equals(getResources().getString(R.string.action_visualizar_pedido))){

                finish();
            }
        }

        return super.onOptionsItemSelected(item);
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
}
