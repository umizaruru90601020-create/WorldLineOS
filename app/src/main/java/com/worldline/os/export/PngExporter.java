package com.worldline.os.export;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

public class PngExporter {

    // View → Bitmap
    public static Bitmap viewToBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(
                v.getWidth(),
                v.getHeight(),
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    // Bitmap → PNG ファイル（Downloads に保存）
    public static File saveBitmapAsPng(Context ctx, Bitmap bmp, String filename) throws Exception {

        File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!downloads.exists()) downloads.mkdirs();

        File outFile = new File(downloads, filename + ".png");

        FileOutputStream fos = new FileOutputStream(outFile);
        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();

        return outFile;
    }
}
