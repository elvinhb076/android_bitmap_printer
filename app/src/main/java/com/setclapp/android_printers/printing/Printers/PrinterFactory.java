package com.setclapp.android_printers.printing.Printers;

import android.content.Context;

import com.setclapp.android_printers.TerminalTypes;
import com.setclapp.android_printers.printing.Base.IPrinter;
import com.setclapp.android_printers.printing.Command.IPrintCommand;
import com.setclapp.android_printers.printing.Enums.PrinterTypes;
import com.setclapp.android_printers.printing.IHardwareConfiguration;

public class PrinterFactory {

    private Context context;
    private IPrintCommand command;
    private IHardwareConfiguration configuration;

    public PrinterFactory(Context context, IPrintCommand command, IHardwareConfiguration configuration) {
        this.context = context;
        this.command = command;
        this.configuration = configuration;
    }

    public IPrinter create() {

        IPrinter printer = null;

        TerminalTypes printerType = configuration.getTerminalType();

        if (printerType.equals(TerminalTypes.AP02))
            printer = new AP02Printer(context, configuration);

        else if (printerType.equals(TerminalTypes.Tactilion))
            printer = new TactilionPrinter(context);
        else if (printerType.equals(TerminalTypes.Aclas))
            printer = new AclasPrinter(context);
        else if (printerType.equals(TerminalTypes.AclasAOBX))
            printer = new AclasAOBXPrinter(context);
        else if (printerType.equals(TerminalTypes.Sunmi))
            printer = new SunmiPrinter(context);

        return printer;
    }
}
