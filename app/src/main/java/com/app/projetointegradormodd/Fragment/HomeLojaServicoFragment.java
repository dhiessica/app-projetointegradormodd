package com.app.projetointegradormodd.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.projetointegradormodd.Adapter.HomeLojaServicoRecyclerViewAdapter;
import com.app.projetointegradormodd.Interfaces.IFragmentListener;
import com.app.projetointegradormodd.Interfaces.ISearch;
import com.app.projetointegradormodd.Model.Object.Servico;
import com.app.projetointegradormodd.R;

import java.util.ArrayList;

public class HomeLojaServicoFragment extends Fragment implements ISearch {

    View view ;

    //Variaveis para referênciar o layout
    private RecyclerView mRecyclerView = null;
    private HomeLojaServicoRecyclerViewAdapter mRecyclerAdapter = null;

    //Variavel de dados da lista de noticias de leitura
    private static final String ARG_SEARCHTERM = "search_term";
    private static final String ARG_SERVICOS = "servicos";
    private String mSearchTerm = null;
    private IFragmentListener mIFragmentListener = null;
    private ArrayList<Servico> servicos = new ArrayList<Servico>();

    public HomeLojaServicoFragment() {
        // Required empty public constructor
    }

    public static HomeLojaServicoFragment newInstance(String searchTerm, ArrayList<Servico> servicos) {
        HomeLojaServicoFragment fragment = new HomeLojaServicoFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ARG_SEARCHTERM, searchTerm);
        bundle.putSerializable(ARG_SERVICOS, servicos);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Chama método para listar todos os servicos
        //setDataServicoList();

        if (getArguments() != null) {
            mSearchTerm = (String) getArguments().get(ARG_SEARCHTERM);
            servicos = (ArrayList<Servico>) getArguments().get(ARG_SERVICOS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_loja_servico, container, false);

        //Inicializa o recycler view
        setUpRecyclerView();

        return view;
    }

    public void setUpRecyclerView(){
        mRecyclerView = view.findViewById(R.id.recycler_view_servico_home_loja);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerAdapter = new HomeLojaServicoRecyclerViewAdapter(getContext(), servicos);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mSearchTerm) {
            onTextQuery(mSearchTerm);
        }
    }

    @Override
    public void onTextQuery(String s) {
        mRecyclerAdapter.getFilter().filter(s);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mIFragmentListener = (IFragmentListener) context;
        mIFragmentListener.addiSearch(HomeLojaServicoFragment.this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (null != mIFragmentListener)
            mIFragmentListener.removeISearch(HomeLojaServicoFragment.this);
    }


}
