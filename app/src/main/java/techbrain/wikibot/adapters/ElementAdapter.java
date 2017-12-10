package techbrain.wikibot.adapters;

import android.content.Context;
import android.os.PatternMatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import techbrain.wikibot.ChatActivity;
import techbrain.wikibot.R;

public class ElementAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource;
    private ArrayList<String> elements;

    public ElementAdapter(Context context, int resource, ArrayList<String> elements) {
        super(context, resource, elements);
        this.context = context;
        this.resource = resource;
        this.elements = elements;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if (convertView == null){
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.single_element, null);
        }

        if(position >= elements.size()){
            System.out.println("");
        }else{
            final String value = elements.get(position);

            if (value != null){
                TextView titleElement = (TextView) convertView.findViewById(R.id.titleView);
                titleElement.setText(value);

                if(ChatActivity.isValidUrl(value)){
                    TextView urlElement = (TextView) convertView.findViewById(R.id.urlView);
                    urlElement.setText(value);

                    titleElement.setGravity(Gravity.RIGHT);

                    String[] splits = value.split("/wiki/");
                    if(splits.length > 1){
                        titleElement.setText(splits[1].replaceAll("_"," "));
                    }
                }
            }
        }

        return convertView;
    }
}
