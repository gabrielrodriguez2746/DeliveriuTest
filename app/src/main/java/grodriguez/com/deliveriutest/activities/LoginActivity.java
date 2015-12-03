package grodriguez.com.deliveriutest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import grodriguez.com.deliveriutest.R;

public class LoginActivity extends FragmentActivity implements FacebookCallback<LoginResult>,
        View.OnClickListener {

    private final String LOG_TAG = getClass().getSimpleName();
    private Button mFbButton; // Facebook Custom Button
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
        mFbButton.setOnClickListener(this);

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
                        // Application code

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
                Log.d(LOG_TAG, "here");
                break;
        }
    }
}
