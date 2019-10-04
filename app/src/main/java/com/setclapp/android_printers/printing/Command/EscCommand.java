package com.setclapp.android_printers.printing.Command;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.enumerate.BarcodeStringPosition;
import com.rt.printerlibrary.enumerate.BarcodeType;
import com.rt.printerlibrary.enumerate.EscBarcodePrintOritention;
import com.rt.printerlibrary.enumerate.SettingEnum;
import com.rt.printerlibrary.exception.SdkException;
import com.rt.printerlibrary.setting.BarcodeSetting;
import com.rt.printerlibrary.setting.BitmapSetting;
import com.rt.printerlibrary.setting.CommonSetting;
import com.rt.printerlibrary.setting.TextSetting;
import com.rt.printerlibrary.utils.BitmapConvertUtil;
import com.rt.printerlibrary.utils.BitmapUtil;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class EscCommand extends Cmd {
    private static final byte[] a = new byte[]{18, 84};
    private static final byte[] b = new byte[]{27, 64};
    private static final byte[] c = new byte[]{10};
    private static final byte[] d = new byte[]{13};
    private static final byte[] e = new byte[]{10, 13};
    private ArrayList<Byte> f = new ArrayList();
    private ArrayList<Byte> g = new ArrayList();
    private ArrayList<Byte> h = new ArrayList();
    private ArrayList<Byte> i = new ArrayList();

    public EscCommand() {
    }

    public byte[] getHeaderCmd() {
        System.out.println("com.rt.printerlibrary.cmd.EscCommand.getHeaderCmd");
        return b;
    }

    public byte[] getCpclHeaderCmd(int var1, int var2, int var3) {
        return new byte[0];
    }

    public byte[] getPrintCopies(int var1) {
        return new byte[0];
    }

    public byte[] getEndCmd() {
        try {
            throw new SdkException("Esc doesn't not support the method getEndCmd()");
        } catch (SdkException var2) {
            var2.printStackTrace();
            return new byte[0];
        }
    }

    public byte[] getLFCmd() {
        return c;
    }

    public byte[] getCRCmd() {
        return d;
    }

    public byte[] getLFCRCmd() {
        return e;
    }

    @SuppressLint({"WrongConstant"})
    public byte[] getCommonSettingCmd(CommonSetting var1) {
        this.g.clear();
        if (var1.getAlign() != -1) {
            this.g.add((byte) 27);
            this.g.add((byte) 97);
            if (var1.getAlign() > 2 || var1.getAlign() < 0) {
                var1.setAlign(0);
            }

            this.g.add((byte) var1.getAlign());
        }

        if (var1.getPageWidth() != 0) {
            this.a(var1);
        }

        return this.listToArray(this.g);
    }

    public byte[] getTextCmd(TextSetting var1, String var2, String var3) throws UnsupportedEncodingException {
        this.f.clear();
        System.out.println("com.rt.printerlibrary.cmd.EscCommand.getTextCmd");
        this.a(var1);
        byte[] var4 = var2.getBytes(var3);

        for (int var5 = 0; var5 < var4.length; ++var5) {
            this.f.add(var4[var5]);
        }

        return this.listToArray(this.f);
    }

    public byte[] getTextCmd(TextSetting var1, String var2) throws UnsupportedEncodingException {
        return this.getTextCmd(var1, var2, this.getChartsetName());
    }

    public byte[] getBitmapCmd(BitmapSetting var1, Bitmap var2) {

        this.h.clear();

        BitmapUtil var3 = new BitmapUtil();

        int var4 = var1.getBimtapLimitWidth();

        if (var4 == 0) {
            var4 = var2.getWidth();
        }

        if (var4 > 576) {
            var4 = 576;
        }

        this.a(var2, var4);

        var3 = null;

        return this.listToArray(this.h);
    }

    private void a(Bitmap var1, int var2) {

        byte var13 = (byte) ((var1.getWidth() + 7) / 8 % 256);

        byte var14 = (byte) ((var1.getWidth() + 7) / 8 / 256);

        byte var5 = 30;

        int var6 = (var1.getHeight() + var5 - 1) / var5;

        int var7 = (var1.getWidth() + 7) / 8;

        byte[] var8 = BitmapConvertUtil.hs_bmpToDatas(var1);

        for (int var12 = 0; var12 < var6; ++var12) {

            byte var9;
            byte var10;
            byte[] var11;

            if (var12 != var6 - 1) {

                var9 = (byte) var5;

                var10 = 0;

                this.arrayAddToList(new byte[]{29, 118, 48, 0, var13, var14, var9, var10}, this.h);

                var11 = Arrays.copyOfRange(var8, var7 * var12 * var5, var7 * var5 * (var12 + 1) - 1);

                this.arrayAddToList(var11, this.h);

                this.arrayAddToList(new byte[]{10}, this.h);

            } else {

                var9 = (byte) (var1.getHeight() % var5);

                var10 = 0;

                this.arrayAddToList(new byte[]{29, 118, 48, 0, var13, var14, var9, var10}, this.h);

                var11 = Arrays.copyOfRange(var8, var7 * var12 * var5, var8.length);

                this.arrayAddToList(var11, this.h);

                this.arrayAddToList(new byte[]{10}, this.h);
            }
        }

        this.arrayAddToList(this.getLFCRCmd(), this.h);
    }

    public byte[] getBarcodeCmd(BarcodeType var1, BarcodeSetting var2, String var3) throws SdkException {
        this.i.clear();
        if (var1.equals(BarcodeType.QR_CODE)) {
            int var4 = var2.getQrcodeDotSize();
            if (var4 > 15) {
                var4 = 12;
                var2.setQrcodeDotSize(var4);
            }

            if (var4 < 0) {
                byte var5 = 3;
                var2.setQrcodeDotSize(var5);
            }
        } else {
            this.a(var2.getBarcodeStringPosition());
            this.a(var2.getEscBarcodePrintOritention());
            this.a(var2);
        }

        this.getVerifiedStr(var3, var1);


        return this.listToArray(this.i);
    }

    private void a(BarcodeSetting var1) {
        int var2 = var1.getBarcodeWidth();
        int var3 = var1.getHeightInDot();
        if (var2 < 2 || var2 > 6) {
            var2 = 3;
        }

        this.i.add((byte) 29);
        this.i.add((byte) 119);
        this.i.add((byte) var2);
        if (var3 < 1 || var3 > 255) {
            var3 = 72;
        }

        this.i.add((byte) 29);
        this.i.add((byte) 104);
        this.i.add((byte) var3);
    }

    public byte[] getSelfTestCmd() {
        return a;
    }

    public byte[] getBeepCmd() {
        byte[] var1 = this.a((byte) 1, (byte) 3);
        return var1;
    }

    public byte[] getAllCutCmd() {
        byte[] var1 = new byte[]{27, 105};
        return var1;
    }

    public byte[] getHalfCutCmd() {
        byte[] var1 = new byte[]{27, 109};
        return var1;
    }

    private byte[] a(byte var1, byte var2) {
        if (var1 < 1 && var1 > 9) {
            var1 = 1;
        }

        if (var2 < 1 && var2 > 9) {
            var2 = 3;
        }

        return new byte[]{27, 66, var1, var2};
    }

    private ArrayList a(TextSetting var1) {
        this.f.clear();
        this.a();
        this.b(var1);
        return this.f;
    }

    @SuppressLint({"WrongConstant"})
    private void b(TextSetting var1) {
        byte var2 = 0;
        byte var3 = 0;
        byte var4 = 0;
        byte var5 = 0;
        if (var1.getIsEscSmallCharactor() == SettingEnum.Enable) {
            var2 = (byte) (var2 | 1);
        } else if (var1.getIsEscSmallCharactor() == SettingEnum.Disable) {
            var2 = (byte) (~(~var2 | 1));
        }

        if (var1.getIsAntiWhite() == SettingEnum.Enable) {
            var2 = (byte) (var2 | 2);
        } else if (var1.getIsAntiWhite() == SettingEnum.Disable) {
            var2 = (byte) (~(~var2 | 2));
        }

        if (var1.getIsAntiWhite() == SettingEnum.Enable) {
            var2 = (byte) (var2 | 2);
            var5 = 1;
        } else if (var1.getIsAntiWhite() == SettingEnum.Disable) {
            var2 = (byte) (~(~var2 | 2));
            var5 = 0;
        }

        if (var1.getBold() == SettingEnum.Enable) {
            var2 = (byte) (var2 | 8);
            var3 = var3 > 0 ? 2 : var3;
        } else if (var1.getBold() == SettingEnum.Disable) {
            var2 = (byte) (~(~var2 | 8));
            var3 = var3 > 0 ? 1 : var3;
        }

        if (var1.getDoubleHeight() == SettingEnum.Enable) {
            var2 = (byte) (var2 | 16);
            var4 = (byte) (var4 | 8);
        } else if (var1.getDoubleHeight() == SettingEnum.Disable) {
            var2 = (byte) (~(~var2 | 16));
            var4 = (byte) (~(~var4 | 8));
        }

        if (var1.getDoubleWidth() == SettingEnum.Enable) {
            var2 = (byte) (var2 | 32);
            var4 = (byte) (var4 | 4);
        } else if (var1.getDoubleWidth() == SettingEnum.Disable) {
            var2 = (byte) (~(~var2 | 32));
            var4 = (byte) (~(~var4 | 4));
        }

        if (var1.getUnderline() == SettingEnum.Enable) {
            if ((var3 & 8) == 8) {
                var3 = 2;
            } else {
                var3 = 1;
            }

            var4 = (byte) (var4 | 128);
        } else if (var1.getUnderline() == SettingEnum.Disable) {
            var3 = 0;
            var4 = (byte) (~(~var4 | 128));
        }

        if (var1.getAlign() != -1) {
            this.f.add((byte) 27);
            this.f.add((byte) 97);
            if (var1.getAlign() > 2 || var1.getAlign() < 0) {
                var1.setAlign(0);
            }

            this.f.add((byte) var1.getAlign());
        }

        this.f.add((byte) 27);
        this.f.add((byte) 33);
        this.f.add(var2);
        this.f.add((byte) 27);
        this.f.add((byte) 45);
        this.f.add(var3);
        this.f.add((byte) 29);
        this.f.add((byte) 66);
        this.f.add(var5);
        this.f.add((byte) 28);
        this.f.add((byte) 33);
        this.f.add(var4);
        this.b();
        if (this.getChartsetName().equals("GBK")) {
            this.a(0);
        } else if (this.getChartsetName().equals("BIG5")) {
            this.a(3);
        } else {
            this.a(1);
        }

    }

    private void a() {
        byte[] var1 = new byte[]{27, 33, 0};
        this.arrayAddToList(var1, this.f);
    }

    private void a(int var1) {
        byte[] var2 = new byte[]{27, 57, (byte) var1};
        this.arrayAddToList(var2, this.f);
    }

    private void b() {
        this.f.add((byte) 28);
        this.f.add((byte) 38);
    }

    public void UPCA(String var1) {
        byte var2 = 0;
        int var3 = var1.length();
        byte var4 = 0;
        byte[] var5 = new byte[var3 + 4];
        int var7 = var4 + 1;
        var5[var4] = 29;
        var5[var7++] = 107;
        var5[var7++] = (byte) var2;

        int var6;
        for (var6 = 0; var6 < var3; ++var6) {
            if (var1.charAt(var6) > '9' || var1.charAt(var6) < '0') {
                return;
            }
        }

        if (var3 <= 30) {
            for (var6 = 0; var6 < var3; ++var6) {
                var5[var7++] = (byte) var1.charAt(var6);
            }

            var5[var7++] = 0;
            this.arrayAddToList(var5, this.i);
        }
    }

    public void UPCE(String var1) {
        byte var2 = 1;
        int var3 = var1.length();
        byte var4 = 0;
        byte[] var5 = new byte[var3 + 4];
        int var7 = var4 + 1;
        var5[var4] = 29;
        var5[var7++] = 107;
        var5[var7++] = (byte) var2;

        int var6;
        for (var6 = 0; var6 < var3; ++var6) {
            if (var1.charAt(var6) > '9' || var1.charAt(var6) < '0') {
                return;
            }
        }

        if (var3 <= 30) {
            for (var6 = 0; var6 < var3; ++var6) {
                var5[var7++] = (byte) var1.charAt(var6);
            }

            var5[var7++] = 0;
            this.arrayAddToList(var5, this.i);
        }
    }

    public void EAN13(String var1) {
        byte var2 = 2;
        int var3 = var1.length();
        byte var4 = 0;
        byte[] var5 = new byte[var3 + 4];
        int var7 = var4 + 1;
        var5[var4] = 29;
        var5[var7++] = 107;
        var5[var7++] = (byte) var2;

        int var6;
        for (var6 = 0; var6 < var3; ++var6) {
            if (var1.charAt(var6) > '9' || var1.charAt(var6) < '0') {
                return;
            }
        }

        if (var3 <= 30) {
            for (var6 = 0; var6 < var3; ++var6) {
                var5[var7++] = (byte) var1.charAt(var6);
            }

            var5[var7++] = 0;
            this.arrayAddToList(var5, this.i);
        }
    }

    public void EAN8(String var1) {
        byte var2 = 3;
        int var3 = var1.length();
        byte var4 = 0;
        byte[] var5 = new byte[var3 + 4];
        int var7 = var4 + 1;
        var5[var4] = 29;
        var5[var7++] = 107;
        var5[var7++] = (byte) var2;

        int var6;
        for (var6 = 0; var6 < var3; ++var6) {
            if (var1.charAt(var6) > '9' || var1.charAt(var6) < '0') {
                return;
            }
        }

        if (var3 <= 30) {
            for (var6 = 0; var6 < var3; ++var6) {
                var5[var7++] = (byte) var1.charAt(var6);
            }

            var5[var7++] = 0;
            this.arrayAddToList(var5, this.i);
        }
    }

    public void CODE39(String var1) {
        byte var2 = 4;
        int var3 = var1.length();
        byte var4 = 0;
        byte[] var5 = new byte[var3 + 4];
        int var7 = var4 + 1;
        var5[var4] = 29;
        var5[var7++] = 107;
        var5[var7++] = (byte) var2;

        int var6;
        for (var6 = 0; var6 < var3; ++var6) {
            if (var1.charAt(var6) > 127 || var1.charAt(var6) < ' ') {
                return;
            }
        }

        if (var3 <= 30) {
            for (var6 = 0; var6 < var3; ++var6) {
                var5[var7++] = (byte) var1.charAt(var6);
            }

            var5[var7++] = 0;
            this.arrayAddToList(var5, this.i);
        }
    }

    public void ITF(String var1) {
        byte var2 = 5;
        int var3 = var1.length();
        byte var4 = 0;
        byte[] var5 = new byte[var3 + 4];
        int var7 = var4 + 1;
        var5[var4] = 29;
        var5[var7++] = 107;
        var5[var7++] = (byte) var2;

        int var6;
        for (var6 = 0; var6 < var3; ++var6) {
            if (var1.charAt(var6) > '9' || var1.charAt(var6) < '0') {
                return;
            }
        }

        if (var3 <= 30) {
            for (var6 = 0; var6 < var3; ++var6) {
                var5[var7++] = (byte) var1.charAt(var6);
            }

            var5[var7++] = 0;
            this.arrayAddToList(var5, this.i);
        }
    }

    public void CODEBAR(String var1) {
        byte var2 = 6;
        int var3 = var1.length();
        byte var4 = 0;
        byte[] var5 = new byte[var3 + 4];
        int var7 = var4 + 1;
        var5[var4] = 29;
        var5[var7++] = 107;
        var5[var7++] = (byte) var2;

        int var6;
        for (var6 = 0; var6 < var3; ++var6) {
            if (var1.charAt(var6) > 127 || var1.charAt(var6) < ' ') {
                return;
            }
        }

        if (var3 <= 30) {
            for (var6 = 0; var6 < var3; ++var6) {
                var5[var7++] = (byte) var1.charAt(var6);
            }

            var5[var7++] = 0;
            this.arrayAddToList(var5, this.i);
        }
    }

    public void CODE93(String var1) {
        byte var2 = 7;
        int var3 = var1.length();
        byte var4 = 0;
        byte[] var5 = new byte[var3 + 3];
        int var7 = var4 + 1;
        var5[var4] = 29;
        var5[var7++] = 107;
        var5[var7++] = (byte) var2;

        int var6;
        for (var6 = 0; var6 < var3; ++var6) {
            if (var1.charAt(var6) > 127 || var1.charAt(var6) < ' ') {
                return;
            }
        }

        if (var3 <= 30) {
            for (var6 = 0; var6 < var3; ++var6) {
                var5[var7++] = (byte) var1.charAt(var6);
            }

            this.arrayAddToList(var5, this.i);
        }
    }

    private void a(String var1) {
        byte var2 = 8;
        byte var3 = 0;
        byte[] var4 = new byte[var1.length() + 4];
        int var6 = var3 + 1;
        var4[var3] = 29;
        var4[var6++] = 107;
        var4[var6++] = (byte) var2;

        for (int var5 = 0; var5 < var1.length(); ++var5) {
            var4[var6++] = (byte) var1.charAt(var5);
        }

        var4[var6++] = 0;
        this.arrayAddToList(var4, this.i);
    }

    public void Code128_B(String var1) {
        byte var2 = 73;
        int var3 = var1.length();
        int var4 = 0;
        byte var5 = 0;
        byte[] var6 = new byte[1024];
        int var11 = var5 + 1;
        var6[var5] = 29;
        var6[var11++] = 107;
        var6[var11++] = (byte) var2;
        int var7 = var11++;
        var6[var11++] = 123;
        var6[var11++] = 66;

        int var8;
        for (var8 = 0; var8 < var3; ++var8) {
            if (var1.charAt(var8) > 127 || var1.charAt(var8) < ' ') {
                return;
            }
        }

        if (var3 <= 42) {
            for (var8 = 0; var8 < var3; ++var8) {
                var6[var11++] = (byte) var1.charAt(var8);
                if (var1.charAt(var8) == '{') {
                    var6[var11++] = (byte) var1.charAt(var8);
                    ++var4;
                }
            }

            var8 = 104;
            int var9 = 1;

            for (int var10 = 0; var10 < var3; ++var10) {
                var8 += var9++ * (var1.charAt(var10) - 32);
            }

            var8 %= 103;
            if (var8 >= 0 && var8 <= 95) {
                var6[var11++] = (byte) (var8 + 32);
                var6[var7] = (byte) (var3 + 3 + var4);
            } else if (var8 == 96) {
                var6[var11++] = 123;
                var6[var11++] = 51;
                var6[var7] = (byte) (var3 + 4 + var4);
            } else if (var8 == 97) {
                var6[var11++] = 123;
                var6[var11++] = 50;
                var6[var7] = (byte) (var3 + 4 + var4);
            } else if (var8 == 98) {
                var6[var11++] = 123;
                var6[var11++] = 83;
                var6[var7] = (byte) (var3 + 4 + var4);
            } else if (var8 == 99) {
                var6[var11++] = 123;
                var6[var11++] = 67;
                var6[var7] = (byte) (var3 + 4 + var4);
            } else if (var8 == 100) {
                var6[var11++] = 123;
                var6[var11++] = 52;
                var6[var7] = (byte) (var3 + 4 + var4);
            } else if (var8 == 101) {
                var6[var11++] = 123;
                var6[var11++] = 65;
                var6[var7] = (byte) (var3 + 4 + var4);
            } else if (var8 == 102) {
                var6[var11++] = 123;
                var6[var11++] = 49;
                var6[var7] = (byte) (var3 + 4 + var4);
            }

            this.arrayAddToList(var6, this.i);
        }
    }

    private void a(String var1, int var2) {
        this.arrayAddToList(new byte[]{29, 40, 107, 3, 0, 49, 67, (byte) var2}, this.i);
        this.arrayAddToList(new byte[]{29, 40, 107, 3, 0, 49, 69, 48}, this.i);
        byte[] var3 = var1.getBytes();
        byte var4 = (byte) ((var3.length + 3) % 256);
        byte var5 = (byte) ((var3.length + 3) / 256);
        ByteBuffer var6 = ByteBuffer.allocateDirect(var3.length + 8);
        var6.put(new byte[]{29, 40, 107, var4, var5, 49, 80, 48}, 0, 8);
        var6.put(var3, 0, var3.length);
        this.arrayAddToList(var6.array(), this.i);
        this.arrayAddToList(new byte[]{27, 97, 0}, this.i);
        this.arrayAddToList(new byte[]{27, 97, 1}, this.i);
        this.arrayAddToList(new byte[]{29, 40, 107, 3, 0, 49, 81, 48}, this.i);
    }

    private void a(BarcodeStringPosition var1) {

    }

    private void a(EscBarcodePrintOritention var1) {
        if (var1 != null) {


        }
    }

    private void a(CommonSetting var1) {
        int var2 = var1.getPageWidth();
        if (var2 > 636) {
            var2 = 636;
        } else if (var2 < 0) {
            var2 = 10;
        }

        int var3 = var2 % 256;
        int var4 = var2 / 256;
        this.g.add((byte) 29);
        this.g.add((byte) 87);
        this.g.add((byte) var3);
        this.g.add((byte) var4);
    }
}

