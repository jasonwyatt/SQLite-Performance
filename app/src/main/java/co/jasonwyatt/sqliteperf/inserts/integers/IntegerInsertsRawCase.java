package co.jasonwyatt.sqliteperf.inserts.integers;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Random;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.inserts.DbHelper;

/**
 * @author jason
 */

public class IntegerInsertsRawCase implements TestCase {
    private DbHelper mDbHelper;
    private final Random mRandom;
    private final int mInsertions;
    private final int mTestSizeIndex;

    public IntegerInsertsRawCase(int insertions, int testSizeIndex) {
        mRandom = new Random(System.currentTimeMillis());
        mInsertions = insertions;
        mTestSizeIndex = testSizeIndex;
    }

    @Override
    public void resetCase() {
        mDbHelper.getWritableDatabase().execSQL("delete from inserts_1");
        mDbHelper.close();
    }

    @Override
    public Metrics runCase() {
        mDbHelper = new DbHelper(App.getInstance(), IntegerInsertsRawCase.class.getSimpleName());
        Metrics result = new Metrics(getClass().getSimpleName()+" ("+mInsertions+" insertions)", mTestSizeIndex);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        result.started();
        Object[] values = new Object[1];
        for (int i = 0; i < mInsertions; i++) {
            values[0] = mRandom.nextInt();
            db.execSQL("INSERT INTO inserts_1 (val) VALUES (?)", values);
        }
        result.finished();
        return result;
    }
}
