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

public class IntegerInsertsRawBatchTransactionCase implements TestCase {
    private DbHelper mDbHelper;
    private final Random mRandom;
    private final int mInsertions;
    private final int mTestSizeIndex;

    public IntegerInsertsRawBatchTransactionCase(int insertions, int testSizeIndex) {
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
        mDbHelper = new DbHelper(App.getInstance(), IntegerInsertsRawBatchTransactionCase.class.getSimpleName());
        Metrics result = new Metrics(getClass().getSimpleName()+" ("+mInsertions+" insertions)", mTestSizeIndex);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        result.started();

        db.beginTransaction();
        doInsertions(db, mInsertions);
        db.setTransactionSuccessful();
        db.endTransaction();

        result.finished();
        return result;
    }

    private void doInsertions(SQLiteDatabase db, int total) {
        if (total > 999) {
            doInsertions(db, total - 999);
            total = 999;
        }
        Object[] values = new Object[total];
        StringBuilder valuesBuilder = new StringBuilder();

        for (int i = 0; i < total; i++) {
            if (i != 0) {
                valuesBuilder.append(", ");
            }
            values[i] = mRandom.nextInt();
            valuesBuilder.append("(?)");
        }

        db.execSQL("INSERT INTO inserts_1 (val) VALUES "+valuesBuilder.toString(), values);
    }
}
