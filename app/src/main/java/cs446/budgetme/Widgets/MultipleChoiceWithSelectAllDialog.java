package cs446.budgetme.Widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs446.budgetme.Model.MultipleChoiceWithSelectAllDialogCallback;
import cs446.budgetme.R;

// Dialog that is used to choose filters, which allows the selection of multiple items, with the first item being an "All" box.
// Selecting the "All" box or not choosing any boxes at all will call the callback with an empty list of items, since that means there are no filters.
// Otherwise, it will call the callback with the chosen options.
public class MultipleChoiceWithSelectAllDialog<T> {
    private List<T> mChosenOptions = new ArrayList<>();
    private List<T> mAvailableOptions;
    private final String[] mAvailableOptionStrings;
    final boolean[] mCheckedIndices;
    private AlertDialog mDialog;
    private Context mContext;
    private MultipleChoiceWithSelectAllDialogCallback mCallback;

    public MultipleChoiceWithSelectAllDialog(Context context, final List<T> options, final MultipleChoiceWithSelectAllDialogCallback<T> callback) {
        mContext = context;

        mAvailableOptions = options;
        mAvailableOptionStrings = new String[mAvailableOptions.size() + 1];
        mAvailableOptionStrings[0] = mContext.getResources().getString(R.string.label_all);
        for (int i = 0; i < options.size(); i++) {
            mAvailableOptionStrings[i+1] = mAvailableOptions.get(i).toString();
        }

        mCallback = callback;

        mCheckedIndices = new boolean[options.size() + 1];
        for (int i = 0; i < mCheckedIndices.length; i++) {
            mCheckedIndices[i] = true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Choose items");

        builder.setMultiChoiceItems(mAvailableOptionStrings, mCheckedIndices, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if (which == 0) {
                    for (int i = 1; i < mAvailableOptionStrings.length; i++) {
                        mCheckedIndices[i] = isChecked;
                        mDialog.getListView().setItemChecked(i, isChecked);
                    }
                } else {
                    if (!isChecked) {
                        mCheckedIndices[0] = false;
                        mDialog.getListView().setItemChecked(0, false);
                    }
                }
            }
        });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mChosenOptions = new ArrayList<>();

                if (!mCheckedIndices[0]) {
                    for (int i = 1; i < mCheckedIndices.length; i++) {
                        if (mCheckedIndices[i]) {
                            mChosenOptions.add(mAvailableOptions.get(i-1));
                        }
                    }
                }
                mCallback.multipleChoiceWithSelectAllDialogCallback(mChosenOptions);
            }
        });
        mDialog = builder.create();
    }

    public void show() {
        mDialog.show();
    }
}
