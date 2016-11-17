package com.lance.lcupdate.http;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.lance.lcupdate.callback.DownloadCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by qiang_xi on 2016/10/9 20:36.
 * 下载请求
 */

public class downloadRequest {
    private static final int downloadSuccess = 2;
    private static final int downloading = 3;
    private static final int downloadFailure = 4;

    private static DownloadCallback downloadCallback;//下载回调
    private static long timestamp;

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            switch (msg.what) {
                //apk文件下载中,1s回调一次
                case downloading:
                    downloadCallback.onProgress(data.getLong("currentLength"), data.getLong("fileLength"));
                    break;
                //apk文件下载成功
                case downloadSuccess:
                    downloadCallback.onDownloadSuccess((File) data.getSerializable("file"));
                    break;
                //apk文件下载失败
                case downloadFailure:
                    downloadCallback.onDownloadFailure((String) msg.obj);
                    break;
            }
        }
    };
    /**
     * 下载专用
     *
     * @param downloadPath 下载地址
     * @param filePath     文件存储路径
     * @param fileName     文件名
     * @param callback     下载回调
     */
    public static void download(@NonNull final String downloadPath, @NonNull final String filePath, @NonNull final String fileName, @NonNull final DownloadCallback callback) {
        downloadCallback = callback;
        new Thread() {
            @Override
            public void run() {
                super.run();
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(downloadPath);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int fileLength = connection.getContentLength();
                    // 文件总长度
                    input = connection.getInputStream();
                    File directory = new File(filePath);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    File file = new File(filePath, fileName);
                    //如果存在该文件,则删除
                    if (file.exists()) {
                        file.delete();
                    }
                    output = new FileOutputStream(file);
                    byte data[] = new byte[4096];
                    //当前下载进度
                    int current = 0;
                    int count;
                    Bundle bundle = new Bundle();
                    while ((count = input.read(data)) != -1) {
                        current += count;
                        output.write(data, 0, count);
                        if (fileLength > 0) {
                            //这里必须要进行消息发送间隔的约束,这里为1s发送一次,不然会发送大量的message,全部都在排着队,
                            //而这造成的后果就是当下载完成时,发送的下载完成message不能及时发送出去,导致
                            //软件已经下载完成,却不能及时回调下载完成方法,从而不能及时进行安装的bug
                            if (current < fileLength) {
                                if (System.currentTimeMillis() - timestamp < 1000) {
                                    continue;
                                }
                            }
                            timestamp = System.currentTimeMillis();
                            //这里需要一直new新的message,不然会报错
                            Message message = new Message();
                            message.what = downloading;
                            bundle.putLong("currentLength", current);
                            bundle.putLong("fileLength", fileLength);
                            message.setData(bundle);
                            handler.sendMessage(message);
                        }
                    }
                    Message message = new Message();
                    message.what = downloadSuccess;
                    bundle.putSerializable("file", file);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = new Message();
                    message.what = downloadFailure;
                    message.obj = e.toString();
                    handler.sendMessage(message);
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                        if (connection != null)
                            connection.disconnect();
                    } catch (IOException ignored) {
                    }
                }
            }
        }.start();
    }
}

