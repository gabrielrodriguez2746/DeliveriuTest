package grodriguez.com.deliveriutest.listeners;

import com.parse.ParseUser;

/**
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public interface OnParseSignInResult {

    /**
     * Callback for user SignIn
     * @param user Parse UserResult
     * @param e Exception if the Login is not success
     */
    void onSingInResultDone(ParseUser user, Exception e);
}
