package techbrain.wikibot.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import techbrain.wikibot.ChatActivity;
import techbrain.wikibot.R;
import techbrain.wikibot.beans.MessageElement;

public class ElementAdapter extends ArrayAdapter<MessageElement> {

    private Context context;
    private int resource;
    private ArrayList<MessageElement> elements;

    public ElementAdapter(Context context, int resource, ArrayList<MessageElement> elements) {
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
            final MessageElement element = elements.get(position);

            if (element != null){

                TextView titleElement = (TextView) convertView.findViewById(R.id.titleView);
                String value = element.getValue();

                switch (element.getType()) {
                    case USERTEXT:
                        titleElement.setText(value);
                        titleElement.setGravity(Gravity.RIGHT);
                        break;
                    case BOTTEXT:
                        titleElement.setText(value);
                        titleElement.setGravity(Gravity.LEFT);
                        break;
                    case URL:
                        if(ChatActivity.isValidUrl(value)){
                            TextView urlElement = (TextView) convertView.findViewById(R.id.urlView);
                            urlElement.setText(value);

                            titleElement.setGravity(Gravity.RIGHT);

                            String[] splits = value.split("/wiki/");
                            if(splits.length > 1){
                                String text = splits[1].replaceAll("_"," ");
                                text = Uri.decode(text);
                                titleElement.setText(text);
                            }
                        }
                        break;
                    case IMAGE:
                        titleElement.setText(value);
                        break;
                    case PROVERB:
                        SpannableString spanStringProv = new SpannableString(value);
                        //spanStringProv.setSpan(new UnderlineSpan(), 0, spanStringProv.length(), 0);
                        spanStringProv.setSpan(new StyleSpan(Typeface.BOLD), 0, spanStringProv.length(), 0);
                        titleElement.setText(spanStringProv);
                        break;
                    case QUOTE:
                        SpannableString spanStringQuote = new SpannableString(value);
                        spanStringQuote.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanStringQuote.length(), 0);
                        titleElement.setText(spanStringQuote);
                        break;
                    case DATE:
                        titleElement.setText(value);
                        titleElement.setGravity(Gravity.CENTER_HORIZONTAL);
                        break;
                    default:
                        titleElement.setText(value);
                        break;
                }
            }
        }

        return convertView;
    }
}
