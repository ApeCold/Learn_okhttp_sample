package cn.bsd.learn.okhttp.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

//    public String url = "http://v.juhe.cn/historyWeather/citys?province_id=2&key=bb52107206585ab074f5e59a8c73875b";
    private String url ="http://xxxxxxxxxxxxx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Okhttp.newQust(url,params,new CallbackListener<User>(){
//            onSuccess(User user);
//        }){
//
//        }
        sendRequest();
    }

    private void sendRequest() {
        LearnOkHttp.sendJsonRequest(url, null, ResponseClass.class, new IJsonDataListener() {
            @Override
            public void onSuccess(Object m) {
                Log.e("===》 ",m.toString());
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
