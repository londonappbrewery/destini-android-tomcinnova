package com.londonappbrewery.destini;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    // TODO: Steps 4 & 8 - Declare member variables here:
    private TextView storyTextView;
    private Button buttonTop;
    private Button buttonBottom;
    private static HashMap<StoryState, HashMap<StoryTextPosition, Integer>> storyTextMap = new
        HashMap<StoryState, HashMap<StoryTextPosition,Integer>>();
    private static HashMap<StoryState,StoryState> topStoryStateMap = new HashMap<StoryState, StoryState>();
    private static HashMap<StoryState, StoryState> bottomStoryStateMap = new HashMap<StoryState, StoryState>();
    private StoryState mStoryState = StoryState.t1;

    private enum StoryState {
        t1, t2, t3, t4, t5, t6
    }

    private enum StoryTextPosition {
        storyTextView,
        buttonTop,
        buttonBottom
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Step 5 - Wire up the 3 views from the layout to the member variables:
        storyTextView = findViewById(R.id.storyTextView);
        buttonTop = findViewById(R.id.buttonTop);
        buttonBottom = findViewById(R.id.buttonBottom);


        // TODO: Steps 6, 7, & 9 - Set a listener on the top button:
        buttonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (StoryState currentState:topStoryStateMap.keySet()) {
                    if (mStoryState == currentState) {
                        mStoryState = topStoryStateMap.get(mStoryState);
                        break;
                    }
                }
                updateStoryText();
            }
        });


        // TODO: Steps 6, 7, & 9 - Set a listener on the bottom button:
        buttonBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (StoryState currentState:bottomStoryStateMap.keySet()) {
                    if (mStoryState == currentState) {
                        mStoryState = bottomStoryStateMap.get(mStoryState);
                        break;
                    }
                }
                updateStoryText();
            }
        });

        initializeStoryTextMap();
        initializeStoryStateMaps();

        if (savedInstanceState != null) {
            StoryState previousStoryState = (StoryState)savedInstanceState.get("mStoryState");
            if (previousStoryState != null) {
                mStoryState = previousStoryState;
                updateStoryText();
            }
        }

    }

    private void initializeStoryTextMap() {
        HashMap<StoryTextPosition, Integer> storyTextPositions = new HashMap<StoryTextPosition, Integer>();
        storyTextPositions.put(StoryTextPosition.storyTextView, R.string.T1_Story);
        storyTextPositions.put(StoryTextPosition.buttonTop, R.string.T1_Ans1);
        storyTextPositions.put(StoryTextPosition.buttonBottom, R.string.T1_Ans2);
        storyTextMap.put(StoryState.t1, storyTextPositions);

        storyTextPositions = new HashMap<StoryTextPosition, Integer>();
        storyTextPositions.put(StoryTextPosition.storyTextView, R.string.T2_Story);
        storyTextPositions.put(StoryTextPosition.buttonTop, R.string.T2_Ans1);
        storyTextPositions.put(StoryTextPosition.buttonBottom, R.string.T2_Ans2);
        storyTextMap.put(StoryState.t2, storyTextPositions);

        storyTextPositions = new HashMap<StoryTextPosition, Integer>();
        storyTextPositions.put(StoryTextPosition.storyTextView, R.string.T3_Story);
        storyTextPositions.put(StoryTextPosition.buttonTop, R.string.T3_Ans1);
        storyTextPositions.put(StoryTextPosition.buttonBottom, R.string.T3_Ans2);
        storyTextMap.put(StoryState.t3, storyTextPositions);

        storyTextPositions = new HashMap<StoryTextPosition, Integer>();
        storyTextPositions.put(StoryTextPosition.storyTextView, R.string.T4_End);
        storyTextMap.put(StoryState.t4, storyTextPositions);

        storyTextPositions = new HashMap<StoryTextPosition, Integer>();
        storyTextPositions.put(StoryTextPosition.storyTextView, R.string.T5_End);
        storyTextMap.put(StoryState.t5, storyTextPositions);

        storyTextPositions = new HashMap<StoryTextPosition, Integer>();
        storyTextPositions.put(StoryTextPosition.storyTextView, R.string.T6_End);
        storyTextMap.put(StoryState.t6, storyTextPositions);
    }

    private void initializeStoryStateMaps() {
        topStoryStateMap.put(StoryState.t1, StoryState.t3);
        topStoryStateMap.put(StoryState.t2, StoryState.t3);
        topStoryStateMap.put(StoryState.t3, StoryState.t6);
        bottomStoryStateMap.put(StoryState.t1, StoryState.t2);
        bottomStoryStateMap.put(StoryState.t2, StoryState.t4);
        bottomStoryStateMap.put(StoryState.t3, StoryState.t5);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("mStoryState",mStoryState);
        super.onSaveInstanceState(outState);
    }

    private void updateStoryText() {
        HashMap<StoryTextPosition, Integer> storyTextPositions = storyTextMap.get(mStoryState);
        storyTextView.setText(storyTextPositions.get(StoryTextPosition.storyTextView));

        if (storyTextPositions.containsKey(StoryTextPosition.buttonTop)) {
            buttonTop.setText(storyTextPositions.get(StoryTextPosition.buttonTop));
        }
        else {
            buttonTop.setVisibility(View.GONE);
        }
        if (storyTextPositions.containsKey(StoryTextPosition.buttonBottom)) {
            buttonBottom.setText(storyTextPositions.get(StoryTextPosition.buttonBottom));
        }
        else {
            buttonBottom.setVisibility(View.GONE);
        }

        Set<StoryState> endStoryStates = new TreeSet<StoryState>();
        endStoryStates.add(StoryState.t4);
        endStoryStates.add(StoryState.t5);
        endStoryStates.add(StoryState.t6);

        if (endStoryStates.contains(mStoryState)) {
            Toast.makeText(getApplicationContext(), "Story has ended", Toast.LENGTH_LONG).show();

            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Restart");
                    alert.setMessage("Do you wish to restart the story?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mStoryState = StoryState.t1;
                            buttonTop.setVisibility(View.VISIBLE);
                            buttonBottom.setVisibility(View.VISIBLE);
                            updateStoryText();
                        }
                    });
                    alert.setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alert.create().show();

                }
            },1000);

        }
    }
}
