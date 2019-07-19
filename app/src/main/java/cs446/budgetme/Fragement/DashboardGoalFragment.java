package cs446.budgetme.Fragement;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cs446.budgetme.Adaptor.DashboardGoalRecyclerViewAdapter;
import cs446.budgetme.Model.Goal;
import cs446.budgetme.Model.Observer;
import cs446.budgetme.Model.SpendingsDataSummary;
import cs446.budgetme.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardGoalFragment extends Fragment implements Observer {

    private SpendingsDataSummary mSpendingsDataSummary;

    private OnListFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private int mColumnCount = 1;

    public DashboardGoalFragment() {
        // Required empty public constructor
    }

    public DashboardGoalFragment(SpendingsDataSummary spendingsDataSummary) {
        mSpendingsDataSummary = spendingsDataSummary;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DashboardGoalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardGoalFragment newInstance() {
        DashboardGoalFragment fragment = new DashboardGoalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_transaction_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mSpendingsDataSummary.register(this);
            update();
        }
        return view;
    }

    @Override
    public void update() {
        mRecyclerView.setAdapter(new DashboardGoalRecyclerViewAdapter(mSpendingsDataSummary.getGoals(), mSpendingsDataSummary.getTransactions(), mListener));
        mRecyclerView.invalidate();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        //addMenuItems(menu);
    }

    protected void addMenuItems(Menu menu) {
        //menu.add(0, MENU_DATE_ID, Menu.NONE, R.string.menu_date).setIcon(R.drawable.ic_date_range_white_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //menu.add(0, MENU_TRANSACTION_CATEGORIES_ID, Menu.NONE, R.string.menu_filter).setIcon(R.drawable.ic_filter_list_white_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
        //return handleMenuItemSelected(id);
    }
/*
    protected boolean handleMenuItemSelected(int id) {
        if (id == MENU_DATE_ID) {
            handleDateMenuItem();
            return true;
        } else if (id == MENU_TRANSACTION_CATEGORIES_ID) {
            handleTransactionCategoriesMenuItem();
            return true;
        }
        return false;
    }

    private void handleDateMenuItem() {
        final DateRangePicker dateRangePicker = new DateRangePicker(getContext(),
                mSpendingsDataSummary.getStartDate(), mSpendingsDataSummary.getEndDate(), new DateRangePicker.OnCalenderClickListener() {
            @Override
            public void onDateSelected(Calendar selectedStartDate, Calendar selectedEndDate) {
                mSpendingsDataSummary.setDateFilters(selectedStartDate, selectedEndDate);
            }
        });
        dateRangePicker.show();
    }

    private void handleTransactionCategoriesMenuItem() {
        mTransactionCategoryFilterDialog.show();
    }*/


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
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Goal goal);
    }
}
