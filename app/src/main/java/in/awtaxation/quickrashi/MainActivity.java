package in.awtaxation.quickrashi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.browser.customtabs.CustomTabsIntent;

public class MainActivity extends Activity {
    private static final String APP_URL =
        "https://script.google.com/macros/s/AKfycbx0JYxTEF1rpnTdUdutFKPXG3GOFYAuveCJV6IQCCLEQ95NtZNU7uMxjjnjypHaBvGU/exec";
    private boolean opened = false;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        launchWebApp();
    }

    private void launchWebApp() {
        if (opened) return;
        opened = true;
        Uri uri = Uri.parse(APP_URL);
        try {
            CustomTabsIntent customTab = new CustomTabsIntent.Builder()
                .setShowTitle(false)
                .setToolbarColor(Color.rgb(17, 73, 133))
                .build();
            customTab.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            customTab.launchUrl(this, uri);
        } catch (Exception e) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } catch (Exception ignored) { }
        }
        finish();
    }
}
