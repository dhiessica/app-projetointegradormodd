package com.app.projetointegradormodd.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.projetointegradormodd.Adapter.HomePetAgendamentosRecyclerViewAdapter;
import com.app.projetointegradormodd.Model.Object.Agendamento;
import com.app.projetointegradormodd.R;
import java.util.ArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomePetAgendamentosFragment extends Fragment {

    View view ;

    //Variaveis para referênciar o layout
    private RecyclerView mRecyclerView = null;
    private HomePetAgendamentosRecyclerViewAdapter mRecyclerAdapter = null;
    private LinearLayout noData;
    private TextView noDataText;

    //Variavel de dados da lista de noticias de leitura
    private static final String ARG_AGENDAMENTOS = "agendamentos";
    private ArrayList<Agendamento> agendamentos = new ArrayList<>();

    public HomePetAgendamentosFragment() {
        // Required empty public constructor
    }

    public static HomePetAgendamentosFragment newInstance(ArrayList<Agendamento> agendamentos) {
        HomePetAgendamentosFragment fragment = new HomePetAgendamentosFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_AGENDAMENTOS, agendamentos);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Chama método para listar todos os produtos

        if (getArguments() != null) {
            agendamentos = (ArrayList<Agendamento>) getArguments().get(ARG_AGENDAMENTOS);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_pet_agendamentos, container, false);

        //Inicializa o recycler view
        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView() {
        noData = view.findViewById(R.id.container_no_data);
        noDataText = view.findViewById(R.id.text_no_data);
        if (agendamentos.size() == 0){
            noData.setVisibility(View.VISIBLE);
            noDataText.setText("Ops! Esse pet ainda não possue registro de agendamentos.");
        }else{
            noData.setVisibility(View.INVISIBLE);
        }

        mRecyclerView = view.findViewById(R.id.recycler_view_agendamentos_home_pet);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerAdapter = new HomePetAgendamentosRecyclerViewAdapter(getContext(), agendamentos);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.notifyDataSetChanged();
    }

}
