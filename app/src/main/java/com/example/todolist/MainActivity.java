package com.example.todolist;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.todolist.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public List<String> recientes;
    public static List<Item> tareas;
    public ArrayAdapter<String> adaptadorRecientes;
    public checkboxAdapter adaptadorTareas;
    public removableAdapter adaptadorBorrables;
    public List<String> carpetas, categorias;

    private void setConfig(){
        com.example.todolist.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
    private void asignarValores(){
        tareas = new ArrayList<>();
        recientes = new ArrayList<>();
        carpetas = new ArrayList<>();
        carpetas.add("Citacion");
        carpetas.add("Cumpleaños");
        carpetas.add("General");
        carpetas.add("Importante");
        categorias = new ArrayList<>();
        loadData(this);
        adaptadorTareas = new checkboxAdapter(this, tareas);
        adaptadorRecientes = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,recientes);
        adaptadorBorrables = new removableAdapter(this, tareas);
        adaptadorTareas.notifyDataSetChanged();
        adaptadorBorrables.notifyDataSetChanged();

    }

    public void notifyChange(){
        adaptadorTareas.notifyDataSetChanged();
        saveData();
    }

    public void checkChangeOnPosition(int position){
        tareas.get(position).check();
        notifyChange();
    }

    public void removeOnPosition(int position){
        tareas.remove(position);
        adaptadorBorrables.notifyDataSetChanged();
        saveData();
    }

    public void loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Item>>() {}.getType();
        List<Item> itemsPosible= gson.fromJson(json, type);
        String json2 = sharedPreferences.getString("carpetas", null);
        Type type2 = new TypeToken<ArrayList<String>>() {}.getType();
        List<String> carpetasPosible = gson.fromJson(json2, type2);

        if (itemsPosible != null) {
            tareas = itemsPosible;
        }
        if(carpetasPosible != null){
            carpetas = carpetasPosible;
        }
    }

    public void saveData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tareas);
        String json2 = gson.toJson(carpetas);
        editor.putString("task list", json);
        editor.putString("carpetas", json2);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asignarValores();
        setConfig();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.canal_name);
            String description = getString(R.string.canal_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("canal_id", name, importance);
            channel.setDescription(description);
            // Registramos el canal en el NotificationManager
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        setAlarm();
    }
    public void setAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class);
        intent.setAction("com.example.alarm.ACTION_ALARM");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        long triggerTime = System.currentTimeMillis() + 5 * 60 * 1000; // first trigger in 5 minutes

        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }


    public void orderByFecha() {
        Collections.sort(tareas);
    }

    @Override
    public void onResume() {
        super.onResume();
        adaptadorTareas.notifyDataSetChanged();
    }
}