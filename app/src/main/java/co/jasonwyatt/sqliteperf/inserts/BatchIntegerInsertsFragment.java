package co.jasonwyatt.sqliteperf.inserts;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.R;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.TestSuiteFragment;

/**
 * @author jason
 */

public class BatchIntegerInsertsFragment extends TestSuiteFragment {
    @Override
    protected String getChartTitle() {
        return App.getInstance().getString(R.string.insert_performance_batch_only);
    }

    @Override
    protected Map<TestScenarioMetadata, TestCase[]> getTestScenarios() {
        Map<TestScenarioMetadata, TestCase[]> result = new HashMap<>(4);

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
    protected MetricsVariableTransformer getMetricsTransformer() {
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
