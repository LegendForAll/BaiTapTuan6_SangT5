package com.example.hothaingoc.ex6;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editText_name, editText_email, editText_phone;
    Button button_change, button_save, button_cancel;
    ImageView ImageAvata;
    RadioGroup radioGroup_gener;
    RadioButton radioButton;

    Intent intent;
    int request_code = 901;
    private static final String FILENAME = "myfile.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();

        //RadioButton
        int selectedId = radioGroup_gener.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,request_code);
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable drawable = (BitmapDrawable) ImageAvata.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                if (editText_name == null) {
                    Toast.makeText(MainActivity.this, "Name is NULL", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        WriteData(editText_name.getText().toString(), editText_email.getText().toString(), editText_phone.getText().toString(),bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        ReadData(FILENAME);
    }
    public void WriteData(String name, String mail, String PhoneNumber, Bitmap img) {
        try {
            FileOutputStream fileOutputStream = openFileOutput(FILENAME, MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(name+"\n");
            outputStreamWriter.write(mail+"\n");
            outputStreamWriter.write(PhoneNumber+"\n");
            outputStreamWriter.write(radioButton.getText().toString()+"\n");
            outputStreamWriter.write(BitmapToString(img));
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ReadData(String FILENAME){
        try {
            FileInputStream fileIn=openFileInput(FILENAME);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[100000];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            String[] items=s.split("\n");
            List<String> list= new ArrayList<String>();
            for(int i=0;i<items.length;i++){
                list.add(items[i]);
            }
            editText_name.setText(items[0]);
            editText_email.setText(items[1]);
            editText_phone.setText(items[2]);
            if(items[3]=="Male"){
                radioButton = (RadioButton) findViewById(R.id.rbt_male);
                radioButton.setChecked(true);
            }if(items[3]=="Famale"){
                radioButton = (RadioButton) findViewById(R.id.rbt_female);
                radioButton.setChecked(true);
            }
            ImageAvata.setImageBitmap(StringToBitmap(items[4]));
            InputRead.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_code && resultCode == RESULT_OK && data != null) {
            Bitmap bm = (Bitmap) data.getExtras().get("data");
            ImageAvata.setImageBitmap(bm);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String BitmapToString(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b,Base64.NO_WRAP);
            return temp;
        } catch (NullPointerException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.NO_WRAP);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (NullPointerException e) {
            e.getMessage();
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
    private void AnhXa() {
        ImageAvata          = (ImageView) findViewById(R.id.imageView);
        editText_name       = (EditText) findViewById(R.id.edit_name);
        editText_email      = (EditText) findViewById(R.id.edit_email);
        editText_phone      = (EditText) findViewById(R.id.edit_phone);
        button_change       = (Button) findViewById(R.id.btn_Change);
        button_save         = (Button) findViewById(R.id.btn_save);
        button_cancel       = (Button) findViewById(R.id.btn_cancel);
        radioGroup_gener    = (RadioGroup) findViewById(R.id.rgroup_gener);
    }

}