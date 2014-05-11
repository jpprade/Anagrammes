package com.jpprade.anagrammes.adapter;

import java.util.List;

import com.jpprade.anagrammes.R;
import com.jpprade.anagrammes.dico.ApplicationState;


import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultAdapter extends BaseAdapter {
	
	private static String LOG_TAG = ResultAdapter.class.getName();
	
	private List<String> ls;
	private Context context;

	public ResultAdapter(Context c,List<String> result){
		ls=result;
		context = c;
	}
	
	@Override
	public int getCount() {
		return ls.size();
	}

	@Override
	public Object getItem(int i) {
		return ls.get(i);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(LOG_TAG,"text = " + ls.get(position));
		
		String text = ls.get(position);
		text =styleResult(text,ApplicationState.getSearched()); 
			
		CharSequence styledText = Html.fromHtml(text);
		
		if(convertView == null){
			View inflated = View.inflate(context, R.layout.oneresult, null);
			TextView tv = (TextView) inflated.findViewById(R.id.texteres);
			tv.setText(styledText);
			return inflated;
		}else{
			TextView tv = (TextView) convertView.findViewById(R.id.texteres);
			tv.setText(styledText);
			return convertView ;
		}
		
	}
	
	public String styleResult(String result,String searched){
		String ret = "";
		boolean open = false;
		for(int i=0;i<result.length();i++){
			char a1 = result.charAt(i);
			char a2 = searched.charAt(i);
			if(a2 == '*'){
				if(open){
					ret += "</b>";
					open = false;
				}
			}else{
				if(!open){
					ret += "<b>";
					open = true;
				}				
			}
			ret += a1;
		}
		return ret;
	
	}

}
