package cn.bsd.learn.okhttp.sample;

public interface IJsonDataListener <T>{
    void onSuccess(T m);
    void onFailure();
}
