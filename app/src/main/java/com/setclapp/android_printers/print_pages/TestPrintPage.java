package com.setclapp.android_printers.print_pages;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import com.setclapp.android_printers.common.ActionResult;
import com.setclapp.android_printers.printing.Base.IPrintPage;
import com.setclapp.android_printers.printing.Base.IPrinter;
import com.setclapp.android_printers.printing.PrintPageModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestPrintPage extends BasePrintPage implements IPrintPage<PrintPageModel> {

    private IPrinter printer;

    public static Activity activity;

    public TestPrintPage(Context context, IPrinter printer) {

        super(context);

        this.printer = printer;
    }


    @Override
    public ActionResult print(PrintPageModel data) {

        try {


            int height = 450;

            Bitmap canvasBitmap = Bitmap.createBitmap(384, height, Bitmap.Config.ARGB_4444);

            Canvas canvas = new Canvas(canvasBitmap);

            canvas.setBitmap(canvasBitmap);

            canvas.drawColor(Color.WHITE);

            Paint paint = new TextPaint();

            paint.setTextSize(23);

            setCanvas(canvas);

            setPaint(paint);

            setTop(40);

            setLineMargin(30);

            drawTextBoldOnCenterLn("TEST PRINT PAGE");

            drawTextOnCenterLn("TEXT CENTER");

            setLeft((canvas.getWidth() - 250) / 2);

            drawQrCodeLn("https://github.com/elvinhb076/android_bitmap_printer/upload/master", 250, 250);

            drawBarcodeLn("1234567890", 250, 80);

            setLeft(0);

            drawTextOnCenterLn(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()));

            ActionResult result = printer.connect();

            if (!result.isSucceed())
                return ActionResult.failure(result.getException());

            printer.print(canvasBitmap, 48);

            return ActionResult.succeed();

        } catch (RuntimeException exc) {

            return ActionResult.failure(exc);

        } catch (Exception exc) {

            return ActionResult.failure(exc);
        }
    }
}

