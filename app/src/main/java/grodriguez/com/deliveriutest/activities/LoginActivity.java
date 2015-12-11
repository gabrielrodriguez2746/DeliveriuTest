package grodriguez.com.deliveriutest.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import grodriguez.com.deliveriutest.R;
import grodriguez.com.deliveriutest.dialog.ConfirmationDialog;
import grodriguez.com.deliveriutest.listeners.OnConfirmationDialogClickListener;
import grodriguez.com.deliveriutest.listeners.OnParseSignInResult;
import grodriguez.com.deliveriutest.listeners.OnParseSignUpResult;
import grodriguez.com.deliveriutest.network.ParseApplication;
import grodriguez.com.deliveriutest.utils.Constants;
import grodriguez.com.deliveriutest.utils.FieldValidator;

public class LoginActivity extends FragmentActivity implements FacebookCallback<LoginResult>,
        View.OnClickListener, OnConfirmationDialogClickListener, OnParseSignUpResult, OnParseSignInResult {

    private final String LOG_TAG = getClass().getSimpleName();
    private Button mFbButton, mLoginButton; // Facebook Custom Button and Login Button
    private EditText mName, mPassword; // Email Edit text and Password Edit Text
    private TextView mEnterWithoutRegister, mSignUp; // Text Enter without register and Text Register
    CallbackManager callbackManager; // Facebook Callback
    List<String> permissionNeeds; // Facebook Permissions
    private ProgressDialog progressDialog; // Loadin Dialog
    private String loginType; // Indicator of the login type
    /**
     * User Information
     */
    private String email;
    private String name;
    private String password;

    /**
     * Shared Preference
     */
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        permissionNeeds = Arrays.asList(Constants.BUNDLE_EMAIL);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        mFbButton = (Button) findViewById(R.id.fb_login);
        mName = (EditText) findViewById(R.id.name);
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
         *Progress Dialog
         */
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        /**
         * Preference
         */
        pref = getApplicationContext().getSharedPreferences(Constants.TAG_SHARE_PREFERENCE_NAME,
                MODE_PRIVATE);
        editor = pref.edit();
    }


    @Override
    public void onSuccess(LoginResult loginResult) {

        Log.d(LOG_TAG, "Token :: " + loginResult.getAccessToken().getToken().toString());
        Log.d(LOG_TAG, "UserId :: " + loginResult.getAccessToken().getUserId().toString());
        password = loginResult.getAccessToken().getUserId().toString();
        final Context context = this;

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            email = object.getString(Constants.BUNDLE_EMAIL);
                            name = object.getString(Constants.BUNDLE_NAME);
                        } catch (Exception e) {
                            Log.d(LOG_TAG, "Facebook Exception :: " + e.toString());
                        }
                        loginType = Constants.TAG_FACEBOOK_LOGIN;
                        progressDialog.show();
                        Log.d(LOG_TAG, "Login with Parse with Facebook Information");
                        ParseApplication.singInWithParse(context, name, password);
                        LoginManager.getInstance().logOut(); // I really should do it later
                        Log.d(LOG_TAG, response.toString());
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString(Constants.TAG_FIELDS, Constants.TAG_ID + ", " + Constants.BUNDLE_EMAIL +
                ", " + Constants.TAG_NAME);
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    public void onCancel() {
        Log.d(LOG_TAG, "cancel Facebook Activity");
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
                if (FieldValidator.isValid(mName) && FieldValidator.isValid(mPassword)) {
                    name = mName.getText().toString();
                    password = mPassword.getText().toString();
                    loginType = Constants.TAG_REGULAR_LOGIN;
                    ParseApplication.singInWithParse(this, name, password);
                } else {
                    Toast.makeText(this, R.string.user_warning, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.without_register:
                editor.putString(Constants.TAG_LOGIN_STATUS, Constants.TAG_UN_LOGGED);
                editor.commit();
                BeginActivity(MainActivity.class, null, true);
                break;
            case R.id.sign_up:
                BeginActivity(RegisterActivity.class, null, false);
                break;
        }
    }


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

    @Override
    public void onSingUpResultDone(Exception e) {
        if (e == null) {
            Log.d(LOG_TAG, "Registration OK");
            Toast.makeText(this, getString(R.string.successfully_login), Toast.LENGTH_LONG).show();
            BeginActivity(MainActivity.class, null, true);
        } else {
            Log.d(LOG_TAG, "There was an error");
            Log.d(LOG_TAG, e.toString());
        }
    }

    @Override
    public void onSingInResultDone(ParseUser user, Exception e) {
        if (user != null) {
            progressDialog.dismiss();
            Log.d(LOG_TAG, user.toString());
            Toast.makeText(this, getString(R.string.successfully_login), Toast.LENGTH_LONG).show();
            editor.putString(Constants.TAG_LOGIN_STATUS, Constants.TAG_LOGGED);
            editor.commit();
            BeginActivity(MainActivity.class, null, true);

        } else {
            if (loginType.equals(Constants.TAG_FACEBOOK_LOGIN))
                ParseApplication.connectWithParse(this, name, email, password);
            else
                progressDialog.dismiss();
            showConfirmationDialog(getString(R.string.not_register_user));
        }
    }
}
