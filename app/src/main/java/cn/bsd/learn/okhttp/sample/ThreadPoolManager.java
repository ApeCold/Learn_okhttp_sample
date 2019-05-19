package cn.bsd.learn.okhttp.sample;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {
    private static ThreadPoolManager threadPoolManager = new ThreadPoolManager();
    public static ThreadPoolManager getInstance(){
        return threadPoolManager;
    }

    //排序模式：先进先出
    private LinkedBlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<>();

    //将异步任务添加到队列中
    public void addTask(Runnable runnable){
        if(runnable!=null){
            try {
                mQueue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //创建延迟队列
    private DelayQueue<HttpTask> mDelayQueue = new DelayQueue<>();


    //将失败的任务添加到DelayQueue
    public void addDelayTask(HttpTask ht){
        if (ht!=null){
            ht.setDelayTime(3000);
            mDelayQueue.offer(ht);
        }
    }

    //创建线程池
    private ThreadPoolExecutor mThreadPoolExcutor;
    private ThreadPoolManager(){
        mThreadPoolExcutor = new ThreadPoolExecutor(3,10,15, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(4),
        new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                addTask(r);
            }
        });
        //线程池需要处理核心线程
        mThreadPoolExcutor.execute(coreThread);
        mThreadPoolExcutor.execute(delayThread);
    }

    //创建"核心"线程，不停的去队列中获取请求，并提交给线程池处理
    public Runnable coreThread = new Runnable() {
        Runnable runn = null;
        @Override
        public void run() {
            while (true){
                try {
                    runn = mQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mThreadPoolExcutor.execute(runn);
            }
        }
    };

    //创建延迟线程，不停的去延迟队列中取数据，跳给线程池处理
    public Runnable delayThread = new Runnable() {
        HttpTask ht = null;
        @Override
        public void run() {
            while (true){
                try {
                    ht= mDelayQueue.take();
                    if(ht.getRetryCount()<3){
                        mThreadPoolExcutor.execute(ht);
                        ht.setRetryCount(ht.getRetryCount()+1);
                        Log.e("==重试机制==",ht.getRetryCount()+"  "+System.currentTimeMillis());
                    }else{
                        Log.e("==重试机制==","总是执行失败，放弃你");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
