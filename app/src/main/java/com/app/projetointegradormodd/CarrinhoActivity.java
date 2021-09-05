package com.app.projetointegradormodd;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projetointegradormodd.Adapter.CarrinhoProdutosRecyclerViewAdapter;
import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Model.Object.Atividade;
import com.app.projetointegradormodd.Model.Object.Endereco;
import com.app.projetointegradormodd.Model.Object.FormaPagamento;
import com.app.projetointegradormodd.Model.Object.Pedido;
import com.app.projetointegradormodd.Model.Object.Produto;
import com.app.projetointegradormodd.Services.API.EnderecoApi;
import com.app.projetointegradormodd.Services.API.FormaPagamentoApi;
import com.app.projetointegradormodd.Services.API.PedidoApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CarrinhoActivity extends AppCompatActivity implements View.OnClickListener {

    //Variaveis de layout
    private Toolbar toolbar;
    private RecyclerView recyclerViewProdutos;
    private TextView textViewFormaPagamento;
    private TextView textViewEndereco;
    private TextView textViewTaxaEntrega;
    private TextView textViewValorTotal;
    private Button buttonFinalizarCompra;

    //Variaveis de dados
    private Pedido pedido = new Pedido();
    private Endereco endereco = new Endereco();
    private FormaPagamento formaPagamento = new FormaPagamento();
    private ArrayList<Produto> produtos = new ArrayList<>();
    private RecyclerView mRecyclerView = null;
    private CarrinhoProdutosRecyclerViewAdapter mRecyclerAdapter = null;
    private Double valorEntrega = 7.0;
    private Double valorTotal = 0.0;
    private Integer tempoEntrega = 4;
    private Date today = Calendar.getInstance().getTime();
    private Boolean result = false;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        //Inciializa o Firebase Auth e pega o usuario logado
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Recebe dados da tela anterior
        getDataPreviousActivity();

        //Inicializa o layout
        initializeLayout();

        setDataInitialize();

        //Inicializa pegando dados da API e configurando/referenciando o layout
        getDataInitialize(currentUser.getUid());
        getFormaPagamento(currentUser.getUid());


        setUpLayout();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 8 && resultCode == RESULT_OK){
            //Inicializa pegando dados da API e configurando/referenciando o layout
            getFormaPagamento(currentUser.getUid());
            result = true;


        }
        else if (requestCode == 9 && resultCode == RESULT_OK) {
            getDataInitialize(currentUser.getUid());
            result = true;
        }


    }

    public void getDataPreviousActivity(){
        //Recebe dados da tela anterior
        Bundle extras = getIntent().getExtras();
        if(extras != null){

            //Atribuindo dados aos objetos
            produtos = (ArrayList<Produto>) getIntent().getSerializableExtra("produtos");
        }
    }

    public void initializeLayout(){
        //Referencia as variaveis de layout
        setUpToolbars();
        textViewFormaPagamento = findViewById(R.id.text_view_forma_pagamento_carrinho);
        textViewEndereco = findViewById(R.id.text_view_endereco_carrinho);
        textViewTaxaEntrega = findViewById(R.id.text_view_valor_entrega_carrinho);
        textViewValorTotal = findViewById(R.id.text_view_valor_total_carrinho);
        buttonFinalizarCompra = findViewById(R.id.button_finalizar_compra_carrinho);
        mRecyclerView = findViewById(R.id.recycler_view_produtos_carrinho);

        //Ação do botão
        buttonFinalizarCompra.setOnClickListener(this);
        textViewFormaPagamento.setOnClickListener(this);
        textViewEndereco.setOnClickListener(this);

    }

    public void setDataInitialize(){

        Double totalProdutos = 0.0;

        try {

            for (int i =0; i< produtos.size(); i++){
                Produto prod = produtos.get(i);
                Double multiplicaProd = 0.0;
                Double somaProd = 0.0;

                multiplicaProd =  prod.getQuantidade() * prod.getValor();

                somaProd =+multiplicaProd;

                totalProdutos = totalProdutos+ somaProd;

                Log.e("soma",somaProd.toString());
            }
            Log.e("teste",totalProdutos.toString());

            valorTotal = totalProdutos+valorEntrega;
            textViewTaxaEntrega.setText("R$ "+truncateDecimal(valorEntrega,2));
            textViewValorTotal.setText("R$ "+truncateDecimal(valorTotal,2));


        }catch (Exception e){
            if (produtos.size() ==0){
                Toast.makeText(CarrinhoActivity.this, "Ops!Acho que você não possui mais produtos no seu carrinho.", Toast.LENGTH_LONG).show();

                textViewTaxaEntrega.setText("R$ "+0.0);
                textViewValorTotal.setText("R$ "+0.0);
            }
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

                        endereco = valido;

                    }
                        setDataApi1();

                        setUpLayout();

                }catch (Exception e){
                    Log.e("ERRO", "CarrinhoActivity:failure", e);
                    Toast.makeText(CarrinhoActivity.this, "Erro ao tentar buscar endereço.", Toast.LENGTH_SHORT).show();
                    setDataApi();
                    setUpLayout();
                }
            }
        });

        //Chama método get da classe intermediária
        enderecoApi.get(id);

    }

    public void getFormaPagamento(String id){

        //Trata a resposta da classe intermediária da chamada da Api
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
                    }

                        setDataApi2();

                        setUpLayout();


                }catch (Exception e){
                    Log.e("ERRO", "CarrinhoActivity:failure", e);
                    Toast.makeText(CarrinhoActivity.this, "Erro ao tentar buscar formapagamento.", Toast.LENGTH_SHORT).show();
                    setDataApi();
                    setUpLayout();
                }
            }
        });


        //Chama método get da classe intermediária
        formaPagamentoApi.get(id);

    }

    public void setDataApi(){
        if (endereco != null){
            textViewEndereco.setText(endereco.getRua() + ", " + endereco.getNumero() + ", " + endereco.getCep() +
                    " - " + endereco.getCidade() + " " + endereco.getUf());


        }else {
            textViewEndereco.setText("Selecione");

            textViewEndereco.setOnClickListener(this);


        }

        if (formaPagamento != null){
            String last ="";
            try {

                String characters = formaPagamento.getNumeroCartao();
                last = characters.substring(characters.length() - 4);
            }catch (Exception e){ }

            textViewFormaPagamento.setText("Cartão de final "
                    + last
                    + " \nValidade "+formaPagamento.getValidade());

        }else {
            textViewFormaPagamento.setText("Selecione");

            textViewFormaPagamento.setOnClickListener(this);


        }
    }

    public void setDataApi1(){
        if (endereco != null){
            textViewEndereco.setText(endereco.getRua() + ", " + endereco.getNumero() + ", " + endereco.getCep() +
                    " - " + endereco.getCidade() + " " + endereco.getUf());


        }else {
            textViewEndereco.setText("Selecione");

            textViewEndereco.setOnClickListener(this);


        }

    }

    public void setDataApi2(){

        if (formaPagamento != null){
            String last ="";
            try {

                String characters = formaPagamento.getNumeroCartao();
                last = characters.substring(characters.length() - 4);
            }catch (Exception e){ }

            textViewFormaPagamento.setText("Cartão de final "
                    + last
                    + " \nValidade "+formaPagamento.getValidade());

        }else {
            textViewFormaPagamento.setText("Selecione");

            textViewFormaPagamento.setOnClickListener(this);


        }
    }

    public void setUpLayout(){
        //Tenta inserir dados no layout
        try {

            //Criando e configurando adapter no recyclerview
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerAdapter = new CarrinhoProdutosRecyclerViewAdapter(this, produtos, CarrinhoActivity.this,
                    new CarrinhoProdutosRecyclerViewAdapter.CarrinhoProdutosRecyclerViewListner() {
                        @Override
                        public void onExcludeClick(Produto produto) {

                            produtos.remove(produto);
                            mRecyclerView.setAdapter(mRecyclerAdapter);
                            mRecyclerAdapter.notifyDataSetChanged();
                            setDataInitialize();
                            setUpLayout();

                            if (produtos.size() ==0){
                                Toast.makeText(CarrinhoActivity.this, "Ops!Acho que você não possui mais produtos no seu carrinho.", Toast.LENGTH_LONG).show();

                                textViewTaxaEntrega.setText("R$ "+0.0);
                                textViewValorTotal.setText("R$ "+0.0);

                                //Passa produto e habilita botao de ver carrinho de compra
                                Intent intent = new Intent(CarrinhoActivity.this, HomeLojaActivity.class);
                                intent.putExtra("produtos_carrinho",produtos);
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        }

                        @Override
                        public void onMenosClick(Produto produto) {

                            for (int i = 0; i< produtos.size();i++){
                                if (produtos.get(i).getId().equals(produto.getId())){

                                    produtos.set(i,produto);

                                }
                            }
                            setDataInitialize();
                            setUpLayout();

                        }

                        @Override
                        public void onMaisClick(Produto produto) {

                            for (int i = 0; i< produtos.size();i++){
                                if (produtos.get(i).getId().equals(produto.getId())){

                                    produtos.set(i,produto);


                                }
                            }
                            setDataInitialize();
                            setUpLayout();
                        }
                    }
            );
            mRecyclerView.setAdapter(mRecyclerAdapter);
            mRecyclerAdapter.notifyDataSetChanged();



        }catch (Exception e){
            Log.e("errolistas",e.toString());
            Toast.makeText(CarrinhoActivity.this, getResources().getString(R.string.hint_sem_registros), Toast.LENGTH_LONG).show();

        }

    }

    public void setUpToolbars(){
        //Toolbar superior
        toolbar = findViewById(R.id.toolbar_superior);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText(getResources().getString(R.string.nome_tela_carrinho));

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

            //Passa produto e habilita botao de ver carrinho de compra
            Intent intent = new Intent(this, HomeLojaActivity.class);
            intent.putExtra("produtos_carrinho",produtos);
            setResult(RESULT_OK,intent);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == textViewFormaPagamento){

            if (textViewFormaPagamento.getText().equals("Selecione")) {

                Intent intent = new Intent(CarrinhoActivity.this,FormFormasPagamentoActivity.class);
                intent.putExtra("action","create");
                startActivityForResult(intent,8);


            }
        }
        else if (view == textViewEndereco){
            if (textViewEndereco.getText().equals("Selecione")) {

                Intent intent = new Intent(CarrinhoActivity.this,FormEnderecoActivity.class);
                intent.putExtra("action","create");
                startActivityForResult(intent,9);


            }
        }
        else if (view == buttonFinalizarCompra){

            pedido.setId("");
            pedido.setStatus(getResources().getString(R.string.hint_pendente));
            pedido.setIdTutor(currentUser.getUid());
            pedido.setIdLoja(getResources().getString(R.string.loja_id));
            pedido.setProdutos(produtos);
            pedido.setEnderecoEntrega(endereco);
            pedido.setFormaPagamento(formaPagamento);
            pedido.setHora(today.getHours()+"h "+today.getMinutes()+"m");
            pedido.setData(today.getDay()+"/"+today.getMonth()+"/"+today.getYear());
            pedido.setPrevisaoEntrega(today.getHours()+tempoEntrega+"h "+today.getMinutes()+"m");
            pedido.setValorTransporte(valorEntrega.toString());
            pedido.setValorTotal(valorTotal.toString());

            //gera pedido no banco e redireciona para acompanhe seu pedido pendente
            if (pedido.getEnderecoEntrega().getCep() == null){
                Toast.makeText(CarrinhoActivity.this,
                        "Selecione o endereço de entrega.",
                        Toast.LENGTH_SHORT).show();
            }
            else if (pedido.getFormaPagamento().getNumeroCartao() == null){
                Toast.makeText(CarrinhoActivity.this,
                        "Selecione a forma de pagamento do pedido.",
                        Toast.LENGTH_SHORT).show();
            }
            else if (pedido.getProdutos().size() == 0){
                Toast.makeText(CarrinhoActivity.this,
                        "Não há produtos no seu carrinho.",
                        Toast.LENGTH_SHORT).show();
            }else {

                criarPedido(pedido);
            }

        }

    }

    public void criarPedido(final Pedido pedido){


        //Trata a resposta da classe intermediária da chamada da Api
        PedidoApi pedidoApi = new PedidoApi(new PedidoApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {
                String valido = response;

                if(valido != null){
                    //Informa que o pet foi excluido
                    Toast.makeText(CarrinhoActivity.this,
                            getResources().getString(R.string.toast_cria_pedido_ok),
                            Toast.LENGTH_SHORT).show();

                    callNextActivity(pedido);

                }else {
                    Toast.makeText(CarrinhoActivity.this,
                            getResources().getString(R.string.toast_cria_pedido_notok),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAsyncData(Date response) {

            }

            @Override
            public void onAsyncObject(Pedido response) {

            }

            @Override
            public void onAsyncObjectList(ArrayList<Pedido> response) {

            }
        });


        //Chama método get da classe intermediária
        pedidoApi.create(currentUser.getUid(),pedido);

    }

    public void callNextActivity(Pedido pedido){

        finishAffinity();
        //Envia dados do item selecionado para a proxima tela
        Intent intent = new Intent(this, DetalhesPedidoActivity.class);

        intent.putExtra("pedido",pedido);
        intent.putExtra("action",getResources().getString(R.string.action_enviar_pedido));

        startActivity(intent);
        finish();



    }

    private static BigDecimal truncateDecimal(double x, int numberofDecimals)
    {
        if ( x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
        }
    }


}
