package com.setclapp.android_printers.activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.setclapp.android_printers.R;
import com.setclapp.android_printers.print_pages.TestPrintPage;
import com.setclapp.android_printers.printing.Base.IPrinter;
import com.setclapp.android_printers.printing.PrintPageModel;
import com.setclapp.android_printers.printing.Printers.TactilionPrinter;

public class MainActivity extends AppCompatActivity {

    //Components
    private Button printButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initiliaze();

        bindListeners();

    }

    private void initiliaze() {

        printButton = findViewById(R.id.main_test_print_button);
    }

    private void bindListeners() {

        printButton.setOnClickListener(printListener);

    }

    View.OnClickListener printListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // you can use PrinterFactory to create printer instance
            IPrinter printer = new TactilionPrinter(getApplicationContext());

            TestPrintPage printPage = new TestPrintPage(getApplicationContext(), printer);

            printPage.print(new PrintPageModel());
        }
    };

}
