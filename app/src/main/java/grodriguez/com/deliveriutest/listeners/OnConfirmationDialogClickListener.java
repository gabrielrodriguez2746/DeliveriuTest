package grodriguez.com.deliveriutest.listeners;

/**
 *
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public interface OnConfirmationDialogClickListener {

    /**
     * User has pressed the OK button.
     */
    void onConfirm();

    /**
     * User has pressed the NO button.
     */
    void onRelease();

}
