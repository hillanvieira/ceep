package br.com.alura.ceep.model;

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

    @ColumnInfo(name = "position")
    public final int position;

    public Nota(String titulo, String descricao, int color, int position) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.color = color;
        this.position = position;
    }

}