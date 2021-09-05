package com.app.projetointegradormodd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.projetointegradormodd.Adapter.AgendarStep1Adapter;
import com.app.projetointegradormodd.Model.Object.Loja;
import com.app.projetointegradormodd.Model.Object.Produto;
import com.app.projetointegradormodd.Model.Object.Tutor;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.security.PrivateKey;

public class DetalhesProdutoActivity extends AppCompatActivity implements View.OnClickListener {

    //Variáreis de layout
    private Toolbar toolbar;
    private ImageView imageViewFoto;
    private TextView textViewNome;
    private TextView textViewDescricao;
    private TextView textViewValorUnitario;
    private TextView textViewValorTotal;
    private TextView textViewQuantidade;
    private Button buttonAdicionarCarrinho;
    private ImageButton imageButtonMais;
    private ImageButton imageButtonMenos;

    //Variáreis de dados
    private Produto produto;
    private Loja loja;
    private Double valorTotal = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);

        //Recebe dados da tela anterior
        recebeDados();

        //Referencia e insere ações do layout
        setUpLayout();

    }

    public void recebeDados(){
        //Recebe dados da tela anterior
        Bundle extras = getIntent().getExtras();
        if(extras != null){

            //Atribuindo dados aos objetos
            produto = new Produto();
            produto = (Produto) getIntent().getSerializableExtra("produto");
            loja = new Loja();
            loja = (Loja) getIntent().getSerializableExtra("loja");

            valorTotal = produto.getValor()*produto.getQuantidade();
        }
    }

    public void setUpLayout(){
        //Referencia as variaveis de layout
        setUpToolbars();
        imageViewFoto = findViewById(R.id.image_view_foto_detalhes_produto);
        textViewNome = findViewById(R.id.text_view_nome_detalhes_produto);
        textViewDescricao = findViewById(R.id.text_view_descricao_detalhes_produto);
        textViewValorUnitario = findViewById(R.id.text_view_valor_unitario_detalhes_produto);
        textViewValorTotal = findViewById(R.id.text_view_valor_total_detalhes_produto);
        textViewQuantidade = findViewById(R.id.text_view_quantidade_detalhes_produto);
        imageButtonMais = findViewById(R.id.image_button_mais_detalhes_produto);
        imageButtonMenos = findViewById(R.id.image_button_menos_detalhes_produto);
        buttonAdicionarCarrinho = findViewById(R.id.button_adicionar_carrinho_detalhes_produto);

        insereDados();

        //Ação do botão
        buttonAdicionarCarrinho.setOnClickListener(this);
        imageButtonMais.setOnClickListener(this);
        imageButtonMenos.setOnClickListener(this);

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

    public void insereDados(){
        //Insere dados sem necessidade de validações
        Picasso.with(this).load(produto.getFoto()).into(imageViewFoto);
        textViewNome.setText(produto.getNome()+" - " +produto.getMarca());
        textViewDescricao.setText(
                "Vendido por "+ loja.getNome()+ "\nEmail: "+loja.getEmail() +"\nTelefone: "+loja.getTelefone()
                        +"\n\n"+produto.getDescricao());
        textViewValorUnitario.setText("R$ "+produto.getValor());
        textViewValorTotal.setText("R$ "+valorTotal);
        textViewQuantidade.setText(produto.getQuantidade()+"");
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

        if(view == imageButtonMais){
            //Limita o maximo da quantidade
            if (produto.getQuantidade() != 100){
                produto.setQuantidade(produto.getQuantidade()+1);
                valorTotal = (produto.getValor()*produto.getQuantidade());
                textViewQuantidade.setText(produto.getQuantidade()+"");
                textViewValorTotal.setText("R$ "+truncateDecimal(valorTotal,2));
            }
        }
        else if(view == imageButtonMenos){
            //Verifica se a quantidade não é 1 para evitar valores negativos
            if (produto.getQuantidade() != 1){
                produto.setQuantidade(produto.getQuantidade()-1);
                valorTotal = (produto.getValor()*produto.getQuantidade());
                textViewQuantidade.setText(produto.getQuantidade()+"");
                textViewValorTotal.setText("R$ "+truncateDecimal(valorTotal,2));
            }
        }
        else if(view == buttonAdicionarCarrinho){

            //Passa produto e habilita botao de ver carrinho de compra
            Intent intent = new Intent(this, HomeLojaActivity.class);
            intent.putExtra("produto_add",produto);
            setResult(RESULT_OK,intent);
            finish();

        }
    }

    private static BigDecimal truncateDecimal(double x, int numberofDecimals) {
        if ( x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
        }
    }
}
