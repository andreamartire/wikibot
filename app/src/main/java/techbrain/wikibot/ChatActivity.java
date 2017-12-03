package techbrain.wikibot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

import newtech.audiolibrary.R;
import techbrain.wikibot.utils.AppRater;
import techbrain.wikibot.utils.WikiConstants;
import techbrain.wikibot.utils.WikiProverbs;
import techbrain.wikibot.utils.WikiQuotes;

/**
 * Created by andrea on 18/10/17.
 */

public class ChatActivity extends Activity {

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_chat);

        final Context context = this;

        AppRater.app_launched(this);

        ImageButton infoButton = (ImageButton) findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.info_message)
                        .setTitle(R.string.info_title);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        final ListView list = (ListView) findViewById(R.id.listContents);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                //manage tap on audiobook list
                String value = (String) adapterView.getItemAtPosition(i);

                if(isValidUrl(value)){
                    //open brower
                    Intent browserIntent = new Intent(context, WebViewActivity.class);

                    //pass data thought intent to another activity
                    browserIntent.putExtra(WebViewActivity.URL, value);

                    startActivity(browserIntent);
                }
            }
        });

        addRandomProverb(listItems, adapter);
        addRandomQuote(listItems, adapter);
        addRandomCuriosita(listItems, adapter);

        Button curiositaBtn = (Button) findViewById(R.id.curiosita_button);
        curiositaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRandomCuriosita(listItems, adapter);
            }
        });

        Button citazioneBtn = (Button) findViewById(R.id.citazione_button);
        citazioneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRandomQuote(listItems, adapter);
            }
        });

        Button proverbBtn = (Button) findViewById(R.id.proverb_button);
        proverbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRandomProverb(listItems, adapter);
            }
        });

        Button nonciclopediaBtn = (Button) findViewById(R.id.nonciclopedia_button);
        nonciclopediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRandomNonciclopedia(listItems, adapter);
            }
        });

        final EditText ediBox = (EditText) findViewById(R.id.editBox);
        ediBox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    manageMessage(context,  ediBox);
                }
                return false;
            }
        });
        ediBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    manageMessage(context,  ediBox);
                }
                return false;
            }
        });

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageMessage(context, ediBox);
            }
        });
    }

    private void addRandomNonciclopedia(ArrayList<String> listItems, ArrayAdapter<String> adapter) {
        WikiConstants.getRandomNonciclopedia(listItems, adapter);
    }

    private void addRandomCuriosita(ArrayList<String> listItems, ArrayAdapter<String> adapter) {

        String randomItem = WikiConstants.getRandomItem();

        //update list
        listItems.add(randomItem);

        adapter.notifyDataSetChanged();
    }

    private void addRandomProverb(ArrayList<String> listItems, ArrayAdapter<String> adapter) {
        String randomItem = WikiProverbs.getRandomItem();

        //update list
        listItems.add(randomItem);

        adapter.notifyDataSetChanged();
    }

    private void addRandomQuote(ArrayList<String> listItems, ArrayAdapter<String> adapter) {

        String randomItem = WikiQuotes.getRandomItem();

        //update list
        listItems.add(randomItem);

        adapter.notifyDataSetChanged();
    }

    public void manageMessage(Context context, EditText editBox){
        String message = editBox.getText().toString();

        if(!message.isEmpty()){
            //update list
            listItems.add(message);
            adapter.notifyDataSetChanged();
            //clear edit box
            editBox.setText("");

            //hide keyboard
            InputMethodManager inputManager =
                    (InputMethodManager) context.
                            getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    editBox.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            try{
                RetrieveGoogleTask task = new RetrieveGoogleTask(context, listItems, adapter, message);
                task.execute();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    public static boolean isValidUrl(String uri) {
        final URL url;
        try {
            url = new URL(uri);
        } catch (Exception e1) {
            return false;
        }
        return "http".equals(url.getProtocol()) || "https".equals(url.getProtocol());
    }
}