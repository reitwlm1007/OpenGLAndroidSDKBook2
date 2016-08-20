package com.example.rexia7.openglandroidsdkbook2;

import android.app.ActionBar;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
    private Button mRetryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Global.mainActivity = this;

        final MyRenderer renderer = new MyRenderer(this);
        MyGLSurfaceView glSurfaceView = new MyGLSurfaceView(this);
        glSurfaceView.setRenderer(renderer);
        setContentView(glSurfaceView);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity =
                Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        params.setMargins(0, 300, 0, 0);

        this.mRetryButton = new Button(this);
        this.mRetryButton.setText("Retry");
        hideRetryButton();
        addContentView(mRetryButton, params);

        this.mRetryButton.setOnClickListener(
                new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        hideRetryButton();
                        renderer.startNewGame();
                    }
                }
        );
    }

    public void showRetryButton() {
        mRetryButton.setVisibility(View.VISIBLE);
    }

    public void hideRetryButton() {
        mRetryButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();

        TextureManager.deleteAll(Global.gl);
    }
}
