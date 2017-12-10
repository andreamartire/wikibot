package techbrain.wikibot.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatUtils {

    public static void saveChat(Context context, List<String> chats) {
        String chatFilePath = context.getFilesDir().getAbsolutePath() + File.separator + "chats.dat";

        MyFileUtils.deleteFileIfExists(chatFilePath);

        try{
            FileWriter fw = new FileWriter(chatFilePath);
            fw.write(TextUtils.join("\n", chats));
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getSavedChat(Context context){
        String chatFilePath = context.getFilesDir().getAbsolutePath() + File.separator + "chats.dat";

        ArrayList<String> list = new ArrayList<>();
        try{
            if(MyFileUtils.exists(chatFilePath)){
                String fileContent = MyFileUtils.getStringFromFile(chatFilePath);
                list = new ArrayList<String>(Arrays.asList(TextUtils.split(fileContent, "\n")));
            }
        }catch (Exception e){
            //nothing
            e.printStackTrace();
        }

        return list;
    }
}
