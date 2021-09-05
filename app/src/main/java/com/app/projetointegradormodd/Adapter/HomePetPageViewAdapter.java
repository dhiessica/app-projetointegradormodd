package com.app.projetointegradormodd.Adapter;

import com.app.projetointegradormodd.Fragment.HomePetAgendamentosFragment;
import com.app.projetointegradormodd.Fragment.HomePetAtividadesFragment;
import com.app.projetointegradormodd.Fragment.HomeTutorComprasAnterioresFragment;
import com.app.projetointegradormodd.Fragment.HomeTutorComprasEmAndamentoFragment;
import com.app.projetointegradormodd.Model.Object.Agendamento;
import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Model.Object.Atividade;
import com.app.projetointegradormodd.Model.Object.Pedido;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class HomePetPageViewAdapter extends FragmentStatePagerAdapter {

    int tabCount;
    private ArrayList<Atividade> atividades;
    private ArrayList<Agendamento> agendamentos;
    private Animal currentAnimal;
    private PageviewListner listner;

    public HomePetPageViewAdapter(@NonNull FragmentManager fm, int tabCount, ArrayList<Atividade> atividades, ArrayList<Agendamento> agendamentos, Animal currentAnimal, PageviewListner listner) {
        super(fm);
        this.tabCount = tabCount;
        this.atividades = atividades;
        this.agendamentos = agendamentos;
        this.currentAnimal = currentAnimal;
        this.listner = listner;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomePetAtividadesFragment fragmentTab1 = new HomePetAtividadesFragment(atividades, currentAnimal, new HomePetAtividadesFragment.AtividadeFragmentListner() {
                    @Override
                    public void onCreateClick() {
                        listner.onCreateAtividadeClick();
                    }

                    @Override
                    public void onUpdateClick(Atividade atividade) {
                        listner.onUpdateAtividadeClick(atividade);
                    }
                });
                return fragmentTab1;
            case 1:
                HomePetAgendamentosFragment fragmentTab2 = HomePetAgendamentosFragment.newInstance(agendamentos);
                return fragmentTab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public interface PageviewListner{

        void onCreateAtividadeClick();
        void onUpdateAtividadeClick(Atividade atividade);
    }
}
