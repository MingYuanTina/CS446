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
        private int mTimeDivision;

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

        public void setTimeDivision(int timeDivision) {
            mTimeDivision = timeDivision;
        }

        @Override
        public String getFormattedValue(float value) {
            DateFormat outputFormatter = new SimpleDateFormat("MM/dd");
            mCalendar.add(Calendar.DATE, mTimeDivision * (int)value);
            String dateWithoutTime = outputFormatter.format(mCalendar.getTime());
            mCalendar.add(Calendar.DATE, -mTimeDivision * (int)value);
            return dateWithoutTime;
        }
    }

    private static final long MILLISECONDS_10_DAYS = 864000000L;
    private static final long MILLISECONDS_10_WEEKS = 6048000000L;
    private static final long MILLISECONDS_10_MONTHS = 25920000000L;


    public static Double setTransactionsByAppropriateInterval(LineChart lineChart, List<Transaction> transactions, DateValueFormatter dateValueFormatter, String title) {
        List<Entry> entries = new ArrayList<>();
        Double runningTotal = new Double(0.0);
        if (!transactions.isEmpty()) {
            TreeMap<Date, List<Transaction>> groupedByDay = Transaction.getTransactionsGroupedByDay(transactions);
            // Handle null case here.
            Date firstEntryDate = groupedByDay.firstEntry().getKey();
            Date lastEntryDate = groupedByDay.lastEntry().getKey();

            long timeDifference = lastEntryDate.getTime() - firstEntryDate.getTime();

            int index = 0;
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(firstEntryDate);
            // DateUtils.setCalendarToBeginningOfDay(currentCalendar);

            if (timeDifference <= MILLISECONDS_10_DAYS) {
                while (!currentCalendar.getTime().after(lastEntryDate)) {
                    if (groupedByDay.get(currentCalendar.getTime()) != null) {
                        Double total = new Double(0.0);
                        for (Transaction t : groupedByDay.get(currentCalendar.getTime())) {
                            total += t.getCost();
                        }
                        runningTotal += total;
                    }
                    entries.add(new Entry(index, runningTotal.floatValue()));
                    currentCalendar.add(Calendar.DATE, 1);
                    index += 1;
                }
                dateValueFormatter.setTimeDivision(1);
            } else if (timeDifference <= MILLISECONDS_10_WEEKS) {
                while (!currentCalendar.getTime().after(lastEntryDate)) {
                    for (int i = 0; i < 7; i++) {
                        if (groupedByDay.get(currentCalendar.getTime()) != null) {
                            Double total = new Double(0.0);
                            for (Transaction t : groupedByDay.get(currentCalendar.getTime())) {
                                total += t.getCost();
                            }
                            runningTotal += total;
                        }
                        currentCalendar.add(Calendar.DATE, 1);
                    }
                    entries.add(new Entry(index, runningTotal.floatValue()));
                    index += 1;
                }
                dateValueFormatter.setTimeDivision(7);
            } else {
                while (!currentCalendar.getTime().after(lastEntryDate)) {
                    for (int i = 0; i < 30; i++) {
                        if (groupedByDay.get(currentCalendar.getTime()) != null) {
                            Double total = new Double(0.0);
                            for (Transaction t : groupedByDay.get(currentCalendar.getTime())) {
                                total += t.getCost();
                            }
                            runningTotal += total;
                        }
                        currentCalendar.add(Calendar.DATE, 1);
                    }
                    entries.add(new Entry(index, runningTotal.floatValue()));
                    index += 1;
                }
                dateValueFormatter.setTimeDivision(30);
            }
            dateValueFormatter.setStartingDate(firstEntryDate);
            lineChart.getXAxis().setLabelCount(index, true);
        }

        LineDataSet set = new LineDataSet(entries, title);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set); // add the data sets
        lineChart.setData(new LineData(dataSets));
        lineChart.getAxisRight().setEnabled(false);
        lineChart.invalidate();

        return runningTotal;
    }

    public static void setLimitLine(LineChart lineChart, Double limit, Double totalAmount) {
        LimitLine ll1 = new LimitLine(limit.floatValue(), "Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        yAxis.addLimitLine(ll1);

        if (limit > totalAmount) {
            yAxis.setAxisMaximum(limit.floatValue());
        }
    }
}
