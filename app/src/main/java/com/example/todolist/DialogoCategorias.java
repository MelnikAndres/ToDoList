package com.example.todolist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.example.todolist.ui.home.HomeFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class DialogoCategorias extends DialogFragment {

    private ListView listaCategorias;
    private ArrayAdapter<String> arrayAdapter;
    private MainActivity mainActivity;
    private ChipGroup chipsActivadas;
    private android.widget.SearchView searchCategoria;
    private List<String> filtrados;
    private List<Item> backup;
    private HashMap<String, Item> backupHash;

    private HashSet<String> chipsUsadas;

    public DialogoCategorias(List<Item> backup, HashMap<String, Item> backupHash){
        this.backup = backup;
        this.backupHash = backupHash;

    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.categoria_dialogo, container, false);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        mainActivity = (MainActivity) getContext();
        chipsActivadas = view.findViewById(R.id.chipsCategorias);
        filtrados = new ArrayList<>();
        filtrados.addAll(mainActivity.carpetas);
        arrayAdapter = new ArrayAdapter<>(mainActivity,
                android.R.layout.simple_list_item_1,filtrados);
        listaCategorias = view.findViewById(R.id.listaCategorias);
        searchCategoria = view.findViewById(R.id.searchCategorias);
        listaCategorias.setAdapter(arrayAdapter);
        chipsUsadas = new HashSet<>();
        for (Item item: MainActivity.tareas){
            backupHash.put(item.getText(),item);
        }
        for(String categoria: mainActivity.categorias){
            Chip chip = new Chip(mainActivity);
            chip.setText(categoria);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    forceSize();
                    chipsActivadas.removeView(chip);
                    chipsUsadas.remove(categoria);
                }
            });
            chipsActivadas.addView(chip);
            chipsUsadas.add(categoria);
        }
        setListeners();
        forceSize();
    }


    private void setListeners(){
        listaCategorias.setSelector(R.drawable.list_item_highlight);
        listaCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String texto = mainActivity.carpetas.get(i);
                forceSize();
                if (chipsUsadas.contains(texto)){
                    return;
                }
                Chip chip = new Chip(mainActivity);
                chip.setText(texto);
                chip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        forceSize();
                        chipsActivadas.removeView(chip);
                        chipsUsadas.remove(texto);
                    }
                });
                chipsActivadas.addView(chip);
                chipsUsadas.add(texto);
            }
        });
        searchCategoria.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filtrados.clear();
                String sLow = s.toLowerCase();
                for(String categoria: mainActivity.carpetas){
                    String categoriaLow = categoria.toLowerCase();
                    if(categoriaLow.contains(sLow)){
                        filtrados.add(categoria);
                    }
                }
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "categorias_dialogo");
    }

    public void forceSize(){
        searchCategoria.setMinimumWidth(getResources().getDisplayMetrics().widthPixels);
        listaCategorias.setMinimumWidth(getResources().getDisplayMetrics().widthPixels);
    }

    @Override
    public void onDestroy() {
        mainActivity.categorias.clear();
        mainActivity.categorias.addAll(chipsUsadas);
        MainActivity.tareas.clear();
        for(Item item: backup){
            if (mainActivity.categorias.size() == 0){
                MainActivity.tareas.add(backupHash.get(item.getText()));
            }else{
                for(String categoria: mainActivity.categorias){
                    if (item.getCategorias().contains(categoria)){
                        MainActivity.tareas.add(backupHash.get(item.getText()));
                        break;
                    }
                }
            }

        }
        mainActivity.notifyChange();
        super.onDestroy();
    }
}
