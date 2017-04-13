package co.jasonwyatt.sqliteperf.inserts;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Random;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.TestCase;

/**
 * @author jason
 */

public class IntegerInsertsTransactionCase implements TestCase {
    private final DbHelper mDbHelper;
    private final Random mRandom;
    private final int mInsertions;
    private final int mTestSizeIndex;

    public IntegerInsertsTransactionCase(int insertions, int testSizeIndex) {
        mDbHelper = new DbHelper(App.getInstance(), IntegerInsertsTransactionCase.class.getSimpleName());
        mRandom = new Random(System.currentTimeMillis());
        mInsertions = insertions;
        mTestSizeIndex = testSizeIndex;
    }

    @Override
    public void resetCase() {
        Log.d("IntegerInserts", "Clearing for "+mInsertions+" insertions");
        mDbHelper.getWritableDatabase().execSQL("delete from inserts_1");
    }

    @Override
    public Metrics runCase() {
        Log.d("IntegerInserts", "Beginning "+mInsertions+" insertions");
        Metrics result = new Metrics(getClass().getSimpleName()+" ("+mInsertions+" insertions)", mTestSizeIndex);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        result.started();
        db.beginTransaction();
        ContentValues values = new ContentValues(1);
        for (int i = 0; i < mInsertions; i++) {
            values.clear();

            values.put("val", mRandom.nextInt());
            db.insert("inserts_1", null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        result.finished();
        return result;
    }
}
