package techbrain.wikibot.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.lang.annotation.ElementType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import techbrain.wikibot.ChatActivity;
import techbrain.wikibot.R;
import techbrain.wikibot.beans.MessageElement;
import techbrain.wikibot.beans.MessageType;
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

        if (convertView == null){
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.single_element, null);
        }

        if(position >= elements.size()){
            System.out.println("");
        }else{
            final MessageElement element = elements.get(position);

            if (element != null){

                LinearLayout previewView = (LinearLayout) convertView.findViewById(R.id.previewView);
                TextView userTextElement = (TextView) convertView.findViewById(R.id.userTextView);
                TextView dayElement = (TextView) convertView.findViewById(R.id.dayView);
                TextView titleElement = (TextView) convertView.findViewById(R.id.titleView);
                TextView descrElement = (TextView) convertView.findViewById(R.id.descrView);
                TextView sourceElement = (TextView) convertView.findViewById(R.id.sourceView);
                ImageView imageElement = (ImageView) convertView.findViewById(R.id.imageView);
                ImageView previewImageElement = (ImageView) convertView.findViewById(R.id.previewImageView);

//                ShareElementButton shareButton = (ShareElementButton) convertView.findViewById(R.id.shareButton);
//
//                if(shareButton != null){
//                    shareButton.setMessageElement(element);
//                }

                TextView shareButton = (TextView) convertView.findViewById(R.id.shareButton);
                if(shareButton != null){
                    shareButton.setTag(element);
                }

                shareButton.setVisibility(View.VISIBLE);

                shareButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                    //get the row the clicked button is in
                    TextView shareButton = (TextView) v;

                    MessageElement messageElement = (MessageElement) shareButton.getTag();

                    if(messageElement != null){
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");

                        //TODO review message
                        String shareBodyText = messageElement.getMessageValue() + " " +
                                context.getResources().getString(R.string.shared_by_message) + " " +
                                context.getResources().getString(R.string.app_name) + " " +
                                context.getResources().getString(R.string.app_url);

                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                        context.startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
                    }
                    }
                });

                String value = element.getMessageValue();

                switch (element.getMessageType()) {
                    case USERTEXT:
                        userTextElement.setText(value);
                        userTextElement.setVisibility(View.VISIBLE);
                        previewView.setBackgroundResource(R.drawable.usertext_element_rounded);
                        titleElement.setVisibility(View.GONE);
                        descrElement.setVisibility(View.GONE);
                        sourceElement.setVisibility(View.GONE);
                        imageElement.setVisibility(View.GONE);
                        shareButton.setVisibility(View.GONE);
                        previewImageElement.setVisibility(View.GONE);
                        dayElement.setVisibility(View.GONE);
                        break;
                    case BOTTEXT:
                        titleElement.setText(value);
                        titleElement.setGravity(Gravity.LEFT);
                        titleElement.setVisibility(View.VISIBLE);
                        previewView.setBackgroundResource(R.drawable.descr_element_rounded);
                        userTextElement.setVisibility(View.GONE);
                        descrElement.setVisibility(View.GONE);
                        sourceElement.setVisibility(View.GONE);
                        imageElement.setVisibility(View.GONE);
                        previewImageElement.setVisibility(View.GONE);
                        dayElement.setVisibility(View.GONE);
                        break;
                    case WIKIURL:
                        try{
                            titleElement.setText(WikiUrlPreview.getPreviewBaseBey(value));
                            titleElement.setVisibility(View.VISIBLE);
                            previewView.setBackgroundResource(R.drawable.descr_element_rounded);
                            userTextElement.setVisibility(View.GONE);
                            imageElement.setVisibility(View.GONE);
                            descrElement.setVisibility(View.GONE);
                            descrElement.setBackgroundColor(Color.WHITE);
                            sourceElement.setVisibility(View.VISIBLE);
                            sourceElement.setText(WikiUrlPreview.getPreviewDomain(value).toUpperCase());
                            dayElement.setVisibility(View.GONE);

                            if(element.getLocalImageFilePath() != null){
                                //select current image
                                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                Bitmap bitmap = BitmapFactory.decodeFile(element.getLocalImageFilePath(), bmOptions);

                                if (bitmap != null) {
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();

                                    Drawable image = Drawable.createFromPath(element.getLocalImageFilePath());
                                    if (image != null) {
                                        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                                        //Integer realWidth = ImageUtils.getRealWidthSize(wm);
                                        Integer realWidth = new Float(ImageUtils.getRealWidthSize(wm) / (float) 3).intValue();

                                        float prop = height / (float) width;
                                        int customHeight = (int) (realWidth * prop);

                                        //select downloaded image
                                        previewImageElement.setImageDrawable(ImageUtils.scaleImage(context, image, realWidth, customHeight));
                                        previewImageElement.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                previewImageElement.setVisibility(View.GONE);
                                previewImageElement.setImageDrawable(null);
                            }

                            if(ChatActivity.isValidUrl(value)){
                                titleElement.setGravity(Gravity.RIGHT);

                                if(element.getPreviewTextHtml() != null && element.getPreviewTextHtml().trim().length()>0){
                                    descrElement.setText(Html.fromHtml(element.getPreviewTextHtml()));
                                    descrElement.setVisibility(View.VISIBLE);
                                    descrElement.setBackgroundColor(Color.WHITE);
                                    titleElement.setVisibility(View.GONE);
                                }
                            }

                            if(element.getPreviewDone() == 0){
                                boolean scrollDown = position >= elements.size()-2;

                                new WikiUrlPreview().injectPreview(context, this, element, titleElement, descrElement, scrollDown);
                            }
                        }catch (Throwable e){
                            e.printStackTrace();
                        }
                        break;
                    case NOCYLEURL:
                        try{
                            titleElement.setText(WikiUrlPreview.getPreviewBaseBey(value));
                            titleElement.setVisibility(View.VISIBLE);
                            previewView.setBackgroundResource(R.drawable.descr_element_rounded);
                            userTextElement.setVisibility(View.GONE);
                            imageElement.setVisibility(View.GONE);
                            descrElement.setVisibility(View.GONE);
                            sourceElement.setVisibility(View.VISIBLE);
                            sourceElement.setText(WikiUrlPreview.getPreviewDomain(value));
                            previewImageElement.setVisibility(View.GONE);
                            dayElement.setVisibility(View.GONE);
                        }catch (Throwable e){
                            e.printStackTrace();
                        }
                        break;
                    case IMAGE:
                        try {
                            previewView.setBackgroundResource(R.drawable.descr_element_rounded);
                            userTextElement.setVisibility(View.GONE);
                            titleElement.setVisibility(View.GONE);
                            descrElement.setVisibility(View.GONE);
                            sourceElement.setVisibility(View.GONE);
                            previewImageElement.setVisibility(View.GONE);
                            dayElement.setVisibility(View.GONE);
                            //select current image
                            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                            Bitmap bitmap = BitmapFactory.decodeFile(value, bmOptions);

                            if (bitmap != null) {
                                int width = bitmap.getWidth();
                                int customHeight = bitmap.getHeight();

                                Drawable image = Drawable.createFromPath(value);
                                if (image != null) {
                                    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                                    Integer realWidth = ImageUtils.getRealWidthSize(wm);

                                    float prop = customHeight / (float) width;
                                    customHeight = (int) (realWidth * prop);
                                    image = ImageUtils.scaleImage(context, image, realWidth, customHeight);

                                    //select downloaded image
                                    imageElement.setImageDrawable(image);
                                    imageElement.setVisibility(View.VISIBLE);
                                }
                            }
                        }catch (Throwable e){
                            e.printStackTrace();
                        }

                        break;
                    case PROVERB:
                        SpannableString spanStringProv = new SpannableString(value);
                        //spanStringProv.setSpan(new UnderlineSpan(), 0, spanStringProv.length(), 0);
                        spanStringProv.setSpan(new StyleSpan(Typeface.BOLD), 0, spanStringProv.length(), 0);
                        titleElement.setText(spanStringProv);
                        titleElement.setVisibility(View.VISIBLE);
                        userTextElement.setVisibility(View.GONE);
                        previewView.setBackgroundResource(R.drawable.descr_element_rounded);
                        imageElement.setVisibility(View.GONE);
                        descrElement.setVisibility(View.GONE);
                        sourceElement.setVisibility(View.GONE);
                        previewImageElement.setVisibility(View.GONE);
                        dayElement.setVisibility(View.GONE);
                        break;
                    case QUOTE:
                        SpannableString spanStringQuote = new SpannableString(value);
                        spanStringQuote.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanStringQuote.length(), 0);
                        titleElement.setText(spanStringQuote);
                        titleElement.setVisibility(View.VISIBLE);
                        previewView.setBackgroundResource(R.drawable.descr_element_rounded);
                        userTextElement.setVisibility(View.GONE);
                        imageElement.setVisibility(View.GONE);
                        descrElement.setVisibility(View.GONE);
                        sourceElement.setVisibility(View.GONE);
                        previewImageElement.setVisibility(View.GONE);
                        dayElement.setVisibility(View.GONE);
                        break;
                    case DATE:
                        Calendar cal1 = Calendar.getInstance();
                        Calendar cal2 = Calendar.getInstance();
                        cal1.setTime(element.getCreationDate());

                        String dayString = new SimpleDateFormat("dd MMMM yyyy").format(element.getCreationDate()).toUpperCase();

                        if(cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)){
                            dayString = context.getResources().getString(R.string.today_label);
                        }

                        dayElement.setText(dayString);
                        dayElement.setVisibility(View.VISIBLE);

                        titleElement.setVisibility(View.GONE);
                        previewView.setBackgroundResource(R.drawable.descr_element_rounded);
                        userTextElement.setVisibility(View.GONE);
                        imageElement.setVisibility(View.GONE);
                        descrElement.setVisibility(View.GONE);
                        sourceElement.setVisibility(View.GONE);
                        shareButton.setVisibility(View.GONE);
                        previewImageElement.setVisibility(View.GONE);
                        break;
                    default:
                        titleElement.setText(value);
                        titleElement.setVisibility(View.VISIBLE);
                        userTextElement.setVisibility(View.GONE);
                        imageElement.setVisibility(View.GONE);
                        descrElement.setVisibility(View.GONE);
                        sourceElement.setVisibility(View.GONE);
                        previewImageElement.setVisibility(View.GONE);
                        dayElement.setVisibility(View.GONE);
                        break;
                }
            }
        }

        return convertView;
    }
}
