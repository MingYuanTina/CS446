package cs446.budgetme;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

public class EndOnlyEditText extends TextInputEditText {

    public EndOnlyEditText(Context context) {
        super(context);
    }

    public EndOnlyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    EndOnlyEditText (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onSelectionChanged(int start, int end) {

        CharSequence text = getText();
        if (text != null) {
            if (start != text.length() || end != text.length()) {
                setSelection(text.length(), text.length());
                return;
            }
        }

        super.onSelectionChanged(start, end);
    }

}
