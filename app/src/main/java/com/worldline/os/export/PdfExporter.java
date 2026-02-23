package com.worldline.os.export;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class PdfExporter {

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

    // Bitmap → PDF ファイル（Downloads）
    public static File saveBitmapAsPdf(Context ctx, Bitmap bmp, String filename) throws Exception {

        PdfDocument pdf = new PdfDocument();

        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(bmp.getWidth(), bmp.getHeight(), 1).create();

        PdfDocument.Page page = pdf.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(bmp, 0, 0, null);
        pdf.finishPage(page);

        File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!downloads.exists()) downloads.mkdirs();

        File outFile = new File(downloads, filename + ".pdf");

        FileOutputStream fos = new FileOutputStream(outFile);
        pdf.writeTo(fos);
        fos.flush();
        fos.close();

        pdf.close();

        return outFile;
    }
}