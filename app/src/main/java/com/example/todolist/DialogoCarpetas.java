package com.example.todolist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class DialogoCarpetas extends DialogFragment {

    private android.widget.SearchView searchView;
    private ListView visorCarpetas;
    private List<String> subCarpetas;
    private ArrayAdapter<String> arrayAdapter;
    private EditText textoCarpeta;
    private MainActivity mainActivity;

    public DialogoCarpetas(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState) {
        return inflater.inflate(R.layout.carpetas_dialogo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mainActivity = (MainActivity) getContext();
        searchView = view.findViewById(R.id.buscadorCarpetas);
        visorCarpetas = view.findViewById(R.id.listaCarpetas);
        subCarpetas = new ArrayList<>();
        subCarpetas.addAll(mainActivity.carpetas);
        arrayAdapter = new ArrayAdapter<>(mainActivity,
                android.R.layout.simple_list_item_1,subCarpetas);
        visorCarpetas.setAdapter(arrayAdapter);
        setListeners();
        searchView.requestFocus();
        textoCarpeta = mainActivity.findViewById(R.id.textoCarpeta);
    }

    public void setListeners(){

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // This method is called when the user submits the query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // This method is called when the user changes the query
                subCarpetas.clear();
                String busqueda = newText.toLowerCase();
                for (String s : mainActivity.carpetas) {
                    String sLow = s.toLowerCase();
                    if (sLow.contains(busqueda)) {
                        subCarpetas.add(s);
                    }
                }
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });
        visorCarpetas.setSelector(R.drawable.list_item_highlight);
        visorCarpetas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = (String) adapterView.getItemAtPosition(position);
                textoCarpeta.setText(item);
                visorCarpetas.setSelection(position);
            }
        });
        visorCarpetas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                // This method is called when an item is long clicked
                String item = (String) adapterView.getItemAtPosition(position);
                mainActivity.carpetas.remove(item);
                subCarpetas.remove(item);
                arrayAdapter.notifyDataSetChanged();
                mainActivity.saveData();
                return true;
            }
        });
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "dialogo_carpetas");
    }
}