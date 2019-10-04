package com.setclapp.android_printers.printing.Enums;

public enum PaperWidthTypes {

    MMetr48(0),
    MMetr56(1);

    PaperWidthTypes(int value) {
        this.value = value;
    }

    private final int value;

    public int getValue() {
        return value;
    }

    public static PaperWidthTypes getType(int val) {

        switch (val) {
            case 0:
                return PaperWidthTypes.MMetr48;
            case 1:
                return PaperWidthTypes.MMetr56;
        }

        return PaperWidthTypes.MMetr48;
    }
}
