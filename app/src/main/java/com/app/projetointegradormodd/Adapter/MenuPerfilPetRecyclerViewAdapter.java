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
import com.app.projetointegradormodd.FormPerfilPetActivity;
import com.app.projetointegradormodd.HomePetActivity;
import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MenuPerfilPetRecyclerViewAdapter extends RecyclerView.Adapter<MenuPerfilPetRecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Animal> mList;
    private Activity activity;

    private PetRecyclerViewListner listner;

    public MenuPerfilPetRecyclerViewAdapter(Context mContext, ArrayList<Animal> mList, Activity activity, PetRecyclerViewListner listner) {
        this.mContext = mContext;
        this.mList = mList;
        this.activity = activity;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view_selecionar_pet,parent,false);
        ViewHolder vHolder = new ViewHolder(view);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Animal animal = mList.get(position);

        Log.e("teste",animal.getNome());

        //Insere o nome do animal
        holder.textViewNomeAnimal.setText(animal.getNome());

        //Verifica a espécie do animal para inserir o icon
        if(animal.getEspecie().equals("Cachorro")){
            holder.circleImageViewIconAnimal.setImageResource(R.drawable.icon_cao_borda_branca_background);
        }else if(animal.getEspecie().equals("Gato")){
            holder.circleImageViewIconAnimal.setImageResource(R.drawable.icon_gato_borda_branca_background);
        }

        //Ação de clique nos itens da lista
        holder.circleImageViewIconAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Retorna os dados do item clicado para a home pet
                returnActivityResult(animal);
            }
        });

        holder.textViewVisualizarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Retorna os dados do item clicado para a home pet
                returnActivityResult(animal);
            }
        });

        holder.textViewNomeAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Retorna os dados do item clicado para a home pet
                returnActivityResult(animal);
            }
        });

        holder.imageButtonConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            //Abre o menu
            showPopup(holder.imageButtonConfiguracoes,animal);
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

    @SuppressLint("RestrictedApi")
    public void showPopup(View v, final Animal animal){

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
                        Intent intent = new Intent(mContext, FormPerfilPetActivity.class);
                        intent.putExtra("action",mContext.getResources().getString(R.string.action_editar_perfil_pet));
                        intent.putExtra("animal",animal);
                        activity.startActivityForResult(intent,7);
                        return true;

                    case R.id.item_excluir:

                        //Instancia a classe do modal e configura a espera dos cliques do botao no modal
                        AnimalYesCancelDialogFragment dialogFragment = new AnimalYesCancelDialogFragment(animal, new AnimalYesCancelDialogFragment.ButtonListner() {
                            @Override
                            public void onConfirm(Animal animal) {
                                listner.onExcludeClick(animal);
                            }
                        });

                        //Configura dados para passar para o modal
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("animal",animal);
                        dialogFragment.setArguments(bundle);

                        //Abre o modal
                        dialogFragment.show(((AppCompatActivity) mContext).getSupportFragmentManager(),
                                "MenuPerfilPetRecyclerViewAdapter");

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
        private CircleImageView circleImageViewIconAnimal;
        private TextView textViewNomeAnimal;
        private TextView textViewVisualizarPerfil;
        private ImageButton imageButtonConfiguracoes;

        public ViewHolder(@NonNull View itemView ) {
            super(itemView);

            //Referenciando o layout
            circleImageViewIconAnimal = itemView.findViewById(R.id.circle_image_view_icon_pet_item_selecionar_pet);
            textViewNomeAnimal = itemView.findViewById(R.id.text_view_nome_pet_item_selecionar_pet);
            textViewVisualizarPerfil = itemView.findViewById(R.id.text_view_visualizar_item_selecionar_pet);
            imageButtonConfiguracoes = itemView.findViewById(R.id.image_button_engrenagem_item_selecionar_pet);
        }
    }


    public interface PetRecyclerViewListner{

        void onExcludeClick(Animal animal);
    }
}
