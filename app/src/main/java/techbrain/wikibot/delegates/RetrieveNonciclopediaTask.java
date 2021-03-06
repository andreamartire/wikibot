package techbrain.wikibot.delegates;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import techbrain.wikibot.ChatActivity;
import techbrain.wikibot.beans.MessageElement;
import techbrain.wikibot.beans.MessageType;
import techbrain.wikibot.dao.MessageElementDao;

/**
 * Created by andrea on 02/12/17.
 */

public class RetrieveNonciclopediaTask extends AsyncTask<String, Void, String> {

    ArrayList<MessageElement> listItems;
    ArrayAdapter<MessageElement> adapter;
    Activity activity;
    WikiLangEnum lang;

    public RetrieveNonciclopediaTask(Activity activity, ArrayList<MessageElement> listItems, ArrayAdapter<MessageElement> adapter, WikiLangEnum lang){
        this.listItems = listItems;
        this.adapter = adapter;
        this.activity = activity;
        this.lang = lang;
    }

    protected String doInBackground(String... urls) {
        InputStream is = null;
        String item = null;

        try {
            URLConnection con = new URL(WikiConstants.getUnCiclopediaUrlByLang(lang)).openConnection();
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
        MessageElement element = new MessageElement(MessageType.NOCYLEURL, item);
        ChatActivity.addMessage(activity, listItems, element);
        MessageElementDao.getInstance(activity).save(element);

        adapter.notifyDataSetChanged();
    }
}
