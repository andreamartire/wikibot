package techbrain.wikibot.delegates;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class WikiUrlPreview {

	static String WIKI_SUMMARY_PREFIX = "https://it.wikipedia.org/api/rest_v1/page/summary/";

	public void injectPreview(Context context, String article, TextView titleElement, TextView descrElement) {
		// call AsynTask to perform network operation on separate thread
		new HttpAsyncTask(context, WIKI_SUMMARY_PREFIX + article, titleElement, descrElement).execute();
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {

		Context context;
		String articleUrl;
		TextView titleElement;
		TextView descrElement;

		public HttpAsyncTask(Context context, String article, TextView titleElement, TextView descrElement){
			super();
			this.context = context;
			this.articleUrl = article;
			this.titleElement = titleElement;
			this.descrElement = descrElement;
		}

		@Override
		protected String doInBackground(String... urls) {

			return GET(articleUrl);
		}

		public String GET(String url){

			try {

				/* Open a connection to that URL. */
				URLConnection ucon = new URL(url).openConnection();

				/*
				 * Define InputStreams to read from the URLConnection.
				 */
				InputStream inputStream = ucon.getInputStream();

				// convert inputstream to string
				if(inputStream != null) {
					final String result = convertInputStreamToString(inputStream);

					((Activity)context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								final SummaryItWiki element = new Gson().fromJson(result, SummaryItWiki.class);

								String extract = element.getExtract_html();

								if (extract != null){
									if (extract.contains("</p>")) {
										extract = extract.split("</p>")[0];
									}
								}
								descrElement.setText(Html.fromHtml(extract));
							}catch (Throwable e){
								e.printStackTrace();
							}
						}
					});
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}

			return "";
		}

		private String convertInputStreamToString(InputStream inputStream) throws IOException {
			BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
			String line = "";
			String result = "";
			while((line = bufferedReader.readLine()) != null)
				result += line;

			inputStream.close();
			return result;

		}
	}
};