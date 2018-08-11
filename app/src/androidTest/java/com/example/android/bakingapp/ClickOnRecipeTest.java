package com.example.android.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ClickOnRecipeTest {
    @Rule
    public ActivityTestRule<RecipeDetails> recipeDetailsActivityTestRule
            = new ActivityTestRule<>(RecipeDetails.class);


    @Test
    public void clickButton() {
        onView(withId(R.id.container)).check(doesNotExist());
    }
}
