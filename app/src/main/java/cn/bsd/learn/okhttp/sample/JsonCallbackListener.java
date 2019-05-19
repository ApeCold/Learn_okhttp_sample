package cn.bsd.learn.okhttp.sample;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonCallbackListener<T> implements CallbackListener {
    private Class<T> requestClass;
    private IJsonDataListener mJsonDataListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public JsonCallbackListener(Class<T> reponseClass,IJsonDataListener jsonDataListener) {
        this.requestClass = reponseClass;
        this.mJsonDataListener = jsonDataListener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {
        //将流转换成responseClass
        String response = getContent(inputStream);
        final T clazz = JSON.parseObject(response,requestClass);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mJsonDataListener.onSuccess(clazz);
            }
        });
    }

    @Override
    public void onFailure() {

    }

    private String getContent(InputStream inputStream) {
        String content=null;
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try{
                while ((line=reader.readLine())!=null){
                    sb.append(line+"\n");
                }
            } catch (IOException e){
                System.out.println("Error="+e.toString());
            } finally {
                try {
                    inputStream.close();
                }catch (IOException e){
                    System.out.println("Error="+e.toString());
                }
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return content;
    }
}
