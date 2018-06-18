package com.github.baby.owspace.app;

import android.app.Application;
import android.content.Context;

import com.github.baby.owspace.BuildConfig;
import com.github.baby.owspace.R;
import com.github.baby.owspace.di.components.DaggerNetComponent;
import com.github.baby.owspace.di.components.NetComponent;
import com.github.baby.owspace.di.modules.NetModule;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.LogcatLogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.text.SimpleDateFormat;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/7/21
 * Owspace
 */
public class OwspaceApplication extends Application{

    private static OwspaceApplication instance;

    public static OwspaceApplication get(Context context){
        return (OwspaceApplication)context.getApplicationContext();
    }

    private NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initLogger();
        initNet();
        initDatabase();
        initTypeFace();
    }
    private void initTypeFace() {
        CalligraphyConfig calligraphyConfig =new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/PMingLiU.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
        CalligraphyConfig.initDefault(calligraphyConfig);
    }

    private void initLogger(){
        LogStrategy customLog = new LogcatLogStrategy();
        FormatStrategy strategy =  PrettyFormatStrategy.newBuilder().showThreadInfo(false)  // 是否显示线程信息，默认为ture
                .methodCount(2)         // 显示的方法行数，默认为2
                .methodOffset(7)        // 隐藏内部方法调用到偏移量，默认为5
                .logStrategy(customLog) // 更改要打印的日志策略。
                .tag("My liwei tag")   // 每个日志的全局标记。默认PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(strategy){
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FormatStrategy disStrategy = CsvFormatStrategy.newBuilder().dateFormat(format).tag("log file by liwei").build();
        Logger.addLogAdapter(new DiskLogAdapter(disStrategy));

//        LogLevel logLevel;
//        if (!BuildConfig.API_ENV){
//           logLevel = LogLevel.FULL;
//        }else{
//            logLevel = LogLevel.NONE;
//        }
//        Logger.addLogAdapter();
//        Logger.init("GithubOwspace")                 // default PRETTYLOGGER or use just init()
//                .methodCount(3)                 // default 2
//                .logLevel(logLevel) ;       // default LogLevel.FULL
    }
    private void initNet(){
        netComponent = DaggerNetComponent.builder()
                .netModule(new NetModule())
                .build();
    }
    private void initDatabase(){

    }

    public NetComponent getNetComponent() {
        return netComponent;
    }

    public static OwspaceApplication getInstance() {
        return instance;
    }
}
