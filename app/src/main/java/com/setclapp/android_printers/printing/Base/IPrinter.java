package com.setclapp.android_printers.printing.Base;

import android.graphics.Bitmap;

import com.setclapp.android_printers.common.ActionResult;

public interface IPrinter {

    int getSleepValueBetweenBitmap();

    ActionResult connect();

    ActionResult print(Bitmap bitmap, int bitmapWidth);

    ActionResult print(Bitmap[] bitmap, int bitmapWidth);


}
