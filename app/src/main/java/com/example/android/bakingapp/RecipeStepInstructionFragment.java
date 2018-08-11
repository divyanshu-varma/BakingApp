package com.example.android.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeStepInstructionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeStepInstructionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeStepInstructionFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RecipeStepInstructionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeStepInstructionFragment.
     */
    public static RecipeStepInstructionFragment newInstance(String param1, String param2) {
        RecipeStepInstructionFragment fragment = new RecipeStepInstructionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String id = getArguments().getString("id");
        String shortDescriptionParameter = getArguments().getString("item");
        View view = inflater.inflate(R.layout.fragment_recipe_step_instruction, container, false);
        TextView textView = view.findViewById(R.id.step_instruction);
        try {
            JSONArray jsonArray = new JSONArray(RecipeJson.jsonData);

            JSONObject jsonObject = jsonArray.getJSONObject(Integer.parseInt(id));
            JSONArray jsonArray1 = jsonObject.getJSONArray("steps");
            for (int index1 = 0; index1 < jsonArray1.length(); index1++) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(index1);
                String shortDescription = jsonObject1.getString("shortDescription");
                if (shortDescription != null)
                    if (shortDescription.equals(shortDescriptionParameter))
                        textView.setText(jsonObject1.getString("description"));
                if (shortDescription == null)
                    textView.setText(R.string.no_description);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChangeStepFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
