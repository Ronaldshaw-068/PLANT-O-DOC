package com.android.lillian.plantodoc;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.IOException;

public class MyUtils {
    public static final int GALLERY_REQUEST_CODE = 0;
    public static final int IMAGE_CAPTURE_REQUEST_CODE = 1;
    public static final String[] PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    public static Bitmap getBitMap(Context context, int requestCode, Intent data) {
        switch (requestCode) {
            case GALLERY_REQUEST_CODE:
                return getImageBitmap(context, GALLERY_REQUEST_CODE, data);
            case IMAGE_CAPTURE_REQUEST_CODE:
                return getImageBitmap(context, IMAGE_CAPTURE_REQUEST_CODE, data);
            default:
                break;
        }
        return null;
    }

    private static Bitmap getImageBitmap(Context context, final int requestCode, Intent data){

        Bitmap bitmap = null;

        if (requestCode == GALLERY_REQUEST_CODE) {
            ContentResolver resolver = context.getContentResolver();
            Uri originalUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
        }
        return bitmap;
    }
}
