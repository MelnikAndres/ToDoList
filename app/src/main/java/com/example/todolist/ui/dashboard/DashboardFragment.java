package com.example.todolist.ui.dashboard;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.todolist.*;
import com.example.todolist.databinding.FragmentDashboardBinding;

import java.util.*;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    public Button botonAgregarTarea, botonFecha, botonCarpeta;
    private EditText textoTareaNueva,textoDescripcion,fechaLimite,textoCarpeta;
    private ListView visorListaRecientes;
    private ImageButton botonNuevaCarpeta;

    private CheckBox notificarCheckbox;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        MainActivity mainActivity = (MainActivity) getActivity();
        setValues(root);
        visorListaRecientes.setAdapter(mainActivity.adaptadorRecientes);
        setListener(mainActivity);
        String general = "General";
        textoCarpeta.setText(general);
        return root;
    }

    private void setValues(View root){
        botonAgregarTarea = root.findViewById(R.id.botonAgregar);
        textoTareaNueva = root.findViewById(R.id.tareaEtiqueta);
        textoDescripcion = root.findViewById(R.id.descripcionEtiqueta);
        visorListaRecientes = root.findViewById(R.id.listaRecientes);
        fechaLimite = root.findViewById(R.id.editTextDate);
        botonFecha = root.findViewById(R.id.botonFecha);
        botonCarpeta = root.findViewById(R.id.botonCarpetas);
        notificarCheckbox = root.findViewById(R.id.checkBoxNotificar);
        textoCarpeta = root.findViewById(R.id.textoCarpeta);
        botonNuevaCarpeta = root.findViewById(R.id.botonNuevaCarpeta);
    }
    private void setListener(MainActivity mainActivity){
        botonAgregarTarea.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String texto = textoTareaNueva.getText().toString();
                String descripcion = textoDescripcion.getText().toString();
                String fecha = fechaLimite.getText().toString();
                boolean checked = notificarCheckbox.isChecked();
                String carpeta = textoCarpeta.getText().toString();
                List<String> categorias = new ArrayList<>();
                categorias.add(carpeta);
                mainActivity.tareas.add(new Item(texto, false, descripcion, fecha, false, checked, categorias));
                mainActivity.orderByFecha();
                mainActivity.recientes.add(0,texto);
                textoTareaNueva.setText("");
                fechaLimite.setText("");
                textoDescripcion.setText("");
                notificarCheckbox.setChecked(false);
                mainActivity.adaptadorRecientes.notifyDataSetChanged();
                mainActivity.saveData();
            }
        });
        botonFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mainActivity,new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        fechaLimite.setText(String.format("%d/%d/%d", day, month + 1, year));
                    }
                }, Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        botonCarpeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogoCarpetas dialogoCarpetas = new DialogoCarpetas();
                dialogoCarpetas.show(mainActivity.getSupportFragmentManager());
            }
        });
        botonNuevaCarpeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogoAgregarCarpeta dialogoAgregarCarpeta = new DialogoAgregarCarpeta();
                dialogoAgregarCarpeta.show(mainActivity.getSupportFragmentManager());
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}