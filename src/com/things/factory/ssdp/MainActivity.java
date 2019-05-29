package com.things.factory.ssdp;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.things.factory.R;
import java.util.ArrayList;
import java.util.List;

import static com.things.factory.ssdp.SSDPMessage.NEWLINE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<String> listReceive = new ArrayList<String>();
    private static final String TAG = "SSDP";
    private TextView tvReceive; //显示搜寻结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tvReceive = (TextView) findViewById(R.id.tv_show_receive);
        Button btn = (Button) findViewById(R.id.btnSendSSDPSearch);
        btn.setOnClickListener(this);
    }

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

    @Override
    public void onClick(View v) {
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                sendSearchMessage();
////            }
////        }).start();
//
//        // android api
//        String macAddress;
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//            // code for Lollipop and later
//            WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//            WifiInfo info = manager.getConnectionInfo();
//            macAddress = info.getMacAddress();
//        } else {
////            macAddress = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);  // d4657b138d0b5d26
////            macAddress = MainActivity.getMacAddress();
////            macAddress = android.provider.Settings.Secure.ANDROID_ID;   // android_id
////            macAddress = getMacAddr();
//            Device device = new Device();
//            macAddress = device.getMacAddress();
//        }
//        Log.d("SSDP", macAddress);
//
//        SSDPServer server = new SSDPServer();
//        server.start();

        SSDPServer.listen();
        SSDPClient.search();
    }

    protected void receiveResult(final List<String[]> results) {
        //显示接收结果
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listReceive.clear();
                StringBuilder sb = new StringBuilder();
                for (String[] result: results) {
                    sb.append("\r\t").append(String.valueOf(result))
                            .append(NEWLINE).append("-----------------------------------").append(NEWLINE);
                }
                String s = sb.toString();
                tvReceive.setText(s);
            }
        });

        ArrayList<String[]> resultList = new ArrayList<String[]>();
        String json = new Gson().toJson(resultList);
        Log.d(TAG, json);
    }
}
