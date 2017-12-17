package techbrain.wikibot.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import techbrain.wikibot.R;
import techbrain.wikibot.beans.MessageElement;
import techbrain.wikibot.beans.MessageType;

public class ChatUtils {

    public static void appendMessage(Context context, MessageElement messageElement) {
        String chatFilePath = context.getFilesDir().getAbsolutePath() + File.separator + "chats.dat";

        try{
            FileOutputStream fileinput = new FileOutputStream(new File(chatFilePath),true);
            PrintStream printstream = new PrintStream(fileinput);
            printstream.print(messageElement.getType().toString() + "|" + messageElement.getValue() + "\n");
            fileinput.close();
            printstream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<MessageElement> getSavedChat(Context context){
        String chatFilePath = context.getFilesDir().getAbsolutePath() + File.separator + "chats.dat";

        ArrayList<MessageElement> elementList = new ArrayList<>();
        try{
            if(MyFileUtils.exists(chatFilePath)){
                String fileContent = MyFileUtils.getStringFromFile(chatFilePath);

                List<String> strList = Arrays.asList(TextUtils.split(fileContent, "\n"));
                if(strList != null){
                    for(String row : strList){
                        String columns[] = row.split("\\|");
                        if(columns != null && columns.length >= 2){
                            String type = columns[0];
                            String value = columns[1];

                            MessageType messageType = MessageType.valueOf(type);
                            if(messageType != null){
                                MessageElement me = new MessageElement(messageType, value);
                                elementList.add(me);
                            }
                        }
                    }
                }

                //remove empty last element
                if(elementList.size() > 1){
                    if("".equals(elementList.get(elementList.size()-1))){
                        elementList.remove(elementList.size()-1);
                    }
                }
            }
        }catch (Exception e){
            //nothing
            e.printStackTrace();
        }

        return elementList;
    }

    static ArrayList<String> smalltalks;

    public static String getRandomSmallTalk(Context c) {
        initSmalltalks(c);
        Collections.shuffle(smalltalks);
        return smalltalks.get(0);
    }

    private static void initSmalltalks(Context c) {
        if(smalltalks == null){
            String[] mTestArray = c.getResources().getStringArray(R.array.smalltalk);
            smalltalks = new ArrayList<>(Arrays.asList(mTestArray));
        }
    }
}
