package co.jasonwyatt.sqliteperf.inserts.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "inserts_1")
public class IntegerInfo {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    private int mRowid;
    @ColumnInfo(name = "val")
    private int mVal;

    public int getRowid() {
        return mRowid;
    }

    public void setRowid(int rowid) {
        mRowid = rowid;
    }

    public int getVal() {
        return mVal;
    }

    public void setVal(int val) {
        mVal = val;
    }
}
