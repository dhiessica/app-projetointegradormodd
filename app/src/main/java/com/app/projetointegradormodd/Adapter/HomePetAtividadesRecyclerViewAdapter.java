package com.app.projetointegradormodd.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.app.projetointegradormodd.Dialog.AnimalYesCancelDialogFragment;
import com.app.projetointegradormodd.Dialog.AtividadeYesCancelDialogFragment;
import com.app.projetointegradormodd.FormAtividadeActivity;
import com.app.projetointegradormodd.HomePetActivity;
import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Model.Object.Atividade;
import com.app.projetointegradormodd.R;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

public class HomePetAtividadesRecyclerViewAdapter extends RecyclerView.Adapter<HomePetAtividadesRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Atividade> mList;
    private Activity activity;
    private AtividadeRecyclerViewListner listner;

    public HomePetAtividadesRecyclerViewAdapter(Context mContext, ArrayList<Atividade> mList,Activity activity, AtividadeRecyclerViewListner listner) {
        this.mContext = mContext;
        this.mList = mList;
        this.activity = activity;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view_atividades_home_pet,parent,false);
        ViewHolder vHolder = new ViewHolder(view);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Atividade atividade = mList.get(position);

        //Inserindo os dados no layout
        holder.textViewTitulo.setText(atividade.getTitulo());
        holder.textViewCategoria.setText(atividade.getCategoria());
        holder.textViewHorario.setText(atividade.getHorario());
        holder.textViewDataInicial.setText(atividade.getDataInicial());
        holder.textViewDataFinal.setText(atividade.getDataFinal());
        holder.textViewObservacoes.setText(atividade.getObservacoes());

        //Ação de clique nos itens da lista
        holder.buttonConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Abre o menu
                showPopup(holder.buttonConfiguracoes,atividade);
            }
        });

    }

    @SuppressLint("RestrictedApi")
    public void showPopup(View v, final Atividade atividade){

        //Configura o menu forçando os icones
        @SuppressLint("RestrictedApi") MenuBuilder menuBuilder =new MenuBuilder(mContext);
        MenuInflater inflater = new MenuInflater(mContext);

        inflater.inflate(R.menu.popup_config_menu, menuBuilder);
        @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(mContext, menuBuilder, v);
        optionsMenu.setForceShowIcon(true);

        //Configura os listener no clique dos itens
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.item_editar:

                        listner.onUpdateClick(atividade);

                        return true;

                    case R.id.item_excluir:

                        //Instancia a classe do modal e configura a espera dos cliques do botao no modal
                        AtividadeYesCancelDialogFragment dialogFragment = new AtividadeYesCancelDialogFragment(atividade, new AtividadeYesCancelDialogFragment.ButtonListner() {
                            @Override
                            public void onConfirm(Atividade atividade) {
                                listner.onExcludeClick(atividade);
                            }
                        });

                        //Configura dados para passar para o modal
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("atividade",atividade);
                        dialogFragment.setArguments(bundle);

                        //Abre o modal
                        dialogFragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(),
                                "HomePetAtividadesRecyclerViewAdapter");

                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {}
        });

        //Mostra o menu
        optionsMenu.show();

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        //Variaveis para referênciar o layout
        private TextView textViewTitulo;
        private TextView textViewCategoria;
        private TextView textViewHorario;
        private TextView textViewDataInicial;
        private TextView textViewDataFinal;
        private TextView textViewObservacoes;
        private ImageButton buttonConfiguracoes;

        public ViewHolder(@NonNull View itemView ) {
            super(itemView);

            //Referenciando o layout
            textViewTitulo = itemView.findViewById(R.id.text_view_titulo_atividades_item);
            textViewCategoria = itemView.findViewById(R.id.text_view_categoria_atividades_item);
            textViewHorario = itemView.findViewById(R.id.text_view_horario_atividades_item);
            textViewDataInicial = itemView.findViewById(R.id.text_view_data_inicial_atividades_item);
            textViewDataFinal = itemView.findViewById(R.id.text_view_data_final_atividades_item);
            textViewObservacoes = itemView.findViewById(R.id.text_view_observacoes_atividades_item);
            buttonConfiguracoes = itemView.findViewById(R.id.image_button_engrenagem_atividades_item);
        }
    }

    public interface AtividadeRecyclerViewListner{

        void onExcludeClick(Atividade atividade);
        void onUpdateClick(Atividade atividade);
    }

}
