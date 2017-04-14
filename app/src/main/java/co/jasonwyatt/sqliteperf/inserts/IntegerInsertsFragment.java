package co.jasonwyatt.sqliteperf.inserts;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.R;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.TestSuiteFragment;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerInsertsRawBatchCase;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerInsertsRawBatchTransactionCase;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerInsertsRawCase;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerInsertsRawTransactionCase;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerInsertsTestCase;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerInsertsTransactionCase;

/**
 * @author jason
 */

public class IntegerInsertsFragment extends TestSuiteFragment {
    @Override
    protected String getChartTitle() {
        return App.getInstance().getString(R.string.insert_performance);
    }

    @Override
    protected Map<TestSuiteFragment.TestScenarioMetadata, TestCase[]> getTestScenarios() {
        Map<TestScenarioMetadata, TestCase[]> result = new HashMap<>(4);

        result.put(new TestScenarioMetadata("db.insert(..)", 0xFFb71c1c, 1), new TestCase[]{
                new IntegerInsertsTestCase(100, 2),
                new IntegerInsertsTestCase(1000, 3),
                new IntegerInsertsTestCase(10000, 4)
        });

        result.put(new TestScenarioMetadata("db.insert(..) + trans", 0xFFf05545, 50), new TestCase[]{
                new IntegerInsertsTransactionCase(100, 2),
                new IntegerInsertsTransactionCase(1000, 3),
                new IntegerInsertsTransactionCase(10000, 4)
        });

        result.put(new TestScenarioMetadata("db.execSQL(..)", 0xFF4a148c, 1), new TestCase[]{
                new IntegerInsertsRawCase(100, 2),
                new IntegerInsertsRawCase(1000, 3),
                new IntegerInsertsRawCase(10000, 4)
        });

        result.put(new TestScenarioMetadata("db.execSQL(..) + trans", 0xFF7c43bd, 50), new TestCase[] {
                new IntegerInsertsRawTransactionCase(100, 2),
                new IntegerInsertsRawTransactionCase(1000, 3),
                new IntegerInsertsRawTransactionCase(10000, 4)
        });

        result.put(new TestScenarioMetadata("batch exec", 0xFF1b5e20, 1), new TestCase[] {
                new IntegerInsertsRawBatchCase(100, 2),
                new IntegerInsertsRawBatchCase(1000, 3),
                new IntegerInsertsRawBatchCase(10000, 4)
        });

        result.put(new TestScenarioMetadata("batch exec + trans", 0xFF4c8c4a, 50), new TestCase[] {
                new IntegerInsertsRawBatchTransactionCase(100, 2),
                new IntegerInsertsRawBatchTransactionCase(1000, 3),
                new IntegerInsertsRawBatchTransactionCase(10000, 4)
        });

        return result;
    }

    @Override
    protected TestSuiteFragment.MetricsVariableTransformer getMetricsTransformer() {
        return new MetricsVariableTransformer() {
            @Override
            public String variableValueForDisplay(float variableValue) {
                return String.format(Locale.ENGLISH, "%d inserts", (int) Math.pow(10, variableValue));
            }

            @Override
            public String elapsedTimeValueForDisplay(float elapsedTimeMs) {
                return String.format(Locale.ENGLISH, "%.3fs", elapsedTimeMs / 1000f);
            }
        };
    }
}
