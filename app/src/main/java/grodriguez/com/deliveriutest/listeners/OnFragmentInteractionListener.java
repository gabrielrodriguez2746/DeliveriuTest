package grodriguez.com.deliveriutest.listeners;

import android.view.View;

import grodriguez.com.deliveriutest.models.Products;

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
     * @param tag  Page Tag
     */
    void onItemSelected(View view, int id, int tag);

    /**
     * Allows the user to add a product to the Ship
     *
     * @param products
     */
    void onShippingSelected(Products products);
}
