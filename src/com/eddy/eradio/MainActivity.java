package com.eddy.eradio;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OutputFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	
	private MediaRecorder mediaRecorder;
	private String mCurrentAudioPath;
	private ImageButton startBtn;
	private ImageButton stopBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startBtn = (ImageButton) findViewById(R.id.start);
		stopBtn = (ImageButton) findViewById(R.id.stop);
		stopBtn.setEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void start(View v) {
		startBtn.setEnabled(false);
		startBtn.setVisibility(View.INVISIBLE);
		stopBtn.setEnabled(true);
		stopBtn.setVisibility(View.VISIBLE);
		
		
		try {
			mediaRecorder = new MediaRecorder();
			mediaRecorder.setAudioSource(AudioSource.MIC);
			mediaRecorder.setOutputFormat(OutputFormat.AMR_NB);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			
			File storageDir = new File(
					Environment.getExternalStoragePublicDirectory("ERadio"), 
					  "ERadio"
					);
			if(storageDir != null) {
				if(!storageDir.mkdirs()) {
					if (! storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
					}
				}
			}
			
			String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
			mCurrentAudioPath= storageDir.getAbsolutePath() + timeStamp + ".amr";
			System.out.println(mCurrentAudioPath);
			
			mediaRecorder.setOutputFile(mCurrentAudioPath);
			mediaRecorder.prepare();
			mediaRecorder.start();
		}
		catch (Exception e) {
			e.printStackTrace();
			startBtn.setEnabled(true);
			stopBtn.setEnabled(false);
		}
	}
	
	public void stop(View v) {
		startBtn.setEnabled(true);
		startBtn.setVisibility(View.VISIBLE);
		stopBtn.setEnabled(false);
		stopBtn.setVisibility(View.INVISIBLE);
		if(mediaRecorder != null) {
			mediaRecorder.stop();
			mediaRecorder.release();
		}
		mediaRecorder = null;
		galleryAddAudio();
	}

	private void galleryAddAudio() {
		Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(mCurrentAudioPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}
}
