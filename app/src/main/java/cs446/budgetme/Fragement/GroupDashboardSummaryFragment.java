package cs446.budgetme.Fragement;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import cs446.budgetme.R;

public class GroupDashboardSummaryFragment extends DashboardSummaryFragment {
    protected static final int MENU_USERS_ID = View.generateViewId();
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    protected void addMenuItems(Menu menu) {
        super.addMenuItems(menu);
        menu.add(0, MENU_USERS_ID, Menu.NONE, R.string.menu_users).setIcon(R.drawable.ic_group_white_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    protected boolean handleMenuItemSelected(int id) {
        if (id == MENU_USERS_ID) {
            return true;
        }
        return super.handleMenuItemSelected(id);
    }

}
