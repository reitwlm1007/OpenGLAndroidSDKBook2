package com.example.rexia7.openglandroidsdkbook2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by RexiA7 on 2016/08/12.
 */
public class GraphicUtil {

    private static final BitmapFactory.Options options =
            new BitmapFactory.Options();
    static{
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    }


    public static final FloatBuffer makeFloatBuffer(float[] arr){
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }


    public static final  void drawSquare(GL10 gl){
     drawSquare(gl, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f);
    }


    public static final void drawSquare(
            GL10 gl, float r, float g, float b, float a) {
       drawSquare(gl, 0.0f, 0.0f, r, g, b, a);
    }

    public static final void drawSquare(GL10 gl, float x,float y,
        float r, float g, float b, float a) {
        drawSquare(gl, x, y, 1.0f, 1.0f, r, g, b, a);
    }


    public static final void drawSquare(GL10 gl,
                                        float x,float y, float width, float height,
                                        float r, float g, float b, float a){
        float[] vertices = {
                -0.5f * width + x, -0.5f * height + y,
                0.5f * width + x, -0.5f * height + y,
                -0.5f * width + x, 0.5f * height + y,
                0.5f * width + x, 0.5f * height + y,
        };

        float[] colors = {
                r, g, b, a,
                r, g, b, a,
                r, g, b, a,
                r, g, b, a,
        };

        FloatBuffer squareVertices = makeFloatBuffer(vertices);
        FloatBuffer squareColors = makeFloatBuffer(colors);

        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, squareVertices);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, squareColors);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

    }



    public static final void drawCircle(GL10 gl,
                                        float x, float y,
                                        int divides, float radius,
                                        float r, float g, float b, float a){
        int vertexId = 0;
        float[] vertices = new float[divides * 3 * 2];
        for(int i= 0; i < divides; i++) {
            float theta1 = 2.0f / (float)divides * (float)i * (float)Math.PI;
            float theta2 = 2.0f / (float)divides * (float)(i + 1) * (float)Math.PI;
            vertices[vertexId++] = x;
            vertices[vertexId++] = y;
            vertices[vertexId++] =
                    (float) Math.cos((double) theta1) * radius + x;
            vertices[vertexId++] =
                    (float) Math.sin((double) theta1) * radius + y;
            vertices[vertexId++] =
                    (float) Math.cos((double) theta2) * radius + x;
            vertices[vertexId++] =
                    (float) Math.sin((double) theta2) * radius + y;
        }
        FloatBuffer polygonVertices = makeFloatBuffer(vertices);

        gl.glColor4f(r, g, b, a);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, polygonVertices);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, divides * 3);
    }

    public static final void drawTexture(GL10 gl,
                                         float x, float y, float width, float height, int texture,
                                         float r, float g, float b, float a){
        drawTexture(gl, x, y, width, height, texture, 0.0f, 0.0f, 1.0f,1.0f, r, g, b, a);
    }


    public static final void drawTexture(GL10 gl, float x, float y,
                                         float width, float height, int texture,
                                         float u, float v, float tex_w, float tex_h,
                                         float r, float g, float b, float a){
        float[] vertices = {
                -0.5f * width + x, -0.5f * height + y,
                0.5f * width + x, -0.5f * height + y,
                -0.5f * width + x, 0.5f * height + y,
                0.5f * width + x, 0.5f * height + y,
        };

        float[] colors = {
                r, g, b, a,
                r, g, b, a,
                r, g, b, a,
                r, g, b, a,
        };

        float[] coords = {
                u, v + tex_h,
                u + tex_w, v + tex_h,
                u, v,
                u + tex_w, v,
        };

        FloatBuffer squareVertices = makeFloatBuffer(vertices);
        FloatBuffer squareColors = makeFloatBuffer(colors);
        FloatBuffer texCoords = makeFloatBuffer(coords);

        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, squareVertices);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, squareColors);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoords);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }
    public static final void drawNumber(GL10 gl,
                                        float x, float y, float w, float h,
                                        int texture, int number,
                                        float r, float g, float b, float a){
        float u = (float)(number % 4) * 0.25f;
        float v = (float)(number / 4) * 0.25f;
        drawTexture(gl,
                x, y, w, h, texture, u, v, 0.25f, 0.25f,
                r, g, b, a);
    }
    public static final void drawNumbers(GL10 gl,
                                         float x, float y, float width, float height,
                                         int texture, int number, int figures,
                                         float r, float g, float b, float a){
        float totalWidth = width * (float)figures;
        float rightX = x + (totalWidth * 0.5f);
        float figlX = rightX - width * 0.5f;
        for(int i = 0; i < figures; i++){
            float figNX = figlX - (float)i * width;
            int numberToDraw = number % 10;
            number = number / 10;
            drawNumber(gl, figNX, y , width, height,
                    texture, numberToDraw,
                    1.0f, 1.0f, 1.0f, 1.0f);
        }
    }




    public static final int loadTexture(
            GL10 gl, Resources resources, int resId){

        int[] textures = new int[1];

        Bitmap bmp =
                BitmapFactory.decodeResource(resources, resId, options);
        if(bmp == null){
            return 0;
        }

        gl.glGenTextures(1, textures, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);

        bmp.recycle();
        TextureManager.addTexture(resId, textures[0]);
        return textures[0];
    }




}
