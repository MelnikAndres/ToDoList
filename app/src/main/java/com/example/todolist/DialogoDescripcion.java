package com.example.todolist;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DialogoDescripcion extends DialogFragment {
    private int position;
    private EditText textoDescripcion, textoTitulo,textoFecha;

    public DialogoDescripcion(int position){
        this.position = position;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState) {
        return inflater.inflate(R.layout.dialogo_descripcion, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) getContext();
        Item item = mainActivity.tareas.get(position);
        String descripcion = item.getDescription();
        // configurar elementos del diálogo personalizado
        textoDescripcion = view.findViewById(R.id.textoDialogoDescripcion);
        if (!descripcion.equals("")){
            textoDescripcion.setText(descripcion);
        }
        textoTitulo = view.findViewById(R.id.textoDialogoTitulo);
        String titulo = item.getText();
        if (!titulo.equals("")){
            textoTitulo.setText(titulo);
        }
        textoFecha = view.findViewById(R.id.textoFechaDialogo);
        String fecha = item.getDate();
        if (!fecha.equals("")){
            textoFecha.setText(fecha);
        }
        setListener(textoDescripcion,mainActivity);
        setListener(textoTitulo,mainActivity);
        textoFecha.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar calendar1 = Calendar.getInstance();
                try {
                    Date date = null;
                    if (!item.getDate().equals("")){
                        date = sdf.parse(item.getDate());
                    }else{
                        date = calendar1.getTime();
                    }
                    calendar1.setTime(date);

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(mainActivity,new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        textoFecha.setText(String.format("%d/%d/%d", day, month + 1, year));
                    }
                }, calendar1.get(Calendar.YEAR),
                        calendar1.get(Calendar.MONTH),
                        calendar1.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                return true;
            }
        });
    }

    private void setListener(EditText editText, MainActivity mainActivity){
        editText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Enable editing on the EditText
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                return true;
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        MainActivity mainActivity = (MainActivity) getContext();
        Item item = mainActivity.tareas.get(position);
        mainActivity.tareas.set(position, item.setDescripcion(textoDescripcion.getText().toString()));
        mainActivity.tareas.set(position, item.setTitulo(textoTitulo.getText().toString()));
        mainActivity.tareas.set(position, item.setFecha(textoFecha.getText().toString()));
        mainActivity.notifyChange();
    }
    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "dialogo_descripcion");
    }
}
