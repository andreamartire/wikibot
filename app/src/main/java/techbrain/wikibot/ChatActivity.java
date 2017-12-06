package techbrain.wikibot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

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

public class ChatActivity extends AppCompatActivity {

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Context me = this;

        switch (item.getItemId()) {
            case R.id.shareElement:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                String shareBodyText = getResources().getString(R.string.share_message);

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
                return true;

            case R.id.infoElement:
                //Toast.makeText(me, "Tutti i contenuti audio e le immagini sono liberamente accessibili in rete e scaricati direttamente dai siti web dei rispettivi possessori dei diritti. Nessun contenuto è ospitato su server dell'applicazione", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(me);
                builder.setMessage(R.string.info_message)
                        .setTitle(R.string.info_title);

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            case R.id.contactElement:
                String email = getResources().getString(R.string.contact_email);
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Libro Parlante");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_chat);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.showOverflowMenu();
        setSupportActionBar(myToolbar);

        MobileAds.initialize(this, "ca-app-pub-1872225169177247~8401929001");

        AdView mAdView = (AdView) findViewById(R.id.mainChatAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final Context context = this;

        AppRater.app_launched(this);

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

        addRandomProverb(context, listItems, adapter);
        addRandomQuote(context, listItems, adapter);
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
                addRandomQuote(context, listItems, adapter);
            }
        });

        Button proverbBtn = (Button) findViewById(R.id.proverb_button);
        proverbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRandomProverb(context, listItems, adapter);
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

    private void addRandomProverb(Context context, ArrayList<String> listItems, ArrayAdapter<String> adapter) {
        String randomItem = WikiProverbs.getRandomItem();

        //update list
        listItems.add(randomItem);

        adapter.notifyDataSetChanged();

        try{
            RetrieveGoogleTask task = new RetrieveGoogleTask(context, listItems, adapter, randomItem);
            task.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addRandomQuote(Context context, ArrayList<String> listItems, ArrayAdapter<String> adapter) {

        String randomItem = WikiQuotes.getRandomItem();

        //update list
        listItems.add(randomItem);

        adapter.notifyDataSetChanged();

        try{
            RetrieveGoogleTask task = new RetrieveGoogleTask(context, listItems, adapter, randomItem);
            task.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
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