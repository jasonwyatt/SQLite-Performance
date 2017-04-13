package co.jasonwyatt.sqliteperf;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author jason
 */

public abstract class TestSuiteFragment extends Fragment {
    private BarChart mChart;
    private Button mButton;
    private List<TestCaseRunner> mRunners = new LinkedList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.suite_fragment, container, false);

        mChart = (BarChart) v.findViewById(R.id.chart);
        mChart.setNoDataText(null);
        mChart.setNoDataTextColor(0xFF000000);
        mChart.setFitBars(true);

        return v;
    }

    public void runTests() {
        for (TestCaseRunner runner : mRunners) {
            runner.cancel(true);
        }
        mRunners.clear();

        mChart.clear();

        BarData data = new BarData();
        mChart.setData(data);

        MetricsVariableAxisFormatter formatter = new MetricsVariableAxisFormatter(getMetricsTransformer());

        setupYAxes(mChart);
        setupXAxis(mChart, formatter);
        setupDescription(mChart);

        Legend legend = mChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setWordWrapEnabled(true);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        mChart.invalidate();

        Map<TestScenarioMetadata, TestCase[]> scenarios = getTestScenarios();
        for (Map.Entry<TestScenarioMetadata, TestCase[]> scenario : scenarios.entrySet()) {
            TestScenarioMetadata d = scenario.getKey();

            TestCaseRunner r = new TestCaseRunner(d.iterations, mChart, d.title, d.color, formatter);
            r.executeOnExecutor(TestCaseRunner.THREAD_POOL_EXECUTOR, scenario.getValue());
            mRunners.add(r);
        }
    }

    protected abstract String getChartTitle();
    protected abstract Map<TestScenarioMetadata,TestCase[]> getTestScenarios();
    protected abstract MetricsVariableTransformer getMetricsTransformer();

    private void setupDescription(Chart chart) {
        chart.setDescription(null);
    }

    private void setupYAxes(BarLineChartBase chart) {
        YAxis y = chart.getAxisLeft();
        y.setDrawZeroLine(true);
        y.setDrawLabels(false);
        y.setDrawGridLines(false);
        y.setDrawAxisLine(false);
        y.setAxisMinimum(0);
        y = chart.getAxisRight();
        y.setDrawZeroLine(true);
        y.setDrawLabels(false);
        y.setDrawGridLines(false);
        y.setDrawAxisLine(false);
        y.setAxisMinimum(0);
    }

    private void setupXAxis(BarLineChartBase chart, IAxisValueFormatter formatter) {
        XAxis x = chart.getXAxis();
        x.setGranularity(1.0f);
        x.setDrawGridLines(false);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setDrawAxisLine(false);
        x.setCenterAxisLabels(true);
        x.setAxisMinimum(2.0f);
        x.setAxisMaximum(5.0f);
        x.setValueFormatter(formatter);
    }

    protected static class TestScenarioMetadata {
        final String title;
        final int color;
        final int iterations;

        public TestScenarioMetadata(String title, int color, int iterations) {
            this.title = title;
            this.color = color;
            this.iterations = iterations;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestScenarioMetadata && title.equals(((TestScenarioMetadata) obj).title);
        }

        @Override
        public int hashCode() {
            return title.hashCode();
        }
    }

    protected interface MetricsVariableTransformer {
        String variableValueForDisplay(float variableValue);
        String elapsedTimeValueForDisplay(float elapsedTimeMs);
    }

    private static class MetricsVariableAxisFormatter implements IAxisValueFormatter, IValueFormatter {
        private final MetricsVariableTransformer mTransformer;

        MetricsVariableAxisFormatter(MetricsVariableTransformer transformer) {
            mTransformer = transformer;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mTransformer.variableValueForDisplay(value);
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mTransformer.elapsedTimeValueForDisplay(value);
        }
    }
}
