package com.example.aksha.accelerometer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.so_problem.R;

public class MainActivity extends Activity implements SensorEventListener
{

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    TextView title,tvx,tvy,tvz;
    EditText etshowval;
    RelativeLayout layout;
    private String acc;
    private String read_str = "";
    private final String filepath = "/mnt/sdcard/acc.txt";
    private BufferedWriter mBufferedWriter;
    private BufferedReader mBufferedReader;
    private float x;
    private float y;
    private float z;

    public static final int MSG_DONE = 1;
    public static final int MSG_ERROR = 2;
    public static final int MSG_STOP = 3;

    private boolean mrunning;
    private Handler mHandler;
    private HandlerThread mHandlerThread;

    private Handler uiHandler = new Handler(){
        public void handleMessage(Message msg){
            String str = (String) msg.obj;
            switch (msg.what)
            {
                case MSG_DONE:
                    Toast.makeText(getBaseContext(), str,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MSG_ERROR:
                    Toast.makeText(getBaseContext(),str,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MSG_STOP:
                    Toast.makeText(getBaseContext(), str,
                            Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //get layout
        layout = (RelativeLayout) findViewById(R.id.relative);

        //get textviews
        title = (TextView)findViewById(R.id.name);
        tvx = (TextView)findViewById(R.id.xval);
        tvy = (TextView)findViewById(R.id.yval);
        tvz = (TextView)findViewById(R.id.zval);
        etshowval = (EditText)findViewById(R.id.showval);
        title.setText("Accelerator");

        mHandlerThread = new HandlerThread("Working Thread");
        mHandlerThread.start();

        mHandler = new Handler(mHandlerThread.getLooper());
        mHandler.post(r);
    }

    private Runnable r = new Runnable(){
        @Override
        public void run ()
        {
            while(true)
            {
                if (mrunning)
                {
                    Message msg1 = new Message();
                    try
                    {
                        WriteFile(filepath,acc);
                        msg1.what = MSG_DONE;
                        msg1.obj = "Start to write to SD 'acc.txt'";
                    }
                    catch (Exception e)
                    {
                        msg1.what = MSG_ERROR;
                        msg1.obj = e.getMessage();
                    }
                    uiHandler.sendMessage(msg1);
                }
                else
                {
                    Message msg2 = new Message();
                    msg2.what = MSG_STOP;
                    msg2.obj = "Stop to write to SD 'acc.txt'";
                    uiHandler.sendMessage(msg2);
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    public void onStartClick(View view)
    {
        start();
    }

    public void onStopClick(View view)
    {
        stop();
    }

    public void onReadClick(View view)
    {
        etshowval.setText(ReadFile(filepath));
    }

    private synchronized void start()
    {
        mrunning = true;
    }

    private synchronized void stop()
    {
        mrunning = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        // TODO Auto-generated method stub

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
            acc= String.valueOf(x) + ", " + String.valueOf(y) + ", " + String.valueOf(z);

            tvx.setText("X = "+ String.valueOf(x));
            tvy.setText("Y = "+ String.valueOf(y));
            tvz.setText("Z = "+ String.valueOf(z));
        }
    }

    public void CreateFile(String path)
    {
        File f = new File(path);
        try {
            Log.d("ACTIVITY", "Create a File.");
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String ReadFile (String filepath)
    {
        mBufferedReader = null;
        String tmp = null;

        if (!FileIsExist(filepath))
            CreateFile(filepath);

        try
        {
            mBufferedReader = new BufferedReader(new FileReader(filepath));
            // Read string
            while ((tmp = mBufferedReader.readLine()) != null)
            {
                tmp += "\n";
                read_str += tmp;
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return read_str;
    }

    public void WriteFile(String filepath, String str)
    {
        mBufferedWriter = null;

        if (!FileIsExist(filepath))
            CreateFile(filepath);

        try
        {
            mBufferedWriter = new BufferedWriter(new FileWriter(filepath, true));
            mBufferedWriter.write(str);
            mBufferedWriter.newLine();
            mBufferedWriter.flush();
            mBufferedWriter.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean FileIsExist(String filepath)
    {
        File f = new File(filepath);

        if (! f.exists())
        {
            Log.e("ACTIVITY", "File does not exist.");
            return false;
        }
        else
            return true;
    }

    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        mSensorManager.unregisterListener(this);
        Toast.makeText(this, "Unregister accelerometerListener", Toast.LENGTH_LONG).show();
        super.onPause();
    }
}


