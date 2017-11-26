package techbrain.wikibot;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
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


        final EditText ediBox = (EditText) findViewById(R.id.editBox);

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update list
                listItems.add(ediBox.getText().toString());
                //clear edit box
                ediBox.setText("");
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}