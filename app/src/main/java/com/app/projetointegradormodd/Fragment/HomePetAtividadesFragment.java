package com.app.projetointegradormodd.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projetointegradormodd.Adapter.HomePetAtividadesRecyclerViewAdapter;
import com.app.projetointegradormodd.Model.Object.Animal;
import com.app.projetointegradormodd.Model.Object.Atividade;
import com.app.projetointegradormodd.R;
import com.app.projetointegradormodd.Services.API.AtividadeApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HomePetAtividadesFragment extends Fragment {

    View view ;

    //Variaveis para referênciar o layout
    private Button buttonCriarAtividade;
    private RecyclerView mRecyclerView = null;
    private HomePetAtividadesRecyclerViewAdapter mRecyclerAdapter = null;
    private LinearLayout noData;
    private TextView noDataText;

    //Variavel de dados da lista de noticias de leitura
    private static final String ARG_ATIVIDADES= "atividades";
    private static final String ARG_CURRENT_ANIMAL= "animal";
    private static final String ARG_LISTNER= "listner";
    private ArrayList<Atividade> atividades = new ArrayList<>();
    private Animal currentAnimal;
    private AtividadeFragmentListner listner;

    public HomePetAtividadesFragment() {
        // Required empty public constructor
    }

    public HomePetAtividadesFragment(ArrayList<Atividade> atividades, Animal currentAnimal, AtividadeFragmentListner listner) {
        this.atividades = atividades;
        this.currentAnimal = currentAnimal;
        this.listner = listner;
    }

    public static HomePetAtividadesFragment newInstance(ArrayList<Atividade> atividades, Animal animal, AtividadeFragmentListner listner) {
        HomePetAtividadesFragment fragment = new HomePetAtividadesFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_ATIVIDADES, atividades);
        bundle.putSerializable(ARG_CURRENT_ANIMAL, animal);
        bundle.putSerializable(ARG_LISTNER, (Serializable) listner);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Chama método para listar todos

        if (getArguments() != null) {
            atividades = (ArrayList<Atividade>) getArguments().get(ARG_ATIVIDADES);
            currentAnimal = (Animal) getArguments().get(ARG_CURRENT_ANIMAL);
            listner = (AtividadeFragmentListner) getArguments().get(ARG_LISTNER);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_pet_atividades, container, false);

        //Inicializa o recycler view
        setUpLayout();

        return view;
    }

    private void setUpLayout() {
        noData = view.findViewById(R.id.container_no_data);
        noDataText = view.findViewById(R.id.text_no_data);
        if (atividades.size() == 0 || atividades ==null){
            noData.setVisibility(View.VISIBLE);
            noDataText.setText("Ops! Esse pet ainda não possue registro de atividades.");
        }else{
            noData.setVisibility(View.INVISIBLE);
        }
        buttonCriarAtividade = view.findViewById(R.id.button_criar_atividade_home_pet);
        mRecyclerView = view.findViewById(R.id.recycler_view_atividades_home_pet);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerAdapter = new HomePetAtividadesRecyclerViewAdapter(getContext(), atividades, this.getActivity(), new HomePetAtividadesRecyclerViewAdapter.AtividadeRecyclerViewListner() {
            @Override
            public void onExcludeClick(Atividade atividade) {
                deteleAtividade(atividade);
            }

            @Override
            public void onUpdateClick(Atividade atividade) {

                listner.onUpdateClick(atividade);

            }
        });

        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.notifyDataSetChanged();

        buttonCriarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listner.onCreateClick();

            }
        });
    }

    public void deteleAtividade(final Atividade atividade){

        AtividadeApi atividadeApi = new AtividadeApi(new AtividadeApi.ConexaoListner() {
            @Override
            public void onAsyncId(String response) {

            }

            @Override
            public void onAsyncData(Date response) {
                //Verifica se  retorno da criação do usuário é uma data
                try{
                    Date valido = response;

                    if(valido != null){
                        //Informa que o pet foi excluido
                        Toast.makeText(getContext(),
                                getResources().getString(R.string.toast_exclui_atividade_ok),
                                Toast.LENGTH_SHORT).show();

                        atividades.remove(atividade);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        setUpLayout();
                    }

                }catch (Exception e){
                    Log.e("ERRO", "AtividadeYesCancelDialogFragment:failure", e);
                    Toast.makeText(getContext(),
                            getResources().getString(R.string.toast_exclui_atividade_notok),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAsyncObject(Atividade response) {

            }

            @Override
            public void onAsyncObjectList(ArrayList<Atividade> response) {

            }
        });

        atividadeApi.delete(atividade.getId());
    }

    public interface AtividadeFragmentListner{

        void onCreateClick();
        void onUpdateClick(Atividade atividade);
    }

}
