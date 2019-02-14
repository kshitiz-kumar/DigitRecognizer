package com.example.digitrecognizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.tensorflow.lite.Interpreter;

import com.example.digitrecognizer.views.DrawModel;
import com.example.digitrecognizer.views.DrawView;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.graphics.PointF;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener  {

    private static final int Pixel_Width=28;

    //User Interface

    DrawView draw;
    Button clearBtn,classBtn;
    TextView resText;

    //views
    private DrawView drawView;
    private DrawModel drawModel;
    private PointF mTmpPiont = new PointF();

    private float mLastX;
    private float mLastY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawView) findViewById(R.id.draw);

        drawModel = new DrawModel(Pixel_Width,Pixel_Width);

        drawView.setmodel(drawModel);
        drawView.setOnTouchListener(this);

        clearBtn = (Button) findViewById(R.id.btn_clear);
        clearBtn.setOnClickListener(this);

        classBtn =(Button) findViewById(R.id.btn_class);
        classBtn.setOnClickListener(this);

        resText = (TextView) findViewById(R.id.tfRes);

        //loadmodel();
    }

    protected void onResume()
    {
        drawView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        drawView.onPause();
        super.onPause();
    }


    /*private void loadModel(){

    }*/


    public void onClick(View view)
    {
     if(view.getId() == R.id.btn_clear)
     {
        drawModel.clear();
        drawView.reset();
        drawView.invalidate();

        resText.setText("");
     }

     else if(view.getId() == R.id.btn_class)
        {
           float pixels[] = drawView.getPixelData();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int action = event.getAction() & MotionEvent.ACTION_MASK;

        if(action == MotionEvent.ACTION_DOWN)
        {
            processTouchDown(event);
            return true;
        }

        if(action == MotionEvent.ACTION_MOVE)
        {
            processTouchMove(event);
            return true;
        }

        else if(action == MotionEvent.ACTION_UP)
        {
            processTouchUp(event);
            return true;
        }

        return false;
    }

    private void processTouchDown(MotionEvent event) {

        mLastX = event.getX();
        mLastY = event.getY();

        drawView.calcPos(mLastX,mLastY,mTmpPiont);

        float lastConvX = mTmpPiont.x;
        float lastConvY = mTmpPiont.y;

        drawModel.startLine(lastConvX,LastConvY);
    }

    private void processTouchMove(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        drawView.calcPos(x, y, mTmpPiont);
        float newConvX = mTmpPiont.x;
        float newConvY = mTmpPiont.y;
        drawModel.addLineElem(newConvX, newConvY);

        mLastX = x;
        mLastY = y;
        drawView.invalidate();
    }

    private void processTouchUp() {
        drawModel.endLine();
    }

}
