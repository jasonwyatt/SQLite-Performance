package co.jasonwyatt.sqliteperf.blobs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Random;

/**
 * @author jason
 */

public class DbHelper extends SQLiteOpenHelper {
    private final int mFileSize;
    private final int mFiles;
    private String[] mFileNames;

    public DbHelper(Context context, String name, int fileSize, int files) {
        super(context, name, null, 1);
        mFileSize = fileSize;
        mFiles = files;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE files (filename TEXT PRIMARY KEY, data BLOB NOT NULL)");

        db.beginTransaction();
        try {
            createFiles(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void createFiles(SQLiteDatabase db) {
        mFileNames = new String[mFiles];
        byte[] rawData = new byte[mFileSize+mFiles];
        Random random = new Random();
        random.nextBytes(rawData);

        ByteArrayOutputStream[] streams = new ByteArrayOutputStream[mFiles];
        for (int i = 0; i < mFiles; i++) {
            streams[i] = new ByteArrayOutputStream(mFileSize);
            streams[i].write(rawData, i, mFileSize);
            mFileNames[i] = String.valueOf(i);
        }

        SQLiteStatement insert = db.compileStatement("INSERT INTO files (filename, data) VALUES (?, ?)");
        for (int i = 0; i < mFiles; i++) {
            insert.bindString(1, mFileNames[i]);
            insert.bindBlob(2, streams[i].toByteArray());

            insert.execute();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public String[] getFileNames() {
        return mFileNames;
    }
}
