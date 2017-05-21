package co.jasonwyatt.sqliteperf.blobs;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import co.jasonwyatt.sqliteperf.App;
import co.jasonwyatt.sqliteperf.R;
import co.jasonwyatt.sqliteperf.TestCase;
import co.jasonwyatt.sqliteperf.TestSuiteFragment;
import co.jasonwyatt.sqliteperf.blobs.cases.QueryTestCase;
import co.jasonwyatt.sqliteperf.blobs.cases.RawQueryTestCase;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerInsertsTestCase;
import co.jasonwyatt.sqliteperf.inserts.integers.IntegerInsertsTransactionCase;
import co.jasonwyatt.sqliteperf.inserts.tracks.InsertsTestCase;
import co.jasonwyatt.sqliteperf.inserts.tracks.InsertsTransactionTestCase;

/**
 * @author jason
 */

public class SelectsFragment extends TestSuiteFragment {
    @Override
    protected String getChartTitle() {
        return App.getInstance().getString(R.string.select_blob_performance_50k);
    }

    @Override
    protected Map<TestScenarioMetadata, TestCase[]> getTestScenarios() {
        Map<TestScenarioMetadata, TestCase[]> result = new HashMap<>(4);

        result.put(new TestScenarioMetadata("db.query", 0xFFb71c1c, 10), new TestCase[]{
                new QueryTestCase(10*1024, 100, 2),
                new QueryTestCase(100*1024, 100, 3),
                new QueryTestCase(1000*1024, 100, 4),
        });

        result.put(new TestScenarioMetadata("db.rawQuery", 0xFFf05545, 10), new TestCase[]{
                new RawQueryTestCase(10*1024, 100, 2),
                new RawQueryTestCase(100*1024, 100, 3),
                new RawQueryTestCase(1000*1024, 100, 4),
        });

        return result;
    }

    @Override
    protected MetricsVariableTransformer getMetricsTransformer() {
        return new MetricsVariableTransformer() {
            @Override
            public String variableValueForDisplay(float variableValue) {
                return String.format(Locale.ENGLISH, "%d kb", (int) Math.pow(10, variableValue+1));
            }

            @Override
            public String elapsedTimeValueForDisplay(float elapsedTimeMs) {
                return String.format(Locale.ENGLISH, "%.3fs", elapsedTimeMs / 1e9f);
            }
        };
    }
}
