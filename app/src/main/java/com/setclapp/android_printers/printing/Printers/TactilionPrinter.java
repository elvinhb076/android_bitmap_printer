package com.setclapp.android_printers.printing.Printers;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.basewin.aidl.OnPrinterListener;
import com.basewin.services.PrinterBinder;
import com.basewin.services.ServiceManager;
import com.setclapp.android_printers.R;
import com.setclapp.android_printers.common.ActionResult;
import com.setclapp.android_printers.printing.Base.BasePrinter;
import com.setclapp.android_printers.printing.Base.IPrinter;

import org.json.JSONArray;
import org.json.JSONObject;

public class TactilionPrinter extends BasePrinter implements IPrinter {

    private JSONObject printJson;
    private Context context;

    private PrinterListener printer_callback;
    private PrinterBinder binder;

    @Nullable
    private int completed;

    private String message;

    public TactilionPrinter(Context context) {
        this.context = context;
        printJson = new JSONObject();

        printer_callback = new PrinterListener(context);
    }

    public void find(String name) {

    }

    @Override
    public int getSleepValueBetweenBitmap() {
        return 3000;
    }

    @Override
    public ActionResult connect() {

        try {

            ServiceManager.getInstence().init(context);

            binder = ServiceManager.getInstence().getPrinter();

            return ActionResult.succeed();

        } catch (Exception exc) {
            return ActionResult.failure(exc);
        }
    }


    @Override
    public ActionResult print(Bitmap bitmap, int bitmapWidth) {
        return print(new Bitmap[]{bitmap}, bitmapWidth);
    }

    @Override
    public ActionResult print(Bitmap[] bitmaps, int bitmapWidth) {
        try {

            JSONArray printTest = new JSONArray();

            // add picture
            JSONObject json11 = new JSONObject();
            json11.put("content-type", "jpg");
            json11.put("position", "center");

            printTest.put(json11);

            printJson.put("spos", printTest);

            // 设置底部空3行

            binder.setPrintGray(1000);

            binder.print(printJson.toString(), bitmaps, printer_callback);

            completed = -1;

//            waitPrinting();
//
//            if (completed == 1)
//                return ActionResult.succeed();
//            else
//                return ActionResult.failure(new Exception(message));

            return ActionResult.succeed();

        } catch (Exception exc) {

            return ActionResult.failure(exc);
        }
    }

    private void waitPrinting() {

        while (true) {
            if (completed != -1)
                break;
        }

    }

    @Override
    public ActionResult print(byte[] data) {
        return null;
    }


    class PrinterListener implements OnPrinterListener {

        private Context context;

        public PrinterListener(Context context) {
            this.context = context;
        }

        @Override
        public void onStart() {
            // TODO 打印开始
            // Print start
        }

        @Override
        public void onFinish() {
            // TODO 打印结束
            // End of the print
            if (completed != 0)
                completed = 1;
        }

        @Override
        public void onError(int errorCode, String detail) {
            // TODO 打印出错
            // print error
            if (errorCode == PrinterBinder.PRINTER_ERROR_NO_PAPER) {
                message ="No paper";
            } else
                message ="Error occured when printing";

            completed = 0;
        }
    }

}
