package grodriguez.com.deliveriutest.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import grodriguez.com.deliveriutest.R;

public class OrderActivity extends FragmentActivity implements View.OnClickListener {

    private final String LOG_TAG = getClass().getSimpleName();
    private ImageView mBack; // Action Bar Back Button
    private TextView mTitle; // Action Bar Title

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        /**
         * ActionBar views
         */
        mBack = (ImageView) findViewById(R.id.backbtn);
        mBack.setOnClickListener(this);
        mTitle = (TextView) findViewById(R.id.action_bar_title);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(getString(R.string.generate_order));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backbtn:
                onBackPressed();
        }
    }
}
