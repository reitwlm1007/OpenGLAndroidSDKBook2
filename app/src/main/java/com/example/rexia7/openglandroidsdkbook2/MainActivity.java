package com.example.rexia7.openglandroidsdkbook2;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        MyRenderer renderer = new MyRenderer(this);
        MyGLSurfaceView glSurfaceView = new MyGLSurfaceView(this);
        glSurfaceView.setRenderer(renderer);
        setContentView(glSurfaceView);
    }



    @Override
    public void onPause(){
        super.onPause();

        TextureManager.deleteAll(Global.gl);
    }
}
