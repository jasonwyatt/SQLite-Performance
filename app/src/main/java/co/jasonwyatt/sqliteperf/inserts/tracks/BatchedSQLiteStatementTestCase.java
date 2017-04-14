package co.jasonwyatt.sqliteperf.inserts.tracks;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.inserts.DbHelper;

/**
 * @author jason
 */

public class BatchedSQLiteStatementTestCase implements TestCase {
    private DbHelper mDbHelper;
    private final Random mRandom;
    private final int mInsertions;
    private final int mTestSizeIndex;
    private int mInsertId;

    public BatchedSQLiteStatementTestCase(int insertions, int testSizeIndex) {
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
        Map<Integer, SQLiteStatement> statementCache = new HashMap<>();

        result.started();
        db.beginTransaction();
        mInsertId = 1;
        doInsertions(db, mInsertions, statementCache, titleByteArry, urlByteArray, lyricsByteArray, aboutByteArray);
        db.setTransactionSuccessful();
        db.endTransaction();
        result.finished();

        return result;
    }

    private void doInsertions(SQLiteDatabase db, int total, Map<Integer, SQLiteStatement> statementCache, byte[] titleByteArray, byte[] urlByteArray, byte[] lyricsByteArray, byte[] aboutByteArray) {
        // divide 999 by 9 since that's the # of fields in our table
        if (total > 999/9) {
            doInsertions(db, total-999/9, statementCache, titleByteArray, urlByteArray, lyricsByteArray, aboutByteArray);
            total = 999/9;
        }
        SQLiteStatement stmt;
        if (statementCache.containsKey(total)) {
            stmt = statementCache.get(total);
        } else {
            StringBuilder valueBuilder = new StringBuilder();
            for (int i = 0; i < total; i++) {
                if (i > 0) {
                    valueBuilder.append(", ");
                }
                valueBuilder.append("(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            }
            stmt = db.compileStatement("INSERT INTO tracks (id, title, band_id, duration, url, lyrics, about, release_date, mod_date) VALUES "+valueBuilder.toString());
            statementCache.put(total, stmt);
        }

        Charset ascii = Charset.forName("US-ASCII");
        for (int i = 0; i < total; i++) {
            mRandom.nextBytes(titleByteArray);
            mRandom.nextBytes(urlByteArray);
            mRandom.nextBytes(lyricsByteArray);
            mRandom.nextBytes(aboutByteArray);

            stmt.bindLong(9*i+1, mInsertId++);
            stmt.bindString(9*i+2, new String(titleByteArray, ascii));
            stmt.bindLong(9*i+3, mRandom.nextInt());
            stmt.bindDouble(9*i+4, mRandom.nextDouble());
            stmt.bindString(9*i+5, new String(urlByteArray, ascii));
            stmt.bindString(9*i+6, new String(lyricsByteArray, ascii));
            stmt.bindString(9*i+7, new String(aboutByteArray, ascii));
            stmt.bindLong(9*i+8, mRandom.nextLong());
            stmt.bindLong(9*i+9, mRandom.nextLong());
        }

        stmt.executeInsert();
        stmt.clearBindings();
    }
}
