package com.wordiction;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;


public class Wordy extends Fragment {

    Button wordy;
    public Wordy() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.wordy_layout, container, false);
        if (container==null) {
            return null;
        }

        wordy=(Button)view.findViewById(R.id.wordy);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Captain Shiner.ttf");
        wordy.setTypeface(font);

        wordy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Match.class));
            }
        });

        final AlphaAnimation buttonClick=new AlphaAnimation(1f,0.8f);
        final Animation animation= AnimationUtils.loadAnimation(getActivity(),R.anim.pressing_effect);
        wordy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47251, PorterDuff.Mode.SRC_ATOP);
                        //v.startAnimation(buttonClick);
                        v.startAnimation(animation);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        return view;
    }


}
