package grodriguez.com.deliveriutest.listeners;

/**
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public interface OnParseSignUpResult {

    /**
     * Callback for user Sign Up
     * @param e Exception if the SignUp is not success
     */
    void onSingUpResultDone(Exception e);

}
