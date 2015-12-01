package com.example.chrno.editorimagenes;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

/**
 * Created by Chrno on 30/11/2015.
 */
public class Edicion {


    public static Bitmap toEscalaDeGris(Bitmap bmpOriginal) {
        Bitmap bmpGris = Bitmap.createBitmap(bmpOriginal.getWidth(),
                bmpOriginal.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas lienzo = new Canvas(bmpGris);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(cmcf);
        lienzo.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGris;
    }

    public static Bitmap rotarBitmap(Bitmap bmpOriginal, float angulo) {
        Matrix matriz = new Matrix();
        matriz.postRotate(angulo);
        return Bitmap.createBitmap(bmpOriginal, 0, 0,
                bmpOriginal.getWidth(), bmpOriginal.getHeight(), matriz, true);
    }

    public Bitmap cambioBitmapColor(Bitmap sourceBitmap, int color) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
                sourceBitmap.getWidth() - 1, sourceBitmap.getHeight() - 1);
        Paint p = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        p.setColorFilter(filter);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);
        return resultBitmap;
    }

    public Bitmap espejo(Bitmap bitmap) {
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        int pixel, red, green, blue, alpha;
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                pixel = bitmap.getPixel(i, j);
                red = Color.red(pixel);
                green = Color.green(pixel);
                blue = Color.blue(pixel);
                alpha = Color.alpha(pixel);
                bmp.setPixel(bitmap.getWidth() - i - 1, j, Color.argb(alpha, red, green, blue));
            }
        }
        return bmp;
    }

    public Bitmap toSephia(Bitmap bmpOriginal) {
        int width, height, r, g, b, c, gry;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        int depth = 20;

        Bitmap bmpSephia = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpSephia);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setScale(.3f, .3f, .3f, 1.0f);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        canvas.drawBitmap(bmpOriginal, 0, 0, paint);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                c = bmpOriginal.getPixel(x, y);
                r = Color.red(c);
                g = Color.green(c);
                b = Color.blue(c);
                gry = (r + g + b) / 3;
                r = g = b = gry;
                r = r + (depth * 2);
                g = g + depth;
                if (r > 255) {
                    r = 255;
                }
                if (g > 255) {
                    g = 255;
                }
                bmpSephia.setPixel(x, y, Color.rgb(r, g, b));
            }

        }
        return bmpSephia;
    }

    public Bitmap brillo(Bitmap src, double value)
    {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap

        // create a mutable empty bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        // create a canvas so that we can draw the bmOut Bitmap from source bitmap
        Canvas c = new Canvas();
        c.setBitmap(bmOut);

        // draw bitmap to bmOut from src bitmap so we can modify it
        c.drawBitmap(src, 0, 0, new Paint(Color.BLACK));


        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = Color.green(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = Color.blue(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }
}
