package techbrain.wikibot.delegates;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import techbrain.wikibot.adapters.ElementAdapter;
import techbrain.wikibot.beans.MessageElement;
import techbrain.wikibot.dao.MessageElementDao;
import techbrain.wikibot.task.DownloadImageTask;
import techbrain.wikibot.utils.ImageUtils;

public class WikiUrlPreview {

	static String WIKI_SUMMARY_PREFIX_IT = "https://it.wikipedia.org/api/rest_v1/page/summary/";
	static String WIKI_SUMMARY_PREFIX_EN = "https://en.wikipedia.org/w/api.php?format=json&formatversion=2&action=query&prop=revisions|pageprops|info|images|categories&rvprop=ids|timestamp|flags|comment|user|content&cllimit=max&imlimit=max&titles=";

	//example
	//https://en.wikipedia.org/w/api.php?format=json&formatversion=2&action=query&prop=revisions|pageprops|info|images|categories&rvprop=ids|timestamp|flags|comment|user|content&cllimit=max&imlimit=max&titles=Eastbourne_manslaughter

	//to html
	//https://www.mediawiki.org/w/api.php?action=parse&text=The%20%3Ccode%3Euselang%3C/code%3E%20parameter%20from%20the%20[[API:Main%20module|main%20module]]%20&title=italia

	private ElementAdapter elementAdapter;

	public void injectPreview(Context context, ElementAdapter elementAdapter, MessageElement messageElement, TextView titleElement, TextView descrElement, boolean scrollDown) {
		this.elementAdapter = elementAdapter;
		// call AsynTask to perform network operation on separate thread
		new HttpAsyncTask(context, messageElement, titleElement, descrElement, scrollDown).execute();
	}

	public static String getPreviewBaseBey(String remoteUrl){
		String[] splits = remoteUrl.split("/wiki/");
		if(splits.length > 1) {
			String baseKey = splits[1];
			String text = baseKey.replaceAll("_", " ");
			baseKey = Uri.decode(text);

			return baseKey;
		}
		return "";
	}

    public static String getPreviewDomain(String remoteUrl) {
        String[] splits = remoteUrl.split("//");
        if(splits.length > 1) {
            String baseKey = splits[1];
            String[] elements = splits[1].split("/");
            //String text = baseKey.replaceAll("_", " ");
            baseKey = Uri.decode(elements[0]);

            return baseKey;
        }
        return "";
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

		Context context;
		MessageElement messageElement;
		TextView titleElement;
		TextView descrElement;
        boolean scrollDown;

		public HttpAsyncTask(Context context, MessageElement messageElement, TextView titleElement, TextView descrElement, boolean scrollDown){			super();
			this.context = context;
			this.messageElement = messageElement;
			this.titleElement = titleElement;
			this.descrElement = descrElement;
            this.scrollDown = scrollDown;
		}

		@Override
		protected String doInBackground(String... urls) {
			String remoteUrl = messageElement.getMessageValue();
            if(remoteUrl != null){
                String[] splits = remoteUrl.split("/wiki/");
                if(splits.length > 1) {
                    return GET(WIKI_SUMMARY_PREFIX_IT + getPreviewBaseBey(remoteUrl));
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

					final SummaryItWiki element = new Gson().fromJson(result, SummaryItWiki.class);

					String extractHtml = null;
                    String extractText = null;
					String imageFilePath = null;

					if(element != null) {
						String imageUrl = element.getThumbnail().getSource();
						if (imageUrl != null) {
							String imageFileName = ImageUtils.getFileNameFromUrl(imageUrl);
							imageFilePath = context.getFilesDir().getAbsolutePath() + "/preview/" + imageFileName;

							if (!new File(imageFilePath).exists()) {
								try {
									DownloadImageTask sdt = new DownloadImageTask(context, new URL(imageUrl), imageFilePath, null);
									String[] var = new String[0];
									sdt.doInBackground(var);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							extractHtml = element.getExtract_html();
                            extractText = element.getExtract();

                            int limit = 210;

							if (extractHtml != null && extractHtml.trim().length() > 0) {
								if (extractHtml.contains("</p>")) {

									extractHtml = extractHtml.split("</p>")[0];

									if (extractHtml.split("</p>")[0].length() > limit) {
										extractHtml = extractHtml.substring(0, limit) + "...</p>";
									}
								} else {
									if (extractHtml.length() > limit) {
										extractHtml = extractHtml.substring(0, limit) + "...</p>";
									}
								}
							}

                            if (extractText != null && extractText.trim().length() > limit) {
                                extractText = extractText.substring(0, limit) + "...";
                            }
						}
					}

					final String valueExtractHtml = extractHtml;
                    final String valueExtractText = extractText;
					final String valueImageFilePath = imageFilePath;

					((Activity)context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {

								messageElement.setLocalImageFilePath(valueImageFilePath);
								messageElement.setPreviewText(valueExtractText);
								messageElement.setPreviewTextHtml(valueExtractHtml);
								messageElement.setPreviewDone(1);

								if(valueExtractHtml != null){
									descrElement.setText(Html.fromHtml(valueExtractHtml));
								}
								descrElement.setVisibility(View.VISIBLE);

								titleElement.setText("");
								titleElement.setVisibility(View.GONE);

								if (scrollDown) {
									elementAdapter.notifyDataSetChanged();
								}

								MessageElementDao.getInstance((Activity) context).update(messageElement);

							} catch (Throwable ex){
								ex.printStackTrace();
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