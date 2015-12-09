package grodriguez.com.deliveriutest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

import grodriguez.com.deliveriutest.R;
import grodriguez.com.deliveriutest.dialog.ConfirmationDialog;
import grodriguez.com.deliveriutest.helpers.ConnectionManager;
import grodriguez.com.deliveriutest.listeners.OnConfirmationDialogClickListener;
import grodriguez.com.deliveriutest.network.ParseApplication;
import grodriguez.com.deliveriutest.utils.Constants;

public class SplashActivity extends FragmentActivity implements OnConfirmationDialogClickListener {

    private final String LOG_TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        try {
            ParseApplication.startParse(this);
        } catch (Exception e) {
            Log.d(LOG_TAG, "Error starting Parse Application " + e.getMessage());
        }

        if (ConnectionManager.isConnected(this)) {
            /**
             * Time Before Display Main Activity
             */
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(LOG_TAG, "Checking if the user is login");
                    if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                    } else {
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        if (currentUser != null) {
                            Intent menuIntent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(menuIntent);
                            finish();
                        } else {
                            Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                            finish();
                        }
                    }
                }

            }, Constants.SPLASH_INTERVAL);

        } else {
            showConfirmationDialog();
        }

    }

    /**
     * Show Dialog to confirm Network Connection
     */

    private void showConfirmationDialog() {

        String message = getString(R.string.net);
        String yes = getString(R.string.retry);
        String no = getString(R.string.exit);
        String title = getString(R.string.warning_net);
        new ConfirmationDialog(this, title, yes, no, message, this);
    }

    @Override
    public void onConfirm() {
        if (!ConnectionManager.isConnected(getBaseContext())) {
            showConfirmationDialog();
        }
        if (ConnectionManager.isConnected(getBaseContext())) {
            Intent i = new Intent(SplashActivity.this, SplashActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onRelease() {
        finish();
    }
}
