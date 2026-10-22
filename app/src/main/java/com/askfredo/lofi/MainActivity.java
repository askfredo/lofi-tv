package com.askfredo.lofi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/** lofi radio for the TV: a YouTube live station for the sound, lofi.cafe's pixel-art GIFs on screen. */
public class MainActivity extends Activity {
    /** The page needs an https origin: the YouTube player refuses embeds that send no referrer. */
    private static final String BASE_URL = "https://lofi-tv.askfredo.local/";

    private WebView web;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        web = new WebView(this);
        web.setBackgroundColor(0xFF000000);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        web.setWebChromeClient(new WebChromeClient());
        // Keys go to the page through onKey(), not as focus moves inside the WebView.
        web.setFocusable(false);
        setContentView(web);
        web.loadDataWithBaseURL(BASE_URL, readAsset("index.html"), "text/html", "UTF-8", null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        web.onResume();
        web.resumeTimers();
    }

    @Override
    protected void onPause() {
        // Leaving the app stops the music (and the GIFs) instead of playing on in the background.
        web.evaluateJavascript("if (ready && playing) { player.pauseVideo(); playing = false; }", null);
        web.onPause();
        web.pauseTimers();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        web.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        String key = null;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_CHANNEL_UP:
                key = "up";
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_CHANNEL_DOWN:
                key = "down";
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                key = "left";
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                key = "right";
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            case KeyEvent.KEYCODE_INFO:
                key = "ok";
                break;
        }
        if (key == null) return super.onKeyDown(keyCode, event);
        web.evaluateJavascript("onKey('" + key + "')", null);
        return true;
    }

    private String readAsset(String name) {
        try (InputStream in = getAssets().open(name)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            for (int n; (n = in.read(buf)) > 0; ) out.write(buf, 0, n);
            return out.toString("UTF-8");
        } catch (Exception e) {
            return "<body style='background:#000;color:#fff'>" + e + "</body>";
        }
    }
}
