package com.app.projetointegradormodd.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.projetointegradormodd.R;
import com.app.projetointegradormodd.Model.Layout.InicialCardModel;

import java.util.List;

public class InicialCardAdapter extends PagerAdapter{

    private List<InicialCardModel> telaInicialCardModels;
    private LayoutInflater layoutInflater;
    private Context context;

    public InicialCardAdapter(List<InicialCardModel> telaInicialCardModels, Context context) {
        this.telaInicialCardModels = telaInicialCardModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return telaInicialCardModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_view_pager_inicial,container,false);

        TextView info,info2;

        //Variaveis para referenciar o layout
        info = view.findViewById(R.id.text_view_titulo_item_card_inicial);
        info2 = view.findViewById(R.id.text_view_descricao_item_card_inicial);

        info.setText(telaInicialCardModels.get(position).getTitulo());
        info2.setText(telaInicialCardModels.get(position).getDescricao());

        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
