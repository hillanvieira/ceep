package br.com.alura.ceep.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class Nota implements Serializable {

    @NonNull
    @PrimaryKey
    public String uid;

    @ColumnInfo(name = "titulo")
    public  String titulo;

    @ColumnInfo(name = "descricao")
    public  String descricao;

    @ColumnInfo(name = "color")
    public  int color;

    @ColumnInfo(name = "position")
    public  int position;

    public Nota(String uid,String titulo, String descricao, int color, int position) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.color = color;
        this.position = position;
        this.uid = uid;
    }

    @Ignore
    public Nota(String titulo, String descricao, int color, int position) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.color = color;
        this.position = position;
        this.uid = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Nota{" +
                "uid=" + uid +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", color=" + color +
                ", position=" + position +
                '}';
    }
}