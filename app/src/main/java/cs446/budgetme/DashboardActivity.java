package cs446.budgetme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cs446.budgetme.APIClient.APIUtils;
import cs446.budgetme.Adaptor.DashboardTabAdapter;
import cs446.budgetme.APIClient.GetDataService;
import cs446.budgetme.APIClient.RetrofitClient;
import cs446.budgetme.Fragement.DashboardSummaryFragment;
import cs446.budgetme.Fragement.DashboardProfileFragment;
import cs446.budgetme.Fragement.DashboardTransDetailFragment;
import cs446.budgetme.Model.Goal;
import cs446.budgetme.Model.SpendingsDataSummary;
import cs446.budgetme.Model.Transaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity
        implements DashboardSummaryFragment.OnFragmentInteractionListener, DashboardProfileFragment.OnFragmentInteractionListener,
        DashboardTransDetailFragment.OnListFragmentInteractionListener{
    private DashboardTabAdapter mAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private static final int REQUEST_CODE_ADD_TRANSACTION = 10000;
    private static final int REQUEST_CODE_GOAL_SETTING = 11000;

    private final String USER_TOKEN= "5d30ff4e6397c4000427fabe";
    private final String GroupId="5d30ff4e6397c4000427fabd";

    private APIUtils apicall;
    private SpendingsDataSummary mSpendingsDataSummary = new SpendingsDataSummary(Transaction.getFakeData());
    //need current transaction list to check if client Transaction List is the same as database
    private ArrayList<Transaction> currTrans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //get indent information
        Intent intent = getIntent();

        //setup API client
        apicall = new APIUtils();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_filter_list_white_24dp);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddTransaction();
            }
        });

        //set the pager and adapter for the fragments
        mViewPager = findViewById(R.id.dashboard_view_pager);
        mTabLayout = findViewById(R.id.dashboard_tab_layout);
        mAdapter = new DashboardTabAdapter(getSupportFragmentManager());

        //create a DashboardProfileFrag to update the user information
        Bundle bundle = new Bundle();
        bundle.putString("username", intent.getStringExtra("username"));

        DashboardProfileFragment mProfile = new DashboardProfileFragment();
        mProfile.setArguments(bundle);
        //add the fragments
        mAdapter.addFragment(new DashboardSummaryFragment(mSpendingsDataSummary), getResources().getString(R.string.title_dashboard_tab_summary));
        mAdapter.addFragment(new DashboardTransDetailFragment(mSpendingsDataSummary), "Transaction Detail");
        mAdapter.addFragment(mProfile,"Profile");

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager, true);
        mViewPager.setCurrentItem(0);

        loadTransactionList();
    }

    public void loadTransactionList(){
        Call<List<Transaction>> call = apicall.getApiInterface().getTransactionList(USER_TOKEN,GroupId );
        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }
                updateTransactions(response.body());
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void updateTransactions(List<Transaction> transactions) {
        mSpendingsDataSummary.setTransactions(transactions);
        currTrans = (ArrayList) transactions;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onListFragmentInteraction(Transaction item){}


    //addtransaction activity needs the current transationList to see whether the database list is the same as current.
    private void startAddTransaction() {
        Intent i = new Intent(this, AddTransactionActivity.class);
        i.putParcelableArrayListExtra("transactionList",currTrans );
        startActivityForResult(i, REQUEST_CODE_ADD_TRANSACTION);
    }

    public void startGoalSetting(){
        Intent i = new Intent(this, GoalSettingActivity.class);
        startActivityForResult(i, REQUEST_CODE_GOAL_SETTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        //after return back to Dashboard, want to check if there is new updated to the database


        if (requestCode == REQUEST_CODE_ADD_TRANSACTION) {
            if (resultCode == RESULT_OK) {
                Transaction transaction = (Transaction)data.getExtras().getParcelable("transaction");
                //load the new data from server
                loadTransactionList();
            //    mSpendingsDataSummary.addTransaction(transaction);
            }
        }
        else if (requestCode == REQUEST_CODE_GOAL_SETTING){
            if (resultCode == RESULT_OK) {
                Goal goal = (Goal)data.getExtras().getParcelable("goal");
                ((DashboardSummaryFragment)mAdapter.getItem(0)).onGoalAdded(goal);
            }
        }
    }

    public SpendingsDataSummary getSpendingsDataSummary() {
        return mSpendingsDataSummary;
    }


}
