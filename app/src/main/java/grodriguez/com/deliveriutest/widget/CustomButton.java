package grodriguez.com.deliveriutest.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import grodriguez.com.deliveriutest.R;

/**
 * Based on a tutorial by Daniele Spinetti.
 *
 * @link http://www.danielespinetti.it/2013/01/custom-font-for-view.html
 * <p/>
 * Follow this guy on Twitter: @Spinettaro
 */
public class CustomButton extends Button {

    private final String LOG_TAG = getClass().getSimpleName();

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String customFont = a.getString(R.styleable.CustomTextView_customFont);
        setCustomFont(context, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context context, String asset) {
        Typeface typeface = null;
        try {
            typeface = Typeface.createFromAsset(context.getAssets(), asset);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error to get typeface: " + e.getMessage(), e);
            return false;
        }
        setTypeface(typeface);
        return true;
    }
}
