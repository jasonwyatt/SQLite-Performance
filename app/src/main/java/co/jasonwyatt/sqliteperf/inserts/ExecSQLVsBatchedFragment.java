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
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerInsertsTransactionCase;
import co.jasonwyatt.sqliteperf.inserts.tracks.BatchedTestCase;
import co.jasonwyatt.sqliteperf.inserts.tracks.InsertsTransactionTestCase;
import co.jasonwyatt.sqliteperf.inserts.tracks.RawTestCase;

/**
 * @author jason
 */

public class ExecSQLVsBatchedFragment extends TestSuiteFragment {
    @Override
    protected String getChartTitle() {
        return App.getInstance().getString(R.string.insert_performance_one_by_one_vs_batched);
    }

    @Override
    protected Map<TestScenarioMetadata, TestCase[]> getTestScenarios() {
        Map<TestScenarioMetadata, TestCase[]> result = new HashMap<>(4);

        result.put(new TestScenarioMetadata("simple one-by-one exec", 0xFFb71c1c, 1), new TestCase[]{
                new IntegerInsertsRawTransactionCase(1000, 2),
                new IntegerInsertsRawTransactionCase(10000, 3),
                new IntegerInsertsRawTransactionCase(100000, 4)
        });

        result.put(new TestScenarioMetadata("simple batched exec", 0xFFf05545, 1), new TestCase[]{
                new IntegerInsertsRawBatchTransactionCase(1000, 2),
                new IntegerInsertsRawBatchTransactionCase(10000, 3),
                new IntegerInsertsRawBatchTransactionCase(100000, 4)
        });

        result.put(new TestScenarioMetadata("tracks one-by-one exec", 0xFF4a148c, 1), new TestCase[] {
                new RawTestCase(1000, 2),
                new RawTestCase(10000, 3),
                new RawTestCase(100000, 4)
        });

        result.put(new TestScenarioMetadata("tracks batched exec", 0xFF7c43bd, 1), new TestCase[] {
                new BatchedTestCase(1000, 2),
                new BatchedTestCase(10000, 3),
                new BatchedTestCase(100000, 4)
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
