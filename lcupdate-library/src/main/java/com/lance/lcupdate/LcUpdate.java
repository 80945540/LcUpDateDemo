package com.lance.lcupdate;

import android.content.Context;
import android.os.Environment;

import com.lance.lcupdate.dialog.ForceUpdateDialog;
import com.lance.lcupdate.dialog.UpdateDialog;

import java.io.IOException;

/**
 * Created by Administrator on 2016/11/16.
 */

public class LcUpdate {

    public static class Builder {
        private String title="我要好志愿";//标题
        private String newAppSize="8M";//新app大小
        private String newAppVersionName="1.0";//新app版本名
        private String newAppUpdateDesc="";//新app更新描述
        private String newAppReleaseTime="2016-11-16 14:49:52";//新app发布时间
        private String newAppUrl="";//新app下载地址
        private String AppName="新版程序下载中";
        private boolean isForceUpdate=false;//是否强制更新
        private Context context;
        public Builder(Context context) {
            this.context=context;
        }

        public Builder Title(String title) {
            this.title = title;
            return this;
        }

        public Builder AppSize(String newAppSize) {
            this.newAppSize = newAppSize;
            return this;
        }

        public Builder AppVersionName(String newAppVersionName) {
            this.newAppVersionName = newAppVersionName;
            return this;
        }

        public Builder AppUpdateDesc(String newAppUpdateDesc) {
            this.newAppUpdateDesc = newAppUpdateDesc;
            return this;
        }

        public Builder AppName(String appName) {
            AppName = appName;
            return this;
        }

        public Builder AppReleaseTime(String newAppReleaseTime) {
            this.newAppReleaseTime = newAppReleaseTime;
            return this;
        }

        public Builder AppUrl(String newAppUrl) {
            this.newAppUrl = newAppUrl;
            return this;
        }

        public Builder isForceUpdate(boolean isForceUpdate) {
            this.isForceUpdate = isForceUpdate;
            return this;
        }

        public void build(){
            if(isForceUpdate){
                ForceUpdateDialog mForceUpdateDialog = new ForceUpdateDialog(context);
                mForceUpdateDialog.setAppSize(newAppSize)
                        .setDownloadUrl(newAppUrl)
                        .setTitle(title)
                        .setReleaseTime(newAppReleaseTime)
                        .setVersionName(newAppVersionName)
                        .setUpdateDesc(newAppUpdateDesc)
                        .setFileName(getFileName(newAppUrl))
                        .setFilePath(Environment.getExternalStorageDirectory().getPath() + "/youzytb").show();
            }else{
                UpdateDialog mUpdateDialog = new UpdateDialog(context);
                mUpdateDialog.setAppSize(newAppSize)
                        .setDownloadUrl(newAppUrl)
                        .setTitle(title)
                        .setReleaseTime(newAppReleaseTime)
                        .setVersionName(newAppVersionName)
                        .setUpdateDesc(newAppUpdateDesc)
                        .setFileName(getFileName(newAppUrl))
                        .setFilePath(Environment.getExternalStorageDirectory().getPath() + "/youzytb")
                        //该方法需设为true,才会在通知栏显示下载进度,默认为false,即不显示
                        //该方法只会控制下载进度的展示,当下载完成或下载失败时展示的通知不受该方法影响
                        //即不管该方法是置为false还是true,当下载完成或下载失败时都会在通知栏展示一个通知
                        .setShowProgress(true)
                        .setIconResId(R.drawable.icon_downloading)
                        .setAppName(AppName).show();
            }
        }
    }
    /**
     * 得到文件的名称
     * @return
     * @throws IOException
     */
    public static  String getFileName(String url)
    {
        String name= null;
        try {
            name = url.substring(url.lastIndexOf("/")+1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
}
