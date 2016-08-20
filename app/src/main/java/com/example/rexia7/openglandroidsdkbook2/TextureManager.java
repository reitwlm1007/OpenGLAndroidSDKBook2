package com.example.rexia7.openglandroidsdkbook2;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by RexiA7 on 2016/08/12.
 */
public class TextureManager {
    private static Map<Integer, Integer> mTexttures =
            new Hashtable<Integer, Integer>();

    public static final void addTexture(int resId, int texId){
        mTexttures.put(resId, texId);
    }

    public static final void deleteTexture(GL10 gl, int resId){
        if(mTexttures.containsKey(resId)){
            int[] texId = new int[1];
            texId[0] = mTexttures.get(resId);
            gl.glDeleteTextures(1, texId, 0);
            mTexttures.remove(resId);
        }
    }

    public static final void deleteAll(GL10 gl){
        List<Integer> keys = new ArrayList<Integer>(mTexttures.keySet());
        for(Integer key : keys){
            deleteTexture(gl, key);
        }
    }
}
