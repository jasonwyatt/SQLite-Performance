package co.jasonwyatt.sqliteperf;

import android.os.AsyncTask;
import android.support.v4.graphics.ColorUtils;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jason
 */
public class TestCaseRunner extends AsyncTask<TestCase, TestCase.Metrics, Void> {
    private final int mIterations;
    private final BarChart mChart;
    private final BarDataSet mDataSet;
    private final List<BarEntry> mEntries;
    private int mDataSetIndex;

    TestCaseRunner(int iterations, BarChart chart, String title, int color, IValueFormatter valueFormatter) {
        mIterations = iterations;
        mChart = chart;
        mEntries = new LinkedList<>();
        mDataSet = new BarDataSet(mEntries, title);
        mDataSet.setValueFormatter(valueFormatter);
        mDataSet.setColor(color);
    }

    @Override
    protected Void doInBackground(TestCase... params) {
        List<TestCase.Metrics> group = new ArrayList<>(mIterations);
        for (TestCase t : params) {
            if (isCancelled()) {
                return null;
            }
            group.clear();
            for (int i = 0; i < mIterations; i++) {
                if (isCancelled()) {
                    return null;
                }
                group.add(t.runCase());
                t.resetCase();
            }
            if (isCancelled()) {
                return null;
            }
            publishProgress(new TestCase.Metrics(group));
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(TestCase.Metrics... values) {
        super.onProgressUpdate(values);
        mDataSetIndex = mChart.getData().getIndexOfDataSet(mDataSet);
        if (mDataSetIndex < 0) {
            mChart.getData().addDataSet(mDataSet);
            mDataSetIndex = mChart.getData().getIndexOfDataSet(mDataSet);
        }

        BarData data = mChart.getData();
        for (TestCase.Metrics m : values) {
            data.addEntry(new BarEntry(m.getVariable(), (float) m.getElapsedTime()), mDataSetIndex);
        }
        if (data.getDataSetCount() > 1) {
            int count = data.getDataSetCount();
            float groupSpace = 0.06f;
            float barSpace = 0.02f;
            float numBars = count;
            float barWidth = (1.0f - ((count - 1) * barSpace) - groupSpace) / numBars;

            Collections.sort(
                    data.getDataSets(),
                    new Comparator<IBarDataSet>() {
                        @Override
                        public int compare(IBarDataSet o1, IBarDataSet o2) {
                            float[] hsl1 = new float[3];
                            float[] hsl2 = new float[3];
                            ColorUtils.colorToHSL(o1.getColor(), hsl1);
                            ColorUtils.colorToHSL(o2.getColor(), hsl2);

                            if (Math.abs(hsl1[0] - hsl2[0]) > 10) {
                                return Float.compare(hsl1[0], hsl2[0]);
                            }

                            if (Math.abs(hsl1[2] - hsl2[2]) > 0.1) {
                                return Float.compare(hsl1[2], hsl2[2]);
                            }

                            return Float.compare(hsl1[1], hsl2[1]);
                        }
                    }
            );

            data.setBarWidth(barWidth);
            data.groupBars(2, groupSpace, barSpace);
        }
        mChart.setFitBars(true);
        data.notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }
}
