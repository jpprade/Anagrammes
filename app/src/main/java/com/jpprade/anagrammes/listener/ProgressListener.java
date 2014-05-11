package com.jpprade.anagrammes.listener;

import com.jpprade.anagrammes.SearchResult;
import com.jpprade.anagrammes.dico.ApplicationState;

import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ProgressListener implements OnSeekBarChangeListener {
	
	private TextView tvMin;
	private TextView tvMax;
	
	private SeekBar seekBarMin;
	private SeekBar seekBarMax;
	
	private static String LOG_TAG = SearchResult.class.getName();
	
	public ProgressListener(TextView tvMin,TextView tvMax,SeekBar seekBarMin,SeekBar seekBarMax){
		this.tvMin=tvMin;
		this.tvMax=tvMax;
		this.seekBarMax= seekBarMax;
		this.seekBarMin=seekBarMin;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {		
		if(seekBar == seekBarMax){
			Log.d(LOG_TAG,"ProgessChanged Max");
			int mp = seekBar.getProgress();
			seekBarMin.setProgress(Math.min(mp, seekBarMin.getProgress()));
		}
		if(seekBar == seekBarMin){
			Log.d(LOG_TAG,"ProgessChanged Min");
			int mp = seekBar.getProgress();
			seekBarMax.setProgress(Math.max(mp, seekBarMax.getProgress()));
		}
		
		ApplicationState.setMinLetter(seekBarMin.getProgress());
		ApplicationState.setMaxLetter(seekBarMax.getProgress());
			
		tvMin.setText(""+seekBarMin.getProgress());
		tvMax.setText(""+seekBarMax.getProgress());
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

}
