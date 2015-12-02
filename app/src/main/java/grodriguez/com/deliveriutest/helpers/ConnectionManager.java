package grodriguez.com.deliveriutest.helpers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

/**
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public class ConnectionManager {

    @Nullable
    public static Boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.
                getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}
