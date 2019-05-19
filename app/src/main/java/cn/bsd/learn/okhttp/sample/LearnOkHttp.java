package cn.bsd.learn.okhttp.sample;

public class LearnOkHttp {
    public static<T,M> void sendJsonRequest(String url,T requestData,Class<M> response,IJsonDataListener listener){
        IHttpRequest httpRequest = new JsonHttpRequest();
        CallbackListener callbackListener = new JsonCallbackListener<>(response,listener);
        HttpTask httpTask = new HttpTask(url,requestData,httpRequest,callbackListener);
        ThreadPoolManager.getInstance().addTask(httpTask);
    }
}
