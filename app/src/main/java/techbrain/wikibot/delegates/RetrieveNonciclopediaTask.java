package techbrain.wikibot.delegates;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import techbrain.wikibot.constants.WikiConstants;

/**
 * Created by andrea on 02/12/17.
 */

public class RetrieveNonciclopediaTask extends AsyncTask<String, Void, String> {

    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;

    public RetrieveNonciclopediaTask(ArrayList<String> listItems, ArrayAdapter<String> adapter){
        this.listItems = listItems;
        this.adapter = adapter;
    }

    protected String doInBackground(String... urls) {
        InputStream is = null;
        String item = null;

        try {
            URLConnection con = new URL(WikiConstants.NONCICLOPEDIA_URL ).openConnection();
            System.out.println( "orignal url: " + con.getURL() );
            con.connect();
            System.out.println( "connected url: " + con.getURL() );
            is = con.getInputStream();
            item = con.getURL().toString();
            System.out.println( "redirected url: " + con.getURL() );

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();e.printStackTrace();
                }
            }
        }

        return item;
    }

    protected void onPostExecute(String item) {

        //update list
        listItems.add(item);

        adapter.notifyDataSetChanged();
    }
}
