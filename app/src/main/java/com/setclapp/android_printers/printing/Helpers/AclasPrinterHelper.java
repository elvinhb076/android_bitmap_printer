package com.setclapp.android_printers.printing.Helpers;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AclasPrinterHelper {
    private static AclasPrinterHelper sUtil = null;
    private static final String tag = "AclasPrinterHelper";
    public static AclasPrinterHelper getInstance(){
        if(sUtil==null){
            sUtil	= new AclasPrinterHelper();
        }
        return sUtil;
    }

    public byte[] EpsonModePrint() {
        final byte Bold[]	= { 0x1b, 0x21, 0x08, 'B', 'o', 'l', 'd', '\n'};
        final byte Normal[]	= { 0x1b, 0x21, 0x00, 'N', 'o', 'r', 'm', 'a', 'l', '\n'};
        final byte Aligns[] = { 0x1b, 0x21, 0x00,0x1b, 0x61, 0x02, 'A', 'c', 'l', 'a', 's', '0','\n'};//single height right align
        final byte AlignsDL[] = { 0x1b, 0x21, 0x10,0x1b, 0x61, 0x00, 'A', 'c', 'l', 'a', 's', '1','\n'};//double height left align
        final byte AlignsSM[] = { 0x1b, 0x21, 0x00,0x1b, 0x61, 0x01, 'A', 'c', 'l', 'a', 's', '2','\n'};//single height middle align
        final byte aclas[] = {'A', 'c', 'l', 'a', 's', '3', '\n' };
        final byte aclas_big[] = { 0x1b, 0x21, (byte) 0xb0, '-', '-', '-', '-', '-', 'A', 'c', 'l', 'a', 's', '-', '-', '-', '-', '-', '-', '\n' };
        final byte cmd_1[] = {0x1d ,0x68 ,0x41};//CHOOSE THE BARCODE HEIGHT AS 0x35(max = 0x41)
        final byte cmd_2[] = {0x1d ,0x48 ,0x03};//CHOOSE THE PRINTING POSITION OF BARCODE VALUE 0x1 UPPER 0x02 LOWER 0x03 UPPER AND LOWER
        final byte cmd_3[] = {0x1d ,0x77 ,0x2};//CHOOSE THE BARCODE WIDTH AS 0x2(max = 0x8)
        //final byte BAR128[]= {0x1d ,0x6b  ,0x49 ,0x08 ,0x37 ,0x31  ,0x31 ,0x32 ,0x33 ,0x34 ,0x37 ,0x38};//71123478  Type73 code128
        final byte BAR128[]= {0x1d ,0x6b  ,0x49 ,0x08 ,0x7b,0x41,0x37 ,0x31  ,0x31 ,0x32 ,0x33  ,0x38,0x0d ,0x0a};//
        final byte BARITF[]= {0x1d ,0x6b  ,0x46 ,0x04 ,0x37 ,0x31  ,0x31 ,0x32 ,0x0d ,0x0a};//Type70 ITF
        //final byte BAREN8[]= {0x1d ,0x6b  ,0x03 ,0x34 ,0x37 ,0x31  ,0x31 ,0x32 ,0x33 ,0x34  ,0x37 ,0x38 ,0x39 ,0x30  ,0x38 ,0x39 ,0x00 ,0x0d ,0x0a};//en8 4 711234 789089
        final byte BAREN8[]= {0x1d ,0x6b  ,0x04 ,0x34 ,0x37 ,0x31  ,0x31 ,0x32 ,0x33 ,0x34  ,0x37 ,0x38 ,0x39 ,0x30  ,0x38 ,0x39 ,0x00 ,0x0d ,0x0a,0x00};//code39 4 711234 789089
        //final byte BARUPCA[]= {0x1d ,0x6b ,0x00 ,0x30 ,0x31 ,0x32  ,0x34 ,0x35 ,0x36 ,0x37  ,0x38 ,0x39 ,0x30 ,0x30   ,0x00, 0x0d ,0x0a};//012345 678905
        final byte BARUPCA[]= {0x1d ,0x6b ,0x00 ,0x30 ,0x31 ,0x32  ,0x34 ,0x35 ,0x36 ,0x37  ,0x38 ,0x39 ,0x30 ,0x30   ,0x00};//012345 67890
        //final byte BAREN13[]= {0x1d ,0x6b ,0x02 ,0x39 ,0x37 ,0x38  ,0x37 ,0x31 ,0x31 ,0x35  ,0x31 ,0x34 ,0x35 ,0x35,0x34,0x33 ,0x00 , 0x0d ,0x0a};//9 787115 145543
        final byte BAREN13[]= {0x1d ,0x6b ,0x02 ,0x39 ,0x37 ,0x38  ,0x37 ,0x31 ,0x31 ,0x35  ,0x31 ,0x34 ,0x35 ,0x35,0x34,0x00 };//9 787115 145543
        //        final byte cutcmdstep[]= {0x1d,0x56,0x41,0x2};
        final byte cutcmd[]    = {0x1d,0x56,0x00};
        final byte halfcutcmd[]    = {0x1d,0x56,0x01};
        final byte tmpfeed[]    = {0xd,0xa};

        final byte Left[]	= { 0x1b, 0x61, 0x00 };

//	        final byte testchar[] = { 0x1b, 0x21, 0x00,
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	                                  '\n'
//	                                 };
//
//	        final byte testchar1[] = { 0x1b, 0x21, 0x08,
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's',
//					  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	        						  'A', 'c', 'l', 'a', 's','A', 'c', 'l', 'a', 's','\n',
//	                                  '\n'
//	                                 };
        final byte testchar[] = { 0x1b, 0x21, 0x00,
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '\n',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
                '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
                '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
                '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
                '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
                '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
                '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
                '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
                '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9',
                'A', 'A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A',
                'B', 'B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B',
                'C', 'C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C',
                'D', 'D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D',
                'E', 'E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E',
                'F', 'F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F',
                'a', 'a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a',
                'b', 'b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b',
                'c', 'c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c',
                'd', 'd','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d',
                'e', 'e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e',
                'f', 'f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f',
                '1', '1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1',
                '2', '2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2',
                '3', '3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3',
                '\n'
        };
        final byte testchar1[] = { 0x1b, 0x21, 0x08,
                ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '\n',
                '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
                '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
                '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
                '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
                '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
                '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
                '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
                '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
                '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
                '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9',
                'A', 'A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A','A',
                'B', 'B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B','B',
                'C', 'C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C','C',
                'D', 'D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D','D',
                'E', 'E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E','E',
                'F', 'F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F','F',
                'a', 'a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a','a',
                'b', 'b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b','b',
                'c', 'c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c','c',
                'd', 'd','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d','d',
                'e', 'e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e','e',
                'f', 'f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f','f',
                '1', '1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1','1',
                '2', '2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2','2',
                '3', '3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3','3',
                '\n'
        };
        final byte QRCODE_1[]= {0x0d,0x0a,0x1B,0x40,0x10,0x04,0x04,0x10,0x04,0x01,0x10,0x04,0x02,0x10,0x04,0x03,0x10,0x04,0x04,0x1B,0x40,0x1B,0x4C};
        final byte QRCODE_2[]= {0x1D,0x50,0x00,0x00};
        final byte QRCODE_3[]= {0x1B,0x57,0x00,0x00,0x00,0x00,(byte)0xC8,0x01,(byte)0xa8,0x00};

        final byte QRCODE_4[]= {0x1B,0x54,0x00};
        final byte QRCODE_5[]= {0x1B,0x24,(byte)0x70,0x00};
        final byte QRCODE_6[]= {0x1D,0x24,(byte)0x70,0x00};


        final byte QRCODE_7[]= {0x1D,0x28,0x6B,0x04,0x00,0x31,0x41,0x32,0x00};
        final byte QRCODE_8[]= {0x1D,0x28,0x6B,0x03,0x00,0x31,0x43,0x05};
        final byte QRCODE_9[]= {0x1D,0x28,0x6B,0x03,0x00,0x31,0x45,0x31};

        final byte QRCODE_10[]= {0x1D,0x28,0x6B,0x50,0x00,0x31,0x50,0x30,0x54,0x42,0x31,0x32,0x33,0x34,0x39,0x30,0x30,0x38,0x31,0x30,0x32,0x30,0x38,0x31,0x39,0x32,0x33,0x37,0x37,0x30,0x30,0x30,0x30,0x30,0x30,0x34,0x61,0x30,0x30,0x30,0x30,0x30,0x30,0x34,0x65,0x30,0x30,0x30,0x30,0x30,0x30,0x30,0x30,0x31,0x34,0x38,0x31,0x36,0x35,0x32,0x36,0x73,0x64,0x4B,0x54,0x30,0x65,0x7A,0x6F,0x4B,0x2B,0x46,0x77,0x56,0x38,0x4C,0x75,0x76,0x66,0x6F,0x69,0x72,0x77,0x3D,0x3D};
        final byte QRCODE_11[]= {0x1D,0x28,0x6B,0x03,0x00,0x31,0x51,0x30,0x0c,0x1b,0x53};


        int iPos = 0;
        byte[] tmpData = new byte[4096];

        final byte Init[]	  = {0x1B,0x40};
        byte[] nextLine = {'\n'};
        //pThread.clearPrintBuffer();

        //printerBuffer.write(Init, 0, Init.length);
        System.arraycopy(Init, 0, tmpData, iPos, Init.length);
        iPos	+=	Init.length;


        System.arraycopy(nextLine, 0, tmpData, iPos, nextLine.length);
        iPos	+=	nextLine.length;
        //printerBuffer.write(Normal, 0, Normal.length);

        System.arraycopy(Normal, 0, tmpData, iPos, Normal.length);
        iPos	+=	Normal.length;

        //printerBuffer.write(Bold, 0, Bold.length);
        System.arraycopy(Bold, 0, tmpData, iPos, Bold.length);
        iPos	+=	Bold.length;

        //printerBuffer.write(Normal, 0, Normal.length);
        System.arraycopy(Normal, 0, tmpData, iPos, Normal.length);
        iPos	+=	Normal.length;

        //printerBuffer.write(Bold, 0, Bold.length);
        System.arraycopy(Bold, 0, tmpData, iPos, Bold.length);
        iPos	+=	Bold.length;

        //printerBuffer.write(Normal, 0, Normal.length);
        System.arraycopy(Normal, 0, tmpData, iPos, Normal.length);
        iPos	+=	Normal.length;

        String str = "------------打印测试--------------\n";
        byte[] strByte = null;
        try {

            byte[] bChoseChinese = {0x1B,0x52,0x14};
            //printerBuffer.write(bChoseChinese, 0, bChoseChinese.length);

            strByte = str.getBytes("gb2312");
            //printerBuffer.write(strByte, 0, strByte.length);

            System.arraycopy(bChoseChinese, 0, tmpData, iPos, bChoseChinese.length);
            iPos	+=	bChoseChinese.length;
            System.arraycopy(strByte, 0, tmpData, iPos, strByte.length);
            iPos	+=	strByte.length;
        } catch (Exception e) {
            // TODO: handle exception
        }

        byte[] tmp = {'-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-'};
//       	printerBuffer.write(nextLine, 0, nextLine.length);
//       	printerBuffer.write(tmp, 0, tmp.length);
        System.arraycopy(nextLine, 0, tmpData, iPos, nextLine.length);
        iPos	+=	nextLine.length;
        System.arraycopy(tmp, 0, tmpData, iPos, tmp.length);
        iPos	+=	tmp.length;

//       	printerBuffer.write(nextLine, 0, nextLine.length);
        System.arraycopy(nextLine, 0, tmpData, iPos, nextLine.length);
        iPos	+=	nextLine.length;

//       	printerBuffer.write(tmp, 0, tmp.length);
        System.arraycopy(tmp, 0, tmpData, iPos, tmp.length);
        iPos	+=	tmp.length;

//       	printerBuffer.write(nextLine, 0, nextLine.length);
        System.arraycopy(nextLine, 0, tmpData, iPos, nextLine.length);
        iPos	+=	nextLine.length;
//       	printerBuffer.write(nextLine, 0, nextLine.length);
        System.arraycopy(nextLine, 0, tmpData, iPos, nextLine.length);
        iPos	+=	nextLine.length;
//       	printerBuffer.write(nextLine, 0, nextLine.length);
//       	System.arraycopy(nextLine, 0, tmpData, iPos, nextLine.length);
        iPos	+=	nextLine.length;
//       	printerBuffer.write(nextLine, 0, nextLine.length);
        System.arraycopy(nextLine, 0, tmpData, iPos, nextLine.length);
        iPos	+=	nextLine.length;
//       	printerBuffer.write(tmp, 0, tmp.length);
        System.arraycopy(tmp, 0, tmpData, iPos, tmp.length);
        iPos	+=	tmp.length;
//       	printerBuffer.write(nextLine, 0, nextLine.length);
        System.arraycopy(nextLine, 0, tmpData, iPos, nextLine.length);
        iPos	+=	nextLine.length;

        byte[] bChoseUSA = {0x1B,0x52,0x00};
//       	printerBuffer.write(bChoseUSA, 0, bChoseUSA.length);
        System.arraycopy(bChoseUSA, 0, tmpData, iPos, bChoseUSA.length);
        iPos	+=	bChoseUSA.length;

//       	printerBuffer.write(Aligns, 0, Aligns.length);
        System.arraycopy(Aligns, 0, tmpData, iPos, Aligns.length);
        iPos	+=	Aligns.length;

//       	printerBuffer.write(AlignsDL, 0, AlignsDL.length);
        System.arraycopy(AlignsDL, 0, tmpData, iPos, AlignsDL.length);
        iPos	+=	AlignsDL.length;

//       	printerBuffer.write(AlignsSM, 0, AlignsSM.length);
        System.arraycopy(AlignsSM, 0, tmpData, iPos, AlignsSM.length);
        iPos	+=	AlignsSM.length;

//       	printerBuffer.write(aclas, 0, aclas.length);
        System.arraycopy(aclas, 0, tmpData, iPos, aclas.length);
        iPos	+=	aclas.length;

        System.arraycopy(Left, 0, tmpData, iPos, Left.length);
        iPos	+=	Left.length;

//       	printerBuffer.write(testchar1, 0, testchar1.length);
        System.arraycopy(testchar1, 0, tmpData, iPos, testchar1.length);
        iPos	+=	testchar1.length;

//       	printerBuffer.write(testchar, 0, testchar.length);
        System.arraycopy(testchar, 0, tmpData, iPos, testchar.length);
        iPos	+=	testchar.length;

//       	printerBuffer.write(aclas_big, 0, aclas_big.length);
        System.arraycopy(aclas_big, 0, tmpData, iPos, aclas_big.length);
        iPos	+=	aclas_big.length;

        //BARCODE PARAMETER

//       	pThread.startPrintData();
//	    	pThread.clearPrintBuffer();

        byte[] tmpbuf = { 0x1b, 0x40};


//       	printerBuffer.write(tmpbuf, 0, tmpbuf.length);
        System.arraycopy(tmpbuf, 0, tmpData, iPos, tmpbuf.length);
        iPos	+=	tmpbuf.length;

//       	printerBuffer.write(cmd_1, 0, cmd_1.length);
        System.arraycopy(cmd_1, 0, tmpData, iPos, cmd_1.length);
        iPos	+=	cmd_1.length;

//       	printerBuffer.write(cmd_2, 0, cmd_2.length);
        System.arraycopy(cmd_2, 0, tmpData, iPos, cmd_2.length);
        iPos	+=	cmd_2.length;

//       	printerBuffer.write(cmd_3, 0, cmd_3.length);
        System.arraycopy(cmd_3, 0, tmpData, iPos, cmd_3.length);
        iPos	+=	cmd_3.length;


//       	pThread.startPrintData();
//	    	pThread.clearPrintBuffer();

//       	printerBuffer.write(BARITF, 0, BARITF.length);
        System.arraycopy(BARITF, 0, tmpData, iPos, BARITF.length);
        iPos	+=	BARITF.length;

//       	printerBuffer.write(BAR128, 0, BAR128.length);
        System.arraycopy(BAR128, 0, tmpData, iPos, BAR128.length);
        iPos	+=	BAR128.length;

//       	printerBuffer.write(BAREN8, 0, BAREN8.length);
        System.arraycopy(BAREN8, 0, tmpData, iPos, BAREN8.length);
        iPos	+=	BAREN8.length;

//       	printerBuffer.write(BARUPCA, 0, BARUPCA.length);
        System.arraycopy(BARUPCA, 0, tmpData, iPos, BARUPCA.length);
        iPos	+=	BARUPCA.length;

//       	printerBuffer.write(BAREN13, 0, BAREN13.length);
        System.arraycopy(BAREN13, 0, tmpData, iPos, BAREN13.length);
        iPos	+=	BAREN13.length;


//       	byte[] feedBuf = { 0x1b, 0x64,0x05};
//       	printerBuffer.write(feedBuf, 0, feedBuf.length);

//       	byte[] bufMiddle = { 0x1b, 0x68,0x31};
//       	printerBuffer.write(bufMiddle, 0, bufMiddle.length);

//       	printerBuffer.write(QRCODE_5, 0, QRCODE_5.length);
        System.arraycopy(QRCODE_5, 0, tmpData, iPos, QRCODE_5.length);
        iPos	+=	QRCODE_5.length;


//       	printerBuffer.write(QRCODE_7, 0, QRCODE_7.length);
        System.arraycopy(QRCODE_7, 0, tmpData, iPos, QRCODE_7.length);
        iPos	+=	QRCODE_7.length;

//       	printerBuffer.write(QRCODE_8, 0, QRCODE_8.length);
        System.arraycopy(QRCODE_8, 0, tmpData, iPos, QRCODE_8.length);
        iPos	+=	QRCODE_8.length;

//       	printerBuffer.write(QRCODE_9, 0, QRCODE_9.length);
        System.arraycopy(QRCODE_9, 0, tmpData, iPos, QRCODE_9.length);
        iPos	+=	QRCODE_9.length;

//       	printerBuffer.write(QRCODE_10, 0, QRCODE_10.length);
        System.arraycopy(QRCODE_10, 0, tmpData, iPos, QRCODE_10.length);
        iPos	+=	QRCODE_10.length;

//       	printerBuffer.write(QRCODE_11, 0, QRCODE_11.length);
        System.arraycopy(QRCODE_11, 0, tmpData, iPos, QRCODE_11.length);
        iPos	+=	QRCODE_11.length;


//       	printerBuffer.write(halfcutcmd, 0, halfcutcmd.length);
        System.arraycopy(halfcutcmd, 0, tmpData, iPos, halfcutcmd.length);
        iPos	+=	halfcutcmd.length;

        byte[] retData	= new byte[iPos];
        System.arraycopy(tmpData, 0, retData, 0, iPos);
        return retData;
//       	pThread.startPrintData();


    }

    private class GoodsInfo{
        public String 	m_strName;

        public float  	m_fDiscount;
        public float  	m_fPrice;
        public float  	m_fCount;
        public float  	m_fAmount;
        public GoodsInfo(String name,float dis,float price,float cnt){
            m_strName		= name;
            m_fDiscount		= dis;
            m_fPrice		= price;
            m_fCount		= cnt;
            m_fAmount		= m_fPrice*m_fCount*m_fDiscount;
        }
    }

    public byte[] printReceipt(int PrinterType){

        GoodsInfo[] infoArr	= {
                new GoodsInfo("Apple", (float)1,  (float)20, (float)2),
                new GoodsInfo("Grape", (float)1,  (float)30, (float)3),
                new GoodsInfo("Lemon", (float)1, (float)35,(float)2),
                new GoodsInfo("Watermelon",(float)0.9 , (float)37, (float)1.1),
                new GoodsInfo("Pomegranate", (float)0.8, (float)4.5, (float)0.56),
                new GoodsInfo("Mango", (float)0.9, (float)3.6, (float)0.7),
                new GoodsInfo("Black Pepper Mini Steak", (float)1, (float)3.56, (float)1),
                new GoodsInfo("Cheese Lovers Pizza", (float)1, (float)4.56, (float)1),
                new GoodsInfo("Garen Veggies", (float)1, (float)4.65, (float)1),
                new GoodsInfo("Hawaiian Pizza", (float)1, (float)3.72, (float)1),
                new GoodsInfo("Jumbo Shnmp wrapped in bacon", (float)1, (float)6.55, (float)1),
                new GoodsInfo("Banana", (float)1, (float)1.33, (float)1.2),
                new GoodsInfo("Orange", (float)1, (float)2.01, (float)1.5),

        };
        ArrayList<GoodsInfo> list = new ArrayList<GoodsInfo>();
        for (GoodsInfo goodsInfo : infoArr) {
            list.add(goodsInfo);
        }

        return printInfo(list,PrinterType);
    }

    private byte[] printInfo(ArrayList<GoodsInfo> list,int iInchType){
        int DotLineWidth = iInchType==0?384:576;
        ByteArrayOutputStream printerBuffer = new ByteArrayOutputStream(DotLineWidth / 8 * 16 * 1024);


        int		iSpaceDate		= iInchType==0?18:22;
        int		iSpaceName		= iInchType==0?10:14;
        int		iSpaceDis		= iInchType==0?5:7;
        int		iSpacePrice		= iInchType==0?6:8;
        int 	iSpaceCnt		= iInchType==0?6:8;
        int		iSpaceAmt		= iInchType==0?4:4;
        int		iSpaceItems		= iInchType==0?11:21;
        int		iSpaceTalAmt	= iInchType==0?11:15;
        int   	iSpaceTalVal	= iInchType==0?8:12;

        //int  iWidth	= iInchType==0?31:45;
        final byte BoldChinese[]	= { 0x1c, 0x21, 0x0c };
        final byte Bold[]	= { 0x1b, 0x21, 0x38 };
        final byte Normal[]	= { 0x1b, 0x21, 0x00 };
        final byte Middle[]	= { 0x1b, 0x61, 0x01 };
        final byte Left[]	= { 0x1b, 0x61, 0x00 };
        final byte Right[]	= { 0x1b, 0x61, 0x02 };
        final byte NextRow[]	= { 0x1b, 0x4a, 0x00 };
        final byte tmpfeed[]    = {0xd,0xa};
        final byte Interval[]	= { '-', '-','-','-','-','-','-','-','-','-','-','-','-','-', '-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-','-' };

        final byte Init[]	  = {0x1B,0x40};

        //pThread.clearPrintBuffer();

        printerBuffer.write(Init, 0, Init.length);



// 		byte[] bChoseChinese = {0x1B,0x52,0x14};
//
//
// 		/**常量:当前的Adnroid SDK 版本号*/
// 	    int SDK_VER	= Build.VERSION.SDK_INT;
//
// 	    if(SDK_VER>=20){
//         	printerBuffer.write(bChoseChinese, 0, bChoseChinese.length);
// 	    }


        //printerBuffer.write(BoldChinese,0,BoldChinese.length);
//     	printerBuffer.write(Bold,0,Bold.length);
//     	printerBuffer.write(Middle,0,Middle.length);
//     	byte[] bHead	= getChineseByte("收银小票");
//     	printerBuffer.write(bHead,0,bHead.length);
//     	printerBuffer.write(NextRow,0,NextRow.length);


        printerBuffer.write(Normal,0,Normal.length);
        printerBuffer.write(Left,0,Left.length);
        byte[] bLineReceiptNum = null;
        String strNum = "#HK9718081500004";
        bLineReceiptNum	= strNum.getBytes();
        printerBuffer.write(bLineReceiptNum,0,bLineReceiptNum.length);
        printerBuffer.write(NextRow,0,NextRow.length);

        String strCashier	= "Cashier:Admin";
        byte[] bPeople = strCashier.getBytes();
        printerBuffer.write(bPeople,0,bPeople.length);
        printerBuffer.write(NextRow,0,NextRow.length);


        //mprinter.Write(Left);
        Date dtNow = new Date();

        final String FORM_STRING 	= "dd/MM/yyyy";
        final String FORM_TIME 		= "hh:mm:ss";
        SimpleDateFormat date = new SimpleDateFormat(FORM_STRING,Locale.getDefault());
        SimpleDateFormat time = new SimpleDateFormat(FORM_TIME,Locale.getDefault());

        String strDate 	= "Date:"+date.format(dtNow);
        byte[] bDate 	= addSpacebyte(strDate.getBytes(),iSpaceDate);
        String strTime	= "Time:"+time.format(dtNow);
        byte[] bTime = strTime.getBytes();
        printerBuffer.write(bDate,0,bDate.length);

        printerBuffer.write(bTime, 0, bTime.length);
        printerBuffer.write(NextRow,0,NextRow.length);




//	    	String strHead = "品名      折扣 价格  数量  金额";
//	    	mprinter.Write(getChineseByte(strHead));

        String strHeadName		= "Name";
        byte[] bLineHeadName			= addSpacebyte(strHeadName.getBytes(),iSpaceName );

        String strHeadDiscount 	= "-%";
        byte[] bLineHeadDiscount		= addSpacebyte(strHeadDiscount.getBytes(), iSpaceDis);

        String strHeadPrice		= "$/PCS";
        byte[] bLineHeadPrice			= addSpacebyte(strHeadPrice.getBytes(), iSpacePrice);

        String strHeadCount		= "PCS";
        byte[] bLineHeadCount			= addSpacebyte(strHeadCount.getBytes(), iSpaceCnt);

        String strHeadAmount 	= "AMT";
        byte[] bLineHeadAmount			= addSpacebyte(strHeadAmount.getBytes(), iSpaceAmt);


        printerBuffer.write(bLineHeadName,0,bLineHeadName.length);
        //printerBuffer.write(bLineHeadDiscount,0,bLineHeadDiscount.length);
        printerBuffer.write(bLineHeadPrice,0,bLineHeadPrice.length);
        printerBuffer.write(bLineHeadCount,0,bLineHeadCount.length);

        printerBuffer.write(bLineHeadDiscount,0,bLineHeadDiscount.length);
        printerBuffer.write(bLineHeadAmount,0,bLineHeadAmount.length);

        printerBuffer.write(NextRow,0,NextRow.length);

        String 		strPriceUnit  	= "$/kg";
        String 		strUnit			= "kg";
        byte[]		bPriceUnit		= addSpacebyte(strPriceUnit.getBytes(), iSpacePrice);
        byte[]		bUnit			= addSpacebyte(strUnit.getBytes(),iSpaceCnt);

        printerBuffer.write(addSpacebyte(null, iSpaceName), 0, iSpaceName);
        printerBuffer.write(bPriceUnit,0,bPriceUnit.length);
        printerBuffer.write(bUnit,0,bUnit.length);

        printerBuffer.write(NextRow,0,NextRow.length);

        printerBuffer.write(Middle,0,Middle.length);
        printerBuffer.write(Interval,0,Interval.length);
        printerBuffer.write(NextRow,0,NextRow.length);
        printerBuffer.write(Left,0,Left.length);

        float	fTotal = 0;
        for (GoodsInfo b : list) {
            fTotal += b.m_fAmount;
            String strDis  = String.format("%.2f", b.m_fDiscount);
            String strPri  = String.format("%.2f", b.m_fPrice);
            String strCnt  = String.format("%.3f", b.m_fCount);
            String strTol  = String.format("%.2f", b.m_fAmount);

            byte[] bData = null;

            bData = addSpacebyte(b.m_strName.getBytes(), iSpaceName);
            //boolean bNextLine = false;
            printerBuffer.write(bData,0,bData.length);
            if(b.m_strName.getBytes().length>iSpaceName){
                //bNextLine		= true;
                printerBuffer.write(NextRow,0,NextRow.length);
                printerBuffer.write(addSpacebyte(null, iSpaceName),0,iSpaceName);
            }
//				bData = addSpacebyte(strDis.getBytes(), iSpaceDis);
//	        	printerBuffer.write(bData,0,bData.length);
            bData = addSpacebyte(strPri.getBytes(), iSpacePrice);
            printerBuffer.write(bData,0,bData.length);
            bData = addSpacebyte(strCnt.getBytes(), iSpaceCnt);
            printerBuffer.write(bData,0,bData.length);

            bData = addSpacebyte(strDis.getBytes(), iSpaceDis);
            printerBuffer.write(bData,0,bData.length);

            bData = strTol.getBytes();
            printerBuffer.write(bData,0,bData.length);
            printerBuffer.write(NextRow,0,NextRow.length);

        }
        printerBuffer.write(Middle,0,Middle.length);
        printerBuffer.write(Interval,0,Interval.length);
        printerBuffer.write(NextRow,0,NextRow.length);


        printerBuffer.write(Left,0,Left.length);
        String strItem 		= "Items:"+"("+String.valueOf(list.size())+")";
        byte[] bItem = addSpacebyte(strItem.getBytes(),iSpaceItems);
        printerBuffer.write(bItem,0,bItem.length);

        String strTotal		= "Total Amt:";
        byte[] bTotal = addSpacebyteLeft(strTotal.getBytes(),iSpaceTalAmt);
        printerBuffer.write(bTotal,0,bTotal.length);
        byte[] bValTol = addSpacebyteLeft(String.format("$%.2f", fTotal).getBytes(),iSpaceTalVal);

        String strRecVal 	= String.format("%.1f", fTotal);
        float  fRec		= Float.valueOf(strRecVal);
        byte[] bValRec = addSpacebyteLeft(String.format("$%.2f", fRec).getBytes(),iSpaceTalVal);

        printerBuffer.write(bValTol,0,bValTol.length);
        printerBuffer.write(NextRow,0,NextRow.length);

        //mprinter.Write(addSpacebyte(null,iInchType==0?15:21));
        //byte[] bSpace = addSpacebyte(null,iInchType==0?15:21);
        //printerBuffer.write(bSpace,0,bSpace.length);

        String strRec		= "Receivable:";
        byte[] bRec = addSpacebyteLeft(strRec.getBytes(),iSpaceItems+iSpaceTalAmt);
        printerBuffer.write(bRec,0,bRec.length);
        printerBuffer.write(bValRec,0,bValRec.length);
        printerBuffer.write(NextRow,0,NextRow.length);

        //mprinter.Write(addSpacebyte(null,iInchType==0?15:21));
        //printerBuffer.write(bSpace,0,bSpace.length);
        String strCash		= "Cash:";
        byte[] bCash 		= addSpacebyteLeft(strCash.getBytes(),iSpaceItems+iSpaceTalAmt);
        printerBuffer.write(bCash,0,bCash.length);
        printerBuffer.write(bValRec,0,bValRec.length);
        printerBuffer.write(NextRow,0,NextRow.length);
        printerBuffer.write(NextRow,0,NextRow.length);

//	    	String strMac 	= "机器号：XC6V";
//	    	byte[] bMac	= getChineseByte(strMac);
//     	printerBuffer.write(bMac,0,bMac.length);
        printerBuffer.write(tmpfeed,0,tmpfeed.length);
        printerBuffer.write(tmpfeed,0,tmpfeed.length);

        String strMac 	= "Machine ID:HK97";
        byte[] bMac		= strMac.getBytes();
        printerBuffer.write(bMac,0,bMac.length);
        printerBuffer.write(NextRow,0,NextRow.length);

        final byte cmd_1[] = {0x1d ,0x68 ,0x41};//CHOOSE THE BARCODE HEIGHT AS 0x35(max = 0x41)
        final byte cmd_2[] = {0x1d ,0x48 ,0x03};//CHOOSE THE PRINTING POSITION OF BARCODE VALUE 0x1 UPPER 0x02 LOWER 0x03 UPPER AND LOWER
        final byte cmd_3[] = {0x1d ,0x77 ,0x2};//CHOOSE THE BARCODE WIDTH AS 0x2(max = 0x8)

        String strCode 	= "2000011035303";
        byte[] bCode	= createBarcode(strCode);
        printerBuffer.write(cmd_1,0,cmd_1.length);
        printerBuffer.write(cmd_2,0,cmd_2.length);
        printerBuffer.write(cmd_3,0,cmd_3.length);

        printerBuffer.write(bCode,0,bCode.length);
        printerBuffer.write(tmpfeed,0,tmpfeed.length);
        printerBuffer.write(tmpfeed,0,tmpfeed.length);

        //pThread.startPrintData();
        return printerBuffer.toByteArray();
    }

    byte[]	createBarcode(String strCode){

        byte[] bCode = strCode.getBytes();
        int iLen = 3+1+bCode.length;
        byte[] bRet = new byte[iLen];
        final byte BAREN8[]= {0x1d ,0x6b  ,0x04 };//code39
        System.arraycopy(BAREN8, 0, bRet, 0, 3);
        System.arraycopy(bCode, 0, bRet, 3, bCode.length);
        bRet[iLen-1] = 0x00;
        return bRet;
    }

    byte[] addSpacebyte(byte[] src,int cnt){
        byte[] bRet = null;
        int iCnt = src==null?0:src.length;

        if(iCnt<cnt){
            bRet = new byte[cnt+1];
            if(src!=null){
                System.arraycopy(src, 0, bRet, 0, iCnt);
            }
            for(int i=0;i<cnt-iCnt;i++){
                bRet[iCnt+i] += ' ';
            }
            bRet[cnt] = '\0';
        }else {
            bRet = src;
        }
        return bRet;
    }

    byte[] addSpacebyteLeft(byte[] src,int cnt){
        byte[] bRet = null;
        int iCnt = src==null?0:src.length;

        if(iCnt<cnt){
            bRet = new byte[cnt+1];
            if(src!=null){
                System.arraycopy(src, 0, bRet, cnt-iCnt, iCnt);
            }
            for(int i=0;i<cnt-iCnt;i++){
                bRet[i] += ' ';
            }
            bRet[cnt] = '\0';
        }else {
            bRet = src;
        }
        return bRet;
    }
    byte[] getChineseByte(String str){
        byte[] ret = null;
        try {
            ret = str.getBytes("gb2312");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return ret;
    }
    //----------------------------------------
    public byte[] getPrintBmpData(Bitmap bmp,int iType) {
        byte[] data = null;
        data 	= PrintWholeBmp(GetBitmapData(bmp,iType), iType);
        return data;
    }
    public byte[] getPrintColorPicData(Bitmap bmp,int iType) {
        byte[] data = null;
        data  = PrintWholeBmp(getBytesFromBitMapWithSize(bmp,iType), iType);
        return data;
    }

    private	byte AL_PRINTER_MAX_HEIGHT			= 0x18;

    private byte[] PrintWholeBmp(byte[] bmpData,int iPrintType) {

        int DotLineWidth = iPrintType==0?384:576;

        //pThread.clearPrintBuffer();

        byte[]	head 		= {0x1d,0x76,0x30,0x00,0x00,0x00,0x00,0x00};
        int 	iWidth 		= (DotLineWidth+7)/8;
        int		iHeight		= AL_PRINTER_MAX_HEIGHT;
        int     iHeadLen	= head.length;
        head[iHeadLen-4]	= (byte)iWidth;
        head[iHeadLen-3]	= (byte)(iWidth>>8);
        head[iHeadLen-2]	= (byte)iHeight;
        head[iHeadLen-1]	= (byte)(iHeight>>8);
        int     iMaxData	= iWidth*iHeight;
        int		iMaxSendLen		= iMaxData+iHeadLen;

        byte[]	data		= new byte[iMaxSendLen];
        System.arraycopy(head, 0, data, 0, iHeadLen);

        int iNewLen = bmpData.length+(bmpData.length+iMaxData-1)/iMaxData*iHeadLen;

        byte[] retData = new byte[iNewLen];
        int iPos = 0;

        for(int i=0;i<bmpData.length;i+=iMaxData){
            int iBlock 	= (i+iMaxData)<=bmpData.length?iMaxData:(bmpData.length-i);
            if(iBlock<iMaxData){
                int iLeftHeight		= (iBlock+iWidth-1)/iWidth;
                data[iHeadLen-2]	= (byte)iLeftHeight;
                data[iHeadLen-1]	= (byte)(iLeftHeight>>8);
            }
            System.arraycopy(bmpData, i, data, iHeadLen, iBlock);
            System.arraycopy(data, 0, retData, iPos, iBlock+iHeadLen);
            iPos += iBlock+iHeadLen;

            //	pThread.writePrintData(data, 0, iBlock+iHeadLen);
        }
        // pThread.startPrintData();

        return retData;
    }

    private byte[] GetBitmapData(Bitmap bmp,int iPrintType) {

        if(bmp==null){
            return null;
        }
        int DotLineWidth = iPrintType==0?384:576;
        int DotLineBytes = (DotLineWidth+7)/8;
        int height = bmp.getHeight();
        int width = bmp.getWidth();
        int byteofline = DotLineBytes;
        Log.d(tag, "kwq print picutre_bmp_print width:"+width+" h:"+height+" line:"+byteofline);
        if (width > DotLineWidth) {
            width = DotLineWidth;
        }
        byte[] BitMapBuf = new byte[byteofline];
        int[] tmpbuf = new int[width + 8];
        byte[] bmpData = new byte[DotLineBytes*height];



        for(int i=0;i<height;i++){

            bmp.getPixels(tmpbuf, 0, width, 0, i, width, 1);

            for(int j=0; j<width; j+=8)
            {
                for(int k=0; k<8; k++)
                {
                    if(i < 1)
                    {
//		                        Log.d(tag, "pos (" + (j+k) + "," + i + ") = " + Integer.toHexString(tmpbuf[j+k]));
                    }
                    if((tmpbuf[j+k] == Color.TRANSPARENT) || (tmpbuf[j+k] == Color.WHITE))
                    {
                        BitMapBuf[j/8] &= ~(0x80 >> k);
                    }else
                    {
                        BitMapBuf[j/8] |= (0x80 >> k);
                    }
                }
            }
            System.arraycopy(BitMapBuf, 0, bmpData, i*DotLineBytes, DotLineBytes);
        }
        return bmpData;
    }

    /**
     * 将bitmap图转换为头四位有宽高的光栅位图
     */
    public  byte[] getBytesFromBitMapWithSize(Bitmap bitmap,int iPrintType) {
        if(bitmap==null){
            return null;
        }
        int DotLineWidth = iPrintType==0?384:576;
        int width = bitmap.getWidth();
        if(width>DotLineWidth){
            Bitmap.createScaledBitmap(bitmap, DotLineWidth, bitmap.getHeight()*DotLineWidth/bitmap.getWidth(), true);
        }
        width	= bitmap.getWidth();
        int height = bitmap.getHeight();
        int bw = (DotLineWidth - 1) / 8 + 1;

        byte[] rv = new byte[height * bw];

//	        rv[0] = (byte) bw;//xL
//	        rv[1] = (byte) (bw >> 8);//xH
//	        rv[2] = (byte) height;
//	        rv[3] = (byte) (height >> 8);

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int clr = pixels[width * i + j];
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;
                byte gray = (RGB2Gray(red, green, blue));
                rv[(DotLineWidth * i + j) / 8 ] = (byte) (rv[(DotLineWidth * i + j) / 8 ] | (gray << (7 - ((DotLineWidth * i + j) % 8))));
            }
        }

        return rv;
    }


    private byte RGB2Gray(int r, int g, int b) {
        return (false ? ((int) (0.29900 * r + 0.58700 * g + 0.11400 * b) > 200)
                : ((int) (0.29900 * r + 0.58700 * g + 0.11400 * b) < 200)) ? (byte) 1 : (byte) 0;
    }


    public  byte[] merge(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
}
