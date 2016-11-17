package com.lance.lcupdatedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lance.lcupdate.LcUpdate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void update(View view){
        new LcUpdate.Builder(this)
                .AppName(getResources().getString(R.string.app_name)+"下载中")
                .AppReleaseTime("2016-11-17")
                .AppSize("7.3M")
                .AppUpdateDesc("你爸会不会经常把是不错吧\n还是不是就好比是基本计划书\n就是不好手把手教会不是不是\n不好就不是不是比较少不是\n试试保护环境不是不是")
                .AppUrl("http://apptest.youzy.cn/apk/com_eagersoft_youzy_jg003_v1.23.apk")
                .AppVersionName("2.0")
                .isForceUpdate(false)
                .Title("系统检测到新版本")
                .build();
    }
    public void updateq(View view){
        new LcUpdate.Builder(this)
                .AppName(getResources().getString(R.string.app_name)+"下载中")
                .AppReleaseTime("2016-11-17")
                .AppSize("7.3M")
                .AppUpdateDesc("你爸会不会经常把是不错吧\n还是不是就好比是基本计划书\n就是不好手把手教会不是不是\n不好就不是不是比较少不是\n试试保护环境不是不是")
                .AppUrl("http://apptest.youzy.cn/apk/com_eagersoft_youzy_jg003_v1.23.apk")
                .AppVersionName("2.0")
                .isForceUpdate(true)
                .Title("系统检测到新版本")
                .build();
    }
}
