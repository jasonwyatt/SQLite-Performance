package co.jasonwyatt.sqliteperf.blobs.cases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.blobs.DbHelper;

/**
 * @author jason
 */

public class RawQueryTestCase implements TestCase {
    private final int mTestSizeIndex;
    private DbHelper mDbHelper;
    private final int mFileSize;
    private final int mFiles;

    public RawQueryTestCase(int fileSize, int files, int testSizeIndex) {
        mFileSize = fileSize;
        mFiles = files;
        mTestSizeIndex = testSizeIndex;
    }

    @Override
    public void resetCase() {
        mDbHelper.close();
        App.getInstance().deleteDatabase(getClass().getSimpleName());
    }

    @Override
    public Metrics runCase() {
        mDbHelper = new DbHelper(App.getInstance(), getClass().getSimpleName(), mFileSize, mFiles);
        Metrics result = new Metrics(getClass().getSimpleName()+" ("+mFileSize+" blob size, "+mFiles+" blobs)", mTestSizeIndex);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] fileNames = Arrays.copyOf(mDbHelper.getFileNames(), mDbHelper.getFileNames().length);
        List<String> fileList = Arrays.asList(fileNames);
        Collections.shuffle(fileList, new Random());
        fileNames = fileList.toArray(fileNames);

        result.started();
        String[] selectionArgs = new String[1];
        for (int i = 0; i < mFiles; i++) {
            selectionArgs[0] = fileNames[i];
            Cursor c = db.rawQuery("SELECT data FROM files WHERE filename = ?", selectionArgs);
            try {
                c.moveToNext();
                byte[] data = c.getBlob(0);
            } finally {
                c.close();
            }
        }
        result.finished();
        return result;
    }
}
