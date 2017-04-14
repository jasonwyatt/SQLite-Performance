package co.jasonwyatt.sqliteperf.inserts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author jason
 */

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE inserts_1 (val INTEGER)");
        db.execSQL("CREATE TABLE tracks (id INTEGER PRIMARY KEY, title TEXT, band_id INTEGER, duration FLOAT, url TEXT, lyrics TEXT, about TEXT, release_date INTEGER, mod_date INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
