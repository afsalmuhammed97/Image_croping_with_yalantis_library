package com.practies.ucrop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.yalantis.ucrop.UCrop;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    private ImageView img;
    private  final  int CODE_IMG_GALLERY=1;
    private final  String SAMPLE_CROPPED_IMAGE_NAME="SampleCropeImage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  init();
        this.img =findViewById(R.id.imageView);
        Button chooseButton = findViewById(R.id.button);







                chooseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT)
                                .setType("image/*"),CODE_IMG_GALLERY);
                    }
                });
    }


    private  void  init(){
        this.img =findViewById(R.id.imageView);
    //    Button chooseButton = findViewById(R.id.button);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == CODE_IMG_GALLERY && resultCode==RESULT_OK){


             assert data != null;
             Uri imageUri = data.getData();
             if (imageUri != null){
                 startCrop(imageUri);
             }
         }else  if(requestCode == UCrop.REQUEST_CROP && resultCode==RESULT_OK){
             assert data != null;
             Uri imageUriResultCrop = UCrop.getOutput(data);

               if(imageUriResultCrop != null){
                   img.setImageURI(imageUriResultCrop);
               }
         }
    }



    private  void startCrop(@Nullable Uri uri){
        String destinationFileName =SAMPLE_CROPPED_IMAGE_NAME;
        destinationFileName +=".jpg";


        assert uri != null;
     //   String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        UCrop uCrop= UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationFileName))); //getCacheDir()

         uCrop.withAspectRatio(1,1);
       uCrop.withAspectRatio(3,4);
       uCrop.useSourceImageAspectRatio();
       uCrop.withAspectRatio(2,3);
         uCrop.withAspectRatio(0,0);


           uCrop.withMaxResultSize(500,500);
           uCrop.withOptions(getCropOptions());
           uCrop.start(MainActivity.this);


    }

    private  UCrop.Options getCropOptions(){
        UCrop.Options options= new UCrop.Options();

        options.setCompressionQuality(70);
        //Compress Type
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        //UI

        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

        //Colors
        options.setStatusBarColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
        options.setToolbarColor(getResources().getColor(R.color.purple_700));

        options.setToolbarTitle("Crop Image");

        return  options;

    }
}