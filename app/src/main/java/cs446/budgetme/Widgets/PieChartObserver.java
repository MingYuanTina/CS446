package cs446.budgetme.Widgets;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs446.budgetme.Model.Observer;
import cs446.budgetme.Model.Subject;
import cs446.budgetme.Model.Transaction;
import cs446.budgetme.Model.TransactionsDataSummary;

public class PieChartObserver implements Observer {
    private PieChart mPieChart;
    TransactionsDataSummary mTransactionsDataSummary;

    public PieChartObserver(PieChart pieChart, TransactionsDataSummary transactionsDataSummary) {
        mPieChart = pieChart;
        mTransactionsDataSummary = transactionsDataSummary;
    }

    @Override
    public void update() {
        List<PieEntry> data = new ArrayList<>();
        HashMap<String, Double> map = new HashMap<>();
        for (Transaction t : mTransactionsDataSummary.getTransactions()) {
            if (map.containsKey(t.getCategoryName())) {
                map.put(t.getCategoryName(), map.get(t.getCategoryName()) + t.getCost());
            } else {
                map.put(t.getCategoryName(), t.getCost());
            }
        }
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            data.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
        }
        PieDataSet dataSet = new PieDataSet(data, "Category Spending");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(dataSet);

        mPieChart.setData(pieData);
        mPieChart.invalidate();
    }
}
