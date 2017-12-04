package com.taylorsmyths.beanauth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.internal.battery.BatteryProfile;
import com.punchthrough.bean.sdk.message.BatteryLevel;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.ScratchBank;
import com.punchthrough.bean.sdk.message.ScratchData;
import com.punchthrough.bean.sdk.message.LedColor;

import org.w3c.dom.Text;

import static com.taylorsmyths.beanauth.LoginActivity.intBatteryLevel;

public class BeanActivity extends AppCompatActivity {

    private TextView txtBeanName;
    private TextView txtBattery;
    private Button btnRed;
    private Button btnGreen;
    private Button btnBlue;
    private Button btnLedOff;
    private Button btnDisconnect;
    private Button btnLedOpen;

    public Bean connectedBean;


    Toast toast;
    Context context;
    CharSequence toastText = "Connection lost!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bean);

        connectedBean = MainActivity.connectedBean;

        txtBeanName = findViewById(R.id.txtBeanName);
        txtBattery = findViewById(R.id.txtBattery);
        btnRed = findViewById(R.id.btnRed);
        btnGreen = findViewById(R.id.btnGreen);
        btnBlue = findViewById(R.id.btnBlue);
        btnLedOpen = findViewById(R.id.btnLedOpen);
        btnLedOff = findViewById(R.id.btnLedOff);
        btnDisconnect = findViewById(R.id.btnDisconnect);



        txtBeanName.setText(connectedBean.getDevice().getName());
        txtBattery.setText("Device Battery at " + Integer.toString( intBatteryLevel) + "%" );


        context = getApplicationContext();

        btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected(connectedBean)) {
                    connectedBean.setLed(LedColor.create(255,0,0));
                }


            }
        });
        btnGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected(connectedBean)) {
                    connectedBean.setLed(LedColor.create(0,255,0));
                }
            }
        });
        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected(connectedBean)) {
                    connectedBean.setLed(LedColor.create(0,0,255));
                }
            }
        });
        btnLedOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected(connectedBean)) {
                    connectedBean.setLed(LedColor.create(0,0,0));
                }
            }
        });

        btnLedOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected(connectedBean)) {
                    connectedBean.setLed(LedColor.create(0,0,17));
                    connectedBean.setScratchData(ScratchBank.BANK_1, "pulse");
                    btnLedOpen.setEnabled(false);
                    btnLedOpen.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnLedOpen.setEnabled(true);
                        }
                    }, 5000);
                }

            }
        });

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastText = "Device disconnected!";
                disconnect(connectedBean);
            }
        });


        //connectedBean.readRemoteRssi(); //this was for yonghao

    }

    private void disconnect(Bean bean) {        //disconnect and back out to main activity
        connectedBean.setLed(LedColor.create( 0, 0, 0 ));
        toast.makeText(context, toastText, Toast.LENGTH_LONG);
        bean.disconnect();

        Intent disconnectIntent = new Intent( BeanActivity.this, MainActivity.class);
        startActivity( disconnectIntent );
    }

    private boolean isConnected(Bean bean) {    //check if bean is still connected
        if(bean.isConnected()) {
            return true;
        } else {
            disconnect(bean);
            return false;
        }
    }
}
