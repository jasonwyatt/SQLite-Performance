package co.jasonwyatt.sqliteperf.inserts.integers;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.inserts.DbHelper;

/**
 * @author jason
 */

public class IntegerSQLiteStatementBatchTransactionCase implements TestCase {
    private DbHelper mDbHelper;
    private final Random mRandom;
    private final int mInsertions;
    private final int mTestSizeIndex;

    public IntegerSQLiteStatementBatchTransactionCase(int insertions, int testSizeIndex) {
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
        mDbHelper = new DbHelper(App.getInstance(), IntegerSQLiteStatementBatchTransactionCase.class.getSimpleName());
        Metrics result = new Metrics(getClass().getSimpleName()+" ("+mInsertions+" insertions)", mTestSizeIndex);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        result.started();

        Map<Integer, SQLiteStatement> statementCache = new HashMap<>();

        db.beginTransaction();
        doInsertions(db, mInsertions, statementCache);
        db.setTransactionSuccessful();
        db.endTransaction();

        result.finished();
        return result;
    }

    private void doInsertions(SQLiteDatabase db, int total, Map<Integer, SQLiteStatement> statementCache) {
        if (total > 999) {
            doInsertions(db, total - 999, statementCache);
            total = 999;
        }
        SQLiteStatement stmt;
        if (statementCache.containsKey(total)) {
            stmt = statementCache.get(total);
        } else {
            StringBuilder valuesBuilder = new StringBuilder();
            for (int i = 0; i < total; i++) {
                if (i > 0) {
                    valuesBuilder.append(", ");
                }
                valuesBuilder.append("(?)");
            }
            stmt = db.compileStatement("INSERT INTO inserts_1 (val) VALUES "+valuesBuilder.toString());
            statementCache.put(total, stmt);
        }

        for (int i = 0; i < total; i++) {
            stmt.bindLong(i+1, mRandom.nextInt());
        }

        stmt.executeInsert();
        stmt.clearBindings();
    }
}
