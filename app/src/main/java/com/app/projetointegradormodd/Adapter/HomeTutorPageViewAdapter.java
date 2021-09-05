package com.app.projetointegradormodd.Adapter;

import com.app.projetointegradormodd.Fragment.HomeTutorComprasAnterioresFragment;
import com.app.projetointegradormodd.Fragment.HomeTutorComprasEmAndamentoFragment;
import com.app.projetointegradormodd.Model.Object.Pedido;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class HomeTutorPageViewAdapter extends FragmentStatePagerAdapter {

    int tabCount;
    private ArrayList<Pedido> pedidosAnteriores;
    private ArrayList<Pedido> pedidosAndamento;

    public HomeTutorPageViewAdapter(@NonNull FragmentManager fm, int tabCount, ArrayList<Pedido> pedidosAnteriores, ArrayList<Pedido> pedidosAndamento) {
        super(fm);
        this.tabCount = tabCount;
        this.pedidosAnteriores = pedidosAnteriores;
        this.pedidosAndamento = pedidosAndamento;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeTutorComprasAnterioresFragment fragmentTab1 = HomeTutorComprasAnterioresFragment.newInstance(pedidosAnteriores);
                return fragmentTab1;
            case 1:
                HomeTutorComprasEmAndamentoFragment fragmentTab2 = HomeTutorComprasEmAndamentoFragment.newInstance(pedidosAndamento);
                return fragmentTab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
