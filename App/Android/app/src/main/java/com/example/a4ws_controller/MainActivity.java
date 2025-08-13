package com.example.a4ws_controller;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    //SerialPort Service UUID (SPP)
    private static final UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private static final int CHECK_PERMISSION = 1001;
    private String TargetMACAddress = "No device is connected";
    private BluetoothAdapter mBtAdapter;
    private BluetoothDevice mBtDevice;
    private BluetoothSocket mBtSocket;
    private OutputStream mOutput;
    private InputStream mInput;
    private TextView tv1;
    //private TextView tv2;
    private Button btnSel;
    private Button btnCon;
    private AlertDialog.Builder mAlertDialog;

    private Intent enableBtIntent;
    private ActivityResultLauncher<Intent> launcher;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable;
    //private Paint paint;

    private Timer timer;
    private CountUpTimerTask timerTask;
    private final Handler handler = new Handler(Looper.getMainLooper());

    float downX = 200;
    float downY = 200;
    float downX2 = 200;
    float downY2 = 200;
    int W = 400;

    @SuppressLint({"ClickableViewAccessibility", "MissingInflatedId"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate 0");
        tv1 = (TextView) findViewById(R.id.textView1);
        btnSel = (Button) findViewById(R.id.buttonSelect);
        btnCon = (Button) findViewById(R.id.buttonConnect);
        mAlertDialog = new AlertDialog.Builder(this);
        mAlertDialog.setTitle("Alert");
        mAlertDialog.setPositiveButton("OK", null);

        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        int delay = 0;
        int period = 100;
        timer = new Timer();
        timerTask = new CountUpTimerTask();

        int radius = 75;

        ImageView mImageView = findViewById(R.id.imageView);
        ImageView mImageView2 = findViewById(R.id.imageView2);
        Bitmap bitmap = Bitmap.createBitmap(W, W, Bitmap.Config.ARGB_8888);
        Bitmap bitmap2 = Bitmap.createBitmap(W, W, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Canvas canvas2 = new Canvas(bitmap2);
        Paint paint = new Paint();
        paint.setColor(Color.argb(255,255,0,255));
        paint.setStrokeWidth(10);

        // Setting the bitmap on ImageView
        mImageView.setImageBitmap(bitmap);
        mImageView2.setImageBitmap(bitmap2);
        canvas.drawCircle(W/2, W/2, W/2, paint);
        canvas2.drawCircle(W/2, W/2, W/2, paint);
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(W/2, W/2, radius, paint);
        canvas2.drawCircle(W/2, W/2, radius, paint);

        Rect rect = new Rect(0, 0, W, W);
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch(action) {
                    case (MotionEvent.ACTION_DOWN) :
                    case (MotionEvent.ACTION_MOVE) :
                        downX = event.getX();
                        downY = event.getY();
                        paint.setColor(Color.argb(255,255,152,0));
                        canvas.drawRect(rect, paint);
                        paint.setColor(Color.argb(255,255,0,255));
                        canvas.drawCircle(W/2, W/2, W/2, paint);
                        paint.setColor(Color.YELLOW);
                        canvas.drawCircle(downX, downY, radius, paint);
                        mImageView.invalidate();
                        return true;
                    case (MotionEvent.ACTION_UP) :
                        downX = 200;
                        downY = 200;
                        paint.setColor(Color.argb(255,255,152,0));
                        canvas.drawRect(rect, paint);
                        paint.setColor(Color.argb(255,255,0,255));
                        canvas.drawCircle(W/2, W/2, W/2, paint);
                        paint.setColor(Color.YELLOW);
                        canvas.drawCircle(W/2,W/2, radius, paint);
                        mImageView.invalidate();
                        return true;
                }
                return true;
            }
        });
        mImageView2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch(action) {
                    case (MotionEvent.ACTION_DOWN) :
                    case (MotionEvent.ACTION_MOVE) :
                        downX2 = event.getX();
                        downY2 = event.getY();
                        paint.setColor(Color.argb(255,255,152,0));
                        canvas2.drawRect(rect, paint);
                        paint.setColor(Color.argb(255,255,0,255));
                        canvas2.drawCircle(W/2, W/2, W/2, paint);
                        paint.setColor(Color.YELLOW);
                        canvas2.drawCircle(downX2, downY2, radius, paint);
                        mImageView2.invalidate();
                        return true;
                    case (MotionEvent.ACTION_UP) :
                        downX2 = 200;
                        downY2 = 200;
                        paint.setColor(Color.argb(255,255,152,0));
                        canvas2.drawRect(rect, paint);
                        paint.setColor(Color.argb(255,255,0,255));
                        canvas2.drawCircle(W/2, W/2, W/2, paint);
                        paint.setColor(Color.YELLOW);
                        canvas2.drawCircle(W/2,W/2, radius, paint);
                        mImageView2.invalidate();
                        return true;
                }
                return true;
            }
        });

        tv1.setText(TargetMACAddress);
        //select button
        btnSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getTargetAddress();
            }
        });
        //connect button
        btnCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                prepareSerialCommunication();
                timer.schedule(timerTask, delay, period);
            }
        });

        enableBtIntent = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
        launcher= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.i(TAG,"result");
                    if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Toast.makeText(this, "use of bluetooth is not allowed.", Toast.LENGTH_LONG).show();
                        mAlertDialog.setMessage("use of bluetooth is not allowed.");
                        mAlertDialog.show();
                    } else {
                        Log.i(TAG, "onActivityResult() Bluetooth function is available.");
                    }
                });

        //request permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN}, CHECK_PERMISSION);
                return;
            }
        }
        launcher.launch(enableBtIntent);
    }

    class CountUpTimerTask extends TimerTask{
        @Override
        public void run(){
            handler.post(() -> {
                try {
                    mOutput.write(("A" + (2*downX/W - 1) + "B" + (2*downY/W - 1) + "C" + (2*downX2/W - 1) + "D" + (2*downY2/W - 1)).getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void prepareSerialCommunication() {
        // get instance of bluetooth device
        try {
            mBtDevice = mBtAdapter.getRemoteDevice(TargetMACAddress);
        } catch (Exception e) {
            mAlertDialog.setMessage("Failed to get instance of bluetooth device.");
            mAlertDialog.show();
            return;
        }
        // get instance of bluetooth socket
        // set profile which we will use for communication
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            mBtSocket = mBtDevice.createRfcommSocketToServiceRecord(BT_UUID);
            mBtSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            mAlertDialog.setMessage("Cannot connect! Check 1.If the Mac address of the receiving device is correct." +
                                    "2. If the program for communication on the receiving device is running.");
            mAlertDialog.show();
            btnSel.setEnabled(true);
            btnCon.setEnabled(false);
            return;
        }

        Log.i(TAG, "connect socket");
        //connect a socket
        try {
            mOutput = mBtSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //polling
        mHandler = new Handler(Looper.getMainLooper());
        mRunnable = new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[1024];
                int nBytes = 0;

                try {
                    mInput = mBtSocket.getInputStream();
                    if (mInput.available() != 0)
                        nBytes = mInput.read(buffer, 0, 1024);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mHandler.postDelayed(this, 50);
            }
        };
        mHandler.post(mRunnable);

        btnCon.setEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                mAlertDialog.setMessage("Connection was not permitted.");
                mAlertDialog.show();
                return;
            }
        }
        launcher.launch(enableBtIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBtSocket != null) {
            try {
                mBtSocket.close();
            } catch (IOException connectException) {/*ignore*/}
            mBtSocket = null;
        }
        mHandler.removeCallbacks(mRunnable);
    }

    private String selectedDevice = "";
    private void getTargetAddress() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        //get instance of bluetooth adapter
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBtAdapter = bluetoothManager.getAdapter();
        Log.i(TAG,"getTargetAddress() mBtAdapter==null?" + (mBtAdapter==null));
        //get list of bluetooth devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        int number = pairedDevices.size();
        String[] deviceAddressList = new String[number];
        String[] deviceSelectionList = new String[number];
        if (pairedDevices.size() > 1) {
            int i = 0;
            for (BluetoothDevice device : pairedDevices) {
                deviceSelectionList[i] = device.getName() + " " + device.getAddress();
                deviceAddressList[i] = device.getAddress();
                Log.i(TAG, "getTargetAddress() paired Device " + deviceSelectionList[i]);
                i++;
            }
            TargetMACAddress = deviceAddressList[0];
            selectedDevice = deviceSelectionList[0];
        } else {
            Log.i(TAG,"getTargetAddress() cant found any devices");
            return;
        }

        new android.app.AlertDialog.Builder(MainActivity.this)
                .setTitle("Select Bluetooth Device")
                .setSingleChoiceItems(deviceSelectionList, 0, (dialog, item) -> {
                    TargetMACAddress = deviceAddressList[item];
                    selectedDevice = deviceSelectionList[item];
                })
                .setPositiveButton("Select", (dialog, id) -> {
                    Log.i(TAG, "selectDevice() Selected Device " + selectedDevice);
                    tv1.setText(selectedDevice);
                    btnSel.setEnabled(false);
                    btnCon.setEnabled(true);
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                })
                .show();
    }
}
