package techbrain.wikibot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import newtech.audiolibrary.R;

/**
 * Created by andrea on 03/12/17.
 */

public class WebViewActivity extends AppCompatActivity {

    final static String URL = "URL";

    String currentUrl;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Context me = this;

        switch (item.getItemId()) {
            case R.id.shareElement:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, currentUrl);
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.webview_toolbar);
        myToolbar.showOverflowMenu();
        setSupportActionBar(myToolbar);

        AdView mAdView = (AdView) findViewById(R.id.webviewAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final Context me = this;

        currentUrl = (String) getIntent().getSerializableExtra(URL);

        try{
            WebView webView = (WebView) findViewById(R.id.webviewElement);

            // Enable Javascript
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            // Force links and redirects to open in the WebView instead of in a browser
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);

                    Toast.makeText(me, R.string.network_error, Toast.LENGTH_LONG).show();
                    ((Activity) me).finish();
                }
            });

            webView.loadUrl(currentUrl);
        }catch (Throwable t){
            t.printStackTrace();
            Toast.makeText(this, "Download error: " + t.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
