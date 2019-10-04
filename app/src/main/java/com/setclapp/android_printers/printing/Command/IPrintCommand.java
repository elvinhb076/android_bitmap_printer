package com.setclapp.android_printers.printing.Command;

public interface IPrintCommand {


    byte[] getInitPrinterCommand();

    byte[] getCutCommand();

    byte[] getFeedLineCommand();

    byte[] getSelectBitImageCommand();

    byte[] getUnderLineCommand();

    byte[] getHorizontalTabCommand();

    byte[] getVerticalTabCommand();

    byte[] getQrCodeModelCommand();

    byte[] getQrCodeSizeCommand();

    byte[] getQrCodeErrorCommand();

    byte[] getQrCodePrintCommand();

    byte[] getQrCodeStoreCommand(String data);

    byte[] getSetLineSpacing24Command();

    byte[] getSetLineSpacing30Command();

}
