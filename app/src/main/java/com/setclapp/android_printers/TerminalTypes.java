package com.setclapp.android_printers;

public enum TerminalTypes {

    AP02(0),
    Tactilion(1),
    Aclas(2),
    AclasAOBX(3),
    Sunmi(4),
    Standart(5);

    TerminalTypes(int value) {
        this.value = value;
    }

    private final int value;

    public int getValue() {
        return value;
    }

    public static TerminalTypes getType(int val) {

        switch (val) {
            case 0:
                return TerminalTypes.AP02;
            case 1:
                return TerminalTypes.Tactilion;
            case 2:
                return TerminalTypes.Aclas;
            case 3:
                return TerminalTypes.AclasAOBX;
            case 4:
                return TerminalTypes.Sunmi;
            case 5:
                return TerminalTypes.Standart;
        }

        return TerminalTypes.Standart;
    }

}
