package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class checkboxAdapter extends ArrayAdapter<Item> {
    private final List<Item> items;
    private final LayoutInflater inflater;

    public checkboxAdapter(Context context, List<Item> items) {
        super(context, R.layout.checkboxadapter, items);
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }


    private void setItemConfiguration(int position, ViewHolder holder){

        Item item = items.get(position);
        String texto = item.getText();
        String date = item.getDate();
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
        holder.checkBox.setChecked(item.isChecked());
        setDateString(date, holder);
        holder.textView.setText(texto);
        if (item.getShrink()){
            holder.fechaView.setTextSize(0.0f);
        }else{
            holder.fechaView.setTextSize(14.0f);
        }
    }
    @SuppressLint({"DefaultLocale"})
    private void setDateString(String date, ViewHolder holder) {
        if (date.equals("")) {
            setTextAndBg("",0x1900FFFF,holder);
            holder.fechaView.setHeight(0);
        } else {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            try {
                Date inicio = calendar1.getTime();
                calendar1.setTime(inicio);
                Date fin = sdf.parse(date);
                calendar2.setTime(fin);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            int yearDif = calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR);
            int dayDif = calendar2.get(Calendar.DAY_OF_YEAR) - calendar1.get(Calendar.DAY_OF_YEAR);
            int diffDays = (yearDif * 365) + dayDif;
            if (diffDays == 0) {
                setTextAndBg(String.format("%s \tEn fecha limite", date),0x19FFFF00,holder);
            } else if (diffDays == 1) {
                setTextAndBg(String.format("%s \tFalta %d dia", date, diffDays),0x1900FFFF,holder);
            } else if (diffDays > 1) {
                setTextAndBg(String.format("%s \tFaltan %d dias", date, diffDays),0x1900FFFF,holder);
            } else if (diffDays == -1) {
                diffDays = -diffDays;
                setTextAndBg(String.format("%s \tHa pasado %d dia", date, diffDays),0x19FF0000,holder);
            } else {
                diffDays = -diffDays;
                setTextAndBg(String.format("%s \tHan pasado %d dias", date, diffDays),0x19FF0000,holder);
            }
        }
        if (holder.checkBox.isChecked()){
            setTextAndBg("",0x1900FF00,holder);
        }

    }

    private void setTextAndBg(String text,int color, ViewHolder holder){
        if (!text.equals("")){
            holder.fechaView.setText(text);
        }
        holder.textView.setBackgroundColor(color);
        holder.fechaView.setBackgroundColor(color);
    }

    private void setListeners(ViewHolder holder, int position){
        Item item = items.get(position);
        MainActivity mainActivity = (MainActivity) getContext();
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Guarda el estado de la checkbox en el mapa
                mainActivity.checkChangeOnPosition(position);
            }
        });
        holder.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.tareas.set(position, item.shrinkText());
                if (item.getShrink()){
                    holder.fechaView.setTextSize(0.0f);
                }else{
                    holder.fechaView.setTextSize(14.0f);
                }
                mainActivity.notifyChange();

            }
        });
        holder.boton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogoDescripcion customDialog = new DialogoDescripcion(position);
                customDialog.show(mainActivity.getSupportFragmentManager());
                return false;
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.checkboxadapter, parent, false);
            holder = new ViewHolder();
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.textView = (TextView) convertView.findViewById(R.id.text);
            holder.fechaView = (TextView) convertView.findViewById(R.id.textoFechaLista);
            holder.boton = (Button) convertView.findViewById(R.id.botonVerFecha);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setItemConfiguration(position, holder);
        setListeners(holder, position);
        return convertView;
    }


    private static class ViewHolder {
        CheckBox checkBox;
        TextView textView;
        TextView fechaView;
        Button boton;
    }
}