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

public class OneByOneIntegerInsertsFragment extends TestSuiteFragment {
    @Override
    protected String getChartTitle() {
        return App.getInstance().getString(R.string.insert_performance_one_by_one);
    }

    @Override
    protected Map<TestScenarioMetadata, TestCase[]> getTestScenarios() {
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
