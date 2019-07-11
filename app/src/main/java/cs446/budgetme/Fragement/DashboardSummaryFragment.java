package cs446.budgetme.Fragement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;

import cs446.budgetme.Model.Goal;
import cs446.budgetme.Model.Transaction;
import cs446.budgetme.Model.SpendingsDataSummary;
import cs446.budgetme.R;
import cs446.budgetme.Widgets.BarChartObserver;
import cs446.budgetme.Widgets.PieChartObserver;


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

    private SpendingsDataSummary mSpendingsDataSummary;

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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mSpendingsDataSummary = new SpendingsDataSummary(Transaction.getFakeData());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mSpendingsDataSummary.register(new PieChartObserver((PieChart)getView().findViewById(R.id.summary_pie_chart), mSpendingsDataSummary));
        mSpendingsDataSummary.register(new BarChartObserver((HorizontalBarChart)getView().findViewById(R.id.summary_goal_chart), mSpendingsDataSummary));
        mSpendingsDataSummary.notifyObservers();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_summary, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_summary_filter:
                // do stuff, like showing settings fragment
                return true;
        }

        return super.onOptionsItemSelected(item); // important line
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
        mSpendingsDataSummary.addTransaction(transaction);
    }

    public void onGoalAdded(Goal goal) {
        mSpendingsDataSummary.addGoal(goal);
    }
}
