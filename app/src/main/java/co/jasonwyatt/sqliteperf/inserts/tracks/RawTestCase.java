package co.jasonwyatt.sqliteperf.inserts.tracks;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.nio.charset.Charset;
import java.util.Random;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.inserts.DbHelper;

/**
 * @author jason
 */

public class RawTestCase implements TestCase {
    private final DbHelper mDbHelper;
    private final Random mRandom;
    private final int mInsertions;
    private final int mTestSizeIndex;

    public RawTestCase(int insertions, int testSizeIndex) {
        mDbHelper = new DbHelper(App.getInstance(), getClass().getName());
        mRandom = new Random(System.currentTimeMillis());
        mInsertions = insertions;
        mTestSizeIndex = testSizeIndex;
    }

    @Override
    public void resetCase() {
        mDbHelper.getWritableDatabase().execSQL("delete from tracks");
    }

    @Override
    public Metrics runCase() {
        Metrics result = new Metrics(getClass().getSimpleName()+" ("+mInsertions+" insertions)", mTestSizeIndex);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Charset ascii = Charset.forName("US-ASCII");

        byte[] titleByteArry = new byte[50];
        byte[] urlByteArray = new byte[100];
        byte[] lyricsByteArray = new byte[2000];
        byte[] aboutByteArray = new byte[2000];

        result.started();
        db.beginTransaction();
        Object[] values = new Object[9];
        for (int i = 0; i < mInsertions; i++) {
            mRandom.nextBytes(titleByteArry);
            mRandom.nextBytes(urlByteArray);
            mRandom.nextBytes(lyricsByteArray);
            mRandom.nextBytes(aboutByteArray);

            values[0] = i;
            values[1] = new String(titleByteArry, ascii);
            values[2] = mRandom.nextInt();
            values[3] = mRandom.nextDouble();
            values[4] = new String(urlByteArray, ascii);
            values[5] = new String(lyricsByteArray, ascii);
            values[6] = new String(aboutByteArray, ascii);
            values[7] = mRandom.nextLong();
            values[8] = mRandom.nextLong();

            db.execSQL("INSERT INTO tracks (id, title, band_id, duration, url, lyrics, about, release_date, mod_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        result.finished();

        return result;
    }
}
