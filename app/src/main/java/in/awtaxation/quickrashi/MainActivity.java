package in.awtaxation.quickrashi;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

public class MainActivity extends Activity {
  private static final String APP_URL = "https://script.google.com/macros/s/AKfycbx0JYxTEF1rpnTdUdutFKPXG3GOFYAuveCJV6IQCCLEQ95NtZNU7uMxjjnjypHaBvGU/exec";
  private WebView web;
  private ProgressBar loading;
  private PermissionRequest pendingCamera;
  private android.webkit.ValueCallback<Uri[]> uploadFiles;
  private static final int CAMERA_CODE = 420;
  private static final int PICK_FILE_CODE = 421;

  @Override public void onCreate(Bundle state) {
    super.onCreate(state);
    getWindow().setStatusBarColor(Color.rgb(22, 76, 133));
    LinearLayout page = new LinearLayout(this);
    page.setOrientation(LinearLayout.VERTICAL);
    loading = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
    loading.setMax(100); loading.setProgress(0);
    page.addView(loading, new LinearLayout.LayoutParams(-1, 5));
    web = new WebView(this);
    page.addView(web, new LinearLayout.LayoutParams(-1, 0, 1));
    setContentView(page);
    web.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_YES);
    WebSettings s = web.getSettings();
    s.setJavaScriptEnabled(true);
    s.setDomStorageEnabled(true);
    s.setDatabaseEnabled(true);
    s.setAllowFileAccess(false);
    s.setAllowContentAccess(true);
    s.setJavaScriptCanOpenWindowsAutomatically(false);
    s.setMediaPlaybackRequiresUserGesture(true);
    s.setBuiltInZoomControls(false);
    CookieManager.getInstance().setAcceptCookie(true);
    CookieManager.getInstance().setAcceptThirdPartyCookies(web, true);
    web.setWebViewClient(new WebViewClient() {
      @Override public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest request) {
        if (!request.isForMainFrame()) return false;
        Uri url = request.getUrl();
        String host = url.getHost() == null ? "" : url.getHost().toLowerCase();
        String scheme = url.getScheme() == null ? "" : url.getScheme().toLowerCase();
        // Never embed Google account sign-in flows. Google's security policy blocks them.
        if (host.equals("accounts.google.com") || host.equals("myaccount.google.com")) {
          openExternal(url); return true;
        }
        if (scheme.equals("https") && (host.equals("script.google.com") || host.equals("script.googleusercontent.com") || host.equals("servicesawtaxation-blip.github.io"))) return false;
        openExternal(url); return true;
      }
      @Override public void onPageFinished(WebView v, String url) {
        CookieManager.getInstance().flush();
        loading.setVisibility(View.GONE);
      }
    });
    web.setWebChromeClient(new WebChromeClient() {
      @Override public void onProgressChanged(WebView view, int progress) {
        loading.setVisibility(progress == 100 ? View.GONE : View.VISIBLE);
        loading.setProgress(progress);
      }
      @Override public void onPermissionRequest(PermissionRequest request) {
        runOnUiThread(() -> {
          String originHost = request.getOrigin().getHost();
          if (!"servicesawtaxation-blip.github.io".equalsIgnoreCase(originHost)) {request.deny();return;}
          boolean wantsVideo = false;
          for (String res : request.getResources()) if (PermissionRequest.RESOURCE_VIDEO_CAPTURE.equals(res)) wantsVideo = true;
          if (!wantsVideo) {request.deny();return;}
          pendingCamera = request;
          if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) approveCamera();
          else requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
        });
      }
      @Override public void onPermissionRequestCanceled(PermissionRequest request) {
        if (pendingCamera == request) pendingCamera = null;
      }
      @Override public boolean onShowFileChooser(WebView w, android.webkit.ValueCallback<Uri[]> callback, FileChooserParams params) {
        if(uploadFiles != null) uploadFiles.onReceiveValue(null);
        uploadFiles = callback;
        try {startActivityForResult(params.createIntent(), PICK_FILE_CODE);return true;}
        catch(Exception e){uploadFiles.onReceiveValue(null);uploadFiles=null;return false;}
      }
    });
    web.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
      try {openExternal(Uri.parse(url));} catch(Exception e) {Toast.makeText(this,"Download link could not open",Toast.LENGTH_LONG).show();}
    });
    web.loadUrl(APP_URL);
  }
  private void approveCamera() {
    if(pendingCamera != null) {
      pendingCamera.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});pendingCamera=null;
    }
  }
  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
    super.onRequestPermissionsResult(requestCode, permissions, results);
    if(requestCode == CAMERA_CODE && pendingCamera != null) {
      if(results.length>0 && results[0]==PackageManager.PERMISSION_GRANTED) approveCamera();
      else {pendingCamera.deny();pendingCamera=null;}
    }
  }
  @Override protected void onActivityResult(int requestCode,int resultCode,Intent data) {
    super.onActivityResult(requestCode,resultCode,data);
    if(requestCode==PICK_FILE_CODE && uploadFiles != null) {
      uploadFiles.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode,data));uploadFiles=null;
    }
  }
  private void openExternal(Uri uri) {
    try {startActivity(new Intent(Intent.ACTION_VIEW, uri));}
    catch(Exception e){Toast.makeText(this,"A browser is needed to complete this action",Toast.LENGTH_LONG).show();}
  }
  @Override public void onBackPressed() {
    if(web != null && web.canGoBack()) web.goBack(); else super.onBackPressed();
  }
  @Override protected void onDestroy() {
    if(web != null){web.destroy(); web=null;} super.onDestroy();
  }
}
