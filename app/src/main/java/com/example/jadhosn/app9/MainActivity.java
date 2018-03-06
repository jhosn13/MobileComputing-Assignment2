package com.example.jadhosn.app9;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;


public class MainActivity extends AppCompatActivity {


    private final Handler hndlr = new Handler();

    private Runnable runn;

    Button Start;
    Button Stop;

    GraphView graph;

    private static final Random rnd = new Random();
    public LineGraphSeries<DataPoint> input;

    private double lastX = 5d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(40);
        graph.getViewport().setXAxisBoundsManual(true);


        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time");
        gridLabel.setVerticalAxisTitle("ECG");

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
    //Called on Start Button
    public void start() {
        //Feed input data to Graph
        graph.addSeries(input);
        //Run callback for runnable
        super.onStart();
        runn = new Runnable() {
            @Override
            public void run() {
                //input.resetData(generateData());

                lastX+=1d;//Incremental X value for the graph to keep scrolling
                input.appendData(new DataPoint(lastX, rndGen()), true, 100);
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