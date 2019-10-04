package com.setclapp.android_printers.printing.Printers;

import android.content.Context;
import android.graphics.Bitmap;

import com.rt.printerlibrary.bean.SerialPortConfigBean;
import com.rt.printerlibrary.connect.PrinterInterface;
import com.rt.printerlibrary.enumerate.BmpPrintMode;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.factory.connect.PIFactory;
import com.rt.printerlibrary.factory.connect.SerailPortFactory;
import com.rt.printerlibrary.factory.printer.ThermalPrinterFactory;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.setting.BitmapSetting;
import com.rt.printerlibrary.setting.CommonSetting;
import com.rt.printerlibrary.utils.PrinterPowerUtil;
import com.setclapp.android_printers.common.ActionResult;
import com.setclapp.android_printers.printing.Base.BasePrinter;
import com.setclapp.android_printers.printing.Base.IPrinter;
import com.setclapp.android_printers.printing.Command.EscCommand;
import com.setclapp.android_printers.printing.IHardwareConfiguration;

import java.io.File;

public class AP02Printer extends BasePrinter implements IPrinter {

    private Context context;
    private IHardwareConfiguration printerConfiguration;

    private RTPrinter printer;
    private ThermalPrinterFactory factory;

    private String charSet;

    public AP02Printer(Context context, IHardwareConfiguration printerConfiguration) {

        this.context = context;
        this.printerConfiguration = printerConfiguration;

        factory = new ThermalPrinterFactory();
        printer = factory.create();
    }


    @Override
    public int getSleepValueBetweenBitmap() {
        return 0;
    }

    @Override
    public ActionResult connect() {

        try {

            String portName = printerConfiguration.getSerialPort();

            PIFactory piFactory = new SerailPortFactory();
            SerialPortConfigBean configObj = new SerialPortConfigBean();
            configObj.setBaudrate(115200);
            configObj.setFile(new File("/dev/" + portName));

            PrinterInterface printerInterface = piFactory.create();
            printerInterface.setConfigObject(configObj);
            printer.setPrinterInterface(printerInterface);

            printer.connect(configObj);

            PrinterPowerUtil printerPowerUtil = new PrinterPowerUtil(context);

            printerPowerUtil.setPrinterPower(true);//turn printer power on.

            return ActionResult.succeed();

        } catch (Exception exc) {
            return ActionResult.failure(new Exception("Printere qoshula bilmedi"));
        }
    }

    public ActionResult print(Bitmap bitmap, int bitmapWidth) {

        try {
            EscCommand cmd = new EscCommand();

            cmd.append(cmd.getHeaderCmd());

            CommonSetting commonSetting = new CommonSetting();
            commonSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
            cmd.append(cmd.getCommonSettingCmd(commonSetting));


            BitmapSetting bitmapSetting = new BitmapSetting();

            bitmapSetting.setBmpPrintMode(BmpPrintMode.MODE_SINGLE_COLOR);

            bitmapSetting.setBimtapLimitWidth(bitmapWidth * 8);


            cmd.append(cmd.getBitmapCmd(bitmapSetting, bitmap));

            if (printer != null)
                printer.writeMsg(cmd.getAppendCmds());//Sync Write

            return ActionResult.succeed();

        } catch (Exception exc) {
            return ActionResult.failure(exc);
        }
    }

    @Override
    public ActionResult print(Bitmap[] bitmap, int bitmapWidth) {
        return null;
    }

    @Override
    public ActionResult print(byte[] data) {

        try {

            printer.writeMsg(data);

            return ActionResult.succeed();

        } catch (Exception exc) {
            return ActionResult.failure(exc);
        }
    }
}
