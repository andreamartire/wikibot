package techbrain.wikibot.delegates;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import techbrain.wikibot.R;

public class WikiConstants {

	static String WIKI_PREFIX = "https://it.wikipedia.org/wiki/";
	public static String NONCICLOPEDIA_URL = "http://nonciclopedia.wikia.com/wiki/Speciale:PaginaCasuale";

	public static String CX = "004001173590099652672:ls07lv3jtwm";
	public static String GAK = "AIzaSyCf8GaliX65nWeelUq5so0dg5H14sYFv3c";

	public static String getRandomNonciclopedia(ArrayList<String> listItems, ArrayAdapter<String> adapter) {
		String item = null;

		try{
			RetrieveNonciclopediaTask task = new RetrieveNonciclopediaTask(listItems, adapter);
			task.execute();
		}catch (Exception e){
			e.printStackTrace();
		}

		return  item;
	}

	public static void main(String[] args){
		//TODO review init static test
		System.out.println("Size: " + WikiConstants.articles.size());
		System.out.println("Duplicates: " + getDuplicate(WikiConstants.articles));
		System.out.println(generateLinks(WikiConstants.articles));
	}

	private static String generateLinks(ArrayList<String> wikipediaItems) {
		String output = "";
		for (String el : wikipediaItems) {
			output += "[[" + el + "]]\n";
		}
		return output;
	}

	public static <T> List getDuplicate(Collection<T> list) {

		final List<T> duplicatedObjects = new ArrayList<T>();
		Set<T> set = new HashSet<T>() {
			@Override
			public boolean add(T e) {
				if (contains(e)) {
					duplicatedObjects.add(e);
				}
				return super.add(e);
			}
		};
		for (T t : list) {
			set.add(t);
		}
		return duplicatedObjects;
	}

	static ArrayList<String> proverbs;
	static ArrayList<String> quotes;
	static ArrayList<String> articles;

	public static String getRandomItem(Context c) {
		initArticles(c);
		Collections.shuffle(articles);
		return WIKI_PREFIX + articles.get(0).replaceAll(" ","_");
	}

	private static void initArticles(Context c) {
		if(articles == null){
			String[] mTestArray = c.getResources().getStringArray(R.array.articles);
			articles = new ArrayList<>(Arrays.asList(mTestArray));
		}
	}

	private static void initProverbs(Context c) {
		if(proverbs == null){
			String[] mTestArray = c.getResources().getStringArray(R.array.proverbs);
			proverbs = new ArrayList<>(Arrays.asList(mTestArray));
		}
	}

	private static void initQuotes(Context c) {
		if(quotes == null){
			String[] mTestArray = c.getResources().getStringArray(R.array.quotations);
			quotes = new ArrayList<>(Arrays.asList(mTestArray));
		}
	}

	public static String getRandomProverb(Context c) {
		initProverbs(c);

		Collections.shuffle(proverbs);
		return proverbs.get(0);
	}

	public static String getRandomQuote(Context c) {
		initQuotes(c);

		Collections.shuffle(quotes);
		return quotes.get(0);
	}

	public static String getRandomQuoteOrProverb(Context c) {
		initQuotes(c);
		initProverbs(c);

		ArrayList<String> quotesAndProverbs = new ArrayList<>();
		quotesAndProverbs.addAll(quotes);
		quotesAndProverbs.addAll(proverbs);

		Collections.shuffle(quotesAndProverbs);
		return quotesAndProverbs.get(0);
	}
};