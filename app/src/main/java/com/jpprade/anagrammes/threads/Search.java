package com.jpprade.anagrammes.threads;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.jpprade.anagrammes.SearchResult;
import com.jpprade.anagrammes.adapter.ResultAdapter;
import com.jpprade.anagrammes.dico.*;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class Search implements Runnable {
	
	private static String LOG_TAG = Search.class.getName();
	private String searched;
	private SearchResult c;
	private ListView lv;
	private TextView tv;
	
	private ArrayList<String> anagrammes = new ArrayList<String>();
	
	public TextView getTv() {
		return tv;
	}

	public void setTv(TextView tv) {
		this.tv = tv;
	}

	public Search(SearchResult c, ListView v,String searched){
		this.searched=searched;
		this.c = c;
		lv=v;
	}
	
	@Override
	public void run() {
		c.getMyProgressDialog().setMax(searched.length());
		permute("",searched);
		
		/*for(String ana : anagrammes){
			Log.d(LOG_TAG,"anagrammes:" + ana);
		}*/
		int min = ApplicationState.getMinLetter();
		int max = ApplicationState.getMaxLetter();
		
		ArrayList<String> toremove = new ArrayList<String>();
		for(String ana: anagrammes){
			if(ana.length() < min || ana.length() > max){
				toremove.add(ana);
			}
		}
		for(String r: toremove){
			anagrammes.remove(r);
		}
		
		HashSet<String> hs = new HashSet<String>(anagrammes);
		anagrammes = new ArrayList<String>(hs);
		
		
		Collections.sort(anagrammes);
        final int size = anagrammes.size();
        final ResultAdapter ra = new ResultAdapter (this.c,anagrammes);
	        
	    c.getMyProgressDialog().dismiss();
	    lv.post(new Runnable() {
	    		public void run() {
	    			lv.setAdapter(ra);
	    		}
	    	}); 
        if(tv != null){
        	tv.post(
        			new Runnable() {
			     public void run() {
			    	 tv.setText("Nombre de résultat : " + size);
			     }
			   }
        	);
        }
	}
	
	
	
	private void permute(String start,String others){
		//if(others.length() == 0)
		//Log.d(LOG_TAG,start);
		
		char[] cs = others.toCharArray();
		for(int i =0; i < cs.length;i++){			
			char current = cs[i];
			if(start.length() ==0){
				//c.getMyProgressDialog().incrementProgressBy(1);
				Lettre root = Dictionnaire.getFromRessources(this.c,current);
				ApplicationState.setRoot(root);
			}
			
			String others2 = others.substring(0,i) + others.substring(i+1);
			boolean[] isp = Dictionnaire.isPrefix(ApplicationState.getRoot(), start+current);
			//Log.d(LOG_TAG,"is word =" + isp[1]);
			//Log.d(LOG_TAG,"is prefix =" + isp[0]);
			if(isp[1])
				anagrammes.add(start+current);
			if(isp[0])
				permute(start + current, others2);
			if(start.length() ==0){
				c.getMyProgressDialog().incrementProgressBy(1);
			}
		}
	}

	

}
