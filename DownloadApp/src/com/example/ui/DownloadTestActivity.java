package com.example.ui;

import java.io.File;

import com.example.listviewdemo.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import indi.liji.common.download.DownloadFileManager;
import indi.liji.common.download.Request;
import indi.liji.common.download.TaskDBManager;

public class DownloadTestActivity extends Activity{
	
	private static final String TAG = DownloadTestActivity.class.getSimpleName();
	private ProgressBar mProgressBar0;
	private Button      mDownloadBtn0;
	private Button      mStopDownloadBtn0;
	
	private ProgressBar mProgressBar1;
	private Button      mDownloadBtn1;
	private Button      mStopDownloadBtn1;
	
	private Button      mDeleteBtn0;
	private Button      mDeleteBtn1;
	
	private static final String[]     URLs = 
		{"http://p.gdown.baidu.com/bdf28ce5e58bf35b13809f8c37646b6bf2de296b828e44af3a8bf8d9b121b1342fcfb7fc4063c24edbe7132d88fe435c7c607aded9f6202b0046019dcd218fcdbc05e0f1358cc71ac6c742bb8ce182aca92edf213dcfc3525b840052e2ab4cca",
		 "http://dlsw.baidu.com/sw-search-sp/soft/ce/12934/TencentVideo_V9.9.970.0_setup.1439545208.exe"};
	
	private DownloadTaskListener mDownloadListener0 = new DownloadTaskListener();
	private DownloadTaskListener1 mDownloadListener1 = new DownloadTaskListener1();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		
		TaskDBManager.init(this, false, "task.db");
		
		setUpUI();
	}
	
	private void setUpUI(){
		this.mDownloadBtn0 = (Button)findViewById(R.id.download0_start_button);
		this.mProgressBar0 = (ProgressBar)findViewById(R.id.download0_progressbar);
		this.mProgressBar0.setMax(100);
		this.mStopDownloadBtn0 = (Button)findViewById(R.id.download0_stop_button);
		OnDownButtonClickListener listener = new OnDownButtonClickListener();
		this.mDownloadBtn0.setOnClickListener(listener);
		this.mStopDownloadBtn0.setOnClickListener(listener);
		
		this.mProgressBar1 = (ProgressBar)findViewById(R.id.download1_progressbar);
		this.mDownloadBtn1 = (Button)findViewById(R.id.download1_start_button);
		this.mStopDownloadBtn1 = (Button)findViewById(R.id.download1_stop_button);
		
		this.mDownloadBtn1.setOnClickListener(listener);
		this.mStopDownloadBtn1.setOnClickListener(listener);
		
		this.mDeleteBtn0 = (Button)findViewById(R.id.download0_delete_button);
		this.mDeleteBtn1 = (Button)findViewById(R.id.download1_delete_button);
		
		this.mDeleteBtn0.setOnClickListener(listener);
		this.mDeleteBtn1.setOnClickListener(listener);
	}
	
	private void startDownload(int idx){
		Request request = new Request();
		request.requestUrl = URLs[idx];
		request.dstFilePath = getFilesDir().getAbsolutePath() + File.separator + idx +".file";
		if (idx == 0){
			DownloadFileManager.getInstance().registDownloadTaskListener(URLs[idx], mDownloadListener0);
		}else if (idx == 1){
			DownloadFileManager.getInstance().registDownloadTaskListener(URLs[idx], mDownloadListener1);
		}
		DownloadFileManager.getInstance().downloadFile(request);
	}
	
	private void stopDownload(int idx){
		DownloadFileManager.getInstance().stopDownload(URLs[idx]);	
    }
	
	private void deleteDownload(int idx){
		DownloadFileManager.getInstance().deleteTaskAndFileIfExist(URLs[idx]);
	}

	class OnDownButtonClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			Log.d(TAG, "onClick()");
			int viewID = v.getId();
			if (viewID == R.id.download0_start_button){
				startDownload(0);
			}else if (viewID == R.id.download0_stop_button){
				stopDownload(0);
			}else if (viewID == R.id.download1_start_button){
				startDownload(1);
			}else if (viewID == R.id.download1_stop_button){
				stopDownload(1);
			}else if (viewID == R.id.download0_delete_button){
				deleteDownload(0);
			}else if (viewID == R.id.download1_delete_button){
				deleteDownload(1);
			}
		}
	}
	
	private class DownloadTaskListener implements indi.liji.common.download.DownloadTaskListener{

		@Override
		public void onTaskAdded() {
			Log.d(TAG, "onTaskAdded()");
			
		}

		@Override
		public void onTaskWaiting() {
			Log.d(TAG, "onTaskWaiting()");
			
		}

		@Override
		public void onTaskStart() {
			Log.d(TAG, "onTaskStart()");
			
		}

		@Override
		public void onTaskRunning(long curSize, long totalSize) {
			Log.d(TAG, String.format("onTaskRunning() [%d,%d]", curSize,totalSize));
			int progress = (int)(curSize * 100 / totalSize);
			DownloadTestActivity.this.mProgressBar0.setProgress(progress);
		}

		@Override
		public void onTaskStop() {
			Log.d(TAG, "onTaskStop()");
			
		}

		@Override
		public void onTaskFinished() {
			Log.d(TAG, "onTaskFinished()");
			
		}

		@Override
		public void onTaskError(int code, String msg) {
			Log.d(TAG, "onTaskError()");
			
		}
	}
	
	private class DownloadTaskListener1 implements indi.liji.common.download.DownloadTaskListener{

		@Override
		public void onTaskAdded() {
			Log.d(TAG, "onTaskAdded()");
			
		}

		@Override
		public void onTaskWaiting() {
			Log.d(TAG, "onTaskWaiting()");
			
		}

		@Override
		public void onTaskStart() {
			Log.d(TAG, "onTaskStart()");
			
		}

		@Override
		public void onTaskRunning(long curSize, long totalSize) {
			Log.d(TAG, String.format("onTaskRunning() [%d,%d]", curSize,totalSize));
			int progress = (int)(curSize * 100 / totalSize);
			DownloadTestActivity.this.mProgressBar1.setProgress(progress);
		}

		@Override
		public void onTaskStop() {
			Log.d(TAG, "onTaskStop()");
			
		}

		@Override
		public void onTaskFinished() {
			Log.d(TAG, "onTaskFinished()");
			
		}

		@Override
		public void onTaskError(int code, String msg) {
			Log.d(TAG, "onTaskError()");
			
		}
	}
}
