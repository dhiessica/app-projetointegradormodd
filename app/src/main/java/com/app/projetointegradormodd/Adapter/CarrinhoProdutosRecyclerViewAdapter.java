package com.app.projetointegradormodd.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.projetointegradormodd.HomePetActivity;
import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Model.Object.Produto;
import com.app.projetointegradormodd.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CarrinhoProdutosRecyclerViewAdapter extends RecyclerView.Adapter<CarrinhoProdutosRecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Produto> mList;
    private Activity activity;

    private CarrinhoProdutosRecyclerViewListner listner;

    public CarrinhoProdutosRecyclerViewAdapter(Context mContext, ArrayList<Produto> mList, Activity activity, CarrinhoProdutosRecyclerViewListner listner) {
        this.mContext = mContext;
        this.mList = mList;
        this.activity = activity;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view_produto_carrinho,parent,false);
        ViewHolder vHolder = new ViewHolder(view);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Produto produto = mList.get(position);

        //Insere dados
        Picasso.with(mContext).load(produto.getFoto()).into(holder.imageView);
        holder.textViewNome.setText(produto.getNome());
        holder.textViewQuantidade.setText(produto.getQuantidade()+"");
        holder.textViewValor.setText("R$ "+produto.getValor());

        holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove produto da lista
                listner.onExcludeClick(produto);
            }
        });

        holder.imageButtonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Verifica se a quantidade não é 1 para evitar valores negativos
                if (produto.getQuantidade() != 1){
                    produto.setQuantidade(produto.getQuantidade()-1);

                    //atualiza valor total
                    listner.onMenosClick(produto);
                }
            }
        });

        holder.imageButtonMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Limita o maximo da quantidade
                if (produto.getQuantidade() != 100){
                    produto.setQuantidade(produto.getQuantidade()+1);

                    //atualiza valor total
                    listner.onMaisClick(produto);
                }
            }
        });



    }

    public void returnActivityResult(Animal animal){

        //Retorna os dados do item clicado para a home pet
        Intent intent = new Intent(mContext, HomePetActivity.class);
        intent.putExtra("animal_selecionado",animal);

        activity.setResult(Activity.RESULT_OK,intent);
        activity.finish();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        //Variaveis para referênciar o layout
        private ImageView imageView;
        private TextView textViewNome;
        private TextView textViewValor;
        private TextView textViewQuantidade;
        private ImageButton imageButtonMenos;
        private ImageButton imageButtonMais;
        private ImageButton imageButtonDelete;

        public ViewHolder(@NonNull View itemView ) {
            super(itemView);

            //Referenciando o layout
            imageView = itemView.findViewById(R.id.image_view_produto_carrinho_item);
            textViewNome = itemView.findViewById(R.id.text_view_nome_produto_carrinho_item);
            textViewValor = itemView.findViewById(R.id.text_view_valor_produto_carrinho_item);
            textViewQuantidade = itemView.findViewById(R.id.text_view_quantidade_produto_carrinho_item);
            imageButtonMenos = itemView.findViewById(R.id.image_button_menos_produto_carrinho_item);
            imageButtonMais = itemView.findViewById(R.id.image_button_mais_produto_carrinho_item);
            imageButtonDelete = itemView.findViewById(R.id.image_button_delete_produto_carrinho_item);
        }
    }


    public interface CarrinhoProdutosRecyclerViewListner{

        void onExcludeClick(Produto produto);
        void onMenosClick(Produto produto);
        void onMaisClick(Produto produto);
    }
}
