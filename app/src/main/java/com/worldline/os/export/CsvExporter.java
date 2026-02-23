package com.worldline.os.export;

import android.content.Context;
import android.os.Environment;

import com.worldline.os.core.model.Worldline;

import java.io.File;
import java.io.FileOutputStream;

public class CsvExporter {

    public static String worldlineToCsvLine(Worldline wl) {

        StringBuilder orderSb = new StringBuilder();
        for (int i = 0; i < wl.order.size(); i++) {
            orderSb.append(wl.order.get(i));
            if (i < wl.order.size() - 1) orderSb.append("-");
        }

        String line =
                wl.type + "," +
                wl.correctionScore + "," +
                escape(wl.reason) + "," +
                orderSb + "," +
                wl.logs.size() + "," +
                System.currentTimeMillis();

        return line;
    }

    private static String escape(String s) {
        if (s.contains(",") || s.contains("\"")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    public static File saveCsvToDownloads(Context ctx, String filename, String csvLine) throws Exception {

        File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!downloads.exists()) downloads.mkdirs();

        File outFile = new File(downloads, filename + ".csv");

        FileOutputStream fos = new FileOutputStream(outFile);
        fos.write(csvLine.getBytes("UTF-8"));
        fos.flush();
        fos.close();

        return outFile;
    }
}