package cs446.budgetme.Fragement;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs446.budgetme.Adaptor.DashboardTransDetailRecyclerViewAdapter;
import cs446.budgetme.Client.GetDataService;
import cs446.budgetme.Client.RetrofitClient;
import cs446.budgetme.Model.Transaction;
import cs446.budgetme.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DashboardTransDetailFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private ArrayList<Transaction> mTransactions;
    private RecyclerView recyclerView;
    private static final String TAG = DashboardTransDetailFragment.class.getName();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DashboardTransDetailFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DashboardTransDetailFragment newInstance(int columnCount) {
        DashboardTransDetailFragment fragment = new DashboardTransDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        mTransactions =  (ArrayList)Transaction.getFakeData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
//
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
           recyclerView.setAdapter(new DashboardTransDetailRecyclerViewAdapter(mTransactions, mListener));
            getTransList();
        }
        return view;
    }

    public void getTransList(){
        RetrofitClient retrofit = new RetrofitClient();
        GetDataService apiInterface= retrofit.getRetrofitClient().create(GetDataService.class);
        String token="abc";
        Call<List<Transaction>> call = apiInterface.getUserTrans(token);
        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }
                List<Transaction>  gets = response.body();
                List<Transaction>  post = Transaction.getFakeData();
                onListUpdate(post);
                recyclerView.setAdapter(new DashboardTransDetailRecyclerViewAdapter(gets, mListener));
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    public  void onListUpdate(List<Transaction> translist){
        Log.d(TAG, "in ListUpdate");
        recyclerView.setAdapter(new DashboardTransDetailRecyclerViewAdapter(translist, mListener));
        recyclerView.invalidate();
    }

    public void onTransactionAdded(Transaction transaction) {
        mTransactions.add(transaction);
        recyclerView.setAdapter(new DashboardTransDetailRecyclerViewAdapter(mTransactions, mListener));
        recyclerView.invalidate();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Transaction item);
    }
}
