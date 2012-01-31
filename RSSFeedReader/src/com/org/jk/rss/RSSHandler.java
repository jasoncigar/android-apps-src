package com.org.jk.rss;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class RSSHandler extends DefaultHandler {

	private StringBuffer charbuffer;
	boolean isChannel = false;
	private RSSChannel rssfeed;
	private RSSFeedItems currentItem;

	@Override
	public void characters(char[] ch, int start, int length) {
		try {
			super.characters(ch, start, length);
		} catch (SAXException e) {
			e.printStackTrace();
		}
		charbuffer.append(ch, start, length);
	}

	@Override
	public void endDocument() {
		try {
			super.endDocument();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		try {
			super.endElement(uri, localName, qName);
		} catch (SAXException e) {
			Log.e("RSS", e.toString());
			e.printStackTrace();
		}

		if (qName.equalsIgnoreCase("title") && !isChannel) {
			currentItem.setTitle(charbuffer.toString().trim());

		} else if (qName.equalsIgnoreCase("description") && !isChannel) {
			currentItem.setDescription(charbuffer.toString().trim());

		} else if (qName.equalsIgnoreCase("link") && !isChannel) {
			currentItem.setLink(charbuffer.toString().trim());

		} else if (qName.equalsIgnoreCase("pubDate") && !isChannel) {

			currentItem.setPubDate(charbuffer.toString().trim());
		} else if (qName.equalsIgnoreCase("title") && isChannel) {
			rssfeed.setTitle(charbuffer.toString().trim());

		} else if (qName.equalsIgnoreCase("description") && isChannel) {
			rssfeed.setDescription(charbuffer.toString().trim());

		} else if (qName.equalsIgnoreCase("link") && isChannel) {
			rssfeed.setLink(charbuffer.toString().trim());

		} else if (qName.equalsIgnoreCase("item")) {
			rssfeed.addItemToList(currentItem);
			Log.e("RSS", "adding item to list size :"
					+ rssfeed.getItemList().size());
			Log.e("RSS", "current item :" + currentItem.getTitle() + " "
					+ currentItem.getLink());
			currentItem = null;
		}
		charbuffer.setLength(0);
	}

	@Override
	public void startDocument() {
		try {
			super.startDocument();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		charbuffer = new StringBuffer();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {
		try {
			super.startElement(uri, localName, qName, attributes);
		} catch (SAXException e) {
			e.printStackTrace();
		}
		if (qName.equalsIgnoreCase("channel")) {
			Log.e("RSS", "start channel");
			rssfeed = new RSSChannel();
			isChannel = true;
		} else if (qName.equalsIgnoreCase("item")) {
			currentItem = new RSSFeedItems();
			Log.e("RSS", "Creating new item");
			isChannel = false;
		}
	}

	public List<RSSFeedItems> getItems() {
		return rssfeed.getItemList();
	}
}
