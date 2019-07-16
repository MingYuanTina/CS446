package cs446.budgetme.Widgets;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cs446.budgetme.Model.Observer;
import cs446.budgetme.Model.SpendingsDataSummary;
import cs446.budgetme.Model.Transaction;

public class LineChartObserver implements Observer {
    private LineChart mLineChart;
    SpendingsDataSummary mSpendingsDataSummary;

    private class DateValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            DateFormat outputFormatter = new SimpleDateFormat("MM/dd");
            String dateWithoutTime = outputFormatter.format(new Date(new Float(value).longValue()));
            return dateWithoutTime;
        }
    }

    public LineChartObserver(LineChart lineChart, SpendingsDataSummary spendingsDataSummary) {
        mLineChart = lineChart;
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setValueFormatter(new DateValueFormatter());
        xAxis.setCenterAxisLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.getDescription().setEnabled(false);

        mSpendingsDataSummary = spendingsDataSummary;
    }

    @Override
    public void update() {
        List<Entry> entries = new ArrayList<>();
        TreeMap<Date, List<Transaction>> groupedByDay = Transaction.getTransactionsGroupedByDay(mSpendingsDataSummary.getFilteredTransactions());
        Double runningTotal = new Double(0.0);
        for (Map.Entry<Date, List<Transaction>> entry : groupedByDay.entrySet()) {
            Double total = new Double(0.0);
            for (Transaction t : entry.getValue()) {
                total += t.getCost();
            }
            runningTotal += total;
            entries.add(new Entry(new Long(entry.getKey().getTime()).floatValue(), runningTotal.floatValue()));
        }
        LineDataSet set = new LineDataSet(entries, "Running Total");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set); // add the data sets
        mLineChart.setData(new LineData(dataSets));
        mLineChart.invalidate();
    }
}
