package com.wordiction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class Settings extends Activity {

    String name="";
    TextView tv;
    ImageButton ib;
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_FILE = 1889;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        tv= (TextView) findViewById(R.id.UserText);
        ib= (ImageButton) findViewById(R.id.imageButton);

        DatabaseHandler db= new DatabaseHandler(getApplicationContext());
        if (db.getUserName().equals("")){
            tv.setText("guest");
        }
        else {
            tv.setText(db.getUserName());
        }

        Data d=new Data(getApplicationContext());
        if (d.getImage()==null){
            ib= (ImageButton) findViewById(R.id.imageButton);
        }
        else {
            Bitmap bm=d.getImage();
            ib.setImageBitmap(bm);
        }

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                LayoutInflater li = LayoutInflater.from(Settings.this);
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



        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
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
                ib.setImageBitmap(pic);

                Toast.makeText(getApplicationContext(), "cam", Toast.LENGTH_SHORT).show();
                Data db=new Data(getApplicationContext());
                byte[] im=bytes.toByteArray();
                db.insertImage(pic);
                Log.e("1", "1");
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
                ib.setImageBitmap(bm);

                Data db=new Data(getApplicationContext());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 50, bytes);

                Toast.makeText(getApplicationContext(), "gal", Toast.LENGTH_SHORT).show();
                byte[] im=bytes.toByteArray();
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
        Log.e("2", "2");
        Bitmap bm=db.getImage();
        ib.setImageBitmap(bm);

    }

}
