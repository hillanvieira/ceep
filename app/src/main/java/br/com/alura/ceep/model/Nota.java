package br.com.alura.ceep.model;

import android.graphics.Color;

import java.io.Serializable;

public class Nota implements Serializable {

    private final String titulo;
    private final String descricao;
    private final int color;

    public Nota(String titulo, String descricao, int color) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.color = color;
    }


    public Nota(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.color = 0xFFFFFFFF;
    }


    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getColor() {
        return color;
    }

}