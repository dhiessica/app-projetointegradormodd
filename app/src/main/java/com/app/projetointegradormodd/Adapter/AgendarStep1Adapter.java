package com.app.projetointegradormodd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.projetointegradormodd.AgendarStep2Activity;
import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Model.Object.Servico;
import com.app.projetointegradormodd.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AgendarStep1Adapter extends RecyclerView.Adapter<AgendarStep1Adapter.AgendarStep1Holder>{

    //Variaveis de dados
    private ArrayList<Animal> animais;
    private int row_index;
    private Context context;
    private Servico servico;

    public AgendarStep1Adapter(Context context, ArrayList<Animal> animais) {
        this.animais = animais;
        this.context = context;
    }

    @NonNull
    @Override
    public AgendarStep1Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_recycler_view_agendar_step1,parent,false);

        AgendarStep1Holder animalViewHolder = new AgendarStep1Holder(view);

        return animalViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AgendarStep1Holder holder, final int position) {
        Animal animal = animais.get(position);


        //Verifica a espécie do animal para inserir o icon
        if(animal.getEspecie().equals("Cachorro")){
            holder.circleImageViewIconAnimal.setImageResource(R.drawable.icon_cao_borda_branca_background);
        }else if(animal.getEspecie().equals("Gato")){
            holder.circleImageViewIconAnimal.setImageResource(R.drawable.icon_gato_borda_branca_background);
        }

        holder.textViewNomeAnimal.setText(animal.getNome());

        //Seleciona apenas um checkbox
        holder.checkBoxSelecionaAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                holder.checkBoxSelecionaAnimal.setChecked(true);
                notifyDataSetChanged();
            }
        });

        //Valida se não há nenhum outro selecionado
        if (row_index == position) {
            holder.checkBoxSelecionaAnimal.setChecked(true);
        } else {
            holder.checkBoxSelecionaAnimal.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return animais.size();
    }

    public void sentDataNextStep(Servico servico){
        //Pega os dados dependendo da posição do item clicado
        Animal animal = animais.get(row_index);

        //Envia dados do item selecionado para a proxima tela
        Intent intent = new Intent(context, AgendarStep2Activity.class);
        intent.putExtra("animal",animal);
        intent.putExtra("servico",servico);

        context.startActivity(intent);
    }

    public class AgendarStep1Holder extends RecyclerView.ViewHolder {

        //Variaveis de layout
        private CircleImageView circleImageViewIconAnimal;
        private TextView textViewNomeAnimal;
        private CheckBox checkBoxSelecionaAnimal;

        public AgendarStep1Holder(@NonNull View itemView) {
            super(itemView);

            //Referênciando variáveis de layout
            circleImageViewIconAnimal = itemView.findViewById(R.id.circle_image_view_icon_pet_item);
            textViewNomeAnimal = itemView.findViewById(R.id.text_view_nome_pet_item);
            checkBoxSelecionaAnimal = itemView.findViewById(R.id.check_box_select_pet_item);
        }
    }
}
