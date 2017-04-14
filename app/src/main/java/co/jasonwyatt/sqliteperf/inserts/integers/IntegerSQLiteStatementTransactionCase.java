package co.jasonwyatt.sqliteperf.inserts.integers;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.Random;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.inserts.DbHelper;

/**
 * @author jason
 */

public class IntegerSQLiteStatementTransactionCase implements TestCase {
    private DbHelper mDbHelper;
    private final Random mRandom;
    private final int mInsertions;
    private final int mTestSizeIndex;

    public IntegerSQLiteStatementTransactionCase(int insertions, int testSizeIndex) {
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
        mDbHelper = new DbHelper(App.getInstance(), IntegerSQLiteStatementTransactionCase.class.getSimpleName());
        Metrics result = new Metrics(getClass().getSimpleName()+" ("+mInsertions+" insertions)", mTestSizeIndex);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        result.started();
        SQLiteStatement stmt = db.compileStatement("INSERT INTO inserts_1 (val) VALUES (?)");
        db.beginTransaction();
        for (int i = 0; i < mInsertions; i++) {
            stmt.bindLong(1, mRandom.nextInt());
            stmt.executeInsert();
            stmt.clearBindings();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        result.finished();
        return result;
    }
}
