package techbrain.wikibot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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
            final String link = elements.get(position);

            if (link != null){

                TextView bookTitle = (TextView) convertView.findViewById(R.id.bookTitleView);
                bookTitle.setText(link);
            }
        }

        return convertView;
    }
}
