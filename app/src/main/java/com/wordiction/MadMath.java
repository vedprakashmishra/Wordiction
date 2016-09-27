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
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class MadMath extends Fragment {

    Button madmath;
    public MadMath() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.madmath_layout, container, false);

        if (container==null) {
            return null;
        }
        madmath=(Button)view.findViewById(R.id.MadMath);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Captain Shiner.ttf");
        madmath.setTypeface(font);

        final AlphaAnimation buttonClick=new AlphaAnimation(1f,0.8f);
        madmath.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47251, PorterDuff.Mode.SRC_ATOP);
                        v.startAnimation(buttonClick);
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
        madmath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),mad.class));
            }
        });

        return view;
    }


}
