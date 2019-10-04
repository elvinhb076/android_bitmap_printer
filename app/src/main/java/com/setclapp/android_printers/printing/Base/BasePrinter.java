package com.setclapp.android_printers.printing.Base;

import com.setclapp.android_printers.common.ActionResult;

public abstract class BasePrinter implements IPrinter {

    public abstract ActionResult print(byte[] data);

}
