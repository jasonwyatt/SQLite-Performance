package co.jasonwyatt.sqliteperf.inserts.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * @author jason
 */
@Database(entities = {TrackInfo.class, IntegerInfo.class}, version = 1)
public abstract class Db extends RoomDatabase {
    public abstract IntegersDao integers();
    public abstract TracksDao tracks();
}
