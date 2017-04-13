package co.jasonwyatt.sqliteperf.inserts;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Random;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.TestCase;

/**
 * @author jason
 */

public class IntegerInsertsRawBatchCase implements TestCase {
    private final DbHelper mDbHelper;
    private final Random mRandom;
    private final int mInsertions;
    private final int mTestSizeIndex;

    public IntegerInsertsRawBatchCase(int insertions, int testSizeIndex) {
        mDbHelper = new DbHelper(App.getInstance(), IntegerInsertsRawBatchCase.class.getSimpleName());
        mRandom = new Random(System.currentTimeMillis());
        mInsertions = insertions;
        mTestSizeIndex = testSizeIndex;
    }

    @Override
    public void resetCase() {
        Log.d(getClass().getSimpleName(), "Clearing for "+mInsertions+" insertions");
        mDbHelper.getWritableDatabase().execSQL("delete from inserts_1");
    }

    @Override
    public Metrics runCase() {
        Log.d(getClass().getSimpleName(), "Beginning "+mInsertions+" insertions");
        Metrics result = new Metrics(getClass().getSimpleName()+" ("+mInsertions+" insertions)", mTestSizeIndex);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        result.started();

        doInsertions(db, mInsertions);

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
