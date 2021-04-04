package br.com.alura.ceep.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.alura.ceep.model.Nota;

@Dao
public interface NotaDao {


    @Query("SELECT * FROM nota")
    List<Nota> getAll();

    @Query("SELECT * FROM nota WHERE uid IN (:notaIds)")
    List<Nota> loadAllByIds(String[] notaIds);

    @Query("SELECT * FROM nota WHERE titulo LIKE :first AND " +
            "descricao LIKE :last LIMIT 1")
    Nota findByName(String first, String last);

    @Query("SELECT * FROM nota WHERE position LIKE :first LIMIT 1")
    Nota findByPosition(int first);


    @Update
    void update(Nota... notas);

    @Insert
    void insertAll(Nota... notas);

    @Delete
    void delete(Nota nota);

//    void change(int posicaoInicio, int posicaoFim);

}
