package techbrain.wikibot.delegates;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import techbrain.wikibot.beans.MessageElement;
import techbrain.wikibot.dao.MessageElementDao;

public class WikiUrlPreview {

	static String WIKI_SUMMARY_PREFIX = "https://it.wikipedia.org/api/rest_v1/page/summary/";

	public void injectPreview(Context context, MessageElement messageElement, TextView descrElement) {
		// call AsynTask to perform network operation on separate thread
		new HttpAsyncTask(context, messageElement, descrElement).execute();
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {

		Context context;
		MessageElement messageElement;
		TextView descrElement;

		public HttpAsyncTask(Context context, MessageElement messageElement, TextView descrElement){
			super();
			this.context = context;
			this.messageElement = messageElement;
			this.descrElement = descrElement;
		}

		@Override
		protected String doInBackground(String... urls) {
			String remoteUrl = messageElement.getMessageValue();
            if(remoteUrl != null){
                String[] splits = remoteUrl.split("/wiki/");
                if(splits.length > 1) {
                    String baseKey = splits[1];
                    String text = baseKey.replaceAll("_", " ");
                    baseKey = Uri.decode(text);

                    return GET(WIKI_SUMMARY_PREFIX + baseKey);
                }
            }

			return "";
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

							if(element != null){
								String extractHtml = element.getExtract_html();

								if (extractHtml != null){
									if (extractHtml.contains("</p>")) {
										extractHtml = extractHtml.split("</p>")[0];
									}
								}

								messageElement.setPreviewText(element.getExtract());
								messageElement.setPreviewTextHtml(extractHtml);

								descrElement.setText(Html.fromHtml(extractHtml));
								descrElement.setVisibility(View.VISIBLE);

								MessageElementDao.getInstance((Activity)context).update(messageElement);
							}
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