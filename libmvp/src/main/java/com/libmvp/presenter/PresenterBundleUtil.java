package com.libmvp.presenter;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class PresenterBundleUtil {
    private static final String MAP_KEY = PresenterBundle.class.getName();
    private static final String LOG_TAG = PresenterBundleUtil.class.getSimpleName();

    private PresenterBundleUtil() {
        // No instances
    }

    /**
     * Read {@link PresenterBundle} from android {@link Bundle}
     * @param savedInstanceState android bundle
     * @return presenter bundle
     */
    @SuppressWarnings("unchecked") // Handled internally
    public static PresenterBundle getPresenterBundle(@Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            try {
                HashMap<String, Object> map = (HashMap<String, Object>) savedInstanceState
                        .getSerializable(MAP_KEY);
                return new PresenterBundle(map);
            } catch (ClassCastException e) {
                Log.e(LOG_TAG, "getPresenterBundle", e);
            }
        }

        return null;
    }

    /**
     * Write {$link PresenterBundle} to android {@link Bundle}
     * @param outState android bundle
     * @param presenterBundle presenter bundle
     */
    public static void setPresenterBundle(Bundle outState, PresenterBundle presenterBundle) {
        outState.putSerializable(MAP_KEY, presenterBundle.getMap());
    }
}
