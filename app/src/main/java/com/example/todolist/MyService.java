package com.example.todolist;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyService extends Service {
    private Handler handler;
    private Runnable runnable;
    private List<Item> tareas;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                // Perform your task here
                tareas = MainActivity.tareas;
                if (tareas == null){
                    loadData();
                }
                Calendar calendar2 = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar calendar1 = Calendar.getInstance();
                Date inicio = calendar1.getTime();
                calendar1.setTime(inicio);
                for(int i = 0; i< tareas.size();i++){
                    Item item = tareas.get(i);
                    if (item.getDate().equals("")){
                        continue;
                    }
                    Date fin = null;
                    try {
                        fin = sdf.parse(item.getDate());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    calendar2.setTime(fin);
                    int yearDif = calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR);
                    int dayDif = calendar2.get(Calendar.DAY_OF_YEAR) - calendar1.get(Calendar.DAY_OF_YEAR);
                    int diffDays = (yearDif * 365) + dayDif;
                    if (diffDays == 0) {
                        if (tareas.get(i).isnotificable()){
                            setNotificado(i);
                        }

                    }
                }
                setAlarm();
            }
        };
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // This method is called when the service is started
        // Put your background task code here
        handler.post(runnable);
        // Return START_STICKY to indicate that the service should be restarted if it is stopped by the system
        return START_STICKY;
    }
    public IBinder onBind(Intent intent) {
        // Return null if the service does not support binding
        return null;
    }

    @Override
    public void onDestroy() {
        // Stop the task when the service is stopped
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    public void setAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, MyReceiver.class);
        intent.setAction("com.example.alarm.ACTION_ALARM");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        long triggerTime = System.currentTimeMillis() + 5 * 60 * 1000; // first trigger in 5 minutes

        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }


    public void setNotificado(int position){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("notification_channel_id", name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        tareas.set(position, tareas.get(position).setNotificado());
        String titulo = tareas.get(position).getText();
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel_id")
                .setSmallIcon(R.drawable.ic_dashboard_black_24dp)
                .setContentTitle(titulo)
                .setContentText("La tarea ha llegado a su fecha limite")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(position, builder.build());
        saveData();
    }
    public void loadData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Item>>() {}.getType();
        List<Item> itemsPosible= gson.fromJson(json, type);

        if (itemsPosible != null) {
            tareas = itemsPosible;
        }
    }

    public void saveData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tareas);
        editor.putString("task list", json);
        editor.apply();
    }
}