package es.source.code.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;



import java.util.ArrayList;


import es.source.code.activity.FoodDetailed;
import es.source.code.activity.R;
import es.source.code.adapter.FoodAdapter;
import es.source.code.model.Food;


public abstract class BaseFragment extends Fragment {
    public static String TABLAYOUT_FRAGMENT = "tab_fragment";
    public ArrayList<Food> foodlist = new ArrayList<Food>();
    private int type;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = (int) getArguments().getSerializable(TABLAYOUT_FRAGMENT);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tablayout, container, false);
        FoodAdapter adapter = new FoodAdapter(getActivity(), R.layout.food_item_list, foodlist);
        ListView listView =  view.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        initFoods();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), FoodDetailed.class);
                intent.putParcelableArrayListExtra("foods",foodlist);
                intent.putExtra("position",i);
                intent.putExtra("from", "ListViewItem");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
            }
        });
        return view;
    }

    public abstract void initFoods();


    public void updataView(){

        onResume();
    }
}