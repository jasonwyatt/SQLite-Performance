package co.jasonwyatt.sqliteperf.inserts.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tracks")
public class TrackInfo {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long mId;
    @ColumnInfo(name = "title")
    private String mTitle;
    @ColumnInfo(name = "band_id")
    private long mBandId;
    @ColumnInfo(name = "duration")
    private double mDuration;
    @ColumnInfo(name = "url")
    private String mUrl;
    @ColumnInfo(name = "lyrics")
    private String mLyrics;
    @ColumnInfo(name = "about")
    private String mAbout;
    @ColumnInfo(name = "release_date")
    private long mReleaseDate;
    @ColumnInfo(name = "mod_Date")
    private long mModDate;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public long getBandId() {
        return mBandId;
    }

    public void setBandId(long bandId) {
        mBandId = bandId;
    }

    public double getDuration() {
        return mDuration;
    }

    public void setDuration(double duration) {
        mDuration = duration;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getLyrics() {
        return mLyrics;
    }

    public void setLyrics(String lyrics) {
        mLyrics = lyrics;
    }

    public String getAbout() {
        return mAbout;
    }

    public void setAbout(String about) {
        mAbout = about;
    }

    public long getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(long releaseDate) {
        mReleaseDate = releaseDate;
    }

    public long getModDate() {
        return mModDate;
    }

    public void setModDate(long modDate) {
        mModDate = modDate;
    }
}
