package br.com.alura.ceep.database;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import br.com.alura.ceep.dao.NotaDao;
import br.com.alura.ceep.model.Nota;

@Database(entities = {Nota.class}, version = 1)
public abstract class CeepDatabase extends RoomDatabase {
    public abstract NotaDao notaDao();
}


