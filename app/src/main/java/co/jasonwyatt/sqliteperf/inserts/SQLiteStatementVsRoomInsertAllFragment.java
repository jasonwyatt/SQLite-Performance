package co.jasonwyatt.sqliteperf.inserts;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.R;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.TestSuiteFragment;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerRoomTestCase;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerSQLiteStatementTransactionCase;
import co.jasonwyatt.sqliteperf.inserts.tracks.SQLiteStatementTestCase;
import co.jasonwyatt.sqliteperf.inserts.tracks.TracksRoomTestCase;

/**
 * @author jason
 */

public class SQLiteStatementVsRoomInsertAllFragment extends TestSuiteFragment {
    @Override
    protected String getChartTitle() {
        return App.getInstance().getString(R.string.insert_performance_sqlitestatement_vs_room);
    }

    @Override
    protected Map<TestScenarioMetadata, TestCase[]> getTestScenarios() {
        Map<TestScenarioMetadata, TestCase[]> result = new HashMap<>(4);

        result.put(new TestScenarioMetadata("simple SQLiteStatement", 0xFFb71c1c, 3), new TestCase[]{
                new IntegerSQLiteStatementTransactionCase(1000, 2),
                new IntegerSQLiteStatementTransactionCase(10000, 3),
                new IntegerSQLiteStatementTransactionCase(100000, 4)
        });

        result.put(new TestScenarioMetadata("simple Room insertAll", 0xFFf05545, 3), new TestCase[]{
                new IntegerRoomTestCase(1000, 2),
                new IntegerRoomTestCase(10000, 3),
                new IntegerRoomTestCase(100000, 4)
        });

        result.put(new TestScenarioMetadata("tracks SQLiteStatement", 0xFF4a148c, 3), new TestCase[] {
                new SQLiteStatementTestCase(1000, 2),
                new SQLiteStatementTestCase(10000, 3),
                new SQLiteStatementTestCase(100000, 4)
        });

        result.put(new TestScenarioMetadata("tracks Room insertAll", 0xFF7c43bd, 3), new TestCase[] {
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
