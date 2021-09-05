package com.app.projetointegradormodd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.projetointegradormodd.DetalhesProdutoActivity;
import com.app.projetointegradormodd.Model.Object.Produto;
import com.app.projetointegradormodd.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class DetalhesPedidosRecyclerViewAdapter extends RecyclerView.Adapter<DetalhesPedidosRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Produto> mList;


    public DetalhesPedidosRecyclerViewAdapter(Context mContext, ArrayList<Produto> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view_produto_detalhes_pedido,parent,false);
        ViewHolder vHolder = new ViewHolder(view);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Produto produto = mList.get(position);

        //Inserindo os dados no layout
        Picasso.with(mContext).load(produto.getFoto()).into(holder.imageViewFoto);
        holder.textViewNome.setText(produto.getNome());
        holder.textViewQuantidade.setText("x "+produto.getQuantidade());
        holder.textViewValor.setText("R$ "+produto.getValor().toString());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        //Variaveis para referênciar o layout
        private ImageView imageViewFoto;
        private TextView textViewNome;
        private TextView textViewQuantidade;
        private TextView textViewValor;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Referenciando o layout
            imageViewFoto = itemView.findViewById(R.id.image_view_produto_detalhes_item);
            textViewNome = itemView.findViewById(R.id.text_view_nome_produto_detalhes_item);
            textViewQuantidade = itemView.findViewById(R.id.text_view_quantidade_produto_detalhes_item);
            textViewValor = itemView.findViewById(R.id.text_view_valor_produto_detalhes_item);
        }
    }

}
