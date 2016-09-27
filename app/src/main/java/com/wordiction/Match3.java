package com.wordiction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Match3 extends Activity implements SpellCheckerSession.SpellCheckerSessionListener{

    String m,str1,str2;
    private SpellCheckerSession scs;
    TextView tv2,tv3,tt;
    int x,points;
    ImageButton s,c,r;
    ImageView iv;
    CountDownTimer countDownTimer;
    AlertDialog alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match3);

        tt= (TextView) findViewById(R.id.texttime);
        iv= (ImageView) findViewById(R.id.imagealarm );
        countDownTimer=new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                long m=millisUntilFinished/1000;
                tt.setText(""+String.format("%d : %02d ", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                if(m<=20) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrate_anim);
                    iv.startAnimation(animation);
                    Random myColor = new Random();
                    tt.setTextColor(Color.rgb(myColor.nextInt(255), myColor.nextInt(255), myColor.nextInt(255)));
                }
            }

            @Override
            public void onFinish() {
                timefinish();
                iv.clearAnimation();
                tt.setText("0:00");
            }
        };
        countDownTimer.start();

        final TextView tv1=(TextView)findViewById(R.id.textset);
        tv2=(TextView)findViewById(R.id.textget);
        tv3=(TextView)findViewById(R.id.textpoints);
        tv3.setTextColor(Color.BLUE);
        tt.setTextColor(Color.BLUE);
        tt.setTypeface(Typeface.SANS_SERIF);

        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(Typeface.MONOSPACE);
        tv1.setTextSize(20);
        s= (ImageButton) findViewById(R.id.buttonsub);
        c= (ImageButton) findViewById(R.id.buttoncan);
        r= (ImageButton) findViewById(R.id.buttonres);

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scs.getSuggestions(new TextInfo(tv1.getText().toString()), 1);
                tv1.setText("");
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tv1.getText().toString().equals("")) {
                    String s = tv1.getText().toString();
                    s = s.substring(0, s.length() - 1);
                    tv1.setText(s);
                }

            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setText("");
            }
        });


        if(tv1.getText().toString().equals(""))
        {
            s.setEnabled(false);
        }

        TableLayout t=(TableLayout) findViewById(R.id.fragtable);
        for(int i=0;i<6;i++)
        {
            TableRow tr=new TableRow(getApplicationContext());
            for(int j=0;j<6;j++)
            {
                TextView tv=new TextView(getApplicationContext());
                final String [] st={"A","B","C","D","E","F", "G", "H", "I", "I","J","K","L","M","N"
                        , "O", "P","Q","R","S","T","U","V","W","X","Y", "Z"};

                tv.setText(st[(int) (Math.random() * 27)]);
                tv.setTextSize(20);
                tv.setTextColor(Color.BLUE);
                tv.setGravity(Gravity.CENTER);
                tv.setTypeface(null, Typeface.BOLD);
                tv.setBackgroundResource(R.drawable.r1);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        m = ((TextView) v).getText().toString();
                        tv1.append(m);
                        str1=tv1.getText().toString();
                        ((TextView) v).setText(st[(int) (Math.random() * 27)]);
                        if (tv1.getText().toString().length() > 2) {
                            s.setEnabled(true);
                        }
                    }
                });

                tr.addView(tv);
            }
            t.addView(tr);
        }
        t.setPadding(20, 10, 10, 10);
    }

    public void timefinish() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Match3.this);
        LayoutInflater li = LayoutInflater.from(Match3.this);
        builder.setCancelable(false);
        View promptsView = li.inflate(R.layout.result_prompt, null);
        builder.setView(promptsView);
        Button replay = (Button) promptsView.findViewById(R.id.replay);
        Button home = (Button) promptsView.findViewById(R.id.home);
        Button forward = (Button) promptsView.findViewById(R.id.forward);
        TextView resultPoint= (TextView) promptsView.findViewById(R.id.resultpoint);
        RatingBar ratingBar= (RatingBar) promptsView.findViewById(R.id.resultRating);
        TextView comment= (TextView) promptsView.findViewById(R.id.comment);

        replay.setEnabled(false);
        home.setEnabled(false);
        forward.setEnabled(false);

        points=Integer.parseInt(tv3.getText().toString());
        resultPoint.setText("You Scored : " + points + " Points");

        if (points<30) {
            replay.setEnabled(true);
            home.setEnabled(true);
            forward.setEnabled(false);
            ratingBar.setRating(0);
            comment.setText("Work hard baby...!! "+" 100 points needed only ");
        }
        if (points>30 && points<75) {
            replay.setEnabled(true);
            home.setEnabled(true);
            forward.setEnabled(false);
            comment.setText("Put some efforts buddy...!! "+" 100 points needed only ");
            ratingBar.setRating(1);
        }
        if (points>75&&points<100) {
            replay.setEnabled(true);
            home.setEnabled(true);
            forward.setEnabled(false);
            comment.setText("Increase your speed buddy...!!"+" few points more champ ");
            ratingBar.setRating(2);
        }
        if(points>=100) {
            replay.setEnabled(true);
            home.setEnabled(true);
            forward.setEnabled(true);
            comment.setText("Gotcha...!! Play the next level champ ");
            ratingBar.setRating(3);
        }

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Match3.class));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),NewMain.class));
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),NewMain.class));
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        countDownTimer.cancel();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Game Paused");
        builder.setMessage("Do you want to stop the game now...??");
        builder.setCancelable(false);
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                countDownTimer.start();
                alert.dismiss();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(),NewMain.class));
            }
        });
        alert=builder.create();
        alert.show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        final TextServicesManager tsm=(TextServicesManager) getApplicationContext().getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
        scs=tsm.newSpellCheckerSession(null, null, (SpellCheckerSession.SpellCheckerSessionListener) this, true);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        if(scs!=null)
        {
            scs.close();
        }
    }

    @Override
    public void onGetSuggestions(SuggestionsInfo[] arg) {
        final StringBuilder sb = new StringBuilder();
        for(int i=0;i< arg.length;i++)
        {
            final int len = arg[i].getSuggestionsCount();
            for (int j = 0; j < len; ++j) {
                sb.append(arg[i].getSuggestionAt(j));
            }
        }
        this.runOnUiThread(new Runnable() {
            public void run() {
                tv2.setText(sb.toString());
            }
        });

        str2=tv2.getText().toString();
        if(str1.equalsIgnoreCase(str2))
        {
            Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
            tv2.setText("");
            s.setEnabled(false);
            x=str2.length()*10+Integer.parseInt(tv3.getText().toString());
            tv3.setText("");
            tv3.setText(Integer.toString(x));
        } else
        {
            Toast.makeText(getApplicationContext(),"incorrect",Toast.LENGTH_SHORT).show();
            tv2.setText("");
        }
    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] arg) {

    }
}