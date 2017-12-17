package techbrain.wikibot.delegates;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import techbrain.wikibot.R;

public class WikiCommons {

	static String WIKI_COMMONS_PREFIX = "https://commons.wikimedia.org/wiki/Commons:Immagine_del_giorno";

	public static String getImageOfDayUrl(final Activity myApp ){
		String imageUrl = "";

		try {

			/* An instance of this class will be registered as a JavaScript interface */
			class MyJavaScriptInterface
			{
				@JavascriptInterface
				@SuppressWarnings("unused")
				public void processHTML(String content)
				{
					// process the html as needed by the app
					System.out.println(content);

					if(content != null){
						String[] elements = content.split("headline");
						if(elements != null){
							int size = elements.length;
							for(String element : elements){
								String[] sections = element.split("\" class=\"image\"");
								String[] links = sections[0].split("><a href=\"");
								System.out.println(links[1]);
							}
						}
					}
				}
			}

			final WebView browser = (WebView) myApp.findViewById(R.id.hiddenWebview);
			/* JavaScript must be enabled if you want it to work, obviously */
			browser.getSettings().setJavaScriptEnabled(true);

			/* Register a new JavaScript interface called HTMLOUT */
			browser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

			/* WebViewClient must be set BEFORE calling loadUrl! */
			browser.setWebViewClient(new WebViewClient() {
				@Override
				public void onPageFinished(WebView view, String url)
				{
					/* This call inject JavaScript into the page which just finished loading. */
					browser.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
				}
			});

			/* load a web page */
			browser.loadUrl(WIKI_COMMONS_PREFIX);
		}catch ( Exception ex ) {
			ex.printStackTrace();
		}

		return imageUrl;
	}
};