package techbrain.wikibot.delegates;

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
