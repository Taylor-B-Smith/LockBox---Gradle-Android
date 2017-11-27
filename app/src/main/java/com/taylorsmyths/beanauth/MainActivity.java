package com.taylorsmyths.beanauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;



import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.*;
import android.bluetooth.BluetoothAdapter.LeScanCallback;

import android.widget.*;
import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.*;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final List<Bean> beans = new ArrayList<>();
    public static Bean connectedBean;
    private Context thisActivity;

    private static TextView txtSearching;
    private static Button btnDiscoverBeans;
    private static ListView lstBeans;
    private BeanListAdapter beanAdapter;
    private int intRssi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        thisActivity = this;

        lstBeans = (ListView)findViewById(R.id.lstBeans);
        txtSearching = (TextView)findViewById(R.id.txtSearching);
        btnDiscoverBeans = (Button)findViewById(R.id.btnDiscoverBeans);

        beanAdapter = new BeanListAdapter(lstBeans.getContext(), R.layout.bean_row, beans);
        lstBeans.setAdapter(beanAdapter);

        btnDiscoverBeans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beans.clear();
                txtSearching.setText("Searching for Beans...");
                DiscoverBeans(view);
            }
        } );

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
    ///////////////////////////////////////////////////////
    public void DiscoverBeans( View v ) {
        //Toast.makeText(thisActivity, "great success", Toast.LENGTH_SHORT).show();
        //final List<Bean> beans = new ArrayList<>();

        BeanDiscoveryListener listener = new BeanDiscoveryListener() {
            @Override
            public void onBeanDiscovered(Bean bean, int rssi) {
                if (!beans.contains(bean)) {
                    beanAdapter.add(bean);
                    System.out.println(rssi);
                    System.out.println("RSSI^^^^");
                    intRssi = rssi;
                }
            }

            @Override
            public void onDiscoveryComplete() {
                // This is called when the scan times out, defined by the .setScanTimeout(int seconds) method

                for (Bean bean : beans) {
                    System.out.println(bean.getDevice().getName());   // "Bean"
                    System.out.println("^bean name^");//             (example)
                    System.out.println(bean.getDevice().getAddress());    // "B4:99:4C:1E:BC:75" (example)
                    txtSearching.setText(bean.getDevice().getName());
                    System.out.println(intRssi);
                    System.out.println("RSSI^^^^");

                }
                txtSearching.setText("Beans found:");
            }
        };

        BeanManager.getInstance().setScanTimeout(5);  // Timeout in seconds, optional, default is 30 seconds
        BeanManager.getInstance().startDiscovery(listener);
    };

    public void onBeanSelected(View view) {
        final Bean bean = (Bean)view.getTag();
        lstBeans.setClickable(false);
        for (int i = 0;i<lstBeans.getChildCount();i++) {
            View v = lstBeans.getChildAt(i);
            View b = v.findViewById(R.id.connectButton);
            b.setClickable(false);
        }


        final Button connectButton = (Button)view.findViewById(R.id.connectButton);
        connectButton.setText("Connecting to " + bean.getDevice().getName() + "...");


        BeanListener beanListener = new BeanListener() {


            @Override
            public void onConnected() {
                connectedBean = bean;
                LedColor blue = LedColor.create(0,0,255);

                bean.setLed(blue);
                System.out.println("Connected to Bean!");
                Toast.makeText(thisActivity, "Connected to Bean!", Toast.LENGTH_SHORT).show();
                bean.readDeviceInfo(new Callback<DeviceInfo>() {
                    @Override
                    public void onResult(DeviceInfo deviceInfo) {
                        System.out.println(deviceInfo.hardwareVersion());
                        System.out.println(deviceInfo.firmwareVersion());
                        System.out.println(deviceInfo.softwareVersion());
                    }
                });

//                try {
//                    //bean.setLed(blue);
//                    //bean.wait(2000);
//                    //bean.setLed(off);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                Intent loginIntent = new Intent( MainActivity.this, LoginActivity.class);
                startActivity( loginIntent );
            }

            // In practice you must implement the other Listener methods


            @Override
            public void onDisconnected() {
                System.out.println("Bean Disconnected!!!");
                Toast.makeText(thisActivity, "Bean Disconnected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReadRemoteRssi(int rssi) {
                System.out.println("RSSI: " + rssi);
            }

            @Override
            public void onError(BeanError error) {
                System.out.println("Error: " + error);
            }

            @Override
            public void onScratchValueChanged(ScratchBank bank, byte[] value) {}

            @Override
            public void onSerialMessageReceived(byte[] data) {}

            @Override
            public void onConnectionFailed() {
                System.out.println("!!!CONNECTION FAILED!!!");
                Toast.makeText(thisActivity, "Connection Failed", Toast.LENGTH_SHORT).show();
                connectButton.setText(bean.getDevice().getName());
            }
        };

// Assuming you are in an Activity, use 'this' for the context
        bean.connect(this, beanListener);
    }


    ///////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
