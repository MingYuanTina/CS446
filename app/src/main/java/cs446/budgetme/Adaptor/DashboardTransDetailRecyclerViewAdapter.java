package cs446.budgetme.Adaptor;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import cs446.budgetme.APIClient.APIUtils;
import cs446.budgetme.Fragement.DashboardTransDetailFragment.OnListFragmentInteractionListener;
import cs446.budgetme.Model.Transaction;
import cs446.budgetme.Model.User;
import cs446.budgetme.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DashboardTransDetailRecyclerViewAdapter extends RecyclerView.Adapter<DashboardTransDetailRecyclerViewAdapter.ViewHolder> {

    private final List<Transaction> mValues;
    private final OnListFragmentInteractionListener mListener;
    private APIUtils apicall;
    private String USER_TOKEN= "5d30ff4e6397c4000427fabe";
    private String groupID = "5d30ff4e6397c4000427fabd";

    public DashboardTransDetailRecyclerViewAdapter(List<Transaction> items, OnListFragmentInteractionListener listener, User user) {
        mValues = items;
        mListener = listener;
        apicall = new APIUtils();
        USER_TOKEN = user.getUserAuthToken();
        groupID = user.getDefaultGroupId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_transactionview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDateView.setText(mValues.get(position).getStringDate());
        holder.mCategoryView.setText(mValues.get(position).getCategoryName());
        holder.mAmountView.setText(mValues.get(position).getCost().toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

    }
    public void delete(int position) { //removes the row
        //first send delete request to server
        //setup API client
        deleteTrans(mValues.get(position));
    }

    public void deleteTrans(Transaction tran) {

        apicall.getApiInterface().deleteTransaction(USER_TOKEN, tran.getId(), groupID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    mListener.transListChanged();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API.");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDateView;
        public final TextView mCategoryView;
        public final TextView mAmountView;
        public Transaction mItem;
        public ImageButton mDeleteButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.textDate);
            mCategoryView = (TextView) view.findViewById(R.id.transCategory);
            mAmountView = (TextView) view.findViewById(R.id.transAmount);
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
            return super.toString()+": " + mDateView.getText() + ", " +mCategoryView.getText()+", "+mAmountView.getText();
        }
    }
}
