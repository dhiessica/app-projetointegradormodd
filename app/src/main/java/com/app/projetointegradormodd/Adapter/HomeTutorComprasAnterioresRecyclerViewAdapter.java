package com.app.projetointegradormodd.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.projetointegradormodd.DetalhesPedidoActivity;
import com.app.projetointegradormodd.Model.Object.Pedido;
import com.app.projetointegradormodd.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeTutorComprasAnterioresRecyclerViewAdapter extends RecyclerView.Adapter<HomeTutorComprasAnterioresRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Pedido> mList;

    public HomeTutorComprasAnterioresRecyclerViewAdapter(Context mContext, ArrayList<Pedido> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view_compras_anteriores_home_tutor,parent,false);
        ViewHolder vHolder = new ViewHolder(view);

        return vHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Pedido pedido = mList.get(position);

        //Inserindo os dados no layout
        if (pedido.getStatus().equals("Confirmado")){

            holder.textViewTituloStatus.setText(R.string.variable_compra_anteriores_title);
            holder.textViewStatusText.setText(pedido.getStatus());
            holder.textViewStatusText.setTextColor(mContext.getResources().getColor(R.color.colorStatusConfirmadoText));
            holder.relativeLayoutStatusBackground.setBackground(mContext.getDrawable(R.drawable.status_confirmado));

            holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext.getApplicationContext()));
            holder.mRecyclerAdapter = new HomeTutorCardItemProdutosRecyclerViewAdapter(mContext, pedido.getProdutos());
            holder.mRecyclerView.setAdapter(holder.mRecyclerAdapter);
            holder.mRecyclerAdapter.notifyDataSetChanged();

            //Ação de clique nos itens da lista
            holder.buttonDetalhes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Envia dados do item clicado para a activity colaborador
                    Intent intent = new Intent(mContext, DetalhesPedidoActivity.class);
                    intent.putExtra("pedido",pedido);
                    intent.putExtra("action",mContext.getResources().getString(R.string.action_visualizar_pedido));

                    mContext.startActivity(intent);
                }
            });

        }else {}

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        //Variaveis para referênciar o layout
        private TextView textViewTituloStatus;
        private TextView textViewStatusText;
        private RelativeLayout relativeLayoutStatusBackground;
        private Button buttonDetalhes;

        private RecyclerView mRecyclerView;
        private HomeTutorCardItemProdutosRecyclerViewAdapter mRecyclerAdapter;


        public ViewHolder(@NonNull View itemView ) {
            super(itemView);

            //Referenciando o layout
            textViewTituloStatus = itemView.findViewById(R.id.text_view_titulo_status_compras_anteriores_item);
            textViewStatusText = itemView.findViewById(R.id.text_view_status_text_compras_anteriores_item);
            relativeLayoutStatusBackground = itemView.findViewById(R.id.relative_layout_status_background_compras_anteriores_item);
            buttonDetalhes = itemView.findViewById(R.id.button_detalhes_compras_anteriores_item);
            mRecyclerView = itemView.findViewById(R.id.recycler_view_item_compras_anteriores_home_tutor);
        }
    }

}
