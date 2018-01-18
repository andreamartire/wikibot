package techbrain.wikibot.dao;

import techbrain.wikibot.beans.MessageElement;
import techbrain.wikibot.beans.MessageElement.MessageElementEntry;
import techbrain.wikibot.beans.MessageType;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class MessageElementDao extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "wikibot.db";

    public static MessageElementDao messageElementDbHelper;

    public static MessageElementDao getInstance(Context context){
        if(messageElementDbHelper == null){
            messageElementDbHelper = new MessageElementDao(context);
        }
        return messageElementDbHelper;
    }

    public long save(MessageElement messageElement){
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MessageElementEntry.COLUMN_NAME_MESSAGE_TYPE, messageElement.getMessageType().toString());
        values.put(MessageElementEntry.COLUMN_NAME_MESSAGE_VALUE, messageElement.getMessageValue());
        values.put(MessageElementEntry.COLUMN_NAME_REMOTE_IMAGE_URL, messageElement.getRemoteImageUrl());
        values.put(MessageElementEntry.COLUMN_NAME_LOCAL_IMAGE_FILE_PATH, messageElement.getLocalImageFilePath());
        values.put(MessageElementEntry.COLUMN_NAME_PREVIEW_TEXT, messageElement.getPreviewText());
        values.put(MessageElementEntry.COLUMN_NAME_PREVIEW_TEXT_HTML, messageElement.getPreviewTextHtml());
        values.put(MessageElementEntry.COLUMN_NAME_CREATION_DATE, Calendar.getInstance().getTimeInMillis());

        // Insert the new row, returning the primary key value of the new row
        return db.insert(MessageElementEntry.TABLE_NAME, null, values);
    }

    public ArrayList<MessageElement> findAll(){
        ArrayList<MessageElement> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
            MessageElementEntry._ID,
            MessageElementEntry.COLUMN_NAME_MESSAGE_TYPE,
            MessageElementEntry.COLUMN_NAME_MESSAGE_VALUE,
            MessageElementEntry.COLUMN_NAME_REMOTE_IMAGE_URL,
            MessageElementEntry.COLUMN_NAME_LOCAL_IMAGE_FILE_PATH,
            MessageElementEntry.COLUMN_NAME_PREVIEW_TEXT,
            MessageElementEntry.COLUMN_NAME_PREVIEW_TEXT_HTML,
            MessageElementEntry.COLUMN_NAME_PREVIEW_DONE,
            MessageElementEntry.COLUMN_NAME_CREATION_DATE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = MessageElementEntry._ID + " ASC";

        Cursor cursor = db.query(
            MessageElementEntry.TABLE_NAME,                     // The table to query
            projection,                               // The columns to return
            null,                                // The columns for the WHERE clause
            null,                            // The values for the WHERE clause
            null,                                     // don't group the rows
            null,                                     // don't filter by row groups
            sortOrder                                 // The sort order
        );

        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(MessageElementEntry._ID));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(MessageElementEntry.COLUMN_NAME_MESSAGE_TYPE));
            String value = cursor.getString(cursor.getColumnIndexOrThrow(MessageElementEntry.COLUMN_NAME_MESSAGE_VALUE));
            String remoteImageUrl = cursor.getString(cursor.getColumnIndexOrThrow(MessageElementEntry.COLUMN_NAME_REMOTE_IMAGE_URL));
            String localImageFilePath = cursor.getString(cursor.getColumnIndexOrThrow(MessageElementEntry.COLUMN_NAME_LOCAL_IMAGE_FILE_PATH));
            String previewText = cursor.getString(cursor.getColumnIndexOrThrow(MessageElementEntry.COLUMN_NAME_PREVIEW_TEXT));
            String previewTextHtml = cursor.getString(cursor.getColumnIndexOrThrow(MessageElementEntry.COLUMN_NAME_PREVIEW_TEXT_HTML));
            String creationDate = cursor.getString(cursor.getColumnIndexOrThrow(MessageElementEntry.COLUMN_NAME_CREATION_DATE));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(creationDate));

            MessageElement me = new MessageElement();
            me.setId(itemId);
            me.setMessageType(MessageType.valueOf(type));
            me.setMessageValue(value);
            me.setRemoteImageUrl(remoteImageUrl);
            me.setLocalImageFilePath(localImageFilePath);
            me.setPreviewText(previewText);
            me.setPreviewTextHtml(previewTextHtml);
            me.setCreationDate(calendar.getTime());
            list.add(me);
        }
        cursor.close();

        return list;
    }

    public void update(MessageElement messageElement) {
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MessageElementEntry.COLUMN_NAME_MESSAGE_TYPE, messageElement.getMessageType().toString());
        values.put(MessageElementEntry.COLUMN_NAME_MESSAGE_VALUE, messageElement.getMessageValue());
        values.put(MessageElementEntry.COLUMN_NAME_REMOTE_IMAGE_URL, messageElement.getRemoteImageUrl());
        values.put(MessageElementEntry.COLUMN_NAME_LOCAL_IMAGE_FILE_PATH, messageElement.getLocalImageFilePath());
        values.put(MessageElementEntry.COLUMN_NAME_PREVIEW_TEXT, messageElement.getPreviewText());
        values.put(MessageElementEntry.COLUMN_NAME_PREVIEW_TEXT_HTML, messageElement.getPreviewTextHtml());
        values.put(MessageElementEntry.COLUMN_NAME_PREVIEW_DONE, messageElement.getPreviewDone());

        // update the row
        int output = db.update(MessageElementEntry.TABLE_NAME, values, " _id = " + messageElement.getId(), null);

        return;
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MessageElementEntry.TABLE_NAME, "", new String[0]);
    }

    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + MessageElementEntry.TABLE_NAME + " (" +
            MessageElementEntry._ID + " INTEGER PRIMARY KEY," +
            MessageElementEntry.COLUMN_NAME_MESSAGE_TYPE + " TEXT," +
            MessageElementEntry.COLUMN_NAME_MESSAGE_VALUE + " TEXT," +
            MessageElementEntry.COLUMN_NAME_REMOTE_IMAGE_URL + " TEXT," +
            MessageElementEntry.COLUMN_NAME_LOCAL_IMAGE_FILE_PATH + " TEXT," +
            MessageElementEntry.COLUMN_NAME_PREVIEW_TEXT + " TEXT," +
            MessageElementEntry.COLUMN_NAME_PREVIEW_TEXT_HTML + " TEXT," +
            MessageElementEntry.COLUMN_NAME_PREVIEW_DONE + " INTEGER DEFAULT 0," +
            MessageElementEntry.COLUMN_NAME_CREATION_DATE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MessageElementEntry.TABLE_NAME;

    public MessageElementDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //onUpgrade(db, oldVersion, newVersion);
    }
}