package com.cowge.anim.multiscrollnumber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MultiScrollNumber mMultiScrollNumber = (MultiScrollNumber) findViewById(R.id.scroll_num1);
        mMultiScrollNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMultiScrollNumber.setNumber(231, 889);
            }
        });
        final MultiScrollNumber mMultiScrollNumber2 = (MultiScrollNumber) findViewById(R.id.scroll_num2);
        mMultiScrollNumber2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMultiScrollNumber2.setNumber(999, 231);
            }
        });
    }
}
