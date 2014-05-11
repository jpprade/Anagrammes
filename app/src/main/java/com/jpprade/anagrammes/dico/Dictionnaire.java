package com.jpprade.anagrammes.dico;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;




import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;

public class Dictionnaire {
	
	private static Lettre root = new Lettre('0', null, null);
	private static String ser ;
	
	private static String LOG_TAG = Dictionnaire.class.getName();

	
	
	public static void addMot(String mot,Lettre parent){
		if(mot.length() >0){
			boolean endword = false;
			if(mot.length() == 1)
				endword = true;
				
			char c = mot.charAt(0);
			Lettre current = parent.getSuffixes();
			if(current == null){				
				Lettre l = new Lettre(c,null,null,endword);
				parent.setSuffixes(l);
				addMot(mot.substring(1), l);
				return;
			}
			
			Lettre exist=null;
			Lettre previous = null;
			do{
				char cl = current.getLettre();
				if(cl == c){
					exist = current;
					break;
				}else if(cl < c){
					previous = current;
				}else{
					break;
				}
				current = current.getSuivants();
			}while(current != null);
			if(exist != null){
				addMot(mot.substring(1),exist);
			}else if(previous != null){
				Lettre l = new Lettre(c,null,previous.getSuivants(),endword);
				previous.setSuivants(l);
				addMot(mot.substring(1),l);
			}else{
				Lettre l = new Lettre(c,null,parent.getSuffixes(),endword);
				parent.setSuffixes(l);
			}
		}
	}
	
	public static void printDico(String prefix, Lettre l){
		if(l == null){
			System.out.println(prefix);
			return;
		}
		do{
			String current = prefix + l.getLettre();
			if(l.isEndWord() && l.getSuffixes() != null){
				System.out.println(current);
			}
			printDico(current, l.getSuffixes());
			l = l.getSuivants();
		}while(l != null);
		
	}
	
	public static void printDicoLog(String prefix, Lettre l){
		if(l == null){
			Log.d(LOG_TAG, prefix);
			return;
		}
		do{
			String current = prefix + l.getLettre();
			if(l.isEndWord() && l.getSuffixes() != null){
				Log.d(LOG_TAG, current);
			}
			printDico(current, l.getSuffixes());
			l = l.getSuivants();
		}while(l != null);
		
	}
	
	public static Lettre getFromRessources(Activity motsFleches,char startchar) {
        int c = startchar;
        Resources res = motsFleches.getResources();
        int filerawid = res.getIdentifier("l"+c  , "raw", "com.jpprade.anagrammes");
        Lettre nu = new Lettre('0', null, null);
        Lettre tmp = nu;       
        Log.d(LOG_TAG,"open ressources " + filerawid + "(" +startchar +")");
        InputStream is = res.openRawResource(filerawid);
        if (is == null)
        	return null;
        try {

            BufferedInputStream bin = new BufferedInputStream(is);
            byte[] contents = new byte[2048];
            /*Reader bin = new InputStreamReader(is, "iso-8859-1");
            char[] contents = new char[2048];*/
            int bytesRead=0;             

            int k=0;
            while( (bytesRead = bin.read(contents)) != -1){
                 String tmpstr = new String(contents, 0, bytesRead,"iso-8859-1");
                 //String tmpstr = new String(contents);
                 k++;
                 //Log.d(LOG_TAG,"data read " + (k * 2048) );
                 for(int j =0;j < tmpstr.length();j++){
                       /*if(k==44)
                             Log.d(LOG_TAG,"data read " + tmpstr.charAt(j) );*/
                       tmp = unserialize(tmpstr.charAt(j), tmp);
                 }
            }
            is.close();
      } catch (IOException e) {
            e.printStackTrace();
      }
      Log.d(LOG_TAG," ressources loaded unserilizing" );
      nu= nu.getSuffixes();
      return nu;
	}
	
	public static void addFromFile(){
		int tot = 0;
		try{
		    //FileInputStream fstream = new FileInputStream("res/mottestutf8.txt");
			FileInputStream fstream = new FileInputStream("res/motutf8.txt");
			//FileInputStream fstream = new FileInputStream("res/testjp.txt");

		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    while ((strLine = br.readLine()) != null)   {
		      //System.out.println (strLine);
		    	tot += strLine.length();
		      Dictionnaire.addMot(strLine, root);
		    }
		    in.close();
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
		  //System.out.println (tot);
	}
	
	public static void write(String content){
		 try{
		    // Create file 
		    FileWriter fstream = new FileWriter("res/out.txt");
		    BufferedWriter out = new BufferedWriter(fstream);
		    out.write(content);
		    //Close the output stream
		    out.close();
	    }catch (Exception e){//Catch exception if any
	      System.err.println("Error: " + e.getMessage());
	    }
	}
	 
	
	public static void main(String[] args){
		/*Dictionnaire.addMot("ab", root);
		Dictionnaire.addMot("abc", root);
		Dictionnaire.addMot("abd", root);
		Dictionnaire.addMot("bac", root);*/
		addFromFile();
		root = root.getSuffixes();
		//printDico("", root);
		String seri = serialize(root);
		ser = seri;
		write(seri);
		System.out.println("unserialize");
		Lettre nu = new Lettre('0', null, null);
		unserialize(0,nu);
		nu= nu.getSuffixes();
		//printDico("", nu);
		
		//System.out.println (ser.length());
	}
	
	public static String serialize(Lettre l){
		String s = "";
		if(l == null)
			return "$";
		do{
			s += l.getLettre()+"";
			if(l.isEndWord() && l.getSuffixes() != null){
				s+= "*";
			}
			s += serialize(l.getSuffixes());

			l = l.getSuivants();
		}while(l != null);
		s+= "$";
		return s;
	}
	
	public static Lettre unserialize(){
		Lettre nu = new Lettre('0', null, null);
		unserialize(0,nu);
		nu= nu.getSuffixes();
		return nu;
	}

	
	protected static int unserialize(int pos,Lettre l){
		while(pos < ser.length()){
			char c = ser.charAt(pos);
			if(c == '$')
				return pos;
			char n ;
			if(pos < ser.length() -1)
				n = ser.charAt(pos +1);
			else 
				n = '\0';
			
			
			Lettre cl = new Lettre(c,null,null);
			if(n == '*'){
				cl.setEndWord(true);
				pos++;
			}
			if(l.getSuffixes() == null)
				l.setSuffixes(cl);
			else{
				Lettre lastsuiv = l.getSuffixes();
				while(lastsuiv.getSuivants() !=null){
					lastsuiv = lastsuiv.getSuivants();
					
				}
				lastsuiv.setSuivants(cl);
			}
			
			pos = unserialize(pos + 1, cl);
			pos++;
		}
		return pos;
	}
	
	public static Lettre unserialize(char c,Lettre l){
		if(c == '$')
			return l.getParent();
		if(c == '*'){
			l.setEndWord(true);
			return l;
		}
		
		Lettre cl = new Lettre(c,null,null);
		cl.setParent(l);
		if(l.getSuffixes() == null)
			l.setSuffixes(cl);
		else{
			Lettre lastsuiv = l.getSuffixes();
			while(lastsuiv.getSuivants() !=null){
				lastsuiv = lastsuiv.getSuivants();
				
			}
			lastsuiv.setSuivants(cl);
		}
		return cl;
				
	}
	
	public static boolean[] isPrefix(Lettre root,String prefix){
		Lettre current =root;
		char a1 = fastnormalize( prefix.charAt(0));
		//char a1 = prefix.charAt(0);
		if(prefix.equals("meche"))
			Log.d(LOG_TAG,prefix);
		while(current !=null){
			char a2 = current.getLettre();
			char a2n = fastnormalize(a2);
			
			//Log.d(LOG_TAG,"char a1=" + a1 + "  char a2=" + a2);
			
			
			if(a1 == a2n){
				if(prefix.length()==1){
					boolean[] res= new boolean[2];			
					res[0] = true;//is prefix
					
					if(current.isEndWord())
						res[1] = true;//is word
					else
						res[1] = false;//is word
					return res;
				}else{
					boolean[] res = isPrefix(current.getSuffixes(),prefix.substring(1));
					if(! res[0] && !res[1])
						current = current.getSuivants();
					else
						return res;
				}
			}else{
				current = current.getSuivants();
			}
		}
		boolean[] res= new boolean[2];			
		res[0] = false;
		res[1] = false;
		return res;
		
		
	}
	
	public static List<String>  searchWord(Lettre root,String search){
		ArrayList<String> result = searchWord(root,search,0,"");		
		return result;
	}
	
	private static ArrayList<String>  searchWord(Lettre root,String search,int pos,String w){
		if(pos >= search.length())
				return null;
		Lettre current =root;
		ArrayList<String> result = new ArrayList<String>();
		char a1 = search.charAt(pos);
		if(a1  != '*'){
			a1 = fastnormalize(a1);
			a1 = Character.toLowerCase(a1);
		}
		
		do{
			char a2 = current.getLettre();
			a2 = fastnormalize(a2);
			a2 = Character.toLowerCase(a2);
			if(a1 == '*' 
				|| a1 == a2	){
				//System.out.println("word =  " + w + " pos ="+ pos+", a1=" + a1 +", a2=" + a2);
				String word = w + current.getLettre();
				if(pos == (search.length()-1) && (current.isEndWord() || current.getSuffixes() == null)){
					//System.out.println("adding  " + word + "current =" + current.getLettre());
					result.add(word);
				}else{
					if(current.getSuffixes() != null && pos < search.length()){
						ArrayList<String> tmp = searchWord(current.getSuffixes(),search,pos+1,word);
						if(tmp != null)
							result.addAll(tmp);
					}
				}
				
			}
			current= current.getSuivants();
		}while(current != null);
		return result;
	}
//[àáâãäåæÀÁÂÄÆÅÃ çÇ ÈÉÊËèéêë ÙÚÛÜùúûü ÌÍÎÏìíîï ÒÓÔÕÖòóôõö ÑñÝý]
	private static char normalize(char charAt) {
		char[] es = new char[8];
		es[0] ='È';
		es[1] ='É';
		es[2] ='Ê';
		es[3] ='Ë';
		es[4] ='è';
		es[5] ='é';
		es[6] ='ê';
		es[7] ='ë';
		for(int i =0; i< es.length;i++)
			if(charAt == es[i])
				return 'e';
		
		char[] as = new char[14];
		as[0] ='à';
		as[1] ='á';
		as[2] ='â';
		as[3] ='ã';
		as[4] ='ä';
		as[5] ='å';
		as[6] ='æ';		
		as[7] ='À';
		as[8] ='Á';
		as[9] ='Â';
		as[10] ='Ä';
		as[11] ='Æ';
		as[12] ='Å';
		as[13] ='Ã';
		for(int i =0; i< as.length;i++)
			if(charAt == as[i])
				return 'a';
		
		char[] us = new char[8];
		us[0] ='Ù';
		us[1] ='Ú';
		us[2] ='Û';
		us[3] ='Ü';
		us[4] ='ù';
		us[5] ='ú';
		us[6] ='û';
		us[7] ='ü';
		for(int i =0; i< us.length;i++)
			if(charAt == us[i])
				return 'u';
		
		char[] is = new char[8];
		is[0] ='Ì';
		is[1] ='Í';
		is[2] ='Î';
		is[3] ='Ï';
		is[4] ='ì';
		is[5] ='í';
		is[6] ='î';
		is[7] ='ï';
		for(int i =0; i< is.length;i++)
			if(charAt == is[i])
				return 'i';
		
		char[] os = new char[10];
		os[0] ='Ò';
		os[1] ='Ó';
		os[2] ='Ô';
		os[3] ='Õ';
		os[4] ='Ö';
		os[5] ='ò';
		os[6] ='ó';
		os[7] ='ô';
		os[8] ='õ';
		os[9] ='ö';
		for(int i =0; i< os.length;i++)
			if(charAt == os[i])
				return 'o';
		
		char[] ns = new char[2];
		ns[0] ='Ñ';
		ns[1] ='ñ';				
		for(int i =0; i< ns.length;i++)
			if(charAt == ns[i])
				return 'n';
		
		char[] ys = new char[2];
		ys[0] ='Ý';
		ys[1] ='ý';		
		for(int i =0; i< ys.length;i++)
			if(charAt == ys[i])
				return 'y';
		
		return charAt;
	}
	
    /*
   50 à
	1 ä
20979 â
119259 é
10717 è
   25 ë
 2283 ê
  978 ï
 3181 î
    2 ö
  906 ô
    1 ù
   44 ü
 1318 û
 2783 ç
*/
	
	private static char fastnormalize(char charAt) {
		char[] es = new char[4];		
		es[0] ='è';
		es[1] ='é';
		es[2] ='ê';
		es[3] ='ë';
		for(int i =0; i< es.length;i++)
			if(charAt == es[i])
				return 'e';
		
		char[] as = new char[3];
		as[0] ='à';
		as[1] ='â';
		as[2] ='ä';

		for(int i =0; i< as.length;i++)
			if(charAt == as[i])
				return 'a';
		
		char[] us = new char[3];
		us[0] ='ù';
		us[1] ='û';
		us[2] ='ü';
		for(int i =0; i< us.length;i++)
			if(charAt == us[i])
				return 'u';
		
		char[] is = new char[2];
		is[0] ='î';
		is[1] ='ï';
		for(int i =0; i< is.length;i++)
			if(charAt == is[i])
				return 'i';
		
		char[] os = new char[2];
		os[0] ='ô';
		os[1] ='ö';
		for(int i =0; i< os.length;i++)
			if(charAt == os[i])
				return 'o';
		
		if(charAt == 'ç')
				return 'c';
		
		return charAt;
	}
	
	

}
