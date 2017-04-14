package co.jasonwyatt.sqliteperf.inserts.tracks;

import android.database.sqlite.SQLiteDatabase;

import java.nio.charset.Charset;
import java.util.Random;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.inserts.DbHelper;

/**
 * @author jason
 */

public class BatchedTestCase implements TestCase {
    private DbHelper mDbHelper;
    private final Random mRandom;
    private final int mInsertions;
    private final int mTestSizeIndex;
    private int mInsertId;

    public BatchedTestCase(int insertions, int testSizeIndex) {
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

        byte[] titleByteArry = new byte[50];
        byte[] urlByteArray = new byte[100];
        byte[] lyricsByteArray = new byte[2000];
        byte[] aboutByteArray = new byte[2000];

        result.started();
        db.beginTransaction();
        mInsertId = 1;
        doInsertions(db, mInsertions, titleByteArry, urlByteArray, lyricsByteArray, aboutByteArray);
        db.setTransactionSuccessful();
        db.endTransaction();
        result.finished();

        return result;
    }

    private void doInsertions(SQLiteDatabase db, int total, byte[] titleByteArray, byte[] urlByteArray, byte[] lyricsByteArray, byte[] aboutByteArray) {
        // divide 999 by 9 since that's the # of fields in our table
        if (total > 999/9) {
            doInsertions(db, total-999/9, titleByteArray, urlByteArray, lyricsByteArray, aboutByteArray);
            total = 999/9;
        }
        Charset ascii = Charset.forName("US-ASCII");

        Object[] values = new Object[total*9];
        StringBuilder valueBuilder = new StringBuilder();
        for (int i = 0; i < total; i++) {
            if (i > 0) {
                valueBuilder.append(", ");
            }
            mRandom.nextBytes(titleByteArray);
            mRandom.nextBytes(urlByteArray);
            mRandom.nextBytes(lyricsByteArray);
            mRandom.nextBytes(aboutByteArray);

            values[9*i+0] = mInsertId++;
            values[9*i+1] = new String(titleByteArray, ascii);
            values[9*i+2] = mRandom.nextInt();
            values[9*i+3] = mRandom.nextDouble();
            values[9*i+4] = new String(urlByteArray, ascii);
            values[9*i+5] = new String(lyricsByteArray, ascii);
            values[9*i+6] = new String(aboutByteArray, ascii);
            values[9*i+7] = mRandom.nextLong();
            values[9*i+8] = mRandom.nextLong();

            valueBuilder.append("(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        }

        db.execSQL("INSERT INTO tracks (id, title, band_id, duration, url, lyrics, about, release_date, mod_date) VALUES "+valueBuilder.toString(), values);
    }
}
