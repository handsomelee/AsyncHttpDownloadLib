package indi.liji.common.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;
import indi.liji.common.download.DownloadFileManager.Configuration;
import indi.liji.common.util.FileOperateUtil;

public class DownloadFileTask implements Runnable{

    private static final String TAG = DownloadFileTask.class.getSimpleName();
    private static final int BUFFER_SIZE = 4 * 1024; 
    
    //final 关键字声明的成员变量，可以在构造方法里面初始化
    private final DownloadFileManager  mDLManager;
    private final byte[] mBuffer;
    private final Task   mTask;
    private final int    mConnectTimeoutMills;
    private final int    mReadTimeoutMills;
    private final int    mNotifyProgressInterVal;
    private volatile boolean mCancelled = false;
    
    public DownloadFileTask(DownloadFileManager manager,Task task,Configuration config){
    	this.mTask = task;
    	this.mBuffer = new byte[BUFFER_SIZE];
    	this.mDLManager = manager;
    	if (config != null){
    		this.mReadTimeoutMills = config.readTimeout;
    		this.mConnectTimeoutMills = config.connectTimeout;
    		this.mNotifyProgressInterVal = config.notifyProgressInterVal;
    	}else{
    		this.mReadTimeoutMills = Configuration.DEFAULT_READ_TIMEOUT;
    		this.mConnectTimeoutMills = Configuration.DEFAULT_CONNECT_TIMEOUT;
    		this.mNotifyProgressInterVal = Configuration.DEFAULT_NOTIFY_INTERVALUE;
    	}
    }
    
	@Override
	public void run() {
		String name = Thread.currentThread().getName();
		Log.d(TAG, name + "run() start");
		
		Task task = mTask;
		String urlStr = task.url;
		URL url = null;
		HttpURLConnection conn = null;
		RandomAccessFile raf;
		byte[] buf = mBuffer;
		try {
			url = new URL(urlStr);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestProperty("Range", "bytes=" + task.downloadedSize + "-");
			conn.setConnectTimeout(this.mConnectTimeoutMills);
			conn.setReadTimeout(this.mReadTimeoutMills);
			int responseCode = conn.getResponseCode();
			Log.d(TAG, "responseCode = " + responseCode);
			if (responseCode == HttpResponseCode.OK || responseCode == HttpResponseCode.PARTIAL_CONTENT){
				DownloadFileManager manager = mDLManager;
				if (task.fileSize == Task.NO_INIT_SIZE){
					//有面试官说，通过获取文件的大小 contentLengh,有问题
					task.fileSize =  conn.getContentLength(); 
				}
				task.status = Task.STATUS_RUNNABLE;
				manager.updateTaskInfo(task.ID, task);
				manager.notifyTaskStart(task.url);
				
				BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
				int len;
				raf = getRandomAccessFile(task);
				long last = System.currentTimeMillis();
				long current = 0L;
				while ((len = bis.read(buf)) != -1 && !mCancelled){
					raf.write(buf, 0, len);
					task.downloadedSize += len;
					manager.updateTaskInfo(task.ID, task);
					current = System.currentTimeMillis();
					if (current - last >= this.mNotifyProgressInterVal){
						manager.notifyTaskProgress(task.url, task.downloadedSize, task.fileSize);
					    last = current;
					}
				}
				
				if (mCancelled && task.downloadedSize != task.fileSize){
					task.status = Task.STATUS_STOPPED;
					manager.updateTaskInfo(task.ID, task);
					manager.notifyTaskStop(task.url);
					return;
				}
				
				if (task.downloadedSize == task.fileSize){
					task.status = Task.STATUS_FINISHED;
					manager.updateTaskInfo(task.ID, task);
					manager.notifyTaskFinished(task.url);
					return;
				}
			}else if (responseCode == HttpResponseCode.INTERNAL_SERVER_ERROR){
				mDLManager.notifyTaskError(task.url,task.errorCode,task.errorMsg);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			//Calls to disconnect() may return the socket to a pool of connected sockets
			if (conn != null){
			    conn.disconnect();
			}
		} 
		Log.d(TAG, name + "run() end");
	}
	
	private static final RandomAccessFile getRandomAccessFile(Task task) throws IOException{
		File file = FileOperateUtil.createFile(task.filePath);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		if (task.downloadedSize >= 0L){
			raf.seek(task.downloadedSize);
		}
		return raf;
	}
	
	public void cancel() {
		this.mCancelled = true;
	}
}
