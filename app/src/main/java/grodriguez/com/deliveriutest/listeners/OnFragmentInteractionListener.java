package grodriguez.com.deliveriutest.listeners;

import android.view.View;

/**
 * @author Gabriel Rodríguez
 * @version 1.0
 */
public interface OnFragmentInteractionListener {

    /**
     * Allows to notify the fragment when a item is selected.
     *
     * @param view The view which will be inflated
     * @param id   The item id
     */
    void onItemSelected(View view, String id);

}
