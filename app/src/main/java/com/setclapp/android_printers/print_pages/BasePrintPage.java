package com.setclapp.android_printers.print_pages;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class BasePrintPage {

    protected Context context;
    private int left, top, lineMargin;
    private Paint paint;
    private Canvas canvas;

    public BasePrintPage(Context context) {
        this.context = context;
    }

    protected void setCanvas(Canvas value) {
        canvas = value;
    }

    protected void setPaint(Paint value) {
        paint = value;
    }

    protected void setLineMargin(int value) {
        lineMargin = value;
    }

    protected void increaseTop() {
        increaseTop(lineMargin);
    }

    protected void increaseTop(int value) {
        top += value;
    }

    protected void decreaseTop() {
        decreaseTop(top);
    }

    protected void decreaseTop(int value) {
        top -= value;
    }

    protected void setTop(int value) {
        top = value;
    }

    protected int getLineMargin() {
        return lineMargin;
    }

    protected void setLeft(int value) {
        left = value;
    }

    protected int getLeft() {
        return left;
    }

    protected void drawText(String text) {
        canvas.drawText(text, left, top, paint);
    }

    protected void drawText(String text, int leftVal) {
        canvas.drawText(text, leftVal, top, paint);
    }

    protected void drawTextLn(String text) {
        canvas.drawText(text, left, top, paint);
        top += lineMargin;
    }

    protected void drawQrCode(String content, int width, int height) throws Exception {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        canvas.drawBitmap(bitmap, left, top, paint);
    }

    protected void drawQrCodeLn(String content, int width, int height) throws Exception {
        drawQrCode(content, width, height);
        top += height;
    }

    protected void drawBarcode(String content, int width, int height) throws Exception {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.CODE_128, width, height);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        canvas.drawBitmap(bitmap, left, top, paint);
    }

    protected void drawBarcodeLn(String content, int width, int height) throws Exception {
        drawBarcode(content, width, height);
        top += height + lineMargin;
    }

    protected void drawTextOnRight(String text, int marginRight) {
        canvas.drawText(text, canvas.getWidth() - paint.measureText(text) - marginRight, top, paint);
    }

    protected void drawTextOnRightLn(String text, int marginRight) {
        drawTextOnRight(text, marginRight);
        top += lineMargin;
    }

    protected void drawTextBoldOnRight(String text, int marginRight) {
        paint.setFakeBoldText(true);
        drawTextOnRight(text, marginRight);
        paint.setFakeBoldText(false);
    }

    protected void drawTextOnRightBoldLn(String text, int marginRight) {
        drawTextBoldOnRight(text, marginRight);
        top += lineMargin;
    }

    protected void drawTextBold(String text) {
        paint.setFakeBoldText(true);
        canvas.drawText(text, left, top, paint);
        paint.setFakeBoldText(false);
    }

    protected void drawTextBold(String text, int leftValue) {
        paint.setFakeBoldText(true);
        canvas.drawText(text, leftValue, top, paint);
        paint.setFakeBoldText(false);
    }

    protected void drawTextBoldLn(String text) {
        drawTextBold(text);
        top += lineMargin;
    }

    protected void drawTextOnCenter(String text) {
        float textWidth = paint.measureText(text);
        int xOffset = (int) ((canvas.getWidth() - textWidth) / 2f) - (int) (paint.getTextSize() / 2f);
        canvas.drawText(text, xOffset, top, paint);
    }

    protected void drawTextOnCenterLn(String text) {
        drawTextOnCenter(text);
        top += lineMargin;
    }

    protected void drawTextOnCenterBold(String text) {
        paint.setFakeBoldText(true);
        drawTextOnCenter(text);
        paint.setFakeBoldText(false);
    }

    protected void drawTextBoldOnCenterLn(String text) {
        drawTextOnCenterBold(text);
        top += lineMargin;
    }
}