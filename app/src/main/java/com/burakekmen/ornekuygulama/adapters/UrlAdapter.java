package com.burakekmen.ornekuygulama.adapters;

import android.net.Uri;

/****************************
 * Created by Burak EKMEN   |
 * 20.12.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class UrlAdapter {

    public static final String API_KEY = "91a502271145ddc8e311ec1945883b13";
    public static final String PREF_SEARCH_QUERY ="searchQuery";

    private static final String ENDPOINT = "https://api.flickr.com/services/rest/";
    private static final String METHOD_GETRECENT = "flickr.photos.getRecent";
    private static final String METHOD_SEARCH = "flickr.photos.search";

    private static volatile UrlAdapter instance = null;

    private UrlAdapter(){

    }

    public static UrlAdapter getInstance() {
        if (instance == null) {
            synchronized (UrlAdapter.class) {
                if (instance == null) {
                    instance = new UrlAdapter();
                }
            }
        }
        return instance;
    }

    public static String getItemUrl(String query, int page) {
        String url;
        if (query != null) {
            url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_SEARCH)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("text", query)
                    .appendQueryParameter("page", String.valueOf(page))
                    .build().toString();
        } else {
            url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_GETRECENT)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("page", String.valueOf(page))
                    .build().toString();
        }
        return url;
    }
}
