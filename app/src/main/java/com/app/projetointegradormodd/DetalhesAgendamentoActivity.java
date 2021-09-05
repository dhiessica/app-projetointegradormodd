package com.app.projetointegradormodd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.projetointegradormodd.Model.Object.Agendamento;

public class DetalhesAgendamentoActivity extends AppCompatActivity implements View.OnClickListener {

    //Variáreis de layout
    private Toolbar toolbar;
    private ImageButton imageButtonHomePet;
    private ImageButton imageButtonHomeLoja;
    private ImageButton imageButtonHomeTutor;
    private TextView textViewNomeServico;
    private RelativeLayout relativeLayoutBackgroundStatus;
    private TextView textViewTextStatus;
    private TextView textViewValorServico;
    private CircleImageView circleImageViewIconPet;
    private TextView textViewNomePet;
    private TextView textViewData;
    private TextView textViewHorario;
    private TextView textViewTransporteTitulo;
    private TextView textViewTransporteInfo;
    private LinearLayout linearLayoutTransporteValor;
    private TextView textViewValorTotalTransporte;
    private TextView textViewValorTotalServico;


    //Variáreis de dados
    private Agendamento agendamento;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_agendamento);

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
            agendamento = new Agendamento();
            agendamento = (Agendamento) getIntent().getSerializableExtra("agendamento");
            action = getIntent().getStringExtra("action");

        }
    }

    public void insereDados(){
        //Insere dados sem necessidade de validações
        textViewNomeServico.setText(agendamento.getServico().getNome());
        textViewValorServico.setText("R$ "+agendamento.getServico().getValor().toString());
        textViewNomePet.setText(agendamento.getAnimal().getNome());
        textViewData.setText(agendamento.getData());
        textViewHorario.setText(agendamento.getHorario());
        textViewValorTotalServico.setText("R$ "+agendamento.getValorTotal());

        //Valida status
        if (agendamento.getStatus().equals(getResources().getString(R.string.hint_confirmado))){
            relativeLayoutBackgroundStatus.setBackground(getDrawable(R.drawable.status_confirmado));
            textViewTextStatus.setTextColor(getResources().getColor(R.color.colorStatusConfirmadoText));
            textViewTextStatus.setText(agendamento.getStatus());
        }
        else if (agendamento.getStatus().equals(getResources().getString(R.string.hint_pendente))){
            relativeLayoutBackgroundStatus.setBackground(getDrawable(R.drawable.status_pendente));
            textViewTextStatus.setTextColor(getResources().getColor(R.color.colorStatusPendenteText));
            textViewTextStatus.setText(agendamento.getStatus());
        }

        //Valida a especie para inserir o icon do animal
        if(agendamento.getAnimal().getEspecie().equals(getResources().getString(R.string.variable_especie_cachorro))){
            circleImageViewIconPet.setImageResource(R.drawable.icon_cao_borda_branca_background);
        }else if(agendamento.getAnimal().getEspecie().equals(getResources().getString(R.string.variable_especie_gato))){
            circleImageViewIconPet.setImageResource(R.drawable.icon_gato_borda_branca_background);
        }

        //Valida se o serviço de busca e entrega do pet foi contratado
        if (agendamento.getTipoTransporte().equals("false")){
            textViewTransporteTitulo.setText(getResources().getString(R.string.variable_transporte_nao_contratado));
            textViewTransporteInfo.setText(getResources().getString(R.string.variable_endereco_loja));
        }else if (!agendamento.getTipoTransporte().equals("false")){
            textViewTransporteTitulo.setText(getResources().getString(R.string.variable_transporte_contratado));
            textViewTransporteInfo.setText(agendamento.getTipoTransporte());
            linearLayoutTransporteValor.setVisibility(View.VISIBLE);
            textViewValorTotalTransporte.setText("R$ "+agendamento.getValorTransporte());
        }
    }

    public void setUpLayout(){
        //Referencia as variaveis de layot
        setUpToolbars();
        imageButtonHomePet = findViewById(R.id.image_button_home_pet_action_bar);
        imageButtonHomeLoja = findViewById(R.id.image_button_home_loja_action_bar);
        imageButtonHomeTutor = findViewById(R.id.image_button_home_tutor_action_bar);
        textViewNomeServico = findViewById(R.id.text_view_nome_servico_detalhes_agendamento);
        textViewTextStatus = findViewById(R.id.include_status_text_detalhes_agendamento);
        textViewValorServico = findViewById(R.id.text_view_valor_servico_detalhes_agendamento);
        textViewNomePet = findViewById(R.id.text_view_nome_pet_detalhes_agendamento);
        textViewData = findViewById(R.id.text_view_data_detalhes_agendamento);
        textViewHorario = findViewById(R.id.text_view_horario_detalhes_agendamento);
        textViewTransporteTitulo = findViewById(R.id.text_view_titulo_transporte_detalhes_agendamento);
        textViewTransporteInfo = findViewById(R.id.text_view_info_transporte_detalhes_agendamento);
        textViewValorTotalTransporte = findViewById(R.id.text_view_valor_total_transporte_detalhes_agendamento);
        textViewValorTotalServico = findViewById(R.id.text_view_valor_total_detalhes_agendamento);
        relativeLayoutBackgroundStatus = findViewById(R.id.relative_layout_status_background_detalhes_agendamento);
        linearLayoutTransporteValor = findViewById(R.id.linear_layout_transporte_valor_detalhes_agendamento);
        circleImageViewIconPet = findViewById(R.id.circle_image_view_icon_pet_detalhes_agendamento);

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

        mTitle.setText(getResources().getString(R.string.nome_tela_detalhes_agendamento));

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
            if (action.equals(getResources().getString(R.string.action_solicitar_agendamento))){

                Intent intent = new Intent(DetalhesAgendamentoActivity.this,HomeLojaActivity.class);
                startActivity(intent);
                finish();

            }else if (action.equals(getResources().getString(R.string.action_visualizar_agendamento))){

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
