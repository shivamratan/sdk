package com.crackretail.sdk.nativeads;

/**
 * Created by Shivam on 21-Jul-16.
 */
public class Tracker
{
    private String type;
    private String url;

    public Tracker(String type, String url) {
        this.type = type;
        this.url = url;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
