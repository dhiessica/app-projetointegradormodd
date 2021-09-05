package com.app.projetointegradormodd.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.projetointegradormodd.DetalhesPedidoActivity;
import com.app.projetointegradormodd.Model.Object.Endereco;
import com.app.projetointegradormodd.Model.Object.Pedido;
import com.app.projetointegradormodd.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeTutorComprasEmAndamentoRecyclerViewAdapter extends RecyclerView.Adapter<HomeTutorComprasEmAndamentoRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Pedido> mList;


    public HomeTutorComprasEmAndamentoRecyclerViewAdapter(Context mContext, ArrayList<Pedido> mList) {
        this.mContext = mContext;
        this.mList = mList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view_compras_em_andamento_home_tutor,parent,false);
        ViewHolder vHolder = new ViewHolder(view);

        return vHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Pedido pedido = mList.get(position);

        //Inserindo os dados no layout
        if (pedido.getStatus().equals("Pendente")){

            Endereco endereco = pedido.getEnderecoEntrega();

            holder.textViewPrevisaoEntrega.setText(pedido.getHora()+" - "+pedido.getPrevisaoEntrega());
            holder.textViewStatusText.setText(pedido.getStatus());
            holder.textViewStatusText.setTextColor(mContext.getResources().getColor(R.color.colorStatusPendenteText));
            holder.relativeLayoutStatusBackground.setBackground(mContext.getDrawable(R.drawable.status_pendente));

            holder.textViewEnderecoEntrega.setText(endereco.getRua() + ", "+endereco.getNumero()+" - "
                    + endereco.getCep()+" - "+endereco.getCidade() + " "+endereco.getUf());

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
        private TextView textViewPrevisaoEntrega;
        private TextView textViewStatusText;
        private TextView textViewEnderecoEntrega;
        private RelativeLayout relativeLayoutStatusBackground;
        private Button buttonDetalhes;

        private RecyclerView mRecyclerView;
        private HomeTutorCardItemProdutosRecyclerViewAdapter mRecyclerAdapter;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Referenciando o layout
            textViewPrevisaoEntrega = itemView.findViewById(R.id.text_view_previsao_entrega_compras_em_andamento_item);
            textViewStatusText = itemView.findViewById(R.id.text_view_status_text_compras_em_andamento_item);
            textViewEnderecoEntrega = itemView.findViewById(R.id.text_view_endereco_compras_em_andamento_item);
            relativeLayoutStatusBackground = itemView.findViewById(R.id.relative_layout_status_background_compras_em_andamento_item);
            buttonDetalhes = itemView.findViewById(R.id.button_detalhes_compras_em_andamento_item);
            mRecyclerView = itemView.findViewById(R.id.recycler_view_item_compras_em_andamento_item);
        }
    }

}
