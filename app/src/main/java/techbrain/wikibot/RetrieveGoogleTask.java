package techbrain.wikibot;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import techbrain.wikibot.utils.WikiConstants;

/**
 * Created by andrea on 02/12/17.
 */

public class RetrieveGoogleTask extends AsyncTask<String, Void, String> {

    Context context;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    String message;

    public RetrieveGoogleTask(Context context, ArrayList<String> listItems, ArrayAdapter<String> adapter, String message){
        this.context = context;
        this.listItems = listItems;
        this.adapter = adapter;
        this.message = message;
    }

    protected String doInBackground(String... urls) {
        InputStream is = null;
        String item = null;

        try{
            URL url = new URL("https://www.googleapis.com/customsearch/v1?key="+WikiConstants.GAK+ "&cx=" +  WikiConstants.CX + "&q="+ message + "&alt=json");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");

            String firstLink = null;
            while ((output = br.readLine()) != null) {

                if(output.contains("\"link\": \"")){
                    String link=output.substring(output.indexOf("\"link\": \"")+("\"link\": \"").length(), output.indexOf("\","));
                    System.out.println(link);       //Will print the google search links

                    if(firstLink == null
                            && !link.contains("wiki/Discussione")
                            && !link.contains("wiki/Discussioni_")
                            && !link.contains("wiki/Template")
                            && !link.contains("wiki/Wikipedia:Bar")
                            && !link.contains("wiki/File:")
                            && !link.contains("wiki/Utente:")){
                        firstLink = link;
                        //update list
                        listItems.add(firstLink);
                        ChatActivity.saveChat(context, listItems);
                    }
                }
            }
            conn.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

        return item;
    }

    protected void onPostExecute(String item) {

    }
}
