package com.setclapp.android_printers.printing.Printers;

import android.content.Context;
import android.graphics.Bitmap;

import com.setclapp.android_printers.common.ActionResult;
import com.setclapp.android_printers.printing.Base.BasePrinter;
import com.setclapp.android_printers.printing.Base.IPrinter;
import com.setclapp.android_printers.printing.Helpers.AclasPrinterHelper;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import CommDevice.USBPort;
import aclasdriver.AclasDriverLibrary;
import aclasdriver.Printer;

public class AclasAOBXPrinter extends BasePrinter implements IPrinter {

    private Context context;
    private AclasDriverLibrary aclasLib = new AclasDriverLibrary();
    private Printer printer;
    private PrinterThread printerThread;

    private int printerType = 0;
    private static int dotLineWidth = 384;


    public AclasAOBXPrinter(Context context) {
        this.context = context;
    }


    @Override
    public int getSleepValueBetweenBitmap() {
        return 2000;
    }

    @Override
    public ActionResult connect() {

        try {
            aclasLib.Init("ac38b5c47e8dd7182e37aa19490df35db84aa8c6");

            printer = new Printer();

            printer.SetStdEpsonMode(1);

            String strPrinterSerial = USBPort.getDeviceName(0);

            int result = -1;

            if (strPrinterSerial.length() > 0) {

                result = printer.Open(printerType, new USBPort("", strPrinterSerial, ""));// type,0: 2 inch ; 1: 3 inch

                if (result > 0) {

                    printerThread = new PrinterThread();
                    printerThread.start();

                } else
                    return ActionResult.failure(new Exception("Cannot open printer"));
            } else
                return ActionResult.failure(new Exception("Cannot find serial number"));

            return ActionResult.succeed();

        } catch (Exception exc) {
            return ActionResult.failure(exc);
        }
    }

    @Override
    public ActionResult print(Bitmap bitmap, int bitmapWidth) {

        try {

            byte[] data2 = AclasPrinterHelper.getInstance().getPrintBmpData(bitmap, printerType);
            printerThread.appendPrintData(data2, 0, 0);
            printerThread.startPrintData();

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
        return null;
    }


    class PrinterThread extends Thread {

        private ReentrantLock bufferLock = new ReentrantLock();
        private ArrayList<byte[]> listData = new ArrayList<byte[]>();

        private boolean runflag = false, enablePrint = false;
        public int cutterType = 1;


        public synchronized void appendPrintData(byte[] data, int offset,
                                                 int len) {
            bufferLock.lock();
            if (data != null) {
                listData.add(data);
            }
            bufferLock.unlock();
        }

        public synchronized byte[] getPrintData() {
            byte[] data = null;
            bufferLock.lock();
            int iSize = listData.size();
            if (iSize > 0) {
                data = listData.get(iSize - 1);
                listData.remove(iSize - 1);
            }
            bufferLock.unlock();
            return data;
        }

        public synchronized int startPrintData() {
            if (printer == null) {
                return -1;
            }
            if (enablePrint == true) {
                return 1;
            }

            enablePrint = true;
            return 0;
        }

        private synchronized int printData(int cuttype) {
            int ret = 0;
            final byte cutcmd[] = {0x1d, 0x56, 0x00};
            final byte halfcutcmd[] = {0x1d, 0x56, 0x01};

            byte[] byteSend = getPrintData();
            if (byteSend != null) {
                //bufferLock.lock();

                byte[] head = {0x1d, 0x76, 0x30, 0x00, 0x00, 0x00, 0x00, 0x00};
                int iWidth = (dotLineWidth + 7) / 8;
                int iHeight = AL_PRINTER_MAX_HEIGHT;
                int iHeadLen = head.length;
                int iMaxData = iWidth * iHeight;
                int iMaxSendLen = iMaxData + iHeadLen;

                int iPos = 0;
                while (iPos < byteSend.length) {

                    int iSend = 0;
                    if (byteSend[iPos + 0] == 0x1d && byteSend[iPos + 1] == 0x76 && byteSend[iPos + 2] == 0x30) {
                        int iRealWidth = byteSend[iPos + 4] + byteSend[iPos + 5] * 256;
                        int iRealHeight = byteSend[iPos + 6] + byteSend[iPos + 7] * 256;
                        int iRealSendData = iRealWidth * iRealHeight + iHeadLen;
                        iSend = iRealSendData;
                    } else {
                        iSend = byteSend.length - iPos;
                    }
                    byte[] byteSendPart = new byte[iSend];
                    System.arraycopy(byteSend, iPos, byteSendPart, 0, iSend);

                    printer.Write(byteSendPart);

                    iPos += iSend;


                    try {
                        sleep(5);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (cuttype >= 0) {
                    if (print_mode != 0) {
                        printer.SetPrintMode(0);
                    }
                    switch (cuttype) {
                        case 1:
                            printer.Write(halfcutcmd);
                            break;
                        case 0:
                            printer.Write(cutcmd);
                            break;
                        default:
                            break;
                    }
                    if (print_mode != 0) {
                        printer.SetPrintMode(1);
                    }
                }
            }
            return ret;
        }

        public synchronized boolean checkPaper() {
            boolean havePaper = false;
//            havePaper = mprinter.IsPaperExist();
//
//            Message msg_paperstatus = gui_show.obtainMessage();
//            msg_paperstatus.arg1 = 1;
//            if (havePaper) {
//                msg_paperstatus.obj = new String(PAPER_STATUS_STRING
//                        + HAVE_PAPER_STRING);
//            } else {
//                msg_paperstatus.obj = new String(PAPER_STATUS_STRING
//                        + NO_PAPER_STRING);
//            }
//            gui_show.sendMessage(msg_paperstatus);
            return havePaper;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            int timer = 0;
            int timerMax = 5;
            boolean havePaper = true;
            while (runflag) {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (!runflag)
                    break;
                if (timer++ > timerMax) {
                    timer = 0;
                    timerMax = 5;
                    checkPaper(); // check paper status
                }
                if (havePaper && enablePrint) {

                    int ret = printData(cutterType);

                    enablePrint = false;

                    if (ret > 0) { // delay some seconds to wait printer buffer
                        // clear
                        timer = 0;
                        timerMax = 10;
                    }
                } else if (enablePrint) {
                    enablePrint = false;
                }
            }
        }

        @Override
        public synchronized void start() {
            // TODO Auto-generated method stub
            runflag = true;
            super.start();
        }
    }

    private byte AL_PRINTER_MAX_HEIGHT = 0x18;
    private int print_mode = 0;// 0 epson/1 dot
}
