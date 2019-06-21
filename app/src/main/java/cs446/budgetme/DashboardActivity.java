package cs446.budgetme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.View;

import cs446.budgetme.Adaptor.DashboardTabAdapter;
import cs446.budgetme.Fragement.DashboardSummaryFragment;
import cs446.budgetme.Fragement.DashboardTransactiondetailFragment;
import cs446.budgetme.Model.Transaction;

public class DashboardActivity extends AppCompatActivity
        implements DashboardSummaryFragment.OnFragmentInteractionListener, DashboardTransactiondetailFragment.OnFragmentInteractionListener{
    private DashboardTabAdapter mAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private static final int REQUEST_CODE_ADD_TRANSACTION = 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddTransaction();
            }
        });

        mViewPager = findViewById(R.id.dashboard_view_pager);
        mTabLayout = findViewById(R.id.dashboard_tab_layout);
        mAdapter = new DashboardTabAdapter(getSupportFragmentManager());
        //TODO: add profile of the user
        mAdapter.addFragment(new Fragment(), "Tab 1");
        mAdapter.addFragment(new DashboardSummaryFragment(), getResources().getString(R.string.title_dashboard_tab_summary));

        //TODO:add a list view for the expense
        mAdapter.addFragment(new DashboardTransactiondetailFragment(), "Transaction Details");

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager, true);
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void startAddTransaction() {
        Intent i = new Intent(this, AddTransactionActivity.class);
        startActivityForResult(i, REQUEST_CODE_ADD_TRANSACTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CODE_ADD_TRANSACTION) {
            if (resultCode == RESULT_OK) {
                Transaction transaction = (Transaction)data.getExtras().getParcelable("transaction");
                ((DashboardSummaryFragment)mAdapter.getItem(mViewPager.getCurrentItem())).onTransactionAdded(transaction);
                ((DashboardTransactiondetailFragment)mAdapter.getItem(mViewPager.getCurrentItem())).onTransactionAdded(transaction);
            }
        }
    }
}
