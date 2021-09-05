package com.app.projetointegradormodd.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.projetointegradormodd.Adapter.HomeLojaProdutoRecyclerViewAdapter;
import com.app.projetointegradormodd.Adapter.HomeLojaServicoRecyclerViewAdapter;
import com.app.projetointegradormodd.Interfaces.IFragmentListener;
import com.app.projetointegradormodd.Interfaces.ISearch;
import com.app.projetointegradormodd.Model.Object.Produto;
import com.app.projetointegradormodd.Model.Object.Servico;
import com.app.projetointegradormodd.R;

import java.io.Serializable;
import java.util.ArrayList;


public class HomeLojaProdutosFragment extends Fragment implements ISearch {

    View view ;

    //Variaveis para referênciar o layout
    private RecyclerView mRecyclerView = null;
    private HomeLojaProdutoRecyclerViewAdapter mRecyclerAdapter = null;

    //Variavel de dados da lista de noticias de leitura
    private static final String ARG_SEARCHTERM = "search_term";
    private static final String ARG_PRODUTOS = "produtos";
    private static final String ARG_LISTNER= "listner";
    private String mSearchTerm = null;
    private IFragmentListener mIFragmentListener = null;
    private ArrayList<Produto> produtos = new ArrayList<>();
    private HomeLojaProdutosFragmentListner listner;

    public HomeLojaProdutosFragment() {
        // Required empty public constructor
    }

    public HomeLojaProdutosFragment(String searchTerm, ArrayList<Produto> produtos, HomeLojaProdutosFragmentListner listner) {
        this.mSearchTerm = searchTerm;
        this.produtos = produtos;
        this.listner = listner;
    }

    public static HomeLojaProdutosFragment newInstance(String searchTerm, ArrayList<Produto> produtos, HomeLojaProdutosFragmentListner listner) {
        HomeLojaProdutosFragment fragment = new HomeLojaProdutosFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ARG_SEARCHTERM, searchTerm);
        bundle.putSerializable(ARG_PRODUTOS, produtos);
        bundle.putSerializable(ARG_LISTNER, (Serializable) listner);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Chama método para listar todos os produtos

        if (getArguments() != null) {
            mSearchTerm = (String) getArguments().get(ARG_SEARCHTERM);
            produtos = (ArrayList<Produto>) getArguments().get(ARG_PRODUTOS);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_loja_produtos, container, false);

        //Inicializa o recycler view
        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView() {

        mRecyclerView = view.findViewById(R.id.recycler_view_produto_home_loja);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerAdapter = new HomeLojaProdutoRecyclerViewAdapter(getContext(), produtos, new HomeLojaProdutoRecyclerViewAdapter.HomeLojaProdutoRecyclerViewListner() {
            @Override
            public void onProdutoClick(Produto produto) {
                listner.onFragmentReceiveClick(produto);

            }
        });
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
        mIFragmentListener.addiSearch(HomeLojaProdutosFragment.this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (null != mIFragmentListener)
            mIFragmentListener.removeISearch(HomeLojaProdutosFragment.this);
    }

    public interface HomeLojaProdutosFragmentListner{

        void onFragmentReceiveClick(Produto produto);
    }
}
