package com.jpprade.anagrammes;


import com.jpprade.anagrammes.dico.ApplicationState;
import com.jpprade.anagrammes.listener.ProgressListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class Anagrammes extends Activity implements OnClickListener, OnKeyListener, TextWatcher {
	
	private int beforeLength;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ImageButton b = (ImageButton)findViewById(R.id.buttonSearch);
        b.setOnClickListener(this);
        
        EditText et = (EditText)findViewById(R.id.searchtxt);
        et.setOnKeyListener(this);
        //et.setOnEditorActionListener(this);
        et.addTextChangedListener(this);
        
        SeekBar sbmin = (SeekBar)findViewById(R.id.SeekBarMin);
		SeekBar sbmax = (SeekBar)findViewById(R.id.SeekBarMax);
		TextView tvmin = (TextView)findViewById(R.id.textmin);		
		TextView tvmax = (TextView)findViewById(R.id.textmax);
		ProgressListener dual = new ProgressListener(tvmin,tvmax,sbmin,sbmax);
		
		sbmin.setOnSeekBarChangeListener(dual);
		sbmax.setOnSeekBarChangeListener(dual);
		tvmin.setText(""+sbmin.getProgress());		
		tvmax.setText(""+sbmax.getProgress());
    }
    
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.buttonSearch){
			EditText et = (EditText)findViewById(R.id.searchtxt);
			ApplicationState.setSearched(et.getText().toString());
			Intent intent = new Intent(this, SearchResult.class);
			startActivity(intent);
		}
		
	}
	
	
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.searchtxt){
			EditText et = (EditText)findViewById(R.id.searchtxt);
			ApplicationState.setSearched(et.getText().toString());
			SeekBar sbmin = (SeekBar)findViewById(R.id.SeekBarMin);
			SeekBar sbmax = (SeekBar)findViewById(R.id.SeekBarMax);
			
			if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
				Intent intent = new Intent(this, SearchResult.class);
				startActivity(intent);
			}else if((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_DEL){
				/*Log.d("onKey","Del press " + v.getClass().getName());
				sbmin.setProgress(sbmin.getProgress() - 1);
				sbmax.setProgress(sbmax.getProgress() - 1);
				sbmin.setMax(et.getText().toString().length() - 1);
				sbmax.setMax(sbmax.getMax() - 1);*/
			}else if((event.getAction() == KeyEvent.ACTION_UP) ){
				/*Log.d("onKey","Del press " + v.getClass().getName());
				Log.d("onKey","Other press AVANT min="  + sbmin.getProgress() + "/" + sbmin.getMax() + " max"  + sbmax.getProgress() + "/" + sbmax.getMax());
				sbmin.setMax(sbmin.getMax() + 1);
				sbmax.setMax(sbmax.getMax() + 1);
				sbmin.setProgress(sbmin.getProgress() + 1);
				sbmax.setProgress(sbmax.getProgress() + 1);
				Log.d("onKey","Other press APRES min="  + sbmin.getProgress() + "/" + sbmin.getMax() + " max"  + sbmax.getProgress() + "/" + sbmax.getMax());
				*/
				
			}
			
			/*if((event.getAction() == KeyEvent.ACTION_UP) ){
				Log.d("onKey","Action up " + et.getText().toString());
			}
			
			if((event.getAction() == KeyEvent.ACTION_DOWN) ){
				Log.d("onKey","Action down " + et.getText().toString());
			}*/
			
			
			
			
		}
		return false;
	}

	@Override
	public void afterTextChanged(Editable et) {		
		SeekBar sbmin = (SeekBar)findViewById(R.id.SeekBarMin);
		SeekBar sbmax = (SeekBar)findViewById(R.id.SeekBarMax);
		Log.d("afterTextChanged","AVANT min="  + sbmin.getProgress() + "/" + sbmin.getMax() + " max"  + sbmax.getProgress() + "/" + sbmax.getMax());
		int l = et.toString().length();
		
		sbmin.setMax(l);
		sbmax.setMax(l);
		
		if(sbmin.getProgress() > l ){
			sbmin.setProgress(l);
		}
		if(sbmax.getProgress() > l ){
			sbmax.setProgress(l);
		}		
		
		
		if(l > beforeLength){
			sbmin.setProgress(sbmin.getProgress() + ( l - beforeLength));
			sbmax.setProgress(sbmax.getProgress() + ( l - beforeLength));
		}
		
		TextView tvmin = (TextView)findViewById(R.id.textmin);
		tvmin.setText(""+sbmin.getProgress());
		TextView tvmax = (TextView)findViewById(R.id.textmax);
		tvmax.setText(""+sbmax.getProgress());
		
		Log.d("afterTextChanged","APRES min="  + sbmin.getProgress() + "/" + sbmin.getMax() + " max"  + sbmax.getProgress() + "/" + sbmax.getMax());
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		beforeLength = arg0.length();
		Log.d("beforeTextChanged","Avant min=" + arg0.toString());
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public boolean onEditorAction(TextView v, int actionid, KeyEvent event) {
		//Log.d("onEditorAction",v.getClass().getName() + " action id =" +  actionid + " event =" + event);
		return false;
	}*/
}