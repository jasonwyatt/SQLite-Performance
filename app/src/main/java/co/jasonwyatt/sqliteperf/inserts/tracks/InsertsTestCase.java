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

public class InsertsTestCase implements TestCase {
    private final DbHelper mDbHelper;
    private final Random mRandom;
    private final int mInsertions;
    private final int mTestSizeIndex;

    public InsertsTestCase(int insertions, int testSizeIndex) {
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
        ContentValues values = new ContentValues(9);
        for (int i = 0; i < mInsertions; i++) {
            mRandom.nextBytes(titleByteArry);
            mRandom.nextBytes(urlByteArray);
            mRandom.nextBytes(lyricsByteArray);
            mRandom.nextBytes(aboutByteArray);

            values.put("id", i);
            values.put("title", new String(titleByteArry, ascii));
            values.put("band_id", mRandom.nextInt());
            values.put("duration", mRandom.nextDouble());
            values.put("url", new String(urlByteArray, ascii));
            values.put("lyrics", new String(lyricsByteArray, ascii));
            values.put("about", new String(aboutByteArray, ascii));
            values.put("release_date", mRandom.nextLong());
            values.put("mod_date", mRandom.nextLong());

            db.insert("tracks", null, values);
        }
        result.finished();
        return result;
    }
}
