package com.alvarezaaronai.edam;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
//Imports from MeteWear
import com.alvarezaaronai.edam.Device.DeviceSetupActivity;
import com.alvarezaaronai.edam.Dots.Settings;
import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.android.BtleService;
import android.bluetooth.BluetoothDevice;


import com.mbientlab.bletoolbox.scanner.BleScannerFragment;

import java.util.UUID;

import bolts.Continuation;
import bolts.Task;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class HomeActivity extends AppCompatActivity implements BleScannerFragment.ScannerCommunicationBus, ServiceConnection {
    //Fragments
    public static final int REQUEST_START_APP= 1;

    //MeteWear
    private BtleService.LocalBinder serviceBinder;
    private MetaWearBoard board;
        //Testing Purpose Specific MMR board.
    private String MMR_MAC_Address = "F5:64:B2:18:F2:09";

    //Log Cats
    private String TAGI = "HomeActivity_Info";

    //On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // Bind the service when the activity is created
        getApplicationContext().bindService(new Intent(this, BtleService.class),
                this, Context.BIND_AUTO_CREATE);

    }
    //Options Menu
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
            //Go to Settings Activity
            Intent intent = new Intent(this, Settings.class);
            //intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_device_info) {
            //Go to Device Info Activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Frament
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_START_APP:
                ((BleScannerFragment) getFragmentManager().findFragmentById(R.id.scanner_fragment)).startBleScan();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    // Bind Service Connections / Unbind Service Connection
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        // Typecast the binder to the service's LocalBinder class
        serviceBinder = (BtleService.LocalBinder) iBinder;
        Log.i(TAGI, "onServiceConnected: Service is Connected.");


    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    // Life Cycles
    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unbind the service when the activity is destroyed
        getApplicationContext().unbindService(this);
    }

    //Implemented Methods
    @Override
    public UUID[] getFilterServiceUuids() {
        return new UUID[] {MetaWearBoard.METAWEAR_GATT_SERVICE};
    }

    @Override
    public long getScanDuration() {
        return 10000L;
    }

    @Override
    public void onDeviceSelected(BluetoothDevice device) {
        board = serviceBinder.getMetaWearBoard(device);

        final ProgressDialog connectDialog = new ProgressDialog(this);
        connectDialog.setTitle(getString(R.string.title_connecting));
        connectDialog.setMessage(getString(R.string.message_wait));
        connectDialog.setCancelable(false);
        connectDialog.setCanceledOnTouchOutside(false);
        connectDialog.setIndeterminate(true);
        connectDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), (dialogInterface, i) -> board.disconnectAsync());
        connectDialog.show();

        board.connectAsync().continueWithTask(task -> task.isCancelled() || !task.isFaulted() ? task : reconnect(board))
                .continueWith(task -> {
                    if (!task.isCancelled()) {
                        runOnUiThread(connectDialog::dismiss);
                        Intent navActivityIntent = new Intent(HomeActivity.this, DeviceSetupActivity.class);
                        navActivityIntent.putExtra(DeviceSetupActivity.EXTRA_BT_DEVICE, device);
                        startActivityForResult(navActivityIntent, REQUEST_START_APP);
                    }

                    return null;
                });
    }
    public static Task<Void> reconnect(final MetaWearBoard board) {
        return board.connectAsync().continueWithTask(task -> task.isFaulted() ? reconnect(board) : task);
    }


}
