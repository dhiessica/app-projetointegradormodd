package com.app.projetointegradormodd.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.projetointegradormodd.Adapter.HomeLojaServicoRecyclerViewAdapter;
import com.app.projetointegradormodd.Adapter.HomeTutorComprasEmAndamentoRecyclerViewAdapter;
import com.app.projetointegradormodd.Interfaces.IFragmentListener;
import com.app.projetointegradormodd.Interfaces.ISearch;
import com.app.projetointegradormodd.Model.Object.Pedido;
import com.app.projetointegradormodd.Model.Object.Servico;
import com.app.projetointegradormodd.R;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeTutorComprasEmAndamentoFragment extends Fragment {

    View view ;

    //Variaveis para referênciar o layout
    private RecyclerView mRecyclerView;
    private HomeTutorComprasEmAndamentoRecyclerViewAdapter mRecyclerAdapter;
    private LinearLayout noData;
    private TextView noDataText;

    //Variavel de dados da lista de noticias de leitura
    private static final String ARG_PEDIDOS = "pedidos";
    private ArrayList<Pedido> pedidos = new ArrayList<>();

    public HomeTutorComprasEmAndamentoFragment() {
        // Required empty public constructor
    }

    public static HomeTutorComprasEmAndamentoFragment newInstance(ArrayList<Pedido> pedidos) {
        HomeTutorComprasEmAndamentoFragment fragment = new HomeTutorComprasEmAndamentoFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PEDIDOS, pedidos);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Chama método para listar todos os servicos
        //setDataServicoList();

        if (getArguments() != null) {
            pedidos = (ArrayList<Pedido>) getArguments().get(ARG_PEDIDOS);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_tutor_compras_em_andamento, container, false);

        //Inicializa o recycler view
        setUpRecyclerView();

        return view;
    }

    public void setUpRecyclerView(){

        noData = view.findViewById(R.id.container_no_data);
        noDataText = view.findViewById(R.id.text_no_data);

        if (pedidos.size() == 0 || pedidos == null){
            noData.setVisibility(View.VISIBLE);
            noDataText.setText("Ops! No momento não há nenhuma compra em andamento.");
        }else{
            noData.setVisibility(View.INVISIBLE);
        }

        mRecyclerView = view.findViewById(R.id.recycler_view_compras_em_andamento_home_tutor);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerAdapter = new HomeTutorComprasEmAndamentoRecyclerViewAdapter(getContext(), pedidos);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.notifyDataSetChanged();
    }

}
