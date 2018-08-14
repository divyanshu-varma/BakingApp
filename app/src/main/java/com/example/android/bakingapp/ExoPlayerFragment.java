package com.example.android.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExoPlayerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExoPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExoPlayerFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    SimpleExoPlayer player;
    long startPosition;
    @InjectView(R.id.video_view)
    PlayerView mPlayerView;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public ExoPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExoPlayerFragment.
     */

    public static ExoPlayerFragment newInstance(String param1, String param2) {
        ExoPlayerFragment fragment = new ExoPlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exo_player, container, false);
        ButterKnife.inject(this, view);
        if (savedInstanceState != null)
            startPosition = savedInstanceState.getLong("currentPosition");
        return view;
    }

    private void initializePlayer() {
        if (player == null) {
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mPlayerView.setPlayer(player);
            DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(),
                            "BakingApp"),
                    bandwidthMeter);
            String id = getArguments().getString("id");
            String url = "", videoURL = "";
            String shortDescriptionParameter = getArguments().getString("item");

            try {
                JSONArray jsonArray = new JSONArray(RecipeJson.jsonData);
                JSONObject jsonObject = jsonArray.getJSONObject(Integer.parseInt(id));
                JSONArray jsonArray1 = jsonObject.getJSONArray("steps");
                int index1;
                for (index1 = 0; index1 < jsonArray1.length(); index1++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(index1);
                    if (jsonObject1.getString("shortDescription").equals(shortDescriptionParameter)) {
                        videoURL = jsonObject1.getString("videoURL");
                        if (videoURL.equals("")) {
                            mPlayerView.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "NO VIDEO", Toast.LENGTH_SHORT).show();
                        } else
                            url = videoURL;
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (!url.equals("")) {
                MediaSource mediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.parse(url));
                player.prepare(mediaSource);
                player.setPlayWhenReady(true);

            }
            onButtonPressed(shortDescriptionParameter);
        }
        player.seekTo(startPosition);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("currentPosition", player.getCurrentPosition());
    }

    public void onButtonPressed(String shortDescription) {
        if (mListener != null) {
            mListener.onFragmentInteraction(shortDescription);
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
        void onFragmentInteraction(String shortDescription);
    }

}