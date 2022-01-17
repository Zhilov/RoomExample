package com.example.roomexample;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class Anim {

    boolean isMove = false;

    public void setOnTouchAnimListener(View view){
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        startScaleAnimation(view);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        cancelScaleAnimation(view);
                        isMove =true;
                        break;

                    case MotionEvent.ACTION_UP:
                        cancelScaleAnimation(view);

                        if (!isMove){
                            isMove = false;
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            },150);
                        }else {
                            isMove = false;
                        }

                        break;
                }
                return true;
            }
        });
    }
    private void startScaleAnimation(View view) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f);
        scaleDownX.setDuration(150);
        scaleDownY.setDuration(150);

        scaleDownX.start();
        scaleDownY.start();
    }

    private void cancelScaleAnimation(View view) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f);
        scaleDownX.setDuration(150);
        scaleDownY.setDuration(150);

        scaleDownX.start();
        scaleDownY.start();
    }
}
