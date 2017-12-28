package techbrain.wikibot.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;

import techbrain.wikibot.beans.Article;
import techbrain.wikibot.delegates.WikiConstants;

public class ArticleDao extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "wikibot.db";

    public static ArticleDao articleDao;
    public Context context;

    public static ArticleDao getInstance(Context context){
        if(articleDao == null){
            articleDao = new ArticleDao(context);
            articleDao.context = context;
        }
        return articleDao;
    }

    public long save(Article article){
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Article.ArticleEntry.COLUMN_NAME_URL, article.getUrlIta());

        values.put(Article.ArticleEntry.COLUMN_NAME_PREVIEW_TEXT, article.getPreviewText());
        values.put(Article.ArticleEntry.COLUMN_NAME_PREVIEW_TEXT_HTML, article.getPreviewTextHtml());

        values.put(Article.ArticleEntry.COLUMN_NAME_REMOTE_IMAGE_URL, article.getRemoteImageUrl());
        values.put(Article.ArticleEntry.COLUMN_NAME_LOCAL_IMAGE_FILE_PATH, article.getLocalImageFilePath());

        values.put(Article.ArticleEntry.COLUMN_NAME_CREATION_DATE, Calendar.getInstance().getTimeInMillis());

        // Insert the new row, returning the primary key value of the new row
        return db.insert(Article.ArticleEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Article> findAll(){
        ArrayList<Article> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
            Article.ArticleEntry._ID,
            Article.ArticleEntry.COLUMN_NAME_URL,
            Article.ArticleEntry.COLUMN_NAME_PREVIEW_TEXT,
            Article.ArticleEntry.COLUMN_NAME_PREVIEW_TEXT_HTML,
            Article.ArticleEntry.COLUMN_NAME_REMOTE_IMAGE_URL,
            Article.ArticleEntry.COLUMN_NAME_LOCAL_IMAGE_FILE_PATH,
            Article.ArticleEntry.COLUMN_NAME_CREATION_DATE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = Article.ArticleEntry._ID + " ASC";

        Cursor cursor = db.query(
            Article.ArticleEntry.TABLE_NAME,                     // The table to query
            projection,                               // The columns to return
            null,                                // The columns for the WHERE clause
            null,                            // The values for the WHERE clause
            null,                                     // don't group the rows
            null,                                     // don't filter by row groups
            sortOrder                                 // The sort order
        );

        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(Article.ArticleEntry._ID));
            String url = cursor.getString(cursor.getColumnIndexOrThrow(Article.ArticleEntry.COLUMN_NAME_URL));

            String previewText = cursor.getString(cursor.getColumnIndexOrThrow(Article.ArticleEntry.COLUMN_NAME_PREVIEW_TEXT));
            String previewTextHtml = cursor.getString(cursor.getColumnIndexOrThrow(Article.ArticleEntry.COLUMN_NAME_PREVIEW_TEXT_HTML));

            String remoteImageUrl = cursor.getString(cursor.getColumnIndexOrThrow(Article.ArticleEntry.COLUMN_NAME_REMOTE_IMAGE_URL));
            String localImageFilePath = cursor.getString(cursor.getColumnIndexOrThrow(Article.ArticleEntry.COLUMN_NAME_LOCAL_IMAGE_FILE_PATH));

            Long creationDate = cursor.getLong(cursor.getColumnIndexOrThrow(Article.ArticleEntry.COLUMN_NAME_CREATION_DATE));
            Calendar calendarCreationDate = Calendar.getInstance();
            calendarCreationDate.setTimeInMillis(creationDate);

            Article me = new Article();
            me.setId(itemId);
            me.setUrlIta(url);
            me.setPreviewText(previewText);
            me.setPreviewTextHtml(previewTextHtml);
            me.setRemoteImageUrl(remoteImageUrl);
            me.setLocalImageFilePath(localImageFilePath);
            me.setCreationDate(calendarCreationDate.getTime());
            list.add(me);
        }
        cursor.close();

        return list;
    }

    public void update(Article article) {
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Article.ArticleEntry.COLUMN_NAME_URL, article.getUrlIta());
        values.put(Article.ArticleEntry.COLUMN_NAME_PREVIEW_TEXT, article.getPreviewText());
        values.put(Article.ArticleEntry.COLUMN_NAME_PREVIEW_TEXT_HTML, article.getPreviewTextHtml());
        values.put(Article.ArticleEntry.COLUMN_NAME_REMOTE_IMAGE_URL, article.getRemoteImageUrl());
        values.put(Article.ArticleEntry.COLUMN_NAME_LOCAL_IMAGE_FILE_PATH, article.getLocalImageFilePath());

        String[] where = {article.getId()+""};

        // update the row
        db.update(Article.ArticleEntry.TABLE_NAME, values, " _id = ? ", where);
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        String[] el = new String[0];

        // update the row
        db.delete(Article.ArticleEntry.TABLE_NAME, "", el);
    }

    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + Article.ArticleEntry.TABLE_NAME + " (" +
            Article.ArticleEntry._ID + " INTEGER PRIMARY KEY," +
            Article.ArticleEntry.COLUMN_NAME_URL + " TEXT," +
            Article.ArticleEntry.COLUMN_NAME_PREVIEW_TEXT + " TEXT," +
            Article.ArticleEntry.COLUMN_NAME_PREVIEW_TEXT_HTML + " TEXT," +
            Article.ArticleEntry.COLUMN_NAME_REMOTE_IMAGE_URL + " TEXT," +
            Article.ArticleEntry.COLUMN_NAME_LOCAL_IMAGE_FILE_PATH + " TEXT," +
            Article.ArticleEntry.COLUMN_NAME_CREATION_DATE + " INTEGER)";

    //private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MessageElementEntry.TABLE_NAME;

    public ArticleDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

        ArrayList<String> articles = WikiConstants.getArticles();
        if(articles != null){
            for(String articleUrl : articles){
                Article article = new Article();
                article.setUrlIta(articleUrl);
                save(article);
            }
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}