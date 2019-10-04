package com.setclapp.android_printers.printing.Base;

import com.setclapp.android_printers.common.ActionResult;
import com.setclapp.android_printers.printing.PrintPageModel;

public interface IPrintPage<T extends PrintPageModel> {

    ActionResult print(T data);

}
