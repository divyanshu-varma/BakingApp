package com.example.android.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeDetails extends AppCompatActivity implements IngredientsFragment.OnFragmentInteractionListener,
        RecipeStepsFragment.OnListFragmentInteractionListener,
        ExoPlayerFragment.OnFragmentInteractionListener,
        RecipeStepInstructionFragment.OnFragmentInteractionListener {

    static int counter = 0;
    ExoPlayerFragment exoPlayerFragment = new ExoPlayerFragment();
    RecipeStepInstructionFragment recipeStepInstructionFragment = new RecipeStepInstructionFragment();
    int dp;
    String id;
    boolean alreadyAdded;
    FrameLayout frameLayout;
    Button next, previous;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        frameLayout = findViewById(R.id.steps_container);

        next = findViewById(R.id.next_step);
        previous = findViewById(R.id.previous_step);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        if (savedInstanceState == null) {
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();


            FragmentManager fragmentManager = getSupportFragmentManager();


            fragmentManager.beginTransaction()
                    .add(R.id.ingredients_container, ingredientsFragment)
                    .add(R.id.steps_container, recipeStepsFragment)
                    .commit();


            try {
                String ingredients;
                JSONArray jsonArray = new JSONArray(RecipeJson.jsonData);
                JSONObject recipeObject;
                if (id != null)
                    recipeObject = jsonArray.getJSONObject(Integer.parseInt(id));
                else
                    recipeObject = jsonArray.getJSONObject(0);
                JSONArray ingredientsArray = recipeObject.getJSONArray("ingredients");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("INGREDIENTS:\n");
                for (int index = 0; index < ingredientsArray.length(); index++) {
                    JSONObject jsonObject = ingredientsArray.getJSONObject(index);
                    stringBuilder.append(jsonObject.get("quantity")).append(" ");
                    stringBuilder.append(jsonObject.get("measure")).append(" ");
                    stringBuilder.append(jsonObject.get("ingredient")).append(" ");
                    stringBuilder.append("\n");
                }
                ingredients = stringBuilder.toString();
                Bundle bundle = new Bundle();
                bundle.putString("ingredients", ingredients);
                ingredientsFragment.setArguments(bundle);

                JSONArray stepsArray = recipeObject.getJSONArray("steps");
                //stringBuilder = new StringBuilder();
                ArrayList<String> arrayList = new ArrayList<>();
                for (int index = 0; index < stepsArray.length(); index++) {
                    JSONObject jsonObject = stepsArray.getJSONObject(index);
                    //stringBuilder.append(jsonObject.get("shortDescription")).append("\n");
                    arrayList.add(jsonObject.get("shortDescription").toString());
                }
                // String steps = stringBuilder.toString();
                bundle = new Bundle();
                // bundle.putString("steps", steps);
                bundle.putStringArrayList("list", arrayList);
                bundle.putString("recipeId", id);
                recipeStepsFragment.setArguments(bundle);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextStep(temp, id);
                }
            });
            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previousStep(temp, id);
                }
            });
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().smallestScreenWidthDp < 600) {
            // Checks the orientation of the screen
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
                getSupportActionBar().hide();
                next.setVisibility(View.GONE);
                previous.setVisibility(View.GONE);
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
                decorView.setSystemUiVisibility(uiOptions);
                getSupportActionBar().show();
                next.setVisibility(View.VISIBLE);
                previous.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            next.setVisibility(View.INVISIBLE);
            previous.setVisibility(View.INVISIBLE);
        }
        if ((keyCode == KeyEvent.KEYCODE_BACK) && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            frameLayout.setVisibility(View.VISIBLE);
        }
        return super.onKeyDown(keyCode, event);
    }


    void nextStep(String shortDescription, String recipeId) {
        try {
            JSONArray jsonArray = new JSONArray(RecipeJson.jsonData);

            JSONObject jsonObject = jsonArray.getJSONObject(Integer.parseInt(recipeId));
            JSONArray jsonArray1 = jsonObject.getJSONArray("steps");
            for (int index1 = 0; index1 < jsonArray1.length(); index1++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(index1);
                String shortDescription1 = jsonObject1.getString("shortDescription");
                if (shortDescription1.equals(shortDescription)) {
                    Bundle bundle = new Bundle();
                    String next = jsonArray1.getJSONObject(index1 + 1).getString("shortDescription");
                    bundle.putString("item", next);
                    bundle.putString("id", id);
                    recipeStepInstructionFragment.setArguments(bundle);
                    exoPlayerFragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction()
                            .detach(exoPlayerFragment)
                            .detach(recipeStepInstructionFragment)
                            .attach(exoPlayerFragment)
                            .attach(recipeStepInstructionFragment)
                            .commit();

                    break;

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void previousStep(String shortDescription, String recipeId) {
        try {
            JSONArray jsonArray = new JSONArray(RecipeJson.jsonData);

            JSONObject jsonObject = jsonArray.getJSONObject(Integer.parseInt(recipeId));
            JSONArray jsonArray1 = jsonObject.getJSONArray("steps");
            for (int index1 = 0; index1 < jsonArray1.length(); index1++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(index1);
                String shortDescription1 = jsonObject1.getString("shortDescription");
                if (shortDescription1.equals(shortDescription)) {
                    Bundle bundle = new Bundle();
                    String next = jsonArray1.getJSONObject(index1 - 1).getString("shortDescription");
                    bundle.putString("item", next);
                    bundle.putString("id", id);
                    recipeStepInstructionFragment.setArguments(bundle);
                    exoPlayerFragment.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction()
                            .detach(exoPlayerFragment)
                            .detach(recipeStepInstructionFragment)
                            .attach(exoPlayerFragment)
                            .attach(recipeStepInstructionFragment)
                            .commit();

                    break;

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onListFragmentInteraction(String item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("item", item);
        bundle.putString("id", id);
        //item contains "short description" of the recipe....see json
        recipeStepInstructionFragment.setArguments(bundle);
        exoPlayerFragment.setArguments(bundle);

        Configuration config = getResources().getConfiguration();
        dp = config.smallestScreenWidthDp;
        int orientation = config.orientation;
        if (dp < 600 && orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentManager.beginTransaction()
                    .replace(R.id.ingredients_container, exoPlayerFragment)
                    .replace(R.id.steps_container, recipeStepInstructionFragment)
                    .addToBackStack(null)
                    .commit();
            next.setVisibility(View.VISIBLE);
            previous.setVisibility(View.VISIBLE);
        } else if (dp < 600 && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction()
                    .replace(R.id.ingredients_container, exoPlayerFragment)
                    .addToBackStack(null)
                    .commit();
            frameLayout.setVisibility(View.GONE);
        } else if (!alreadyAdded) {
            fragmentManager.beginTransaction()
                    .add(R.id.player_container, exoPlayerFragment)
                    .add(R.id.steps_details_container, recipeStepInstructionFragment)
                    .addToBackStack(null)
                    .commit();
            alreadyAdded = true;
        } else
            getSupportFragmentManager().beginTransaction()
                    .detach(exoPlayerFragment)
                    .detach(recipeStepInstructionFragment)
                    .attach(exoPlayerFragment)
                    .attach(recipeStepInstructionFragment)
                    .commit();
    }

    @Override
    public void onFragmentInteraction(String shortDescription) {
        temp = shortDescription;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
