package grodriguez.com.deliveriutest.listeners;

/**
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public interface OnOrderButtonListener {

    /**
     * Notify the user on the button click
     *
     * @param indicator minus or plus
     * @param position  list item position
     */
    void OnOrderButtonSelected(int indicator, int position, int quantity);
}
