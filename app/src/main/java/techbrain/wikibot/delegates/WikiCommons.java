package techbrain.wikibot.delegates;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import techbrain.wikibot.R;
import techbrain.wikibot.task.DownloadImageTask;
import techbrain.wikibot.utils.ImageUtils;

public class WikiCommons {

	static String WIKI_COMMONS_PREFIX = "https://commons.wikimedia.org/wiki/Commons:Immagine_del_giorno";
    static String IMAGE_PATH = "/commons_images/";

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

					Pattern pattern = Pattern.compile("data-src=\"(.+)\" data-alt");

					List<String> urlList = new LinkedList<>();
					if(content != null){
						String[] elements = content.split("headline");
						if(elements != null){
							int size = elements.length;
							for(String element : elements){

								//search images
								Matcher matcherUrl = pattern.matcher(element);
								if(matcherUrl.find()){
									String url = matcherUrl.group(1);
									System.out.println(url);

									url = url.replace("thumb/", "");
									String[] sections = url.split("/");

									sections[sections.length-1] = "";
									url = TextUtils.join("/", sections);
									//remove last separator
									url = url.substring(0, url.length()-1);

									urlList.add(url);

									String imageFileName = ImageUtils.getFileNameFromUrl(url);
									String imageFilePath = myApp.getFilesDir().getAbsolutePath() + IMAGE_PATH + imageFileName;

									if(!new File(imageFilePath).exists()){
										//download file out of main thread
										try {
											DownloadImageTask sdt = new DownloadImageTask(myApp, new URL(url), imageFilePath, new Callable<Integer>() {
                                                @Override
                                                public Integer call() throws Exception {
                                                    //TODO
                                                    return 0;
                                                }
                                            });

											sdt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}

								System.out.println(element);
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

	public static java.lang.String getRandomItem(Context context) {
		String imagePath = context.getFilesDir().getAbsolutePath() + IMAGE_PATH;

		if(new File(imagePath).exists()){
			File[] files = new File(imagePath).listFiles();
			if(files != null && files.length > 0){
				File file = files[new Random().nextInt(files.length)];
				return file.getAbsolutePath();
			}
		}

		return null;
	}
};