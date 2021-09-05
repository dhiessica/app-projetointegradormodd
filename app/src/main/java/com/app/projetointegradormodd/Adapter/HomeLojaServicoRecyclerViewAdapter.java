package com.app.projetointegradormodd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.app.projetointegradormodd.AgendarStep1Activity;
import com.app.projetointegradormodd.Model.Object.Servico;
import com.app.projetointegradormodd.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class HomeLojaServicoRecyclerViewAdapter extends RecyclerView.Adapter<HomeLojaServicoRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Servico> mList;
    private ArrayList<Servico> mListFull;


    public HomeLojaServicoRecyclerViewAdapter(Context mContext, ArrayList<Servico> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mListFull = new ArrayList<>(mList);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view_servico_home_loja,parent,false);
        ViewHolder vHolder = new ViewHolder(view);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Servico servico = mList.get(position);

        //Inserindo os dados no layout
        holder.textViewNome.setText(servico.getNome());
        holder.textViewCategoria.setText(servico.getCategoria());
        holder.textViewValor.setText("R$ "+servico.getValor().toString());

        //Ação de clique nos itens da lista
        holder.constraintLayoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Envia dados do item clicado para a activity colaborador
                Intent intent = new Intent(mContext, AgendarStep1Activity.class);
                intent.putExtra("servico_item_click",servico);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        //Variaveis para referênciar o layout
        private TextView textViewNome;
        private TextView textViewCategoria;
        private TextView textViewValor;
        private ConstraintLayout constraintLayoutItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Referenciando o layout
            textViewNome = itemView.findViewById(R.id.text_view_nome_servico_item);
            textViewCategoria = itemView.findViewById(R.id.text_view_categoria_servico_item);
            textViewValor = itemView.findViewById(R.id.text_view_valor_servico_item);
            constraintLayoutItem = itemView.findViewById(R.id.constraint_layout_servico_item);
        }
    }

    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Servico> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(mListFull);

            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Servico item : mListFull){
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
}
