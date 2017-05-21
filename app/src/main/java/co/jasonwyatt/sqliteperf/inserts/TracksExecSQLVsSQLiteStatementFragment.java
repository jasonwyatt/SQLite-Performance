package co.jasonwyatt.sqliteperf.inserts;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.R;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.TestSuiteFragment;
import co.jasonwyatt.sqliteperf.inserts.tracks.BatchedTestCase;
import co.jasonwyatt.sqliteperf.inserts.tracks.RawTestCase;
import co.jasonwyatt.sqliteperf.inserts.tracks.BatchedSQLiteStatementTestCase;
import co.jasonwyatt.sqliteperf.inserts.tracks.SQLiteStatementTestCase;

/**
 * @author jason
 */

public class TracksExecSQLVsSQLiteStatementFragment extends TestSuiteFragment {
    @Override
    protected String getChartTitle() {
        return App.getInstance().getString(R.string.insert_performance_batched_vs_sqlitestatement_tracks);
    }

    @Override
    protected Map<TestScenarioMetadata, TestCase[]> getTestScenarios() {
        Map<TestScenarioMetadata, TestCase[]> result = new HashMap<>(4);

        result.put(new TestScenarioMetadata("tracks batched execSQL", 0xFFb71c1c, 10), new TestCase[]{
                new BatchedTestCase(1000, 2),
                new BatchedTestCase(10000, 3),
                new BatchedTestCase(100000, 4)
        });

        result.put(new TestScenarioMetadata("tracks SQLiteStatement", 0xFF4a148c, 10), new TestCase[]{
                new SQLiteStatementTestCase(1000, 2),
                new SQLiteStatementTestCase(10000, 3),
                new SQLiteStatementTestCase(100000, 4)
        });

        result.put(new TestScenarioMetadata("tracks batched SQLiteStatement", 0xFF7c43bd, 10), new TestCase[] {
                new BatchedSQLiteStatementTestCase(1000, 2),
                new BatchedSQLiteStatementTestCase(10000, 3),
                new BatchedSQLiteStatementTestCase(100000, 4)
        });

        return result;
    }

    @Override
    protected MetricsVariableTransformer getMetricsTransformer() {
        return new MetricsVariableTransformer() {
            @Override
            public String variableValueForDisplay(float variableValue) {
                return String.format(Locale.ENGLISH, "%d inserts", (int) Math.pow(10, variableValue+1));
            }

            @Override
            public String elapsedTimeValueForDisplay(float elapsedTimeMs) {
                return String.format(Locale.ENGLISH, "%.3fs", elapsedTimeMs / 1E9f);
            }
        };
    }
}
