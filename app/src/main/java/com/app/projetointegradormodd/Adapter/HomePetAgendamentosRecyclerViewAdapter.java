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

import com.app.projetointegradormodd.DetalhesAgendamentoActivity;
import com.app.projetointegradormodd.Model.Object.Agendamento;
import com.app.projetointegradormodd.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomePetAgendamentosRecyclerViewAdapter extends RecyclerView.Adapter<HomePetAgendamentosRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Agendamento> mList;

    public HomePetAgendamentosRecyclerViewAdapter(Context mContext, ArrayList<Agendamento> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view_agendamentos_home_pet,parent,false);
        ViewHolder vHolder = new ViewHolder(view);

        return vHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Agendamento agendamento = mList.get(position);

        //Inserindo os dados no layout

        //Valida status
        if (agendamento.getStatus().equals(mContext.getResources().getString(R.string.hint_confirmado))){
            holder.relativeLayoutBackgroundStatus.setBackground(mContext.getDrawable(R.drawable.status_confirmado));
            holder.textViewStatusText.setTextColor(mContext.getResources().getColor(R.color.colorStatusConfirmadoText));
            holder.textViewStatusText.setText(agendamento.getStatus());
        }
        else if (agendamento.getStatus().equals(mContext.getResources().getString(R.string.hint_pendente))){
            holder.relativeLayoutBackgroundStatus.setBackground(mContext.getDrawable(R.drawable.status_pendente));
            holder.textViewStatusText.setTextColor(mContext.getResources().getColor(R.color.colorStatusPendenteText));
            holder.textViewStatusText.setText(agendamento.getStatus());
        }

        //Valida se o serviço de busca e entrega do pet foi contratado
        if (agendamento.getTipoTransporte().equals("false")){

            holder.textViewTituloTransporte.setText(mContext.getResources().getString(R.string.variable_transporte_nao_contratado));
            holder.textViewTextTransporte.setText(mContext.getResources().getString(R.string.variable_endereco_loja));

        }else if (!agendamento.getTipoTransporte().equals("false")){
            holder.textViewTituloTransporte.setText(mContext.getResources().getString(R.string.variable_transporte_contratado));
            holder.textViewTextTransporte.setText(agendamento.getTipoTransporte());
        }

        holder.textViewTitulo.setText(agendamento.getServico().getNome());
        holder.textViewData.setText(agendamento.getData());
        holder.textViewHorario.setText(agendamento.getHorario());

        //Ação de clique nos itens da lista
        holder.buttonDetalhes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Envia dados do item selecionado para a proxima tela
                Intent intent = new Intent(mContext, DetalhesAgendamentoActivity.class);

                intent.putExtra("agendamento",agendamento);
                intent.putExtra("action",mContext.getResources().getString(R.string.action_visualizar_agendamento));
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
        private TextView textViewTitulo;
        private TextView textViewStatusText;
        private TextView textViewTituloTransporte;
        private TextView textViewTextTransporte;
        private TextView textViewData;
        private TextView textViewHorario;
        private RelativeLayout relativeLayoutBackgroundStatus;
        private Button buttonDetalhes;



        public ViewHolder(@NonNull View itemView ) {
            super(itemView);

            //Referenciando o layout
            textViewTitulo = itemView.findViewById(R.id.text_view_titulo_agedamentos_item);
            textViewStatusText = itemView.findViewById(R.id.text_view_status_test_agendamentos_item);
            textViewTituloTransporte = itemView.findViewById(R.id.text_view_titulo_transporte_agendamentos_item);
            textViewTextTransporte = itemView.findViewById(R.id.text_view_text_transporte_agedamentos_item);
            textViewData = itemView.findViewById(R.id.text_view_data_agedamentos_item);
            textViewHorario = itemView.findViewById(R.id.text_view_horario_agedamentos_item);
            relativeLayoutBackgroundStatus = itemView.findViewById(R.id.reltive_layout_status_background_agendamentos_item);
            buttonDetalhes = itemView.findViewById(R.id.button_detalhes_agedamentos_item);
        }
    }

}
