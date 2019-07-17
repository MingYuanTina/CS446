package cs446.budgetme.Adaptor;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cs446.budgetme.Model.Transaction;
import cs446.budgetme.R;

public class TransactionListViewAdaptor extends ArrayAdapter<Transaction>{

    private ArrayList<Transaction> transactions;
    private Context mContext;
    private LayoutInflater mInflater;


    public TransactionListViewAdaptor(Context context, ArrayList<Transaction> data) {
        super(context, R.layout.content_transaction_list_view_adaptor, data);
        this.transactions = data;
        this.mContext=context;
        this.mInflater = LayoutInflater.from(getContext());

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.content_transaction_list_view_adaptor, parent, false);

        Transaction trans = transactions.get(position);

        if (trans != null) {
            TextView date = convertView.findViewById(R.id.textDate);
            TextView category = convertView.findViewById(R.id.textTransCategory);
            TextView amount = convertView.findViewById(R.id.textTransAmount);
            if (date != null) {
                date.setText(trans.getStringDate());
            }
            if (category != null) {
                category.setText(trans.getCategoryName());
            }
            if (amount != null) {
                amount.setText(trans.getCost().toString());
            }
        }

        return convertView;
    }
}
