package techbrain.wikibot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import newtech.audiolibrary.R;

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

        final ListView list = (ListView) findViewById(R.id.listContents);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        list.setAdapter(adapter);

        final Context context = this;

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

    public void manageMessage(Context context, EditText editBox){
        String message = editBox.getText().toString();

        if(!message.isEmpty()){
            //update list
            listItems.add(message);
            //clear edit box
            editBox.setText("");

            //hide keyboard
            InputMethodManager inputManager =
                    (InputMethodManager) context.
                            getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    editBox.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}