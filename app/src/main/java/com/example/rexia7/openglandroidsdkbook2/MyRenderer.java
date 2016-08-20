package com.example.rexia7.openglandroidsdkbook2;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiConfiguration;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by RexiA7 on 2016/08/12.
 */
public class MyRenderer implements GLSurfaceView.Renderer {

    private Context mContext;
    private int mWidth;
    private int mHeight;
    private int mBgTexture;
    private int mTargetTexture;

    private float mTargetAngle;
    private float mTargetX, mTargetY;
    private float mTargetSize;
    private float mTargetSpeed;
    private float mTargetTurnAngle;

    public MyRenderer(Context context){
        this.mContext = context;
        this.mTargetAngle = 30.0f;

        this.mTargetX = 0.5f;
        this.mTargetY = -0.5f;
        this.mTargetSize = 0.5f;
        this.mTargetSpeed = 0.01f;
        this.mTargetTurnAngle = 1.0f;

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


    private void loadTextures(GL10 gl){

        Resources res = mContext.getResources();
        this.mBgTexture =
                GraphicUtil.loadTexture(gl, res, R.drawable.circuit);
        if(mBgTexture == 0){
            Log.e(getClass().toString(), "load texture error! circuit");
        }

        this.mTargetTexture =
                GraphicUtil.loadTexture(gl, res, R.drawable.fly);
        if(mTargetTexture == 0){
            Log.e(getClass().toString(), "load texture error! fly");
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glViewport(0, 0, mWidth,mHeight);
        gl.glMatrixMode(GL10.GL_PROJECTION);

        gl.glLoadIdentity();
        gl.glOrthof(-1.0f,1.0f,-1.5f,1.5f,0.5f,-0.5f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        renderMain(gl);
    }

    public void touched(float x, float y) {
        Log.i(getClass().toString(),
                String.format("touched! x = %f, y = %f", x, y));

        float dx = x - mTargetX;
        float dy = y - mTargetY;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);

        if(distance <= mTargetSize * 0.5f){
            Log.i(getClass().toString(), "Hit!");
        }

    }

    public void renderMain(GL10 gl){
        Random rand = Global.rand;
        if(rand.nextInt(100) == 0){
            mTargetTurnAngle = rand.nextFloat() * 4.0f -2.0f;
        }
        mTargetAngle += mTargetTurnAngle;
        float theta = mTargetAngle / 180.0f * (float)Math.PI;
        mTargetX = mTargetX + (float)Math.cos(theta) * mTargetSpeed;
        mTargetY = mTargetY + (float)Math.sin(theta) * mTargetSpeed;

        if(mTargetX >= 2.0f) mTargetX -= 4.0f;
        if(mTargetX <= -2.0f) mTargetX += 4.0f;
        if(mTargetY >= 2.5f) mTargetY -= 5.0f;
        if(mTargetY <= -2.5f) mTargetY += 5.0f;
        
        
        GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 2.0f, 3.0f,
                mBgTexture, 1.0f, 1.0f, 1.0f, 1.0f);
        gl.glPushMatrix();
        {
            gl.glTranslatef(mTargetX, mTargetY, 0.0f);
            gl.glRotatef(mTargetAngle, 0.0f, 0.0f, 1.0f);
            gl.glScalef(mTargetSize, mTargetSize, 1.0f);
            GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 1.0f, 1.0f,
                    mTargetTexture, 1.0f, 1.0f, 1.0f, 1.0f);
        }
        gl.glPopMatrix();

    }




}
