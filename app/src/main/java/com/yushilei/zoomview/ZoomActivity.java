package com.yushilei.zoomview;

import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ZoomActivity extends AppCompatActivity implements View.OnTouchListener {

    private ImageView img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        img2 = (ImageView) findViewById(R.id.img_2);
        View view = findViewById(R.id.activity_zoom);
        view.setOnTouchListener(this);

    }

    float lastX;
    Matrix matrix;
    float scale = 1.0f;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                matrix = img2.getMatrix();
                break;
            case MotionEvent.ACTION_MOVE:
                float offset = x - lastX;

                float v1 = offset / v.getWidth();
                float sx = scale + v1;
                Log.i("ZoomActivity", "sx" + sx);
                Matrix matrix1=new Matrix(matrix);
                matrix1.setScale(sx, sx, img2.getWidth() / 2, img2.getHeight() / 2);
                img2.setImageMatrix(matrix1);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }
}
