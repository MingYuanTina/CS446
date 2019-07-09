package cs446.budgetme.ReceiptOCR;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import cs446.budgetme.R;
import android.view.View;
import android.widget.TextView;

public class ReceiptOCRActivity extends AppCompatActivity implements IOCRCallBack {

    private String mAPiKey = "f32d8ec8d188957"; //TODO Add your own Registered API key
    private boolean isOverlayRequired;
    private String mImageUrl;
    private String mLanguage;
    private TextView mTxtResult;
    private IOCRCallBack mIOCRCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_ocr);
        mIOCRCallBack = this;
        mImageUrl = "http://dl.a9t9.com/blog/ocr-online/screenshot.jpg"; // Image url to apply OCR API
        mLanguage = "eng"; //Language
        isOverlayRequired = true;
        init();

    }

    private void init() {
        mTxtResult = (TextView) findViewById(R.id.actual_result);
        TextView btnCallAPI = (TextView) findViewById(R.id.btn_call_api);

        if (btnCallAPI != null) {
            btnCallAPI.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OCRAsyncTask oCRAsyncTask = new OCRAsyncTask(ReceiptOCRActivity.this, mAPiKey, isOverlayRequired, mImageUrl, mLanguage,mIOCRCallBack);
                    oCRAsyncTask.execute();

                }
            });
        }
    }

    @Override
    public void getOCRCallBackResult(String response) {
        mTxtResult.setText(response);
    }
}

