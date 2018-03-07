package com.example.jadhosn.app9;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements SensorEventListener{

    //Database Helper Class - Initial Call
    DatabaseHelper myDb;
    EditText text;

    private final Handler hndlr = new Handler();

    private Runnable runn;

    Button Start;
    Button Stop;
    Button Load;

    GraphView graph;

    //-----------------------------------------------------
    //Accelerometer Initialization Variables
    private float slastX, slastY, slastZ;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private TextView currentX, currentY, currentZ;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    //-----------------------------------------------------

    private static final Random rnd = new Random();
    public LineGraphSeries<DataPoint> input;
    public LineGraphSeries<DataPoint> input1;
    public LineGraphSeries<DataPoint> input2;

    private double lastX = 5d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Database onCreate Call
        myDb = new DatabaseHelper(this);
        text = (EditText) findViewById(R.id.text);

        //LoadDB Button Listener
        Load = (Button)findViewById(R.id.load);
        Load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDB();
            }
        });


        //---------------------------------------------------------------
        // Accelerometer Code
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
        {
            // success! we have an accelerometer
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else
        {
            // fail! we dont have an accelerometer!
        }
        //------------------------------------------------------------------------


        //Start Button Listener
        Start = (Button)findViewById(R.id.Start);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        //Stop Button Listener
        Stop = (Button)findViewById(R.id.stop);
        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
        //Graph Definition
        graph = (GraphView) findViewById(R.id.graph);
        //Graph Properties Defined
        Viewport viewport = graph.getViewport();
        viewport.setScrollable(true);
        //Creating Input as LineGraphSeries before feeding the data into the graph
        input = new LineGraphSeries<>();
        input.setColor(Color.RED);
        input1 = new LineGraphSeries<>();
        input1.setColor(Color.GREEN);
        input2 = new LineGraphSeries<>();
        input2.setColor(Color.BLUE);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(40);
        graph.getViewport().setXAxisBoundsManual(true);


        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time");
        gridLabel.setVerticalAxisTitle("Accelerometer");


        /*
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(10);
        viewport.setScalable(true);
        viewport.setScalableY(true);
        viewport.setScrollable(true);
        viewport.setScrollableY(true);
        viewport.setMinY(0);
        */
    }

    //onResume() register the accelerometer for listening the events
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // clean current values
        displayCleanValues();
        // display the current x,y,z accelerometer values
        displayCurrentValues();

        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(slastX - event.values[0]);
        deltaY = Math.abs(slastY - event.values[1]);
        deltaZ = Math.abs(slastZ - event.values[2]);

        // if the change is below 2, it is just plain noise
        if (deltaX < 2)
            deltaX = 0;
        if (deltaY < 2)
            deltaY = 0;
    }

    public void displayCleanValues() {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
    }

    // display the current x,y,z accelerometer values
    public void displayCurrentValues() {
        currentX.setText(Float.toString(deltaX));
        currentY.setText(Float.toString(deltaY));
        currentZ.setText(Float.toString(deltaZ));
    }
    //-----------------------------------------------------------------------------

    public void LoadDB(){
        //Add new table for each patient
        myDb.addTable(text.getText().toString());
    }

    //Called on Start Button
    public void start() {
        //Feed input data to Graph
        graph.addSeries(input);
        graph.addSeries(input1);
        graph.addSeries(input2);

        //Run callback for runnable
        super.onStart();
        runn = new Runnable() {
            @Override
            public void run() {
                //input.resetData(generateData());

                lastX+=1d;//Incremental X value for the graph to keep scrolling
                myDb.insertData(text.getText().toString(),Double.toString(lastX), deltaX,deltaY,deltaZ);
                input.appendData(new DataPoint(lastX, deltaX), true, 1000);
                input1.appendData(new DataPoint(lastX, deltaY), true, 1000);
                input2.appendData(new DataPoint(lastX, deltaZ), true, 1000);
                hndlr.postDelayed(this, 250);
            }
        };
        hndlr.postDelayed(runn, 250);
    }

    public void stop()
    {
        hndlr.removeCallbacks(runn);
        super.onPause();
        graph.removeAllSeries();
    }
    //Random Data Generator whithin scale
    double rndInit = 5;
    private double rndGen() {
        return rndInit += rnd.nextDouble()*0.4;
    }


}