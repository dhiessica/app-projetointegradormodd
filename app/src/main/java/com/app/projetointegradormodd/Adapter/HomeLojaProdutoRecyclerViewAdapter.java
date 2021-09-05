package com.app.projetointegradormodd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.projetointegradormodd.AgendarStep1Activity;
import com.app.projetointegradormodd.DetalhesProdutoActivity;
import com.app.projetointegradormodd.Model.Object.Atividade;
import com.app.projetointegradormodd.Model.Object.Produto;
import com.app.projetointegradormodd.Model.Object.Servico;
import com.app.projetointegradormodd.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class HomeLojaProdutoRecyclerViewAdapter extends RecyclerView.Adapter<HomeLojaProdutoRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Produto> mList;
    private ArrayList<Produto> mListFull;
    private HomeLojaProdutoRecyclerViewListner listner;


    public HomeLojaProdutoRecyclerViewAdapter(Context mContext, ArrayList<Produto> mList, HomeLojaProdutoRecyclerViewListner listner) {
        this.mContext = mContext;
        this.mList = mList;
        mListFull = new ArrayList<>(mList);
        this.listner = listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view_produto_home_loja,parent,false);
        ViewHolder vHolder = new ViewHolder(view);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Produto produto = mList.get(position);

        //Inserindo os dados no layout
        Picasso.with(mContext).load(produto.getFoto()).into(holder.imageViewFoto);
        holder.textViewNome.setText(produto.getNome());
        holder.textViewCategoria.setText(produto.getCategoria());
        holder.textViewValor.setText("R$ "+produto.getValor());

        //Ação de clique nos itens da lista
        holder.constraintLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onProdutoClick(produto);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        //Variaveis para referênciar o layout
        private ImageView imageViewFoto;
        private TextView textViewNome;
        private TextView textViewCategoria;
        private TextView textViewValor;
        private ConstraintLayout constraintLayoutItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Referenciando o layout
            imageViewFoto = itemView.findViewById(R.id.image_view_produto_item);
            textViewNome = itemView.findViewById(R.id.text_view_nome_produto_item);
            textViewCategoria = itemView.findViewById(R.id.text_view_categoria_produto_item);
            textViewValor = itemView.findViewById(R.id.text_view_valor_produto_item);
            constraintLayoutItem = itemView.findViewById(R.id.constraint_layout_produto_item);
        }
    }

    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Produto> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(mListFull);

            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Produto item : mListFull){
                    if(item.getNome().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mList.clear();
            mList.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };

    public interface HomeLojaProdutoRecyclerViewListner{

        void onProdutoClick(Produto produto);
    }
}
