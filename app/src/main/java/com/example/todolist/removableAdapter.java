package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class removableAdapter extends ArrayAdapter<Item> {
    private final List<Item> items;
    private final LayoutInflater inflater;

    public removableAdapter(Context context, List<Item> items) {
        super(context, R.layout.checkboxadapter, items);
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.removableadapter, parent, false);
            holder = new ViewHolder();
            holder.cross = (ImageButton) convertView.findViewById(R.id.imageButton);
            holder.textView = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Item item = items.get(position);
        holder.textView.setText(item.getText());
        MainActivity mainActivity = (MainActivity) getContext();
        holder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.removeOnPosition(position);

            }
        });

        return convertView;
    }


    private static class ViewHolder {
        ImageButton cross;
        TextView textView;
    }
}