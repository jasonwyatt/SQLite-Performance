package co.jasonwyatt.sqliteperf.inserts.integers;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import java.util.Random;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.inserts.room.Db;
import co.jasonwyatt.sqliteperf.inserts.room.IntegerInfo;

/**
 * @author jason
 */

public class IntegerRoomTestCase implements TestCase {
    private Db mDb;
    private final Random mRandom;
    private final int mInsertions;
    private final int mTestSizeIndex;

    public IntegerRoomTestCase(int insertions, int testSizeIndex) {
        mDb = Room.databaseBuilder(App.getInstance(), Db.class, "roomdb").build();
        mRandom = new Random(System.currentTimeMillis());
        mInsertions = insertions;
        mTestSizeIndex = testSizeIndex;
    }
    @Override
    public void resetCase() {
        mDb.integers().deleteAll();
    }

    @Override
    public Metrics runCase() {
        Metrics result = new Metrics(getClass().getSimpleName()+" ("+mInsertions+" insertions)", mTestSizeIndex);
        result.started();
        IntegerInfo[] data = new IntegerInfo[mInsertions];
        for (int i = 0; i < mInsertions; i++) {
            data[i] = new IntegerInfo();
            data[i].setVal(mRandom.nextInt());
        }
        mDb.integers().insertAll(data);
        result.finished();
        return result;
    }
}
