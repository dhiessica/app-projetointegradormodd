package com.app.projetointegradormodd.Dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Model.Object.Atividade;
import com.app.projetointegradormodd.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AtividadeYesCancelDialogFragment extends DialogFragment implements View.OnClickListener {

    //Variáveis de layout
    private TextView textViewAlertMessage;
    private Button buttonConfirma;
    private Button buttonCancela;

    //Variáveis de dados
    private Atividade atividade;
    private ButtonListner buttonListner;

    public AtividadeYesCancelDialogFragment(Atividade atividade, ButtonListner buttonListner) {
        this.buttonListner = buttonListner;
        this.atividade = atividade;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        try{
            atividade = (Atividade) getArguments().getSerializable("atividade");

        }catch(Exception e){
            try {
                atividade = (Atividade) savedInstanceState.getSerializable("atividade");

            }catch (Exception c) {
                Log.e("catch", c.toString());
                Log.e("catch", e.toString());
            }
        }

        return inflater.inflate(R.layout.fragment_dialog_yes_cancel,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Instância as variáveis do layout
        textViewAlertMessage = view.findViewById(R.id.text_view_yes_cancel_alert);
        buttonConfirma = view.findViewById(R.id.button_confirmar_alert);
        buttonCancela = view.findViewById(R.id.button_cancel_alert);

        //Insere a mensagem no modal
        textViewAlertMessage.setText(getResources().getString(R.string.text_modal_atividade_alerta) +" "+ atividade.getTitulo() + "?");

        buttonCancela.setOnClickListener(this);
        buttonConfirma.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonConfirma){

            //Chama o método especifico para atualizar o animal
            buttonListner.onConfirm(atividade);
            dismiss();

        }
        else if(view == buttonCancela){
            dismiss();
        }
    }

    public interface ButtonListner{
        void onConfirm(Atividade atividade);
    }
}
