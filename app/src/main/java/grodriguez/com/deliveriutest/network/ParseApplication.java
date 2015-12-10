package grodriguez.com.deliveriutest.network;

import android.app.Application;
import android.content.Context;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

import grodriguez.com.deliveriutest.R;
import grodriguez.com.deliveriutest.listeners.OnParseSignInResult;
import grodriguez.com.deliveriutest.listeners.OnParseSignUpResult;
import grodriguez.com.deliveriutest.listeners.OnFindQueryParse;
import grodriguez.com.deliveriutest.utils.FieldValidator;

/**
 * Parse ap
 *
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public class ParseApplication extends Application {

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
     * Connect with Parse to Sign Up a User
     *
     * @param context  Context of the Activity is Calling the SignUp
     * @param name     User Name
     * @param email    User Email
     * @param password User Password
     */
    public static void connectWithParse(Context context, String name, String email, String password) {

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

    /**
     * Connect with Parse to Sign Up a User
     *
     * @param context  Context of the Activity is Calling the SignIn
     * @param name     User Name
     * @param password User Password
     */
    public static void singInWithParse(Context context, String name, String password) {
        final OnParseSignInResult onParseSignInResult = (OnParseSignInResult) context;
        ParseUser.logInInBackground(name, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                onParseSignInResult.onSingInResultDone(user, e);
            }
        });
    }

    /**
     * Allows to Connect with a Table from Parse and return it
     *
     * @param context    Context of the Activity
     * @param queryTable Name of the Table where the user wants to find
     * @param fieldName  Name of the Table field
     * @param fieldValue Value search
     */
    public static void findSimpleParse(Context context, String queryTable, String fieldName,
                                       String fieldValue) {
        final OnFindQueryParse onFindQueryParse = (OnFindQueryParse) context;
        ParseQuery query = ParseQuery.getQuery(queryTable);
        if (FieldValidator.isValid(fieldName) && FieldValidator.isValid(fieldValue)) {
            query.whereEqualTo(fieldName, fieldValue);
        }
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                onFindQueryParse.OnParseSimpleFindResult(objects, e);
            }
        });
    }

    public static void findInnerParse(Context context, String innerTable, String fieldInnerName,
                                      String fieldInnerValue, String queryTable, String fieldSimpleName) {
        final OnFindQueryParse onFindQueryParse = (OnFindQueryParse) context;
        ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery(innerTable);
        innerQuery.whereEqualTo(fieldInnerName, fieldInnerValue);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(queryTable);
        query.whereMatchesQuery(fieldSimpleName, innerQuery);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                onFindQueryParse.OnParseInnerFindResult(objects, e);
            }
        });
    }


}


