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
    private LineChartUtils.DateValueFormatter mDateValueFormatter;
    SpendingsDataSummary mSpendingsDataSummary;


    public LineChartObserver(LineChart lineChart, SpendingsDataSummary spendingsDataSummary) {
        mLineChart = lineChart;
        XAxis xAxis = mLineChart.getXAxis();
        mDateValueFormatter = new LineChartUtils.DateValueFormatter();
        xAxis.setValueFormatter(mDateValueFormatter);
        xAxis.setCenterAxisLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.getDescription().setEnabled(false);

        mSpendingsDataSummary = spendingsDataSummary;
    }

    @Override
    public void update() {
        LineChartUtils.setDailyDataFromTransactions(mLineChart, mSpendingsDataSummary.getFilteredTransactions(), mDateValueFormatter, "Cumulative Spendings");
    }
}
