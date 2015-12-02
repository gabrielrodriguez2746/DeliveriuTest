package grodriguez.com.deliveriutest.listeners;

/**
 *
 * @author Richard Ricciardelli (rricciardelli@wuelto.com.mx)
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
