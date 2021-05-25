package com.recipe.recipebook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayOutputStream;



public class imageUtils {

//    private static long MAX_SIZE = 307200;
    //private static long THUMB_SIZE = 6553;
    private static final float PREFERRED_WIDTH = 600;
    private static final float PREFERRED_HEIGHT = 600;

    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }



//    public static Bitmap reduceBitmapSize(Bitmap bitmap) {
//        double ratioSquare;
//        int bitmapHeight, bitmapWidth;
//        bitmapHeight = bitmap.getHeight();
//        bitmapWidth = bitmap.getWidth();
//        ratioSquare = (bitmapHeight * bitmapWidth) / MAX_SIZE;
//        if (ratioSquare <= 1)
//            return bitmap;
//        double ratio = Math.sqrt(ratioSquare);
////        Log.d("mylog", "Ratio: " + ratio);
//        int requiredHeight = (int) Math.round(bitmapHeight / ratio);
//        int requiredWidth = (int) Math.round(bitmapWidth / ratio);
//        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true);
//
//
//    }

    public static Bitmap resizeBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = PREFERRED_WIDTH / width;
        float scaleHeight = PREFERRED_HEIGHT / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

}
