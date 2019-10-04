package com.setclapp.android_printers.printing.Printers;

import android.content.Context;
import android.graphics.Bitmap;

import com.setclapp.android_printers.AclasSdkHelper;
import com.setclapp.android_printers.common.ActionResult;
import com.setclapp.android_printers.printing.Base.BasePrinter;
import com.setclapp.android_printers.printing.Base.IPrinter;

import java.io.ByteArrayOutputStream;

import cn.weipass.pos.sdk.IPrint;
import cn.weipass.pos.sdk.Printer;
import cn.weipass.pos.sdk.impl.WeiposImpl;

public class AclasPrinter extends BasePrinter implements IPrinter {

    private Context context;
    private Printer printer;

    public AclasPrinter(Context context) {
        this.context = context;
    }


    @Override
    public int getSleepValueBetweenBitmap() {
        return 1000;
    }

    @Override
    public ActionResult connect() {

        try {

            AclasSdkHelper.initSdk(context);

            printer = WeiposImpl.as().openPrinter();

            return ActionResult.succeed();

        } catch (Exception exc) {
            return ActionResult.failure(exc);
        }
    }

    @Override
    public ActionResult print(Bitmap bitmap, int bitmapWidth) {

        try {
            byte[] bmpData = bitmap2Bytes(bitmap);


            printer.setOnEventListener(new IPrint.OnEventListener() {
                @Override
                public void onEvent(int i, String s) {

                }
            });

            printer.printImage(bmpData, IPrint.Gravity.CENTER);

            return ActionResult.succeed();

        } catch (Exception exc) {
            return ActionResult.failure(exc);
        }
    }

    @Override
    public ActionResult print(Bitmap[] bitmap, int bitmapWidth) {
        return null;
    }

    public static final byte[] bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return new byte[]{};
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    public ActionResult print(byte[] data) {
        return null;
    }
}
