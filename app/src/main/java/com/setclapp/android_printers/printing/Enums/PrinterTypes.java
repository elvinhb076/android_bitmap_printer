package com.setclapp.android_printers.printing.Enums;

public enum PrinterTypes {

    USB(0),
    SerialPort(1);

    PrinterTypes(int value) {
        this.value = value;
    }

    private final int value;

    public int getValue() {
        return value;
    }

    public static PrinterTypes getType(int val) {

        switch (val) {
            case 0:
                return PrinterTypes.USB;
            case 1:
                return PrinterTypes.SerialPort;
        }

        return PrinterTypes.USB;
    }
}
