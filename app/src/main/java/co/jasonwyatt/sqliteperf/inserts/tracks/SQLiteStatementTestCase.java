package co.jasonwyatt.sqliteperf.inserts.tracks;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.nio.charset.Charset;
import java.util.Random;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.inserts.DbHelper;

/**
 * @author jason
 */

public class SQLiteStatementTestCase implements TestCase {
    private DbHelper mDbHelper;
    private final Random mRandom;
    private final int mInsertions;
    private final int mTestSizeIndex;

    public SQLiteStatementTestCase(int insertions, int testSizeIndex) {
        mRandom = new Random(System.currentTimeMillis());
        mInsertions = insertions;
        mTestSizeIndex = testSizeIndex;
    }

    @Override
    public void resetCase() {
        mDbHelper.getWritableDatabase().execSQL("delete from tracks");
        mDbHelper.close();
    }

    @Override
    public Metrics runCase() {
        mDbHelper = new DbHelper(App.getInstance(), getClass().getName());
        Metrics result = new Metrics(getClass().getSimpleName()+" ("+mInsertions+" insertions)", mTestSizeIndex);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Charset ascii = Charset.forName("US-ASCII");

        byte[] titleByteArry = new byte[50];
        byte[] urlByteArray = new byte[100];
        byte[] lyricsByteArray = new byte[2000];
        byte[] aboutByteArray = new byte[2000];

        result.started();
        db.beginTransaction();
        SQLiteStatement stmt = db.compileStatement("INSERT INTO tracks (id, title, band_id, duration, url, lyrics, about, release_date, mod_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        for (int i = 0; i < mInsertions; i++) {
            mRandom.nextBytes(titleByteArry);
            mRandom.nextBytes(urlByteArray);
            mRandom.nextBytes(lyricsByteArray);
            mRandom.nextBytes(aboutByteArray);

            stmt.bindLong(1, i);
            stmt.bindString(2, new String(titleByteArry, ascii));
            stmt.bindLong(3, mRandom.nextInt());
            stmt.bindDouble(4, mRandom.nextDouble());
            stmt.bindString(5, new String(urlByteArray, ascii));
            stmt.bindString(6, new String(lyricsByteArray, ascii));
            stmt.bindString(7, new String(aboutByteArray, ascii));
            stmt.bindLong(8, mRandom.nextLong());
            stmt.bindLong(9, mRandom.nextLong());

            stmt.executeInsert();
            stmt.clearBindings();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        result.finished();

        return result;
    }
}
