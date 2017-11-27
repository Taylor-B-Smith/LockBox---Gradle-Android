package com.taylorsmyths.beanauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.internal.battery.BatteryProfile;
import com.punchthrough.bean.sdk.message.BatteryLevel;
import com.punchthrough.bean.sdk.message.ScratchBank;
import com.punchthrough.bean.sdk.message.ScratchData;
import com.punchthrough.bean.sdk.message.LedColor;

public class BeanActivity extends AppCompatActivity {

    private TextView txtBeanName;
    private Button btnRed;
    private Button btnGreen;
    private Button btnBlue;
    private Button btnLedOff;
    private Button btnDisconnect;
    private Button btnLedOpen;

    public Bean connectedBean;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bean);

        connectedBean = MainActivity.connectedBean;

        txtBeanName = findViewById(R.id.txtBeanName);
        btnRed = findViewById(R.id.btnRed);
        btnGreen = findViewById(R.id.btnGreen);
        btnBlue = findViewById(R.id.btnBlue);
        btnLedOpen = findViewById(R.id.btnLedOpen);
        btnLedOff = findViewById(R.id.btnLedOff);
        btnDisconnect = findViewById(R.id.btnDisconnect);

        txtBeanName.setText(connectedBean.getDevice().getName());

        btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectedBean.setLed(LedColor.create(255,0,0));
            }
        });
        btnGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                connectedBean.setLed(LedColor.create(0,255,0));
            }
        });
        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectedBean.setLed(LedColor.create(0,0,255));
            }
        });
        btnLedOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectedBean.setLed(LedColor.create(0,0,0));
            }
        });

        btnLedOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectedBean.setLed(LedColor.create(0,0,17));
                connectedBean.setScratchData(ScratchBank.BANK_1, "pulse");
            }
        });

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectedBean.disconnect();
                Intent disconnectIntent = new Intent(BeanActivity.this, MainActivity.class);
                startActivity( disconnectIntent );
            }
        });


        //connectedBean.readRemoteRssi(); //this was for yonghao

    }

}
