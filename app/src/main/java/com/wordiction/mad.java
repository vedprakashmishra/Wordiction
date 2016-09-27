package com.wordiction;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class mad extends Activity {

    TextView tv1,tv2,tv3;
    String t1="",t2="",t3="";
    Float res1,res2,res3,res4;
    Button b1,b2,b3,b4;
    int flag=0;
    private ProgressBar prgsbar;
    int count=100;
    int f=0;
    int score=0;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mad);
        tv1=(TextView) findViewById(R.id.text1);
        tv2=(TextView) findViewById(R.id.text2);
        tv3=(TextView) findViewById(R.id.text3);
        b1= (Button) findViewById(R.id.Result1);
        b2= (Button) findViewById(R.id.Result2);
        b3= (Button) findViewById(R.id.Result3);
        b4= (Button) findViewById(R.id.Result4);

        final Vibrator vibrator= (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        prgsbar=(ProgressBar) findViewById(R.id.progressBar);
        prgsbar.setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer(11100, 100) {

            public void onTick(long millisUntilFinished) {
                count--;
                prgsbar.setProgress(count);
            }
            public void onFinish() {
                timervalue();
            }
        };

        countDownTimer.start();

        Bundle extra=getIntent().getExtras();
        if(extra!=null) {
            count=Integer.parseInt(extra.getString("status"));
            f=Integer.parseInt(extra.getString("timer"));
            Toast.makeText(getApplicationContext(),Integer.toString(f),Toast.LENGTH_SHORT).show();
        }
        else if (extra==null) {

            count=100;
        }

        updateButtons();


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b1.getText().toString().equals(res1.toString())) {
                    Toast.makeText(getApplicationContext(), "10 points", Toast.LENGTH_SHORT).show();
                    ObjectAnimator animator=ObjectAnimator.ofInt(prgsbar,"progress",prgsbar.getProgress()+10);
                    prgsbar.setProgress(prgsbar.getProgress());
                    animator.setDuration(500);
                    animator.setInterpolator(new DecelerateInterpolator());
                    animator.start();
                    updateButtons();
                }
                else if (!b1.getText().toString().equals(res1.toString())) {
                    updateButtons();
                    ObjectAnimator animator=ObjectAnimator.ofInt(prgsbar,"progress",prgsbar.getProgress()+10);
                    prgsbar.setProgress(prgsbar.getProgress());
                    animator.setDuration(500);
                    animator.setInterpolator(new DecelerateInterpolator());
                    animator.start();
                    vibrator.vibrate(50);
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b2.getText().toString().equals(res1.toString())) {
                    Toast.makeText(getApplicationContext(), "10 points", Toast.LENGTH_SHORT).show();
                    updateButtons();
                }
                else if (!b2.getText().toString().equals(res1.toString())) {
                    updateButtons();
                    vibrator.vibrate(50);
                }
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b3.getText().toString().equals(res1.toString())) {
                    updateButtons();
                    Toast.makeText(getApplicationContext(), "10 points", Toast.LENGTH_SHORT).show();
                }
                else if (!b3.getText().toString().equals(res1.toString())) {
                    updateButtons();
                    vibrator.vibrate(50);
                }
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b4.getText().toString().equals(res1.toString())) {
                    Toast.makeText(getApplicationContext(), "10 points", Toast.LENGTH_SHORT).show();
                    updateButtons();
                } else if (!b4.getText().toString().equals(res1.toString())) {
                    updateButtons();
                    vibrator.vibrate(50);
                }
            }
        });

    }

    public void updateButtons() {
        Random rand = new Random();
        t1=Integer.toString(rand.nextInt(20) + 1);
        t2=Integer.toString(rand.nextInt(4)+1);
        t3=Integer.toString(rand.nextInt(20) + 1);

        tv1.setText(t1);
        tv3.setText(t3);
        switch (Integer.parseInt(t2))
        {
            case 1 :
                res1=Float.parseFloat(tv1.getText().toString())+Float.parseFloat(tv3.getText().toString());
                res2=Float.parseFloat(tv1.getText().toString())+Float.parseFloat(tv3.getText().toString())+1;
                res3=Float.parseFloat(tv1.getText().toString())+Float.parseFloat(tv3.getText().toString())+2;
                res4=Float.parseFloat(tv1.getText().toString())+Float.parseFloat(tv3.getText().toString())-1;
                tv2.setText("+");
                break;
            case 2 :
                res1=Float.parseFloat(tv1.getText().toString())-Float.parseFloat(tv3.getText().toString());
                res2=Float.parseFloat(tv1.getText().toString())-Float.parseFloat(tv3.getText().toString())+1;
                res3=Float.parseFloat(tv1.getText().toString())-Float.parseFloat(tv3.getText().toString())-1;
                res4=Float.parseFloat(tv1.getText().toString())-Float.parseFloat(tv3.getText().toString())+2;
                tv2.setText("-");
                break;
            case 3 :
                res1=Float.parseFloat(tv1.getText().toString())*Float.parseFloat(tv3.getText().toString());
                res2=Float.parseFloat(tv1.getText().toString())*Float.parseFloat(tv3.getText().toString())+1;
                res3=Float.parseFloat(tv1.getText().toString())*Float.parseFloat(tv3.getText().toString())+2;
                res4=Float.parseFloat(tv1.getText().toString())*Float.parseFloat(tv3.getText().toString())-3;
                tv2.setText("*");
                break;
            case 4 :
                res1=Float.parseFloat(tv1.getText().toString())/Float.parseFloat(tv3.getText().toString());
                res2=Float.parseFloat(tv1.getText().toString())/Float.parseFloat(tv3.getText().toString())-1;
                res3=Float.parseFloat(tv1.getText().toString())/Float.parseFloat(tv3.getText().toString())-2;
                res4=Float.parseFloat(tv1.getText().toString())/Float.parseFloat(tv3.getText().toString())+1;
                tv2.setText("/");
                break;
        }

        String [] res={res1.toString(),res2.toString(),res3.toString(),res4.toString()};
        final List<String> list=new ArrayList<>();
        Collections.addAll(list,res);

        if (flag==0) {
            b1.setText(res[(int) (Math.random() * 4)]);
            list.remove(b1.getText().toString());
            res=list.toArray(new String[list.size()]);
        }

        if (flag==0) {
            b2.setText(res[(int) (Math.random() * 3)]);
            list.remove(b2.getText().toString());
            res=list.toArray(new String[list.size()]);
        }

        if (flag==0) {
            b3.setText(res[(int) (Math.random() * 2)]);
            list.remove(b3.getText().toString());
            res=list.toArray(new String[list.size()]);
        }

        if (flag==0) {
            b4.setText(res[(int) (Math.random() * 1)]);
            list.remove(b4.getText().toString());
            //res=list.toArray(new String[list.size()]);
        }
    }

    public void timervalue() {
        if (f==0) {
            startActivity(new Intent(getApplicationContext(),Result.class));
        }
    }

    @Override
     public void onBackPressed() {
        prgsbar.setVisibility(View.INVISIBLE);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Game Paused");
        builder.setMessage("Do you want to stop the game now...??");
        builder.setCancelable(true);
        builder.setPositiveButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(getApplicationContext(),mad.class);
                Bundle bundle=new Bundle();
                bundle.putString("status", Integer.toString(prgsbar.getProgress()));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(),NewMain.class));
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }
}