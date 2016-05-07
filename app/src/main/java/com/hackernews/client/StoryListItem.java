package com.hackernews.client;

/**
 * Created by yogesh on 7/5/16.
 */
public class StoryListItem {

    private String story_id;

    public StoryListItem(String agency_id) {
        this.story_id = agency_id;
    }

    public String getId() {
        return story_id;
    }
    public void setId(String id) {
        this.story_id = id;
    }
}
