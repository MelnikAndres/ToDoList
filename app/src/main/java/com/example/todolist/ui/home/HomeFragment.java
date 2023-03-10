package com.example.todolist.ui.home;

import android.os.Bundle;
import android.view.*;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.todolist.*;
import com.example.todolist.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ListView listView;
    private android.widget.SearchView searcher;
    private MainActivity mainActivity;
    public List<Item> backup;
    public HashMap<String, Item> backupHash;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_button:
                // Do something when the action button is clicked
                DialogoCategorias dialogoCategorias = new DialogoCategorias(backup, backupHash);
                dialogoCategorias.show(mainActivity.getSupportFragmentManager());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mainActivity = (MainActivity) getActivity();
        listView = root.findViewById(R.id.visorLista);
        listView.setAdapter(mainActivity.adaptadorTareas);
        searcher = root.findViewById(R.id.searchTareas);
        backup = new ArrayList<>();
        backup.addAll(MainActivity.tareas);
        backupHash = new HashMap<>();
        for (Item item: MainActivity.tareas){
            backupHash.put(item.getText(),item);
        }
        setListeners();
        return root;

    }

    private void setListeners(){
        MainActivity mainActivity = (MainActivity) getContext();
        searcher.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // This method is called when the user submits the query
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // This method is called when the user changes the query
                MainActivity.tareas.clear();
                String busqueda = newText.toLowerCase();
                for (Item item : backup) {
                    String sLow = item.getText().toLowerCase();
                    if (sLow.contains(busqueda)) {
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
                }
                mainActivity.adaptadorTareas.notifyDataSetChanged();

                return false;
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.tareas.clear();
        for (Item item: backup){
            MainActivity.tareas.add(backupHash.get(item.getText()));
        }
        mainActivity.saveData();
        mainActivity.categorias.clear();
        binding = null;
    }
}