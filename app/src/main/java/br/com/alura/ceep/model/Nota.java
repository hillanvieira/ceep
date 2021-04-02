package br.com.alura.ceep.model;

import android.graphics.Color;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Nota implements Serializable {

    @PrimaryKey
    public Long uid;

    @ColumnInfo(name = "titulo")
    public final String titulo;

    @ColumnInfo(name = "descricao")
    public final String descricao;

    @ColumnInfo(name = "color")
    public final int color;

    public Nota(String titulo, String descricao, int color) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.color = color;
    }

    @Ignore
    public Nota(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.color = 0xFFFFFFFF;
    }

}