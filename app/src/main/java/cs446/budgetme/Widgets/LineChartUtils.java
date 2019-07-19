package cs446.budgetme.Widgets;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import cs446.budgetme.Model.Transaction;
import cs446.budgetme.Utils.DateUtils;

public class LineChartUtils {
    public static class DateValueFormatter extends ValueFormatter {
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

    public static void setDailyDataFromTransactions(LineChart lineChart, List<Transaction> transactions, DateValueFormatter dateValueFormatter, String title) {
        List<Entry> entries = new ArrayList<>();
        if (!transactions.isEmpty()) {
            TreeMap<Date, List<Transaction>> groupedByDay = Transaction.getTransactionsGroupedByDay(transactions);
            Double runningTotal = new Double(0.0);

            // Handle null case here.
            Date firstEntryDate = groupedByDay.firstEntry().getKey();
            Date lastEntry = groupedByDay.lastEntry().getKey();

            int dayOffset = 0;
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(firstEntryDate);
            // DateUtils.setCalendarToBeginningOfDay(currentCalendar);

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
            lineChart.getXAxis().setLabelCount(dayOffset, true);
            dateValueFormatter.setStartingDate(firstEntryDate);
        }
        LineDataSet set = new LineDataSet(entries, title);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set); // add the data sets
        lineChart.setData(new LineData(dataSets));
        lineChart.invalidate();
    }

    public static void setLimitLine(LineChart lineChart, Double limit) {
        LimitLine ll1 = new LimitLine(limit.floatValue(), "Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        yAxis.addLimitLine(ll1);
    }
}
