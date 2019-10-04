package com.setclapp.android_printers.printing.Command;

public class PrinterCommand implements IPrintCommand {

    private static byte ESC = 0x1B;

    @Override
    public byte[] getInitPrinterCommand() {

        byte[] result = {0x1B, 0x40};

        return result;
    }

    @Override
    public byte[] getCutCommand() {

        byte[] result = {ESC, 0x56, 0x41, 0x10};

        return result;
    }

    @Override
    public byte[] getFeedLineCommand() {

        byte[] result = {0x0A};

        return result;
    }

    @Override
    public byte[] getSelectBitImageCommand() {

        byte[] result = new byte[]{0x1B, 0x2A, 33};

        return result;
    }

    @Override
    public byte[] getUnderLineCommand() {

        byte[] result = new byte[]{0x1B, 0x2D, 1};

        return result;
    }

    @Override
    public byte[] getHorizontalTabCommand() {

        byte[] result = {0x09};

        return result;
    }

    @Override
    public byte[] getVerticalTabCommand() {

        byte[] result = {0x09};

        return result;
    }

    @Override
    public byte[] getQrCodeModelCommand() {

        byte[] result = {(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x04, (byte) 0x00, (byte) 0x31, (byte) 0x41, (byte) 0x32, (byte) 0x02};

        return result;
    }

    @Override
    public byte[] getQrCodeSizeCommand() {

        byte[] result = {(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x03, (byte) 0x00, (byte) 0x31, (byte) 0x43, (byte) 0x08};

        return result;
    }

    @Override
    public byte[] getQrCodeErrorCommand() {

        byte[] result = {(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x03, (byte) 0x00, (byte) 0x31, (byte) 0x45, (byte) 0x31};

        return result;
    }

    @Override
    public byte[] getQrCodePrintCommand() {

        byte[] result = {(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x03, (byte) 0x00, (byte) 0x31, (byte) 0x51, (byte) 0x30};

        return result;
    }


    @Override
    public byte[] getQrCodeStoreCommand(String data) {

        int store_len = data.length() + 3;
        byte store_pL = (byte) (store_len % 256);
        byte store_pH = (byte) (store_len / 256);

        byte[] result = {(byte) 0x1d, (byte) 0x28, (byte) 0x6b, store_pL, store_pH, (byte) 0x31, (byte) 0x50, (byte) 0x30};

        return result;
    }

    @Override
    public byte[] getSetLineSpacing24Command() {

        byte[] result = {0x1B, 0x33, 24};

        return result;
    }

    @Override
    public byte[] getSetLineSpacing30Command() {

        byte[] result = {0x1B, 0x33, 30};

        return result;
    }
}
