package co.jasonwyatt.sqliteperf.inserts.tracks;

import android.arch.persistence.room.Room;

import java.nio.charset.Charset;
import java.util.Random;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.inserts.room.Db;
import co.jasonwyatt.sqliteperf.inserts.room.TrackInfo;

/**
 * @author jason
 */

public class TracksRoomTestCase implements TestCase {
    private final Db mDb;
    private final Random mRandom;
    private final int mInsertions;
    private final int mTestSizeIndex;

    public TracksRoomTestCase(int insertions, int testSizeIndex) {
        mDb = Room.databaseBuilder(App.getInstance(), Db.class, "room").build();
        mRandom = new Random(System.currentTimeMillis());
        mInsertions = insertions;
        mTestSizeIndex = testSizeIndex;
    }

    @Override
    public void resetCase() {
        mDb.tracks().deleteAll();
    }

    @Override
    public Metrics runCase() {
        Metrics result = new Metrics(getClass().getSimpleName()+" ("+mInsertions+" insertions)", mTestSizeIndex);

        Charset ascii = Charset.forName("US-ASCII");

        byte[] titleByteArry = new byte[50];
        byte[] urlByteArray = new byte[100];
        byte[] lyricsByteArray = new byte[2000];
        byte[] aboutByteArray = new byte[2000];

        result.started();
        // 100,000 TrackInfo objects will crash the app with an OutOfMemoryError, so hack around it here.
        int insertionsPerRound = mInsertions > 15000 ? 15000 : mInsertions;
        int insertionsSoFar = 0;
        while (insertionsSoFar < mInsertions) {
            int insertionsThisRound = insertionsPerRound < mInsertions - insertionsSoFar ? insertionsPerRound : mInsertions - insertionsSoFar;
            TrackInfo[] tracks = new TrackInfo[insertionsThisRound];
            for (int i = 0; i < insertionsThisRound; i++) {
                mRandom.nextBytes(titleByteArry);
                mRandom.nextBytes(urlByteArray);
                mRandom.nextBytes(lyricsByteArray);
                mRandom.nextBytes(aboutByteArray);

                TrackInfo t = new TrackInfo();
                t.setId(insertionsSoFar + i);
                t.setTitle(new String(titleByteArry, ascii));
                t.setBandId(mRandom.nextInt());
                t.setDuration(mRandom.nextDouble());
                t.setUrl(new String(urlByteArray, ascii));
                t.setLyrics(new String(lyricsByteArray, ascii));
                t.setAbout(new String(aboutByteArray, ascii));
                t.setReleaseDate(mRandom.nextLong());
                t.setModDate(mRandom.nextLong());

                tracks[i] = t;
            }
            insertionsSoFar += tracks.length;
            mDb.tracks().insertAll(tracks);
        }
        result.finished();
        return result;
    }
}
