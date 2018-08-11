package com.example.android.bakingapp.dummy;

import com.example.android.bakingapp.RecipeJson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<>();
    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<>();
    static JSONArray jsonArray;

    static {
        try {
            makeJsonArray();
            for (int i = 0; i < jsonArray.length(); i++)
                addItem(createDummyItem(i));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) throws JSONException {
        return new DummyItem(String.valueOf(position), jsonArray.getJSONObject(position).get("name").toString(), makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    static void makeJsonArray() throws JSONException {
        jsonArray = new JSONArray(RecipeJson.jsonData);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
