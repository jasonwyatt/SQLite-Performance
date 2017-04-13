package co.jasonwyatt.sqliteperf.inserts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author jason
 */

class DbHelper extends SQLiteOpenHelper {
    DbHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE inserts_1 (val INTEGER)");
        db.execSQL("CREATE TABLE inserts_2 (val TEXT)");
        db.execSQL("CREATE TABLE inserts_3 (val REAL)");
        db.execSQL("CREATE TABLE inserts_4 (val BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
