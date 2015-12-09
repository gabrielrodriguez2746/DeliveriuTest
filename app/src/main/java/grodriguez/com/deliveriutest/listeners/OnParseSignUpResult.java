package grodriguez.com.deliveriutest.listeners;

/**
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public interface OnParseSignUpResult {

    /**
     *Allows the user to connect the Parse Application SignUp with a parent activity
     * @param e
     */
    void onSingUpResultDone(Exception e);

}
