package co.jasonwyatt.sqliteperf.inserts.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface IntegersDao {
    @Insert
    void insertAll(IntegerInfo... values);
    @Query("DELETE FROM inserts_1")
    void deleteAll();
}
