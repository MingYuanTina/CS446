package cs446.budgetme.Widgets;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import cs446.budgetme.Model.Observer;
import cs446.budgetme.Model.Transaction;
import cs446.budgetme.Model.SpendingsDataSummary;

public class BarChartObserver implements Observer {
    private HorizontalBarChart mBarChart;
    SpendingsDataSummary mSpendingsDataSummary;
    private static final int NUM_CATEGORY = 5;
    private static final float BAR_WIDTH = 0.2f;

    public BarChartObserver(HorizontalBarChart barChart, SpendingsDataSummary spendingsDataSummary) {
        mBarChart = barChart;
        mSpendingsDataSummary = spendingsDataSummary;
    }

    @Override
    public void update() {
        ArrayList<BarEntry> precentCompeletion = new ArrayList<>();
        ArrayList<BarEntry> curExpenseEntry = new ArrayList<>();
        double goalValue[] = new double[NUM_CATEGORY];
        double curExpenseValue[] = new double[NUM_CATEGORY];

        //calculate the sum for each category
        /*for (Goal t : mSpendingsDataSummary.getGoals()) {
            goalValue[t.getCategoryId()]+=t.getCost();
        }*/
        for (Transaction t : mSpendingsDataSummary.getFilteredTransactions()) {
            curExpenseValue[t.getCategoryId()]+=t.getCost();
        }

        for (int i = 0; i < NUM_CATEGORY; i++) {
            precentCompeletion.add( new BarEntry(i, (float)(curExpenseValue[i]/goalValue[i])));
        }


        BarDataSet progressDataSet = new BarDataSet(precentCompeletion, "Goal Progress");

        progressDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData data = new BarData(progressDataSet);
        data.setBarWidth(BAR_WIDTH);
        mBarChart.getLegend().setEnabled(false);
        mBarChart.getXAxis().setEnabled(false);
        mBarChart.getAxisLeft().setAxisMaximum(1);
        //goalChartView.getAxisLeft().setAxisMinimum(1);
        mBarChart.getAxisRight().setEnabled(false);
        mBarChart.setData(data);
        mBarChart.invalidate();
    }
}
