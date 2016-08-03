package com.jpos.desktopmode.ext.fw;

import android.content.Context;
import android.support.annotation.StringRes;

/**
 * Project: FloatingWindows.
 * Created by jonos on 7/31/2016.
 */

@SuppressWarnings("WeakerAccess")
public class BrainRecognition {
    private static BrainRecognition brInstance;
    private String apiKey;
    private boolean isApiKeyValid;


    private BrainRecognition(String apiKey) {
        this.apiKey = apiKey;
        this.isApiKeyValid = false;
    }

    public void validateApiKey() {
        this.isApiKeyValid = true;
    }

    public int predictInt() throws ApiNotVerifiedException {
        if (!this.isApiKeyValid) {
            throw new ApiNotVerifiedException(
                    "API key has not been validated, did you call validateApiKey() ?");
        }
        return 4;
    }

    public static BrainRecognition createInstance(String apiKey) {
        if (brInstance == null) {
            brInstance = new BrainRecognition(apiKey);
        }
        return brInstance;
    }

    public static BrainRecognition createInstance(Context ctx, @StringRes int apiKey) {
        return BrainRecognition.createInstance(
                ctx.getResources().getString(apiKey)
        );
    }
}

