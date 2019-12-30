package business;

import java.util.ArrayList;
import java.util.Iterator;
import surface.Start;

public class Select {
	private boolean bai;
	private boolean hu;
	private boolean sou;
	private String type;
	private ArrayList<String> baidurules;
	private ArrayList<String> hudongrules;
	private ArrayList<String> sougourules;
	private double rate=0.6;
	
	public Select(){
		this.baidurules=new ArrayList<String>();
		this.hudongrules=new ArrayList<String>();
		this.sougourules=new ArrayList<String>();
	}

	public void run(boolean bai,boolean hu,boolean sou,String type,boolean database) {
		// TODO 自动生成的方法存根
		this.bai=bai;
		this.hu=hu;
		this.sou=sou;
		try{
			if(bai){
				Baidu baidu=new Baidu("http://baike.baidu.com/fenlei/"+type,rate,type);
				baidu.run();
//				setProgress(1);
				parseRules(baidurules);
				System.out.println("baidurules:"+baidurules);
				if(database){
					baidu.getBaiduDateBaseWordList();
					Start.setText("已加入wordlist");
					baidu.getBaiduDateBaseVersionList();
					Start.setText("已加入versionlist");
					baidu.getBaiduDateBaseRule();
					Start.setText("已加入rule");
					baidu.getWords();
					baidu.getHistorys();
					baidu.getVersions();
				}
				Start.setProgress(100);
			}
			if(hu){
				Hudong hudong=new Hudong("http://fenlei.baike.com/"+type+"/list/",rate,type);
				hudong.run();
				for(int i=0;i<hudong.getRules().size();i++){
					hudongrules.add(hudong.getRules().get(i).get(0)+"-"+
							hudong.getRules().get(i).get(1));
				}
				parseRules(hudongrules);
				hudong.getBaiduDateBaseWordList();
				hudong.getBaiduDateBaseVersionList();
				hudong.getBaiduDateBaseRule();
				Start.setProgress(100);
			}
			if(sou){
				Sougou sougou=new Sougou("https://www.sogou.com/sogou?query="+type+"&pid=sogou-wsse-17737832ac17be524&insite=baike.sogou.com&page=1&duppid=1&ie=utf8",rate,type);
				sougou.run();
				for(int i=0;i<sougou.getRules().size();i++){
					sougourules.add(sougou.getRules().get(i).get(0)+"-"+
							sougou.getRules().get(i).get(1));
				}
				parseRules(sougourules);
			}
		}catch(Exception s){
			System.out.println("Select类出错");
			s.printStackTrace();
		}
		System.out.println("baidurules:"+baidurules);
		if(!baidurules.isEmpty())Start.addList(baidurules);
		if(!hudongrules.isEmpty())Start.addList(hudongrules);
		if(!sougourules.isEmpty())Start.addList(sougourules);
	}
	
	public ArrayList<String> getBaiduRules(){
		return baidurules;
	}
	//规则
	private ArrayList<String> parseRules(ArrayList<String> s){
		ArrayList<String> listTemp= new ArrayList<String>();  
		Iterator<String> it=s.iterator();  
		 while(it.hasNext()){  
		  String a=it.next();  
		  if(listTemp.contains(a)){  
		   it.remove();  
		  }  
		  else{  
		   listTemp.add(a);  
		  }  
		 }
		 return listTemp;
	}
	
	public void setRate(double rate){
		this.rate=rate;
	}

	public boolean isBaidu() {
		return bai;
	}

	public boolean isHudong() {
		return hu;
	}

	public boolean isSougou() {
		return sou;
	}

	public String getType() {
		return type;
	}

	public ArrayList<String> getBaidurules() {
		return baidurules;
	}

	public ArrayList<String> getHudongrules() {
		return hudongrules;
	}

	public ArrayList<String> getSougourules() {
		return sougourules;
	}

	public double getRate() {
		return rate;
	}
	
	public void seeResults(){
		
	}
}
