package com.app.projetointegradormodd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.projetointegradormodd.Model.Object.Produto;
import com.app.projetointegradormodd.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeTutorCardItemProdutosRecyclerViewAdapter extends RecyclerView.Adapter<HomeTutorCardItemProdutosRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Produto> mList;


    public HomeTutorCardItemProdutosRecyclerViewAdapter(Context mContext, ArrayList<Produto> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_item_recycler_view_compras_anteriores,parent,false);
        ViewHolder vHolder = new ViewHolder(view);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Produto produto = mList.get(position);

        //Inserindo os dados no layout
        holder.textViewQuantidade.setText(String.valueOf(produto.getQuantidade()));
        holder.textViewNomeProduto.setText(produto.getNome());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        //Variaveis para referênciar o layout
        private TextView textViewQuantidade;
        private TextView textViewNomeProduto;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Referenciando o layout
            textViewQuantidade = itemView.findViewById(R.id.text_view_quantidade_item_compras_anteriores);
            textViewNomeProduto = itemView.findViewById(R.id.text_view_nome_produto_item_compras_anteriores);
        }
    }


}
