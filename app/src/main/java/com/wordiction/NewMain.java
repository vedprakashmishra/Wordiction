package com.wordiction;

import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Vector;

public class NewMain extends FragmentActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_FILE = 1889;
    ImageView iv,set;
    private PagerAdapter pagerAdapter;
    String name="";
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_layout);
        intialisepaging();


        iv = (ImageView) this.findViewById(R.id.pic);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        set= (ImageView) this.findViewById(R.id.settings);

        tv = (TextView) findViewById(R.id.username);
        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Cactus Plain.ttf");
        tv.setTypeface(font);

        DatabaseHandler db= new DatabaseHandler(getApplicationContext());
        if (db.getUserName()==""){
            tv.setText("guest");
        }
            else {
            tv.setText(db.getUserName());
        }

        Data d=new Data(getApplicationContext());
        if (d.getImage()==null){
            iv= (ImageView) findViewById(R.id.pic);
        }
        else {
            Bitmap bm=d.getImage();
            iv.setImageBitmap(bm);
        }


        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewMain.this,Settings.class));
                overridePendingTransition(R.anim.anim1,R.anim.anim2);
            }
        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(NewMain.this);
                LayoutInflater li = LayoutInflater.from(NewMain.this);
                View promptsView = li.inflate(R.layout.prompt, null);
                builder.setView(promptsView);
                final EditText et = (EditText) promptsView.findViewById(R.id.editText);

                builder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name=et.getText().toString();
                        tv.setText(name);
                        if (name.trim().length() > 0) {
                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            db.insertName(name);

                            loadName();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter name",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(NewMain.this);
                builder.setTitle("Add Photo");
                builder.setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_REQUEST);
                        } else if (items[item].equals("Choose from Library")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

    }

    private void intialisepaging() {
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, Wordy.class.getCanonicalName()));
        fragments.add(Fragment.instantiate(this, MadMath.class.getCanonicalName()));

        pagerAdapter = new PagerAdapter(this.getSupportFragmentManager(), fragments);
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setPageTransformer(true, new ZoomOutTransformation());
        viewpager.setAdapter(pagerAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Bitmap pic = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                pic.compress(Bitmap.CompressFormat.PNG, 50, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()
                        + ".png");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                iv.setImageBitmap(pic);

                Toast.makeText(getApplicationContext(), "cam", Toast.LENGTH_SHORT).show();
                Data db=new Data(getApplicationContext());
                db.insertImage(pic);
                Log.e("1","1");
                loadPic();
            }
            else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                iv.setImageBitmap(bm);

                Data db=new Data(getApplicationContext());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 50, bytes);

                Toast.makeText(getApplicationContext(), "gal", Toast.LENGTH_SHORT).show();
                db.insertImage(bm);
                loadPic();
            }
        }
    }
    public void loadName() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        name=db.getUserName();
        Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();
        tv.setText(name);
    }

    public void loadPic() {
        Data db = new Data(getApplicationContext());
        Log.e("2","2");
        Bitmap bm=db.getImage();
        iv.setImageBitmap(bm);

    }
}
