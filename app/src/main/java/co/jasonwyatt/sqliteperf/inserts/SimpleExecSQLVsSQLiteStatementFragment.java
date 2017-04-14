package co.jasonwyatt.sqliteperf.inserts;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.R;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.TestSuiteFragment;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerInsertsRawBatchTransactionCase;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerInsertsRawTransactionCase;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerSQLiteStatementBatchTransactionCase;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerSQLiteStatementTransactionCase;
import co.jasonwyatt.sqliteperf.inserts.tracks.BatchedTestCase;
import co.jasonwyatt.sqliteperf.inserts.tracks.RawTestCase;

/**
 * @author jason
 */

public class SimpleExecSQLVsSQLiteStatementFragment extends TestSuiteFragment {
    @Override
    protected String getChartTitle() {
        return App.getInstance().getString(R.string.insert_performance_batched_vs_sqlitestatement_simple);
    }

    @Override
    protected Map<TestScenarioMetadata, TestCase[]> getTestScenarios() {
        Map<TestScenarioMetadata, TestCase[]> result = new HashMap<>(4);
        result.put(new TestScenarioMetadata("simple execSQL", 0xFFb71c1c, 10), new TestCase[]{
                new IntegerInsertsRawTransactionCase(1000, 2),
                new IntegerInsertsRawTransactionCase(10000, 3),
                new IntegerInsertsRawTransactionCase(100000, 4)
        });

        result.put(new TestScenarioMetadata("simple batched execSQL", 0xFFf05545, 10), new TestCase[]{
                new IntegerInsertsRawBatchTransactionCase(1000, 2),
                new IntegerInsertsRawBatchTransactionCase(10000, 3),
                new IntegerInsertsRawBatchTransactionCase(100000, 4)
        });

        result.put(new TestScenarioMetadata("simple SQLiteStatement", 0xFF4a148c, 10), new TestCase[]{
                new IntegerSQLiteStatementTransactionCase(1000, 2),
                new IntegerSQLiteStatementTransactionCase(10000, 3),
                new IntegerSQLiteStatementTransactionCase(100000, 4)
        });

        result.put(new TestScenarioMetadata("simple batched SQLiteStatement", 0xFF7c43bd, 10), new TestCase[] {
                new IntegerSQLiteStatementBatchTransactionCase(1000, 2),
                new IntegerSQLiteStatementBatchTransactionCase(10000, 3),
                new IntegerSQLiteStatementBatchTransactionCase(100000, 4)
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
                return String.format(Locale.ENGLISH, "%.3fs", elapsedTimeMs / 1000f);
            }
        };
    }
}
