package com.example.rexia7.openglandroidsdkbook2;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.Log;

import java.util.Random;
import java.util.concurrent.RunnableFuture;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by RexiA7 on 2016/08/12.
 */
public class MyRenderer implements GLSurfaceView.Renderer {

    public static final int TARGET_NUM = 10;
    public static final int GAME_INTERVAL = 60;

    private Context mContext;
    private int mWidth;
    private int mHeight;
    private int mBgTexture;
    private int mTargetTexture;
    private int mNumberTexture;
    private int mGameOverTexture;

    private MyTarget[] mTargets = new MyTarget[TARGET_NUM];
    private int mScore;
    private long mStartTime;
    private boolean mGameOverFlag;
    private Handler mHandler = new Handler();

    public MyRenderer(Context context) {
        this.mContext = context;
        startNewGame();
    }

    public void startNewGame() {
        Random rand = Global.rand;
        for (int i = 0; i < TARGET_NUM; i++) {
            float x = rand.nextFloat() * 2.0f - 1.0f;
            float y = rand.nextFloat() * 2.0f - 1.0f;
            float angle = rand.nextInt(360);
            float size = rand.nextFloat() * 0.25f + 0.25f;
            float speed = rand.nextFloat() * 0.01f + 0.01f;
            float turnAngle = rand.nextFloat() * 4.0f - 2.0f;
            mTargets[i] = new MyTarget(x, y, angle, size,
                    speed, turnAngle);
        }

        this.mScore = 0;
        this.mStartTime = System.currentTimeMillis();
        this.mGameOverFlag = false;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        Global.gl = gl;
        loadTextures(gl);
    }


    private void loadTextures(GL10 gl) {

        Resources res = mContext.getResources();
        this.mBgTexture =
                GraphicUtil.loadTexture(gl, res, R.drawable.circuit);
        if (mBgTexture == 0) {
            Log.e(getClass().toString(), "load texture error! circuit");
        }

        this.mTargetTexture =
                GraphicUtil.loadTexture(gl, res, R.drawable.fly);
        if (mTargetTexture == 0) {
            Log.e(getClass().toString(), "load texture error! fly");
        }

        this.mNumberTexture =
                GraphicUtil.loadTexture(gl, res, R.drawable.number_texture);
        if (mNumberTexture == 0) {
            Log.e(getClass().toString(),
                    "load texture error! number_texture");
        }

        this.mGameOverTexture = GraphicUtil.loadTexture(gl,
                res, R.drawable.game_over);
        if (mGameOverTexture == 0) {
            Log.e(getClass().toString(),
                    "load texture error! game_over");
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glViewport(0, 0, mWidth, mHeight);
        gl.glMatrixMode(GL10.GL_PROJECTION);

        gl.glLoadIdentity();
        gl.glOrthof(-1.0f, 1.0f, -1.5f, 1.5f, 0.5f, -0.5f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        renderMain(gl);
    }

    public void touched(float x, float y) {
        Log.i(getClass().toString(),
                String.format("touched! x = %f, y = %f", x, y));
        MyTarget[] targets = mTargets;
        if (!mGameOverFlag) {
            for (int i = 0; i < TARGET_NUM; i++) {
                if (targets[i].isPointInside(x, y)) {
                    float dist = 2.0f;
                    float theta =
                            Global.rand.nextFloat() * 360.0f / 180.0f * (float) Math.PI;
                    targets[i].mX = (float) Math.cos(theta) * dist;
                    targets[i].mY = (float) Math.sin(theta) * dist;
                    Log.i(getClass().toString(), "Hit!");

                    mScore += 100;
                    Log.i(getClass().toString(), "score = " + mScore);
                }
            }
        }

    }

    public void renderMain(GL10 gl) {
        Random rand = Global.rand;
        MyTarget[] targets = mTargets;

        for (int i = 0; i < TARGET_NUM; i++) {
            if (rand.nextInt(100) == 0) {
                targets[i].mTurnAngle = rand.nextFloat() * 4.0f - 2.0f;
            }

            targets[i].mAngle += targets[i].mTurnAngle;
            targets[i].move();
        }


        GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 2.0f, 3.0f,
                mBgTexture, 1.0f, 1.0f, 1.0f, 1.0f);

        for (int i = 0; i < TARGET_NUM; i++) {
            targets[i].draw(gl, mTargetTexture);
        }

        GraphicUtil.drawNumbers(gl, -0.5f, 1.25f, 0.125f, 0.125f,
                mNumberTexture, mScore, 8, 1.0f, 1.0f, 1.0f, 1.0f);

        int passedTime =
                (int) (System.currentTimeMillis() - mStartTime) / 1000;
        int remainTime = GAME_INTERVAL - passedTime;
        if (remainTime <= 0) {
            remainTime = 0;
            mGameOverFlag = true;
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    Global.mainActivity.showRetryButton();
                }
            });
        }

        GraphicUtil.drawNumbers(gl, 0.5f, 1.2f, 0.4f, 0.4f,
                mNumberTexture, remainTime, 2, 1.0f, 1.0f, 1.0f, 1.0f);

        if (mGameOverFlag) {
            GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 2.0f, 0.5f,
                    mGameOverTexture, 1.0f, 1.0f, 1.0f, 1.0f);
        }
    }


}
