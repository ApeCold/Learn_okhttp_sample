package cn.bsd.learn.okhttp.sample;

import java.io.InputStream;

public interface CallbackListener {
    void onSuccess(InputStream inputStream);
    void onFailure();
}
