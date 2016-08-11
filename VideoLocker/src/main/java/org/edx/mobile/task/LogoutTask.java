package org.edx.mobile.task;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.inject.Inject;

import org.edx.mobile.authentication.AuthResponse;
import org.edx.mobile.authentication.LoginAPI;
import org.edx.mobile.http.HttpException;
import org.edx.mobile.module.prefs.LoginPrefs;

import roboguice.RoboGuice;

public class LogoutTask extends Task<Void> {
    public static void executeInstance(@NonNull final Context context) {
        final LoginPrefs loginPrefs = RoboGuice.getInjector(context).getInstance(LoginPrefs.class);
        final AuthResponse currentAuth = loginPrefs.getCurrentAuth();
        if (currentAuth != null && currentAuth.refresh_token != null) {
            new LogoutTask(context, currentAuth.refresh_token).execute();
        }
    }

    @Inject
    private LoginAPI loginAPI;

    @NonNull
    private final String refreshToken;

    private LogoutTask(@NonNull final Context context, @NonNull final String refreshToken) {
        super(context);
        this.refreshToken = refreshToken;
    }

    @Override
    public Void call() throws HttpException {
        loginAPI.logOut(refreshToken);
        return null;
    }
}
