package es.source.code.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import es.source.code.activity.R;

public class MainScreenAdapter extends BaseAdapter{
    private Context context;
    private List listitem;

    public MainScreenAdapter(Context context,List  listitem) {
        this.context = context;
        this.listitem = listitem;
    }

    @Override
    public int getCount() {
        return listitem.size();
    }

    @Override
    public Object getItem(int position) {
        return listitem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup viewGroup) {
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_mainscreen_item,null);
        }
        ImageView imageView = view.findViewById(R.id.img);
        TextView textView = view.findViewById(R.id.text);
        Map map = (Map) listitem.get(position);
        imageView.setImageResource((Integer)map.get("img"));
        textView.setText(map.get("text")+"");
        return view;
    }
}
