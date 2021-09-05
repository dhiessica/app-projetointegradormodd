package com.app.projetointegradormodd.Adapter;

import android.util.Log;

import com.app.projetointegradormodd.Fragment.HomeLojaProdutosFragment;
import com.app.projetointegradormodd.Fragment.HomeLojaServicoFragment;
import com.app.projetointegradormodd.Model.Object.Produto;
import com.app.projetointegradormodd.Model.Object.Servico;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class HomeLojaPageViewAdapter extends FragmentStatePagerAdapter {

    int tabCount;
    private String mSearchTerm;
    private ArrayList<Produto> produtos;
    private ArrayList<Servico> servicos;
    private LojaPageviewListner listner;

    public HomeLojaPageViewAdapter(@NonNull FragmentManager fm, int tabCount, String mSearchTerm, ArrayList<Produto> produtos, ArrayList<Servico> servicos, LojaPageviewListner listner) {
        super(fm);
        this.tabCount = tabCount;
        this.mSearchTerm = mSearchTerm;
        this.produtos = produtos;
        this.servicos = servicos;
        this.listner = listner;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeLojaProdutosFragment fragmentTab1 = new HomeLojaProdutosFragment(mSearchTerm, produtos, new HomeLojaProdutosFragment.HomeLojaProdutosFragmentListner() {
                    @Override
                    public void onFragmentReceiveClick(Produto produto) {
                        listner.onReceiveClick(produto);
                    }
                });
                return fragmentTab1;
            case 1:
                HomeLojaServicoFragment fragmentTab2 = HomeLojaServicoFragment.newInstance(mSearchTerm,servicos);
                return fragmentTab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public void setTextQueryChanged(String newText) {
        mSearchTerm = newText;
    }

    public interface LojaPageviewListner{

        void onReceiveClick(Produto produto);
    }
}
