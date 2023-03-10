package com.example.todolist;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import org.jetbrains.annotations.NotNull;

public class DialogoAgregarCarpeta extends DialogFragment {
    private Button botonAgregar;
    private EditText textoCarpetaNueva;
    private MainActivity mainActivity;
    public DialogoAgregarCarpeta(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState) {
        return inflater.inflate(R.layout.agregar_carpeta_dialogo, container, false);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        botonAgregar = view.findViewById(R.id.botonCrearCarpeta);
        textoCarpetaNueva = view.findViewById(R.id.agregarCarpetaTexto);
        mainActivity = (MainActivity) getContext();
        setListeners();
    }

    private void setListeners() {
        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texto = textoCarpetaNueva.getText().toString();
                if (!texto.equals("")){
                    if (!mainActivity.carpetas.contains(texto)){
                        mainActivity.carpetas.add(texto);
                    }
                    textoCarpetaNueva.setText("");
                    mainActivity.saveData();
                }
            }
        });
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "dialogo_agregar_carpeta");
    }
}
