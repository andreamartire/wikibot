package techbrain.wikibot;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import newtech.audiolibrary.R;

/**
 * Created by andrea on 03/12/17.
 */

public class WebViewActivity extends Activity {

    final static String URL = "URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        String url = (String) getIntent().getSerializableExtra(URL);

        WebView webView = (WebView) findViewById(R.id.webviewElement);

        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
