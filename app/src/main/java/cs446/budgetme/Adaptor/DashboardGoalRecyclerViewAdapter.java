package cs446.budgetme.Adaptor;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;

import cs446.budgetme.APIClient.APIUtils;

import cs446.budgetme.Fragement.DashboardGoalFragment;
import cs446.budgetme.Model.Goal;
import cs446.budgetme.Model.SpendingsDataSummary;
import cs446.budgetme.Model.Transaction;
import cs446.budgetme.R;
import cs446.budgetme.Widgets.LineChartUtils;
import cs446.budgetme.Fragement.DashboardGoalFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DashboardGoalRecyclerViewAdapter extends RecyclerView.Adapter<DashboardGoalRecyclerViewAdapter.ViewHolder> {

    private final List<Goal> mGoals;
    private final List<Transaction> mTransactions;
    private final OnListFragmentInteractionListener mListener;
    private APIUtils apicall;

    public DashboardGoalRecyclerViewAdapter(List<Goal> goals, List<Transaction> transactions, OnListFragmentInteractionListener listener) {
        mGoals = goals;
        mTransactions = transactions;
        mListener = listener;
        apicall = new APIUtils();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_goalview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mGoal = mGoals.get(position);
        LineChartUtils.DateValueFormatter dateValueFormatter = new LineChartUtils.DateValueFormatter();

        XAxis xAxis = holder.mLineChart.getXAxis();
        xAxis.setValueFormatter(dateValueFormatter);
        xAxis.setCenterAxisLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        holder.mLineChart.getDescription().setEnabled(false);

        // mTransactions

        SpendingsDataSummary spendingsDataSummary = new SpendingsDataSummary(mTransactions);
        spendingsDataSummary.setCategoryFilters(holder.mGoal.getCategories());
        spendingsDataSummary.setDateFiltersFromDates(holder.mGoal.getStartDate(), holder.mGoal.getEndDate());
        LineChartUtils.setDailyDataFromTransactions(holder.mLineChart, spendingsDataSummary.getFilteredTransactions(), dateValueFormatter, holder.mGoal.toString());
        LineChartUtils.setLimitLine(holder.mLineChart, holder.mGoal.getLimit());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mGoal);
                }
            }
        });

    }
    public void delete(int position) { //removes the row
        // TODO: implement delete goal
        //first send delete request to server
        //setup API client
        // TODO FIX UPDATE should reload the list of goals, fix in trans detail adapter too
        apicall.deleteGoal(mGoals.get(position));
        mGoals.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mGoals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final LineChart mLineChart;
        public Goal mGoal;
        public ImageButton mDeleteButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mLineChart = view.findViewById(R.id.goal_line_chart);
            mDeleteButton = view.findViewById(R.id.deleteButton);

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(getAdapterPosition());
                }
            });
        }

        @Override
        public String toString() {
            return mGoal.toString();
        }
    }
}
