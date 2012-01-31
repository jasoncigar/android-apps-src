package com.org.jk.rss;

import java.util.ArrayList;
import java.util.List;

public class RSSChannel {

	private String title;
	private String link;
	private String description;
	private List<RSSFeedItems> itemList;

	public RSSChannel() {
		itemList = new ArrayList<RSSFeedItems>();
	}

	public String getTitle() {
		return title;
	}

	public List<RSSFeedItems> getItemList() {
		return itemList;
	}

	public void setItemList(List<RSSFeedItems> itemList) {
		this.itemList = itemList;
	}

	public void addItemToList(RSSFeedItems item) {
		itemList.add(item);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public String getDescription() {
		return description;
	}

}
