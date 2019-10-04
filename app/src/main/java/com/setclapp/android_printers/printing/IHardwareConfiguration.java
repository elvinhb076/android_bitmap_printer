package com.setclapp.android_printers.printing;

import com.setclapp.android_printers.TerminalTypes;
import com.setclapp.android_printers.printing.Enums.PaperWidthTypes;
import com.setclapp.android_printers.printing.Enums.PrinterTypes;

public interface IHardwareConfiguration {

    String getSerialPort();

    PaperWidthTypes getPrinterWidth();

    TerminalTypes getTerminalType();

    PrinterTypes getPrinterConnectionType();

    void refresh();
}

