package com.app.projetointegradormodd;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.projetointegradormodd.Model.Object.Agendamento;
import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Model.Object.Endereco;
import com.app.projetointegradormodd.Model.Object.Servico;
import com.app.projetointegradormodd.Services.API.AgendamentoApi;
import com.app.projetointegradormodd.Services.API.AnimalApi;
import com.app.projetointegradormodd.Services.API.EnderecoApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AgendarStep2Activity extends AppCompatActivity implements View.OnClickListener {

    //Variáreis de layout
    private Toolbar toolbar;
    private CircleImageView circleImageViewIconAnimal;
    private TextView textViewNomeServico;
    private TextView textViewNomeAnimal;
    private TextView textViewEspecieAnimal;
    private TextView textViewCorAnimal;
    private TextView textViewPesoAnimal;
    private TextView textViewPelagemAnimal;
    private TextView textViewObservacoesAnimal;
    private TextView textViewValorServico;
    private TextView textViewDataServico;
    private TextView textViewHorarioServico;
    private TextView textViewValorTotalDeliveryAnimal;
    private TextView textViewValorTotalServico;
    private TextView textViewTituloAlert;
    private TextView textViewAlert;
    private RadioGroup radioGroupTransporte;
    private RadioButton radioButtonIncluir;
    private RadioButton radioButtonNaoIncluir;
    private Button buttonSolicitarAgendamento;
    private RelativeLayout relativeLayoutAlert;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    //Variáreis de dados
    private Animal animal;
    private Servico servico;
    private Agendamento agendamento = new Agendamento();
    private Endereco endereco;
    private Endereco enderecoLoja;
    private Double valorTotalServico = 0.0;
    private Double valorDeliveryAnimal = 7.0;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_step2);

        //Inciializa o Firebase Auth e pega o usuario logado
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Coleta dados do intent da outra tela
        recebeDados();

        //Inicializa pegando dados da API e configurando/referenciando o layout
        getDataInitialize(currentUser.getUid());
        getDataInitialize(getResources().getString(R.string.loja_id));

        //Referencia e insere ações do layout
        setUpLayout();

        //Escuta a seleção da data
        dateDialogListener();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK) {
            getDataInitialize(currentUser.getUid());
        }


    }

    public void recebeDados(){
        //Recebe dados da tela anterior

        Bundle extras = getIntent().getExtras();
        if(extras != null){

            //Atribuindo dados aos objetos
            animal = new Animal();
            animal = (Animal) getIntent().getSerializableExtra("animal");

            servico = new Servico();
            servico = (Servico) getIntent().getSerializableExtra("servico");

            agendamento.setAnimal(animal);
            agendamento.setServico(servico);
            agendamento.setIdAnimal(animal.getId());
            agendamento.setIdLoja(getResources().getString(R.string.loja_id));
            agendamento.setStatus(getResources().getString(R.string.hint_pendente));

        }


    }

    public void setUpToolbars(){
        //Toolbar superior
        toolbar = findViewById(R.id.toolbar_superior);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText(getResources().getString(R.string.nome_tela_agendar_serviço));

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
        circleImageViewIconAnimal = findViewById(R.id.circle_image_view_icon_pet_agendar_step2);
        textViewNomeServico = findViewById(R.id.text_view_nome_servico_agendar_step2);
        textViewNomeAnimal = findViewById(R.id.text_view_nome_pet_agendar_step_2);
        textViewEspecieAnimal = findViewById(R.id.text_view_especie_pet_agendar_step_2);
        textViewCorAnimal = findViewById(R.id.text_view_cor_pet_agendar_step2);
        textViewPesoAnimal = findViewById(R.id.text_view_peso_pet_agendar_step2);
        textViewPelagemAnimal = findViewById(R.id.text_view_pelagem_pet_agendar_step2);
        textViewObservacoesAnimal = findViewById(R.id.text_view_observacoes_pet_agendar_step2);
        textViewValorServico = findViewById(R.id.text_view_valor_agendar_step2);
        textViewDataServico = findViewById(R.id.text_view_data_agendar_step2);
        textViewHorarioServico = findViewById(R.id.text_view_horario_agendar_step2);
        textViewAlert = findViewById(R.id.text_view_alert2_agendar_step2);
        textViewTituloAlert = findViewById(R.id.text_view_alert_title_agendar_step_2);
        radioGroupTransporte = findViewById(R.id.radio_group_transporte_agendar_step2);
        radioButtonIncluir = findViewById(R.id.radio_button_incluir_agendar_step2);
        radioButtonNaoIncluir = findViewById(R.id.radio_button_nao_incluir_agendar_step2);
        textViewValorTotalDeliveryAnimal = findViewById(R.id.text_view_valor_delivery_pet_agendar_step2);
        textViewValorTotalServico = findViewById(R.id.text_view_valor_total_agendar_step2);
        buttonSolicitarAgendamento = findViewById(R.id.button_solicitar_agendamento_agendar_step2);
        relativeLayoutAlert = findViewById(R.id.relative_alert);

        textViewNomeAnimal.setText(animal.getNome());
        textViewEspecieAnimal.setText(animal.getEspecie());
        textViewCorAnimal.setText("Cor: "+animal.getCor());
        textViewPesoAnimal.setText(animal.getPeso());
        textViewPelagemAnimal.setText("Pelagem: "+animal.getPelagem());
        textViewObservacoesAnimal.setText("Obs: "+animal.getObservacoes());

        textViewNomeServico.setText(servico.getNome());
        textViewValorServico.setText("R$ "+servico.getValor());

        valorTotalServico = servico.getValor() + valorDeliveryAnimal;
        textViewValorTotalServico.setText("R$ "+valorTotalServico);

        //Ação do botão e selecionadores
        buttonSolicitarAgendamento.setOnClickListener(this);
        textViewDataServico.setOnClickListener(this);
        textViewHorarioServico.setOnClickListener(this);


        radioGroupTransporte.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radio_button_incluir_agendar_step2) {

                    validaRadiobuttonDefault();

                }
                else if(checkedId == R.id.radio_button_nao_incluir_agendar_step2) {

                    if (enderecoLoja != null) {

                        valorTotalServico = servico.getValor() ;
                        textViewValorTotalServico.setText("R$ " + valorTotalServico);

                        textViewValorTotalDeliveryAnimal.setText("R$ " + 0.0);

                        relativeLayoutAlert.setBackground(getDrawable(R.drawable.status_pendente));
                        textViewTituloAlert.setText("Aviso");

                        textViewAlert.setText(getResources().getString(R.string.text_agendamento_alerta_2) + "\n" +
                                enderecoLoja.getRua() + ", " + enderecoLoja.getNumero() + ", " + enderecoLoja.getCep() +
                                " - " + enderecoLoja.getCidade() + " " + enderecoLoja.getUf());

                        agendamento.setEndereco(endereco);
                        agendamento.setValorTransporte(0.0);
                        agendamento.setTipoTransporte("false");

                    }
                }
            }

        });

    }

    public void validaRadiobuttonDefault() {
        relativeLayoutAlert.setBackgroundColor(getColor(R.color.colorTransparent));
        textViewTituloAlert.setText("Endereço");

        if (endereco != null) {

            valorTotalServico = servico.getValor() + valorDeliveryAnimal;
            textViewValorTotalServico.setText("R$ " + valorTotalServico);

            textViewValorTotalDeliveryAnimal.setText("R$ " + valorDeliveryAnimal);
            textViewAlert.setText(endereco.getRua() + ", " + endereco.getNumero() + ", " + endereco.getCep() +
                    " - " + endereco.getCidade() + " " + endereco.getUf());

            agendamento.setEndereco(endereco);
            agendamento.setValorTransporte(valorDeliveryAnimal);
            agendamento.setValorTotal(valorTotalServico);
            agendamento.setTipoTransporte("Busca e entrega");
        }else{
            textViewAlert.setText("Selecione");

            textViewAlert.setOnClickListener(this);

        }
    }

    public void getDataInitialize(String id){

        //Trata a resposta da classe intermediária da chamada da Api
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

                        if (valido.getIdRemetente().equals(getResources().getString(R.string.loja_id))){
                            enderecoLoja = valido;

                        }else{
                            endereco = valido;

                        }
                    }
                    validaRadiobuttonDefault();
                }catch (Exception e){
                    Log.e("ERRO", "AgendarStep2:failure", e);
                    Toast.makeText(AgendarStep2Activity.this, "Erro ao tentar coletar lista.", Toast.LENGTH_SHORT).show();

                }
            }
        });


        //Chama método get da classe intermediária
        enderecoApi.get(id);

    }

    public void dateDialogListener(){
        //Coleta e insere as informações do dialog na caixa de texto
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                String data = day + "/" + month + "/" + year;
                textViewDataServico.setText(data);
                agendamento.setData(data);

            }
        };
    }

    public void dateSelector(){

        //Pega a data atual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //Configura a janela de seleção da data a aprtir da data atual
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AgendarStep2Activity.this,
                mDateSetListener,
                year,month,day);

        datePickerDialog.show();

    }

    public void timeSelector(){

        //Pega a data atual
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        //Configura a janela de seleção da data a aprtir da data atual
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                String horario = hour + "h " + minute + "m";
                textViewHorarioServico.setText(horario);
                agendamento.setHorario(horario);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

    @Override
    public void onClick(View view) {
        if(view == textViewDataServico){

            dateSelector();

        }
        else if(view == textViewHorarioServico){

            timeSelector();

        }
        else if(view == buttonSolicitarAgendamento){

            if (agendamento.getData() == null){
                Toast.makeText(AgendarStep2Activity.this,
                        "Selecione a data do agendamento.",
                        Toast.LENGTH_SHORT).show();
            }
            else if (agendamento.getHorario() == null){
                Toast.makeText(AgendarStep2Activity.this,
                        "Selecione o horario do agendamento.",
                        Toast.LENGTH_SHORT).show();
            }else if (agendamento.getEndereco() == null){
                Toast.makeText(AgendarStep2Activity.this,
                        "Você ainda não possui um endereço cadastrado.",
                        Toast.LENGTH_SHORT).show();
            }

            if (agendamento.getData() != null && agendamento.getHorario() != null && agendamento.getEndereco() != null) {
                criarAgendamento(agendamento);
            }


        }
        else if (view == textViewAlert){
            if (textViewAlert.getText().equals("Selecione")) {

                Intent intent = new Intent(AgendarStep2Activity.this,FormEnderecoActivity.class);
                intent.putExtra("action","create");
                startActivityForResult(intent,10);
            }
        }


    }

    public void criarAgendamento(final Agendamento agendamento){

        //Trata a resposta da classe intermediária da chamada da Api
        AgendamentoApi agendamentoApi = new AgendamentoApi(new AgendamentoApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {
                String valido = response;

                if(valido != null){
                    //Informa que o pet foi excluido
                    Toast.makeText(AgendarStep2Activity.this,
                            getResources().getString(R.string.toast_cria_agendamento_ok),
                            Toast.LENGTH_SHORT).show();

                    callNextActivity(agendamento);

                }else {
                    Toast.makeText(AgendarStep2Activity.this,
                            getResources().getString(R.string.toast_cria_agendamento_notok),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAsyncData(Date response) {

            }

            @Override
            public void onAsyncObject(Agendamento response) {

            }

            @Override
            public void onAsyncObjectList(ArrayList<Agendamento> response) {

            }
        });


        //Chama método get da classe intermediária
        agendamentoApi.create(animal.getId(),agendamento);

    }

    public void callNextActivity(Agendamento agendamento){

        finishAffinity();
        //Envia dados do item selecionado para a proxima tela
        Intent intent = new Intent(this, DetalhesAgendamentoActivity.class);

        intent.putExtra("agendamento",agendamento);
        intent.putExtra("action",getResources().getString(R.string.action_solicitar_agendamento));
        startActivity(intent);
        finish();

    }


}
