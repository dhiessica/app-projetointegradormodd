package com.app.projetointegradormodd;

import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Model.Object.Atividade;
import com.app.projetointegradormodd.Model.Object.Endereco;
import com.app.projetointegradormodd.Model.Object.FormaPagamento;
import com.app.projetointegradormodd.Model.Object.Loja;
import com.app.projetointegradormodd.Model.Object.Pedido;
import com.app.projetointegradormodd.Model.Object.Produto;
import com.app.projetointegradormodd.Model.Object.Servico;
import com.app.projetointegradormodd.Model.Object.Tutor;
import com.app.projetointegradormodd.Services.API.AtividadeApi;
import com.app.projetointegradormodd.Services.API.LojaApi;
import com.app.projetointegradormodd.Services.API.ProdutoApi;
import com.app.projetointegradormodd.Services.API.ServicoApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.app.projetointegradormodd.Adapter.HomeLojaPageViewAdapter;
import com.app.projetointegradormodd.Interfaces.IDataCallback;
import com.app.projetointegradormodd.Interfaces.IFragmentListener;
import com.app.projetointegradormodd.Interfaces.ISearch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeLojaActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, SearchView.OnQueryTextListener, IFragmentListener, View.OnClickListener, TabLayout.BaseOnTabSelectedListener {

    //Variaveis para referênciar o layout
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton imageButtonHomePet;
    private ImageButton imageButtonHomeLoja;
    private ImageButton imageButtonHomeTutor;
    private FloatingActionButton floatingActionButtonSacola;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    //Search
    ArrayList<ISearch> iSearch = new ArrayList<>();
    private MenuItem searchMenuItem;
    private String newText;
    private HomeLojaPageViewAdapter adapter;
    private Loja currentLoja;
    private  ArrayList<Produto> produtosCarrinho = new ArrayList<>();
    private ArrayList<Produto> produtos = new ArrayList<>();
    private ArrayList<Servico> servicos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_loja);

        //Inciializa o Firebase Auth e pega o usuario logado
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Inicializa o layout
        initializeLayout();

        //Inicializa pegando dados da API e configurando/referenciando o layout
        getDataInitialize();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Boolean FAB_active = false;

        if (requestCode == 5 && resultCode == RESULT_OK){
            if(data.hasExtra("produto_add")) {
                //Atribuindo dados aos objetos
                Produto produtoAdd = (Produto) data.getSerializableExtra("produto_add");
                Boolean set = false;

                if (produtosCarrinho.size()>0) {

                    for (int i = 0; i < produtosCarrinho.size(); i++) {

                        if (produtosCarrinho.get(i).getId().equals(produtoAdd.getId())) {
                            produtosCarrinho.get(i).setQuantidade(produtosCarrinho.get(i).getQuantidade()+produtoAdd.getQuantidade());
                            set = true;
                            break;
                        }
                    }

                    if (!set){
                        produtosCarrinho.add(produtoAdd);
                    }

                }else{
                    produtosCarrinho.add(produtoAdd);
                }

                if (FAB_active == false){
                    floatingActionButtonSacola = findViewById(R.id.floating_action_button_carrinho_home_loja);
                    floatingActionButtonSacola.setVisibility(View.VISIBLE);
                    floatingActionButtonSacola.setClickable(true);
                    floatingActionButtonSacola.setOnClickListener(this);

                    FAB_active = true;
                }


                Log.e("teste_prodadd", produtosCarrinho.size()+"");

            }

        }
        else if (requestCode == 6 && resultCode == RESULT_OK) {

             if (data.hasExtra("produtos_carrinho")){

                produtosCarrinho = (ArrayList<Produto>) data.getSerializableExtra("produtos_carrinho");
                Log.e("teste_prodcarrinho", produtosCarrinho.size()+"");

                if (produtosCarrinho.size() != 0){

                    if (!FAB_active){
                        floatingActionButtonSacola = findViewById(R.id.floating_action_button_carrinho_home_loja);
                        floatingActionButtonSacola.setVisibility(View.VISIBLE);
                        floatingActionButtonSacola.setClickable(true);
                        floatingActionButtonSacola.setOnClickListener(this);

                        FAB_active = true;
                    }
                }else{
                    if (!FAB_active){
                        floatingActionButtonSacola = findViewById(R.id.floating_action_button_carrinho_home_loja);
                        floatingActionButtonSacola.setVisibility(View.INVISIBLE);
                        floatingActionButtonSacola.setClickable(false);

                        FAB_active = false;
                    }
                }
            }

        }


    }


    public void getDataInitialize(){

        //Trata a resposta da classe intermediária da chamada da Api
        LojaApi lojaApi = new LojaApi(new LojaApi.ConexaoListner() {
            @Override
            public void onAsyncData(Date response) {

            }

            @Override
            public void onAsyncObject(Loja response) {
                try{
                    //Pega a resposta em uma lista
                    Loja valido = response;

                    //Verifica se é válido
                    if(valido != null){

                        //Atribui à lista do layout
                        currentLoja = valido;

                        //Coleta as outras informações do banco
                        getDataInitializeProduto();
                    }

                }catch (Exception e){
                    Log.e("ERRO", "getprodutoslist:failure", e);
                    Toast.makeText(HomeLojaActivity.this, "Erro ao tentar coletar lista.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Chama método get da classe intermediária
        lojaApi.get(getResources().getString(R.string.loja_id));

    }

    public void getDataInitializeProduto(){

        //Trata a resposta da classe intermediária da chamada da Api
        ProdutoApi produtoApi = new ProdutoApi(new ProdutoApi.ConexaoListner() {
            @Override
            public void onAsyncData(Date response) {

            }

            @Override
            public void onAsyncObject(Produto response) {

            }

            @Override
            public void onAsyncObjectList(ArrayList<Produto> response) {
                try{
                    //Pega a resposta em uma lista
                    ArrayList<Produto> valido = response;

                    //Verifica se é válido
                    if(valido != null){

                        //Atribui à lista do layout
                        produtos = valido;

                    }

                    //Chama metodos para buscar os outros dados no banco
                    getDataInitializeServico();

                }catch (Exception e){
                    Log.e("ERRO", "getprodutoslist:failure", e);
                    Toast.makeText(HomeLojaActivity.this, "Erro ao tentar coletar lista.", Toast.LENGTH_SHORT).show();

                    //Chama metodos para buscar os outros dados no banco
                }
            }
        });

        //Chama método get da classe intermediária
        produtoApi.getList(currentLoja.getId());

    }

    public void getDataInitializeServico(){

        //Trata a resposta da classe intermediária da chamada da Api
        ServicoApi servicoApi = new ServicoApi(new ServicoApi.ConexaoListner() {
            @Override
            public void onAsyncData(Date response) {

            }

            @Override
            public void onAsyncObject(Servico response) {

            }

            @Override
            public void onAsyncObjectList(ArrayList<Servico> response) {
                try{
                    //Pega a resposta em uma lista
                    ArrayList<Servico> valido = response;

                    //Verifica se é válido
                    if(valido != null){

                        //Atribui à lista do layout
                        servicos = valido;

                    }

                    //Referencia e insere ações do layout
                    setUpLayout();

                }catch (Exception e){
                    Log.e("ERRO", "getservicoslist:failure", e);
                    Toast.makeText(HomeLojaActivity.this, "Erro ao tentar coletar lista.", Toast.LENGTH_SHORT).show();

                    //Referencia e insere ações do layout
                    setUpLayout();
                }
            }
        });

        //Chama método get da classe intermediária
        servicoApi.getList(currentLoja.getId());

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null){
            Intent inicial = new Intent(this, InicialActivity.class);
            startActivity(inicial);
        }
    }

    public void initializeLayout(){
        //Referencia as variaveis de layot
        setUpToolbars();

        //Adicionando tabs
        tabLayout = findViewById(R.id.tab_layout_home_loja);

        View view1 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view1.findViewById(R.id.tab_icon).setBackgroundResource(R.drawable.icon_produtos_azul);
        TextView tab1_title = view1.findViewById(R.id.tab_text);
        tab1_title.setText("PRODUTOS");
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1));


        View view2 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view2.findViewById(R.id.tab_icon).setBackgroundResource(R.drawable.icon_servico_azul);
        TextView tab2_title = view2.findViewById(R.id.tab_text);
        tab2_title.setText("SERVIÇOS");
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Inicializando ViewPager
        viewPager = findViewById(R.id.view_pager_home_loja);

        //Adicionando onTabSelectedListener para deslizar as views
        tabLayout.setOnTabSelectedListener(this);

    }

    public void setUpLayout(){

        try {
            //Criando PagerAdapter
            adapter = new HomeLojaPageViewAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), newText, produtos, servicos, new HomeLojaPageViewAdapter.LojaPageviewListner() {
                @Override
                public void onReceiveClick(Produto produto) {

                    Intent intent = new Intent(HomeLojaActivity.this, DetalhesProdutoActivity.class);
                    intent.putExtra("produto",produto);
                    intent.putExtra("loja",currentLoja);
                    startActivityForResult(intent,5);

                }
            });

            //Adicionando Adapter para o pageView
            viewPager.setAdapter(adapter);

        }catch (Exception e){
            Toast.makeText(HomeLojaActivity.this, "Ops! Tivemos algum problema ao tentar acessar os produtos.", Toast.LENGTH_LONG).show();

        }

    }

    public void setUpToolbars(){
        //Toolbar superior
        toolbar = findViewById(R.id.toolbar_superior);
        setSupportActionBar(toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

        mTitle.setText(getResources().getString(R.string.nome_tela_home_loja));

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Toolbar inferior
        imageButtonHomePet = findViewById(R.id.image_button_home_pet_action_bar);
        imageButtonHomeLoja = findViewById(R.id.image_button_home_loja_action_bar);
        imageButtonHomeTutor = findViewById(R.id.image_button_home_tutor_action_bar);

        //Ação do botão e selecionadores
        imageButtonHomePet.setOnClickListener(this);
        imageButtonHomeLoja.setOnClickListener(this);
        imageButtonHomeTutor.setOnClickListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ACTION BAR MENU E ITENS

        //Infla o menu na toolbar superior (botão search)
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_superior_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        if (view == imageButtonHomePet){

            Intent intent = new Intent(this, HomePetActivity.class);

            startActivity(intent);
            finish();
        }
        else if (view == imageButtonHomeLoja){
        }
        else if (view == imageButtonHomeTutor){
            Intent intent = new Intent(this, HomeTutorActivity.class);

            startActivity(intent);
            finish();

        }else if(view == floatingActionButtonSacola){
            Intent intent = new Intent(this, CarrinhoActivity.class);
            intent.putExtra("produtos",produtosCarrinho);
            startActivityForResult(intent,6);
        }

    }

    @Override
    public void addiSearch(ISearch iSearch) {
        this.iSearch.add(iSearch);
    }

    @Override
    public void removeISearch(ISearch iSearch) {
        this.iSearch.remove(iSearch);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.newText = newText;
        adapter.setTextQueryChanged(newText);

        for (ISearch iSearchLocal : this.iSearch)
            iSearchLocal.onTextQuery(newText);
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
