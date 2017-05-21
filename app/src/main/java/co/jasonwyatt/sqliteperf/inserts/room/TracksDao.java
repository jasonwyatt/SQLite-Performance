package co.jasonwyatt.sqliteperf.inserts.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface TracksDao {
    @Insert
    void insertAll(TrackInfo... tracks);
    @Query("DELETE FROM tracks")
    void deleteAll();
}
