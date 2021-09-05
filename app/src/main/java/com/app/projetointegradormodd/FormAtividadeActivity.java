package com.app.projetointegradormodd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Model.Object.Atividade;
import com.app.projetointegradormodd.Model.Object.Servico;
import com.app.projetointegradormodd.Services.API.AtividadeApi;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class FormAtividadeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    //Variáreis de layout
    private Toolbar toolbar;
    private TextInputLayout textInputLayoutTitulo;
    private Spinner spinnerCategoria;
    private TextView textViewHorario;
    private TextView textViewDataInicial;
    private TextView textViewDataFinal;
    private TextInputLayout textInputLayoutObservacoes;
    private DatePickerDialog.OnDateSetListener mDateSetListenerInicial;
    private DatePickerDialog.OnDateSetListener mDateSetListenerFinal;
    private Button buttonSalvar;

    //Variáreis de dados
    private Atividade atividade = new Atividade();
    private String categoria = null;
    private String action;
    private Animal currentAnimal;
    private Atividade previousAtividade= new Atividade();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_atividade);

        //Recebe dados da tela anterior
        recebeDados();

        //Referencia e insere ações do layout
        setUpLayout();

        //Escuta a seleção da data
        dateDialogListener();

        if (action.equals(getResources().getString(R.string.nome_tela_editar_atividade))){
            previousAtividade = (Atividade) getIntent().getSerializableExtra("atividade");
            setLayoutData();
        }
    }

    public void recebeDados(){

        //Recebe dados da tela anterior
        Bundle extras = getIntent().getExtras();
        if(extras != null){

            //Atribuindo dados ao objeto
            action = getIntent().getStringExtra("action");
            currentAnimal = (Animal) getIntent().getSerializableExtra("animal");

        }


    }

    private void setUpLayout() {
        //Referenciando itens de layout
        setUpToolbars();
        textInputLayoutTitulo = findViewById(R.id.text_input_layout_titulo_form_atividade);
        spinnerCategoria = findViewById(R.id.spinner_categoria_form_atividade);
        textViewHorario = findViewById(R.id.text_view_horario_form_atividade);
        textViewDataInicial = findViewById(R.id.text_view_data_inicial_form_atividade);
        textViewDataFinal = findViewById(R.id.text_view_data_final_form_atividade);
        textInputLayoutObservacoes = findViewById(R.id.text_input_layout_observacoes_form_atividade);
        buttonSalvar = findViewById(R.id.button_salvar_form_atividade);

        //Ação do botão e selecionadores
        spinnerCategoria.setOnItemSelectedListener(this);
        textViewHorario.setOnClickListener(this);
        textViewDataInicial.setOnClickListener(this);
        textViewDataFinal.setOnClickListener(this);
        buttonSalvar.setOnClickListener(this);
    }

    private void setLayoutData() {
        //Insere os dados da atividade selecionada para editar no formulário
        textInputLayoutTitulo.getEditText().setText(previousAtividade.getTitulo());

        //busca a categoria no select
        for (int i = 0; i < spinnerCategoria.getCount(); i++) {
            if (spinnerCategoria.getItemAtPosition(i).equals(previousAtividade.getCategoria())) {
                spinnerCategoria.setSelection(i);
                break;
            }
        }

        textViewHorario.setText(previousAtividade.getHorario());
        textViewDataInicial.setText(previousAtividade.getDataInicial());
        textViewDataFinal.setText(previousAtividade.getDataFinal());
        textInputLayoutObservacoes.getEditText().setText(previousAtividade.getObservacoes());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        categoria = adapterView.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void dateDialogListener(){

        //Coleta e insere as informações do dialog na caixa de texto
        mDateSetListenerInicial = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                String data = day + "/" + month + "/" + year;
                textViewDataInicial.setText(data);

            }
        };

        //Coleta e insere as informações do dialog na caixa de texto
        mDateSetListenerFinal = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                String data = day + "/" + month + "/" + year;
                textViewDataFinal.setText(data);

            }
        };

    }

    public void dateSelector(String tipo){

        //Pega a data atual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;

        if (tipo.equals("inicial")){
            //Configura a janela de seleção da data a apartir da data atual
            datePickerDialog = new DatePickerDialog(
                    FormAtividadeActivity.this,
                    mDateSetListenerInicial,
                    year,month,day);

            datePickerDialog.show();
        }
        else if (tipo.equals("final")){
            //Configura a janela de seleção da data a apartir da data atual
            datePickerDialog = new DatePickerDialog(
                    FormAtividadeActivity.this,
                    mDateSetListenerFinal,
                    year,month,day);

            datePickerDialog.show();
        }

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
                textViewHorario.setText(horario);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

    public void coletaValidaDados(){
        atividade.setTitulo(textInputLayoutTitulo.getEditText().getText().toString());
        atividade.setCategoria(categoria);
        atividade.setHorario(textViewHorario.getText().toString());
        atividade.setDataInicial(textViewDataInicial.getText().toString());
        atividade.setDataFinal(textViewDataFinal.getText().toString());
        atividade.setObservacoes(textInputLayoutObservacoes.getEditText().getText().toString());

        if (atividade.getTitulo().isEmpty()){
            textInputLayoutTitulo.setError("Favor inserir o titulo da atividade");
            return;
        }
        else if(atividade.getCategoria() == null || atividade.getCategoria().equals("Selecione")){
            Toast.makeText(this,"Insira uma categoria para a atividade.",Toast.LENGTH_SHORT).show();
        }
        else if(atividade.getDataInicial().isEmpty()){
            Toast.makeText(this,"Insira a data inicial.",Toast.LENGTH_SHORT).show();
        }

        if (action.equals(getResources().getString(R.string.nome_tela_criar_atividade))){

            atividade.setIdAnimal(currentAnimal.getId());

            //Envia dados do item selecionado para a proxima tela
            Intent intent = new Intent(this, HomePetActivity.class);
            intent.putExtra("atividade_create",atividade);
            intent.putExtra("animal_create",currentAnimal);

            setResult(RESULT_OK,intent);
            finish();

        }else if (action.equals(getResources().getString(R.string.nome_tela_editar_atividade))){

            atividade.setId(previousAtividade.getId());
            atividade.setIdAnimal(currentAnimal.getId());

            //Envia dados do item selecionado para a proxima tela
            Intent intent = new Intent(this, HomePetActivity.class);
            intent.putExtra("atividade_update",atividade);
            intent.putExtra("animal_update",currentAnimal);

            setResult(RESULT_OK,intent);
            finish();
        }

    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    @Override
    public void onClick(View view) {
        if (view == textViewDataInicial){
            dateSelector("inicial");
        }
        else if (view == textViewDataFinal){
            dateSelector("final");
        }
        else if (view == textViewHorario){
            timeSelector();

        }
        else if (view == buttonSalvar){
            hideKeyboard(FormAtividadeActivity.this);
            textInputLayoutTitulo.clearFocus();
            textInputLayoutObservacoes.clearFocus();
            coletaValidaDados();

        }

    }

}
