package cs446.budgetme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.View;

import cs446.budgetme.Adaptor.DashboardTabAdapter;
import cs446.budgetme.Fragement.DashboardSummaryFragment;
import cs446.budgetme.Fragement.DashboardProfileFragment;
import cs446.budgetme.Fragement.DashboardTransDetailFragment;
import cs446.budgetme.Model.Transaction;

public class DashboardActivity extends AppCompatActivity
        implements DashboardSummaryFragment.OnFragmentInteractionListener, DashboardProfileFragment.OnFragmentInteractionListener,
        DashboardTransDetailFragment.OnListFragmentInteractionListener{
    private DashboardTabAdapter mAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private static final int REQUEST_CODE_ADD_TRANSACTION = 10000;
    private static final int REQUEST_CODE_GOAL_SETTING = 11000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //get indent information
        Intent intent = getIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        mAdapter.addFragment(new DashboardSummaryFragment(), getResources().getString(R.string.title_dashboard_tab_summary));
        mAdapter.addFragment(new DashboardTransDetailFragment(), "Transaction Detail");
        mAdapter.addFragment(mProfile,"Profile");

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager, true);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onListFragmentInteraction(Transaction item){}


    private void startAddTransaction() {
        Intent i = new Intent(this, AddTransactionActivity.class);
        startActivityForResult(i, REQUEST_CODE_ADD_TRANSACTION);
    }

    public void startGoalSetting(){
        Intent i = new Intent(this, GoalSettingActivity.class);
        startActivityForResult(i, REQUEST_CODE_GOAL_SETTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CODE_ADD_TRANSACTION) {
            if (resultCode == RESULT_OK) {
                Transaction transaction = (Transaction)data.getExtras().getParcelable("transaction");
                ((DashboardSummaryFragment)mAdapter.getItem(0)).onTransactionAdded(transaction);
                ((DashboardTransDetailFragment)mAdapter.getItem(1)).onTransactionAdded(transaction);
            }
        }
        else if (requestCode == REQUEST_CODE_GOAL_SETTING){

        }
    }
}
