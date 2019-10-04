package com.setclapp.android_printers.printing.Printers;

import android.content.Context;
import android.graphics.Bitmap;

import com.setclapp.android_printers.common.ActionResult;
import com.setclapp.android_printers.printing.Base.BasePrinter;
import com.setclapp.android_printers.printing.Base.IPrinter;
import com.setclapp.android_printers.printing.Helpers.SunmiPrinterHelper;

public class SunmiPrinter extends BasePrinter implements IPrinter {

    private Context context;

    public SunmiPrinter(Context context) {
        this.context = context;
    }

    @Override
    public ActionResult print(byte[] data) {
        return null;
    }

    @Override
    public int getSleepValueBetweenBitmap() {
        return 3000;
    }

    @Override
    public ActionResult connect() {

        try {

            SunmiPrinterHelper.getInstance().connectPrinterService(context.getApplicationContext());
            SunmiPrinterHelper.getInstance().initPrinter();

            return ActionResult.succeed();

        } catch (Exception exc) {
            return ActionResult.failure(exc);
        }
    }

    @Override
    public ActionResult print(Bitmap bitmap, int bitmapWidth) {
        try {

            SunmiPrinterHelper.getInstance().printBitmap(bitmap);

            return ActionResult.succeed();

        } catch (Exception exc) {

            return ActionResult.failure(exc);
        }
    }

    @Override
    public ActionResult print(Bitmap[] bitmap, int bitmapWidth) {
        return null;
    }

    public void printQrCode(String content) {
        SunmiPrinterHelper.getInstance().printQr(content, 10, 1);
        SunmiPrinterHelper.getInstance().printBarCode(content, 3,100,3,2);
    }
}
