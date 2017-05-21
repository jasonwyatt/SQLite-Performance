package co.jasonwyatt.sqliteperf.inserts;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.R;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.TestSuiteFragment;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerInsertsRawBatchTransactionCase;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerRoomTestCase;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerSQLiteStatementTransactionCase;
import co.jasonwyatt.sqliteperf.inserts.tracks.BatchedTestCase;
import co.jasonwyatt.sqliteperf.inserts.tracks.SQLiteStatementTestCase;
import co.jasonwyatt.sqliteperf.inserts.tracks.TracksRoomTestCase;

/**
 * @author jason
 */

public class BatchedVsRoomInsertAllFragment extends TestSuiteFragment {
    @Override
    protected String getChartTitle() {
        return App.getInstance().getString(R.string.insert_performance_batched_vs_room);
    }

    @Override
    protected Map<TestScenarioMetadata, TestCase[]> getTestScenarios() {
        Map<TestScenarioMetadata, TestCase[]> result = new HashMap<>(4);

        result.put(new TestScenarioMetadata("simple batched exec", 0xFFb71c1c, 3), new TestCase[]{
                new IntegerInsertsRawBatchTransactionCase(1000, 2),
                new IntegerInsertsRawBatchTransactionCase(10000, 3),
                new IntegerInsertsRawBatchTransactionCase(100000, 4)
        });

        result.put(new TestScenarioMetadata("simple Room insertAll", 0xFFf05545, 3), new TestCase[]{
                new IntegerRoomTestCase(1000, 2),
                new IntegerRoomTestCase(10000, 3),
                new IntegerRoomTestCase(100000, 4)
        });

        result.put(new TestScenarioMetadata("tracks batched exec", 0xFF4a148c, 2), new TestCase[] {
                new BatchedTestCase(1000, 2),
                new BatchedTestCase(10000, 3),
                new BatchedTestCase(100000, 4)
        });

        result.put(new TestScenarioMetadata("tracks Room insertAll", 0xFF7c43bd, 2), new TestCase[] {
                new TracksRoomTestCase(1000, 2),
                new TracksRoomTestCase(10000, 3),
                new TracksRoomTestCase(100000, 4)
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
                return String.format(Locale.ENGLISH, "%.3fs", elapsedTimeMs / 1e9f);
            }
        };
    }
}
