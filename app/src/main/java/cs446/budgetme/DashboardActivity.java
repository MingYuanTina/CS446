package cs446.budgetme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import cs446.budgetme.APIClient.APIUtils;
import cs446.budgetme.Adaptor.DashboardTabAdapter;
import cs446.budgetme.Fragement.DashboardGoalFragment;
import cs446.budgetme.Fragement.DashboardSummaryFragment;
import cs446.budgetme.Fragement.DashboardProfileFragment;
import cs446.budgetme.Fragement.DashboardTransDetailFragment;
import cs446.budgetme.Model.Goal;
import cs446.budgetme.Model.SpendingsDataSummary;
import cs446.budgetme.Model.Transaction;
import cs446.budgetme.Model.TransactionCategory;
import cs446.budgetme.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity
        implements DashboardSummaryFragment.OnFragmentInteractionListener, DashboardProfileFragment.OnFragmentInteractionListener,
        DashboardTransDetailFragment.OnListFragmentInteractionListener, DashboardGoalFragment.OnListFragmentInteractionListener {
    private DashboardTabAdapter mAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private static final int REQUEST_CODE_ADD_TRANSACTION = 10000;
    private static final int REQUEST_CODE_GOAL_SETTING = 11000;

    private String USER_TOKEN;
    private String GroupId;

    private APIUtils apicall;
    private SpendingsDataSummary mSpendingsDataSummary = new SpendingsDataSummary();
    //need current transaction list to check if client Transaction List is the same as database
    private ArrayList<Transaction> currTrans;
    private ArrayList<Goal> currGoals;
    private User mUser;

    private static final String GOAL_NOTIFICATION_CHANNEL = "GOAL_NOTIFICATION_CHANNEL";

    private boolean firstLoad = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //get indent information
        Intent intent = getIntent();
        mUser = intent.getExtras().getParcelable("user");

        //set up the group id and user token for all the api calls
        GroupId= mUser.getDefaultGroupId();
        USER_TOKEN= mUser.getUserAuthToken();

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
     //   Bundle bundle = new Bundle();
//        DashboardProfileFragment mProfile = ;
//        mProfile.setArguments(bundle);
        //add the fragments
        mAdapter.addFragment(new DashboardSummaryFragment(mSpendingsDataSummary), getResources().getString(R.string.title_dashboard_tab_summary));
        mAdapter.addFragment(new DashboardGoalFragment(mSpendingsDataSummary, mUser), "Goals");
        mAdapter.addFragment(new DashboardTransDetailFragment(mSpendingsDataSummary, mUser), "Transaction Detail");
        mAdapter.addFragment(new DashboardProfileFragment(mUser),"Profile");

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager, true);
        mViewPager.setCurrentItem(0);

        loadCategoryList();
        loadTransactionList();
        loadGoalList();
    }

    public void loadCategoryList() {
        Call<List<TransactionCategory>> call = apicall.getApiInterface().getCategoryList(USER_TOKEN,GroupId );
        call.enqueue(new Callback<List<TransactionCategory>>() {
            @Override
            public void onResponse(Call<List<TransactionCategory>> call, Response<List<TransactionCategory>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }
                updateTransactionCategoryList(response.body());
            }

            @Override
            public void onFailure(Call<List<TransactionCategory>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public void updateTransactionCategoryList(List<TransactionCategory> transactionCategories) {
        mSpendingsDataSummary.setTransactionCategories(transactionCategories);
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

    public void updateUserDefaultGroup(int index){
        //update the user
        GroupId = mUser.getGroupList().get(index).getGroupId();
        mUser.setDefaultGroupId(GroupId);

        //passUserGroupUpdate
        DashboardProfileFragment mProfile= (DashboardProfileFragment)mAdapter.getItem(3);
        mProfile.setDefaultGroup(GroupId);

        DashboardTransDetailFragment mTransFrag = (DashboardTransDetailFragment)mAdapter.getItem(2);
        mTransFrag.setDefaultGroup(GroupId);

        DashboardGoalFragment mGoalFrag = (DashboardGoalFragment)mAdapter.getItem(1);
        mGoalFrag.setDefaultGroup(GroupId);

        firstLoad = true;

        loadCategoryList();
        loadTransactionList();
        loadGoalList();


    }
    public void loadGoalList(){
        Call<List<Goal>> call = apicall.getApiInterface().getGoalList(USER_TOKEN,GroupId );
        call.enqueue(new Callback<List<Goal>>() {
            @Override
            public void onResponse(Call<List<Goal>> call, Response<List<Goal>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }
                updateGoals(response.body());
            }

            @Override
            public void onFailure(Call<List<Goal>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void updateGoals(List<Goal> goals) {
        mSpendingsDataSummary.setGoals(goals);
        currGoals = (ArrayList) goals;
        checkForGoalsExceeded();
    }

    private void checkForGoalsExceeded() {
        if (!firstLoad) return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSpendingsDataSummary.getTransactions().isEmpty()) {
                    checkForGoalsExceeded();
                } else {
                    firstLoad = false;
                    createNotificationChannel();
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                    int i = 0;
                    for (Goal goal : mSpendingsDataSummary.getGoals()) {
                        Double amountRemaining = goal.amountRemaining(mSpendingsDataSummary.getTransactions());
                        if (amountRemaining <= 0) {
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), GOAL_NOTIFICATION_CHANNEL);
                            builder.setSmallIcon(R.drawable.ic_alert_red_24dp).setContentTitle("Goal limit exceeded!")
                                    .setContentText("You have exceeded goal \"" + goal.getTitleString() + "\" by " + amountRemaining.toString() + ".")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                            Date now = new Date();

                            notificationManager.notify(i++, builder.build());

                        }
                    }
                }
            }
        }, 2000);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.goal_alert_channel_name);
            String description = getString(R.string.goal_alert_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(GOAL_NOTIFICATION_CHANNEL, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onListFragmentInteraction(Transaction item){}

    @Override
    public void transListChanged() {
        loadTransactionList();
    }

    @Override
    public void onListFragmentInteraction(Goal goal) {}

    @Override
    public void goalListChanged() {
        loadGoalList();
    }


    //addtransaction activity needs the current transationList to see whether the database list is the same as current.
    private void startAddTransaction() {
        Intent i = new Intent(this, AddTransactionActivity.class);
        i.putParcelableArrayListExtra("transactionList",currTrans );
        startActivityForResult(i, REQUEST_CODE_ADD_TRANSACTION);
    }

    public void startGoalSetting(){
        Intent i = new Intent(this, GoalSettingActivity.class);
        i.putParcelableArrayListExtra("goalList",currTrans );
        startActivityForResult(i, REQUEST_CODE_GOAL_SETTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        //after return back to Dashboard, want to check if there is new updated to the database


        if (requestCode == REQUEST_CODE_ADD_TRANSACTION) {
            if (resultCode == RESULT_OK) {
                //load the new data from server
                loadCategoryList();
                loadTransactionList();
             //   Transaction transaction = (Transaction)data.getExtras().getParcelable("transaction");
            //    mSpendingsDataSummary.addTransaction(transaction);
            }
        }
        else if (requestCode == REQUEST_CODE_GOAL_SETTING){
            if (resultCode == RESULT_OK) {
                loadGoalList();
        }
        }
    }

    public SpendingsDataSummary getSpendingsDataSummary() {
        return mSpendingsDataSummary;
    }


}
