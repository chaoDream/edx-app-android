package org.edx.mobile.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.google.inject.Inject;

import org.edx.mobile.R;

/**
 * Utility class for updating the app.
 */
public final class AppUpdateUtils {
    // Make this class non-instantiable
    private AppUpdateUtils() {
        throw new UnsupportedOperationException();
    }

    @Inject
    private static Config config;

    /**
     * @param context A Context to query the applications info.
     *
     * @return Whether there are any apps registered to handle the update URIs.
     */
    public static boolean canUpdate(@NonNull final Context context) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        final Config.AppUpdateUrisConfig appUpdateUrisConfig = config.getAppUpdateUrisConfig();
        intent.setData(Uri.parse(appUpdateUrisConfig.getNativeUri()));
        if (intent.resolveActivity(packageManager) != null) return true;
        intent.setData(Uri.parse(appUpdateUrisConfig.getWebUri()));
        return intent.resolveActivity(packageManager) != null;
    }

    /**
     * Open an native app or website on a web browser to update the app.
     *
     * @param context A Context for starting the new Activity.
     */
    public static void openAppInAppStore(@NonNull final Context context) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        final Config.AppUpdateUrisConfig appUpdateUrisConfig = config.getAppUpdateUrisConfig();
        intent.setData(Uri.parse(appUpdateUrisConfig.getNativeUri()));
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            intent.setData(Uri.parse(appUpdateUrisConfig.getWebUri()));
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e2) {
                // There is not app store or web browser registered on the device. Show a
                // toast message to that effect.
                Toast.makeText(context, R.string.app_version_upgrade_app_store_unavailable,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Generic click listener that opens an app store to display the app. This is created as a
     * convenience, because this utility seems to be mostly invoked from a click listener.
     */
    public static final View.OnClickListener OPEN_APP_IN_APP_STORE_CLICK_LISTENER =
            new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    openAppInAppStore(v.getContext());
                }
            };
}
