package grodriguez.com.deliveriutest.network;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import grodriguez.com.deliveriutest.R;
import grodriguez.com.deliveriutest.listeners.OnParseSignUpResult;

/**
 * Parse ap
 *
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public class ParseApplication extends Application {

    private final String LOG_TAG = getClass().getSimpleName();

    /**
     * Allows the user to initialize the Parse SDK
     *
     * @param context Activity context
     */
    public static void startParse(Context context) {
        try {
            Parse.enableLocalDatastore(context);
            Parse.initialize(context, context.getString(R.string.parse_application_id),
                    context.getString(R.string.parse_client_key));
            ParseUser.enableAutomaticUser();
            ParseACL defaultACL = new ParseACL();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *
     * @param context
     * @param name
     * @param email
     * @param password
     */
    public static void connectWithParse(Context context,String name, String  email, String  password) {

        final OnParseSignUpResult onParseSignUpResult = (OnParseSignUpResult) context;
        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setEmail(email);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                onParseSignUpResult.onSingUpResultDone(e);
            }
        });

    }

}
