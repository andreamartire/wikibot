package techbrain.wikibot.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import techbrain.wikibot.ChatActivity;
import techbrain.wikibot.R;
import techbrain.wikibot.beans.MessageElement;
import techbrain.wikibot.delegates.WikiUrlPreview;
import techbrain.wikibot.utils.ImageUtils;

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

        //if (convertView == null){
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.single_element, null);
        //}

        if(position >= elements.size()){
            System.out.println("");
        }else{
            final MessageElement element = elements.get(position);

            if (element != null){

                TextView titleElement = (TextView) convertView.findViewById(R.id.titleView);
                TextView descrElement = (TextView) convertView.findViewById(R.id.descrView);
                ImageView imageElement = (ImageView) convertView.findViewById(R.id.imageView);

                String value = element.getValue();

                switch (element.getType()) {
                    case USERTEXT:
                        titleElement.setText(value);
                        titleElement.setGravity(Gravity.RIGHT);
                        titleElement.setVisibility(View.VISIBLE);
                        break;
                    case BOTTEXT:
                        titleElement.setText(value);
                        titleElement.setGravity(Gravity.LEFT);
                        titleElement.setVisibility(View.VISIBLE);
                        break;
                    case URL:
                        if(ChatActivity.isValidUrl(value)){
                            TextView urlElement = (TextView) convertView.findViewById(R.id.urlView);
                            urlElement.setText(value);

                            titleElement.setGravity(Gravity.RIGHT);

                            String[] splits = value.split("/wiki/");
                            if(splits.length > 1){
                                try{
                                    //String text = splits[1].replaceAll("_"," ");
                                    //text = Uri.decode(text);

                                    String baseKey = splits[1];
                                    String text = baseKey.replaceAll("_"," ");
                                    text = Uri.decode(text);

                                    titleElement.setText(text);

                                    new WikiUrlPreview().injectPreview(context, baseKey, titleElement, descrElement);
                                }catch (Throwable e){
                                    e.printStackTrace();
                                }
                            }

                            //https://it.wikipedia.org/api/rest_v1/page/summary/Italia
                        }
                        //titleElement.setVisibility(View.VISIBLE);
                        descrElement.setVisibility(View.VISIBLE);
                        break;
                    case IMAGE:
                        //select current image
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(value,bmOptions);

                        if(bitmap != null){
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();

                            Drawable image = Drawable.createFromPath(value);
                            if(image != null){
                                float prop = height/(float)width;

                                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                                Integer realWidth = ImageUtils.getRealWidthSize(wm);
                                int customHeight = (int) (realWidth*prop);

                                //select downloaded image
                                imageElement.setImageDrawable(ImageUtils.scaleImage(context, image, realWidth, customHeight));
                                imageElement.setVisibility(View.VISIBLE);
                            }
                        }

                        break;
                    case PROVERB:
                        SpannableString spanStringProv = new SpannableString(value);
                        //spanStringProv.setSpan(new UnderlineSpan(), 0, spanStringProv.length(), 0);
                        spanStringProv.setSpan(new StyleSpan(Typeface.BOLD), 0, spanStringProv.length(), 0);
                        titleElement.setText(spanStringProv);
                        titleElement.setVisibility(View.VISIBLE);
                        break;
                    case QUOTE:
                        SpannableString spanStringQuote = new SpannableString(value);
                        spanStringQuote.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanStringQuote.length(), 0);
                        titleElement.setText(spanStringQuote);
                        titleElement.setVisibility(View.VISIBLE);
                        break;
                    case DATE:
                        titleElement.setText(value);
                        titleElement.setGravity(Gravity.CENTER_HORIZONTAL);
                        titleElement.setVisibility(View.VISIBLE);
                        break;
                    default:
                        titleElement.setText(value);
                        titleElement.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }

        return convertView;
    }
}
