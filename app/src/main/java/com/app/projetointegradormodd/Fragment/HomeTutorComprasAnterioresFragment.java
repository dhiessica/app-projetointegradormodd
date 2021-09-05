package com.app.projetointegradormodd.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.projetointegradormodd.Adapter.HomeTutorComprasAnterioresRecyclerViewAdapter;
import com.app.projetointegradormodd.Model.Object.Pedido;
import com.app.projetointegradormodd.R;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeTutorComprasAnterioresFragment extends Fragment {

    View view ;

    //Variaveis para referênciar o layout
    private RecyclerView mRecyclerView = null;
    private HomeTutorComprasAnterioresRecyclerViewAdapter mRecyclerAdapter = null;
    private LinearLayout noData;
    private TextView noDataText;

    //Variavel de dados da lista de noticias de leitura
    private static final String ARG_PEDIDOS = "pedidos";
    private ArrayList<Pedido> pedidos = new ArrayList<>();

    public HomeTutorComprasAnterioresFragment() {
        // Required empty public constructor
    }

    public static HomeTutorComprasAnterioresFragment newInstance( ArrayList<Pedido> pedidos) {
        HomeTutorComprasAnterioresFragment fragment = new HomeTutorComprasAnterioresFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PEDIDOS, pedidos);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Chama método para listar todos os produtos
        //setDataComprasAnterioresList();

        if (getArguments() != null) {
            pedidos = (ArrayList<Pedido>) getArguments().get(ARG_PEDIDOS);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_tutor_compras_anteriores, container, false);

        //Inicializa o recycler view
        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView() {

        noData = view.findViewById(R.id.container_no_data);
        noDataText = view.findViewById(R.id.text_no_data);

        if (pedidos.size() == 0 || pedidos == null){
            noData.setVisibility(View.VISIBLE);
            noDataText.setText("Ops! Você ainda não possue registros de compras anteriores.");
        }else{
            noData.setVisibility(View.INVISIBLE);
        }

        mRecyclerView = view.findViewById(R.id.recycler_view_compas_anteriores_home_tutor);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerAdapter = new HomeTutorComprasAnterioresRecyclerViewAdapter(getContext(), pedidos);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.notifyDataSetChanged();
    }

}
