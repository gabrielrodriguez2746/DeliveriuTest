package grodriguez.com.deliveriutest.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import grodriguez.com.deliveriutest.R;
import grodriguez.com.deliveriutest.dialog.ConfirmationDialog;
import grodriguez.com.deliveriutest.listeners.OnConfirmationDialogClickListener;
import grodriguez.com.deliveriutest.listeners.OnParseSignUpResult;
import grodriguez.com.deliveriutest.network.ParseApplication;
import grodriguez.com.deliveriutest.utils.FieldValidator;

public class RegisterActivity extends FragmentActivity implements View.OnClickListener,
        OnConfirmationDialogClickListener, OnParseSignUpResult {


    private final String LOG_TAG = getClass().getSimpleName();
    private ImageView mBack; // Back Button on the action bar
    private EditText mName, mEmail, mPassword; // User Information
    private boolean success; // Sign up success
    private Button mJoinButton; // Join Button
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mBack = (ImageView) findViewById(R.id.backbtn);
        mBack.setVisibility(View.VISIBLE);
        mBack.setClickable(true);
        mBack.setOnClickListener(this);
        TextView mTitle = (TextView) findViewById(R.id.action_bar_title);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(R.string.registration_title);

        mName = (EditText) findViewById(R.id.name);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mJoinButton = (Button) findViewById(R.id.join);
        mJoinButton.setOnClickListener(this);

        success = false;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbtn:
                onBackPressed();
                break;
            case R.id.join:
                if (FieldValidator.isValid(mName) && FieldValidator.isValid(mEmail) &&
                        FieldValidator.isValid(mPassword)) {
                    connectWithParse();
                } else {
                    Toast.makeText(this, R.string.registration_warning, Toast.LENGTH_SHORT).show();
                }
        }
    }

    /**
     * Start a new activity
     *
     * @param activity Activity lo start
     * @param bundle   Information to pass to next Activity
     */
    private void BeginActivity(Class activity, Bundle bundle, boolean finish) {
        Intent intent = new Intent(RegisterActivity.this, activity);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
        if (finish)
            finish();
    }

    /**
     * User register in Parse
     */
    private void connectWithParse() {
        Log.d(LOG_TAG, "Parse registration");
        progressDialog.show();
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        ParseApplication.connectWithParse(this, name, email, password);
    }


    /**
     * Show a dialog to the user to confirm the information
     *
     * @param message Message to show on the Dialog
     */
    private void showConfirmationDialog(String message) {

        new ConfirmationDialog(this, message, true, this);
    }

    @Override
    public void onConfirm() {
        if (success) {
            BeginActivity(MainActivity.class, null, true);
        } else
            finish();
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onSingUpResultDone(Exception e) {
        progressDialog.dismiss();
        if (e == null) {
            Log.d(LOG_TAG, "Registration OK");
            success = true;
            showConfirmationDialog(getString(R.string.successful_sign_up));
        } else {
            Log.d(LOG_TAG, "There was an error");
            Log.d(LOG_TAG, e.toString());
            success = false;
            showConfirmationDialog(getString(R.string.sign_up_error));
        }
    }
}
