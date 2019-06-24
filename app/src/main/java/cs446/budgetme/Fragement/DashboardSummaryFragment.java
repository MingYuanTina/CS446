package cs446.budgetme.Fragement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs446.budgetme.R;
import cs446.budgetme.Model.Transaction;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardSummaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardSummaryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int NUM_CATEGORY = 5;
    private static final float BAR_WIDTH = 0.2f;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Transaction> mTransactions;
    private List<Transaction> mGoal;
    private PieChart mPieView;
    private HorizontalBarChart goalChartView;

    private OnFragmentInteractionListener mListener;

    public DashboardSummaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardSummaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardSummaryFragment newInstance(String param1, String param2) {
        DashboardSummaryFragment fragment = new DashboardSummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mTransactions = Transaction.getFakeData();
        mGoal = Transaction.getFakeData();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mPieView = getView().findViewById(R.id.summary_pie_chart);
        goalChartView = getView().findViewById(R.id.summary_goal_chart);
        updateCharts();
        updateGoalChart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_dashboard_summary, container, false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onTransactionAdded(Transaction transaction) {
        mTransactions.add(transaction);
        updateCharts();
        updateGoalChart();
    }

    public void onGoalAdded(Transaction transaction) {
        mGoal.add(transaction);
        updateGoalChart();
    }
    private void updateCharts() {

        //updated Pie Chart
        List<PieEntry> data = new ArrayList<>();
        HashMap<String, Double> map = new HashMap<>();
        for (Transaction t : mTransactions) {
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

        mPieView.setData(pieData);
        mPieView.invalidate();
    }

    private void updateGoalChart() {

        ArrayList<BarEntry> precentCompeletion = new ArrayList<>();
        ArrayList<BarEntry> curExpenseEntry = new ArrayList<>();
        double goalValue[] = new double[NUM_CATEGORY];
        double curExpenseValue[] = new double[NUM_CATEGORY];

        //calculate the sum for each category
        for (Transaction t : mGoal) {
            goalValue[t.getCategoryId()]+=t.getCost();
        }
        for (Transaction t : mTransactions) {
            curExpenseValue[t.getCategoryId()]+=t.getCost();
        }

        for (int i = 0; i < NUM_CATEGORY; i++) {
            precentCompeletion.add( new BarEntry(i, (float)(curExpenseValue[i]/goalValue[i])));
        }


        BarDataSet progressDataSet = new BarDataSet(precentCompeletion, "Goal Progress");

        progressDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData data = new BarData(progressDataSet);
        data.setBarWidth(BAR_WIDTH);
        goalChartView.getLegend().setEnabled(false);
        goalChartView.getXAxis().setEnabled(false);
        goalChartView.getAxisLeft().setAxisMaximum(1);
        //goalChartView.getAxisLeft().setAxisMinimum(1);
        goalChartView.getAxisRight().setEnabled(false);
        goalChartView.setData(data);
        goalChartView.invalidate();
    }
}
