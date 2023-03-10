package com.example.todolist;

import android.annotation.SuppressLint;
import android.text.Editable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Item implements Comparable<Item>, Cloneable{
    private  String text;
    private boolean checked;
    private  String description;
    private  String fechaLimite;
    private boolean shrink;
    private boolean notificable;
    private List<String> categorias;

    public int compareTo(Item o) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        try {
            String date = this.getDate();
            if (date.equals("")){
                return 999999;
            }
            Date inicio = sdf.parse(date);
            calendar1.setTime(inicio);
            String date2 = o.getDate();
            if (date2.equals("")){
                return -999999;
            }
            Date fin = sdf.parse(date2);
            calendar2.setTime(fin);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        int yearDif = calendar1.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
        int dayDif = calendar1.get(Calendar.DAY_OF_YEAR) - calendar2.get(Calendar.DAY_OF_YEAR);
        int diffDays = (yearDif * 365) + dayDif;
        return diffDays;
    }

    public Item(String text, boolean isChecked, String description, String fechaLimite, boolean shrink, boolean notificado, List<String> categorias) {
        this.text = text;
        this.checked = isChecked;
        this.description = description;
        this.fechaLimite = fechaLimite;
        this.shrink = shrink;
        this.notificable = notificado;
        this.categorias = categorias;
    }

    public String getText() {
        return text;
    }

    public List<String> getCategorias(){return categorias;}
    public String getDate() {
        return fechaLimite;
    }
    public boolean getShrink(){return shrink;}
    public String getDescription() {
        return description;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isnotificable() {
        return notificable;
    }

    public Item clone() {
        return new Item(this.text,this.checked,this.description,this.fechaLimite,
                this.shrink,this.notificable,new ArrayList<>(this.categorias));
    }

    public Item check(){
        checked = !checked;
        return returnItem();
    }

    public Item shrinkText(){
        shrink = !shrink;
        return returnItem();
    }

    public Item setNotificado(){
        notificable = !notificable;
        return returnItem();
    }

    public Item setTitulo(String texto) {
        text = texto;
        return returnItem();
    }

    public Item setDescripcion(String texto) {
        description = texto;
        return returnItem();
    }

    public Item setFecha(String fecha) {
        fechaLimite = fecha;
        return returnItem();

    }
    private Item returnItem(){
        return new Item(text, checked, description, fechaLimite, shrink,notificable,categorias);
    }
}

