package grodriguez.com.deliveriutest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import grodriguez.com.deliveriutest.R;
import grodriguez.com.deliveriutest.dialog.ConfirmationDialog;
import grodriguez.com.deliveriutest.listeners.OnConfirmationDialogClickListener;
import grodriguez.com.deliveriutest.utils.Constants;
import grodriguez.com.deliveriutest.utils.FieldValidator;

public class LoginActivity extends FragmentActivity implements FacebookCallback<LoginResult>,
        View.OnClickListener, OnConfirmationDialogClickListener {

    private final String LOG_TAG = getClass().getSimpleName();
    private Button mFbButton, mLoginButton; // Facebook Custom Button and Login Button
    private EditText mEmail, mPassword; // Email Edit text and Password Edit Text
    private TextView mEnterWithoutRegister, mSignUp; // Text Enter without register and Text Register
    CallbackManager callbackManager; // Facebook Callback
    List<String> permissionNeeds; // Facebook Permissions


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        permissionNeeds = Arrays.asList("email");
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        mFbButton = (Button) findViewById(R.id.fb_login);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mEnterWithoutRegister = (TextView) findViewById(R.id.without_register);
        mSignUp = (TextView) findViewById(R.id.sign_up);

        /**
         * Listeners and click
         */
        mFbButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        mEnterWithoutRegister.setClickable(true);
        mEnterWithoutRegister.setOnClickListener(this);
        mSignUp.setClickable(true);
        mSignUp.setOnClickListener(this);

        /**
         * Bundle Reading
         */
        Bundle userInformation = getIntent().getExtras();
        if (userInformation != null) {
            mEmail.setText(userInformation.getString(Constants.BUNDLE_EMAIL));
        }
    }


    @Override
    public void onSuccess(LoginResult loginResult) {

        Log.d(LOG_TAG, loginResult.getAccessToken().getToken().toString());
        Log.d(LOG_TAG, loginResult.getAccessToken().getUserId().toString());
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        Bundle bundle = new Bundle();
                        String email = "";
                        String name = "";
                        try {
                            email = object.getString(Constants.BUNDLE_EMAIL);
                            name = object.getString(Constants.BUNDLE_NAME);
                        } catch (Exception e) {
                            Log.d(LOG_TAG, "Facebook Exception :: " + e.toString());
                        }
                        bundle.putString(Constants.BUNDLE_EMAIL, email);
                        bundle.putString(Constants.BUNDLE_NAME, name);
                        BeginActivity(RegisterActivity.class, bundle, false);
                        LoginManager.getInstance().logOut();
                        Log.d(LOG_TAG, response.toString());
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, email, name");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public void onCancel() {
        Log.d(LOG_TAG, "cancel");
    }

    @Override
    public void onError(FacebookException e) {
        Log.d(LOG_TAG, e.getCause().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fb_login:
                LoginManager.getInstance().logInWithReadPermissions(this, permissionNeeds);
                LoginManager.getInstance().registerCallback(callbackManager, this);
                break;
            case R.id.login_button:
                if (FieldValidator.isValid(mEmail) && FieldValidator.isValid(mPassword)) {
                    ParseUser.logInInBackground(mEmail.getText().toString(),
                            mPassword.getText().toString(), loginCallback);
                } else {
                    Toast.makeText(this, R.string.user_warning, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.without_register:
                BeginActivity(MainActivity.class, null, true);
                break;
            case R.id.sign_up:
                BeginActivity(RegisterActivity.class, null, false);
                break;
        }
    }

    /**
     * Callback for Parse login. If user exist in Parse show the Menu
     * if he doesn't ask him to enter without registration or register.
     */

    LogInCallback loginCallback = new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {
            if (user != null) {
                Log.d(LOG_TAG, user.toString());
                Toast.makeText(getApplicationContext(),
                        getString(R.string.successfully_login),
                        Toast.LENGTH_LONG).show();
                BeginActivity(MainActivity.class, null, true);

            } else {
                showConfirmationDialog(getString(R.string.not_register_user));

            }
        }
    };

    /**
     * Start a new activity
     *
     * @param activity Activity lo start
     * @param bundle   Information to pass to next Activity
     */
    private void BeginActivity(Class activity, Bundle bundle, boolean finish) {
        Intent intent = new Intent(LoginActivity.this, activity);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
        if (finish)
            finish();
    }

    /**
     * Show Dialog to Create User Notifications
     */
    private void showConfirmationDialog(String message) {

        new ConfirmationDialog(this, message, true, this);
    }

    @Override
    public void onConfirm() {
        mPassword.setText("");
    }

    @Override
    public void onRelease() {

    }
}
