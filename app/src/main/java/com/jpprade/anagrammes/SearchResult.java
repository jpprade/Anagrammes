package com.jpprade.anagrammes;

import java.util.List;

import com.jpprade.anagrammes.dico.ApplicationState;
import com.jpprade.anagrammes.threads.Search;



import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SearchResult extends Activity {
	
	private static String LOG_TAG = SearchResult.class.getName();
	private ProgressDialog myProgressDialog = null; 
	
	public ProgressDialog getMyProgressDialog() {
		return myProgressDialog;
	}

	public void setMyProgressDialog(ProgressDialog myProgressDialog) {
		this.myProgressDialog = myProgressDialog;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        ListView lv = (ListView) findViewById(R.id.resultlist);
        String searched = ApplicationState.getSearched();
        TextView nbres  = (TextView) findViewById(R.id.nbresult);
        
        myProgressDialog = new ProgressDialog(this);
        myProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        myProgressDialog.setMessage("Recherche...");
        myProgressDialog.setCancelable(false);
        myProgressDialog.show();
        
        LinearLayout ll = (LinearLayout)findViewById(R.id.mainResultLayout);
        //ll.setBackgroundDrawable(new CrossBG());
        
        Search s = new Search(this,lv,searched);
        s.setTv(nbres);
        Log.d(LOG_TAG,"Launching thread");
        ApplicationState.run(s);
        Log.d(LOG_TAG,"thread Launched");
        
        
    }

}
