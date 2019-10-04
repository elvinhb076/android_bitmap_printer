package com.setclapp.android_printers.printing.Command;

public class PrinterCommandFactory {

    public static IPrintCommand createPosPrinterCommand() {
        return new PrinterCommand();
    }

}
