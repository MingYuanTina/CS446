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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cs446.budgetme.Model.Observer;
import cs446.budgetme.Model.SpendingsDataSummary;
import cs446.budgetme.Model.Transaction;

public class LineChartObserver implements Observer {
    private LineChart mLineChart;
    private DateValueFormatter mDateValueFormatter;
    SpendingsDataSummary mSpendingsDataSummary;

    private class DateValueFormatter extends ValueFormatter {
        private Calendar mCalendar = Calendar.getInstance();

        public DateValueFormatter() {
            super();
        }

        public void setStartingDate(Date startDate) {
            mCalendar.setTime(startDate);
            mCalendar.set(Calendar.HOUR_OF_DAY, 0);
            mCalendar.set(Calendar.MINUTE, 0);
            mCalendar.set(Calendar.SECOND, 0);
            mCalendar.set(Calendar.MILLISECOND, 0);
        }
        @Override
        public String getFormattedValue(float value) {
            DateFormat outputFormatter = new SimpleDateFormat("MM/dd");
            mCalendar.add(Calendar.DATE, (int)value);
            String dateWithoutTime = outputFormatter.format(mCalendar.getTime());
            mCalendar.add(Calendar.DATE, -(int)value);
            return dateWithoutTime;
        }
    }

    public LineChartObserver(LineChart lineChart, SpendingsDataSummary spendingsDataSummary) {
        mLineChart = lineChart;
        XAxis xAxis = mLineChart.getXAxis();
        mDateValueFormatter = new DateValueFormatter();
        xAxis.setValueFormatter(mDateValueFormatter);
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

        Date firstEntryDate = groupedByDay.firstEntry().getKey();
        Date lastEntry = groupedByDay.lastEntry().getKey();

        int dayOffset = 0;
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(firstEntryDate);
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);
        while (!currentCalendar.getTime().after(lastEntry)) {
            if (groupedByDay.get(currentCalendar.getTime()) != null) {
                Double total = new Double(0.0);
                for (Transaction t : groupedByDay.get(currentCalendar.getTime())) {
                    total += t.getCost();
                }
                runningTotal += total;
            }
            entries.add(new Entry(dayOffset, runningTotal.floatValue()));
            currentCalendar.add(Calendar.DATE, 1);
            dayOffset += 1;
        }
        mLineChart.getXAxis().setLabelCount(dayOffset, true);
        mDateValueFormatter.setStartingDate(firstEntryDate);
/*
        for (Map.Entry<Date, List<Transaction>> entry : groupedByDay.entrySet()) {
            Double total = new Double(0.0);
            for (Transaction t : entry.getValue()) {
                total += t.getCost();
            }
            runningTotal += total;
            entries.add(new Entry(new Long(entry.getKey().getTime()).floatValue(), runningTotal.floatValue()));
        }*/
        LineDataSet set = new LineDataSet(entries, "Cumulative Spendings");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set); // add the data sets
        mLineChart.setData(new LineData(dataSets));
        mLineChart.invalidate();
    }
}
