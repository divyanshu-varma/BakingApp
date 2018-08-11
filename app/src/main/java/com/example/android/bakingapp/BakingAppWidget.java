package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Nutella Pie :\n");
        try {
            JSONArray jsonArray = new JSONArray(RecipeJson.jsonData);

            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONArray ingredientsArray = jsonObject.getJSONArray("ingredients");
            for (int index = 0; index < ingredientsArray.length(); index++)
                stringBuilder.append("quantity :")
                        .append(ingredientsArray.getJSONObject(index).getString("quantity"))
                        .append("\nmeasure :")
                        .append(ingredientsArray.getJSONObject(index).getString("measure"))
                        .append("\ningredients :")
                        .append(ingredientsArray.getJSONObject(index).getString("ingredient"))
                        .append("\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CharSequence widgetText = stringBuilder.toString();
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);

        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

