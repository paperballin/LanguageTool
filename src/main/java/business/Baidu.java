package business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import database.Connect;
import database.Rule;
import database.VersionList;
import database.WordList;
import surface.Start;

public class Baidu implements SpiderIncomer{
	private String type=null;//分类名称
	private String url=null;//分类链接
	
	//存储数据容器
	private ArrayList<String> titlelist;//存储词条名称
	private ArrayList<String> wordlist;//存储词条链接
	private ArrayList<String> historylist;//存储历史链接
	private ArrayList<ArrayList<String>> versionlist;//存储更新和原本版本链接
	private ArrayList<String> comparelist;//存储对比页面的链接
	private ArrayList<ArrayList<String>> rules;//存储规则
	
	//统计变量
	private int num;//词条总数
	private int hisnum;//合适版本数
	private int totalnum;//历史版本总数
	private double rate=0;//文本相似度
	
	//计算运行时间变量，用于时间分析
	private ArrayList<Long> textTime;
	private long spiderTime=0;
	
	//构造函数
	public Baidu(String url,double rate,String type){
		this.type=type;
		this.url=url;
		this.titlelist=new ArrayList<String>();
		this.wordlist=new ArrayList<String>();
		this.historylist=new ArrayList<String>();
		this.versionlist=new ArrayList<ArrayList<String>>();
		this.comparelist=new ArrayList<String>();
		this.rules=new ArrayList<ArrayList<String>>();
		this.textTime=new ArrayList<Long>();
		this.num=0;
		this.hisnum=0;
		this.totalnum=0;
		this.rate=rate;
	}
//	public Baidu(){}//测试用
	
	//开始
	public void run(){
		long crawlstart=System.currentTimeMillis();//开始计算爬虫时间
		//开始获取该分类下的词条（一级链接）
		if(url!=null){
			getWordList(url);
			Start.setProgress(20);
		}
		else {
			Start.setText("没有分类链接");
			return;
		}
		//开始获取各个词条的历史链接（二级链接）
		if(!wordlist.isEmpty()){
			getHistoryList(wordlist);
			Start.setProgress(40);
		}
		else {
			Start.setText("没有词条");
			return;
		}
		//开始获取合适的对比链接（三级链接）
		if(!historylist.isEmpty()){
			getFixedVersion(historylist);
			Start.setProgress(60);
		}
		else {
			Start.setText("没有历史列表");
			return;
		}
		long crawlend=System.currentTimeMillis();//结束爬虫时间计算
		this.spiderTime=(crawlend-crawlstart);
		long matchstart=System.currentTimeMillis();//开始文本分析时间计算
		if(!comparelist.isEmpty()){
			matchRules(comparelist);
			Start.setProgress(80);
		}
		else {
			Start.setText("没有合适的版本");
		}
		long matchend=System.currentTimeMillis();//结束文本分析时间计算
		//过滤规则
		rules=parseRules(rules);
		//显示中间信息
		printList();
		seeResults();
		seeNum();
		Start.setText("爬虫时间："+(crawlend-crawlstart)+"ms");
		Start.setText("文本时间："+(matchend-matchstart)+"ms");
		System.out.println("爬虫时间："+(crawlend-crawlstart));
		System.out.println("文本时间："+(matchend-matchstart));
	}
	
	//获取词条名称
	public ArrayList<String> getWordList(String url){
		if(url.isEmpty()){	
			return wordlist;
		}
		String end="end";
		try{
			Document doc = Jsoup.connect(url).timeout(200000).get();	    
		    Elements links = doc.getElementsByClass("photo");	    
		    for(Element link:links){  
		    	Elements a=link.select("a[href]");
		        String title=a.attr("title");//词条标题
		        Start.setText("词条"+num+":"+title);
		        Start.setText("链接："+"https://baike.baidu.com/item/"+title);
		        titlelist.add(title);//存储词条
		        wordlist.add("https://baike.baidu.com/item/"+title);
		        num++;
		    }
			//获取下一页的url
	        Element next=doc.getElementById("next");
	        if(next!=null){
	            String nexturl=next.attr("href");
	        	end=nexturl;
	        }
	    }catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Start.setText("分类链接错误");
		}
		//递归算法获取下一页的词条
		if(end!="end"){
			url="http://baike.baidu.com/fenlei/"+end;
			getWordList(url);
		}
		else Start.setText("词条链接检索结束");
		return wordlist;
    }
	
	//获取历史版本链接
	public ArrayList<String> getHistoryList(ArrayList<String> urls){
		if(urls.isEmpty())return null;//空链表直接返回
		for(int i=0;i<urls.size();i++){
//		for(int i=0;i<100;i++){//测试合适版本所用代码
			try {
				System.out.println(urls.get(i));
				Document doc=Jsoup.connect(urls.get(i)).timeout(20000).get();
				Elements links=doc.select("dd.description");
				if(!links.isEmpty()){
					for(Element link:links){
						String s="https://baike.baidu.com"+link.select("a[href]").get(0).attr("href");
						System.out.println("百度历史列表链接："+s);
						Start.setText("百度历史列表链接："+s);
//					Start.setText(urls.get(i));
						historylist.add(s);
					}
				}else{
					historylist.add("无法爬出历史链接："+urls.get(i));
				}
			} catch (Exception e) {
				// TODO 自动生成的 catch块
//				historylist.add("无法爬出历史链接："+urls.get(i));
				e.printStackTrace();
			}
			
		}
		
		return historylist;
	}
	
	//获取结果
	public ArrayList<String> getFixedVersion(ArrayList<String> urls){
		if(urls.isEmpty())return null;//空链表直接返回
		WebClient webClient=new WebClient(BrowserVersion.BEST_SUPPORTED);
		String json=null;	//json数据
        int tkstart=0;		//tk参数开始位置
        int tkend=0;		//tk参数结束位置
        int lmIdstart=0;	//lemmaId参数开始位置
        int lmIdend=0;		//lemmaId参数结束位置
        for(int i=0;i<urls.size();i++){
            int page=1;//页数
        	String surl=urls.get(i);//历史版本列表链接
        	String prefix=surl.replaceAll("historylist", "history");
	        try {
				HtmlPage webpage0=webClient.getPage(surl); //初始页面
				HtmlPage webpage1=(HtmlPage)webpage0.refresh();//刷新页面
				String xml=webpage1.asXml();
				tkstart=xml.indexOf("tk           =")+16;
				tkend=xml.indexOf("lemmaId")-5;
				String tk=xml.substring(tkstart, tkend);//tk参数值
				lmIdstart=xml.indexOf("lemmaId      =")+15;
				lmIdend=xml.indexOf("lemmaTitle")-4;
				String lemmaId=xml.substring(lmIdstart,lmIdend);//lemmaId参数值
				do{
					HtmlPage webpage2=webClient.getPage("https://baike.baidu.com/api/wikiui/gethistorylist?tk="+tk+"&lemmaId="+lemmaId+"&from="+page+"&count=1&size=30");//json数据页面
					json=webpage2.asText().replaceAll("(?:<html>|</html>|<body>|</body>|<head>|</head>|null|<head/>)","");
//					Start.setText(json);
					parseJson(json,page,prefix,i);
					page++;
				}while(!json.contains("versionId"));
				totalnum+=page*25;/*这里用page*25的方法计算版本总数，有所误差*/
				webpage0.cleanUp();
				webpage1.cleanUp();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				Start.setText("获取对比链接错误");
			}
        }
        webClient.close();
        return comparelist;
	}
	
	//解析json数据
		private void parseJson(String json,int page,String prefix,int index){
			try{
				if(json!=null){
					//过滤出存储历史版本信息的json数据组
					JSONObject jsonObj = JSON.parseObject(json);
					String inf=jsonObj.getString("data");
					JSONObject pager=JSON.parseObject(inf);
					String arrays=pager.getString("pages");
					JSONObject array=JSON.parseObject(arrays);
					JSONArray list=JSON.parseArray(array.getString(page+""));
					//查找json数据中的内容修改类版本信息
					for(int i=0;i<list.size();i++){
						String history=list.getString(i);//单个历史版本的json数据
						JSONObject his=JSON.parseObject(history);
						if(isFixedVersion(history)){//判断修改原因
							String versionId=his.getString("versionId");
							String baseVersionId=his.getString("baseVersionId");
							Start.setText("对比链接"+hisnum+":"+prefix.replace("history","historydiff")+"/"+versionId+"/"+baseVersionId);
							ArrayList<String> s=new ArrayList<String>();
							s.add(prefix+"/"+versionId);
							s.add(prefix+"/"+baseVersionId);
							s.add(titlelist.get(i));
							versionlist.add(s);
							comparelist.add(prefix.replace("history","historydiff")+"/"+versionId+"/"+baseVersionId);
							hisnum++;
						}
					}
				}else{
					Start.setText("Json数据为空");
					return;
				}
			}catch(Exception e){
				Start.setText("爬取第"+page+"页错误");
			}
		}
	
		//显示结果
		public void seeResults(){
			System.out.println("wordlist:"+wordlist);
			System.out.println("historylist:"+historylist);
			System.out.println("versionlist:"+versionlist);
			System.out.println("rule:"+rules);
		}
		
		//显示中间数据
		public void seeNum(){
			Start.setText("词条总数："+wordlist.size());
			Start.setText("合适版本数："+versionlist.size());
			Start.setText("版本总数："+Integer.toString(totalnum));
			Start.setText("规则总数："+rules.size());
			System.out.println(("词条总数："+Integer.toString(num)));
			System.out.println("合适版本数："+Integer.toString(hisnum));
			System.out.println("版本总数："+Integer.toString(totalnum));
			System.out.println("规则总数："+rules.size());
		}
		
		//文本处理
		public ArrayList<ArrayList<String>> matchRules(ArrayList<String> urls){
			long matchstart=0;
			long matchend=0;
			for(int i=0;i<urls.size();i++){
//			for(int i=0;i<10;i++){//测试文本相似度阈值
				try {
					Document doc=Jsoup.connect(urls.get(i)).timeout(20000).get();
					Elements links=doc.select("div.item-modify");//在对比页面的修改段组
					for(Element link:links){
						String newtext=link.select("div.divleft").text();//修改段组正确的文本
						String oldtext=link.select("div.divright").text();//修改段组错误的文本
//						Start.setText("new-match:"+isContainChinese(newtext)+"  old-match:"+isContainChinese(oldtext));
						//过滤（加快寻找速度）
						if(link.parent().className().equals("version-catalog"))continue;//修改部分在目录的跳过
						if(!link.select("div.divleft").html().contains("diff-color")||
							!link.select("div.divright").html().contains("diff-color"))continue;//修改段组其中一方没有修改标记，跳过
						//进行文本分析
						if(isContainChinese(newtext)&&isContainChinese(oldtext)){
//							Start.setText("new:"+newtext+"\n"+"old:"+oldtext);
							matchstart=System.currentTimeMillis();
							addRules(MatchHanLP.find(newtext,oldtext,rate,urls.get(i),urls.get(i)));
							matchend=System.currentTimeMillis();
							Start.setText("已获取规则");
						}
					}
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				textTime.add(matchend-matchstart);
			}
			return rules;
		}
		//规则
		private ArrayList<ArrayList<String>> parseRules(ArrayList<ArrayList<String>> s){
			ArrayList<ArrayList<String>> listTemp= new ArrayList<ArrayList<String>>();  
			Iterator<ArrayList<String>> it=s.iterator();  
			 while(it.hasNext()){  
			  ArrayList<String> a=it.next();  
			  if(listTemp.contains(a)){  
			   it.remove();  
			  }  
			  else{  
			   listTemp.add(a);  
			  }  
			 }
			 return listTemp;
		}
		
		//匹配中文
		private boolean isContainChinese(String text){
			Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
	        Matcher m = p.matcher(text);
	        if (m.find()) {
	            return true;
	        }
	        return false;
		}
		
		//合适版本判断
		public boolean isFixedVersion(String word){
			if(word.contains("修改")||
					word.contains("修正")||
					word.contains("改正")||
					word.contains("改为")||
					word.contains("更正")||
					word.contains("补充完善")||
					word.contains("添加概述")||
					word.contains("编辑"))
			{
				return true;
			}
			return false;
			
		}
		//每个词条的rule加到rules中
		private ArrayList<ArrayList<String>> addRules(ArrayList<ArrayList<String>> s){
			for(int i=0;i<s.size();i++){
				rules.add(s.get(i));
			}
			return rules;
		}
		//加入数据库wordlist中
		public void getBaiduDateBaseWordList(){
			for(int i=0;i<wordlist.size();i++){
				Connect.connect();
				Connect.insertWordList(
						new WordList(titlelist.get(i),
								type,
								"百度百科",
								wordlist.get(i),
								historylist.get(i),
								spiderTime));
				Connect.close();
			}
		}
		//加入数据库versionlist中
		public void getBaiduDateBaseVersionList(){
			Connect.connect();
			for(int i=0;i<titlelist.size();i++){
				try{
					for(int j=0;j<versionlist.size();j++){
						Connect.insertVersionList(new VersionList(versionlist.get(i).get(2), 
								versionlist.get(i).get(0),
								versionlist.get(i).get(1),
								comparelist.get(i), 
								textTime.get(i)));
					}
				}catch(IndexOutOfBoundsException e){
//							e.printStackTrace();
					continue;//词条没有合适版本的，继续下一条
				}
			}
			Connect.close();
		}
		
		//加入rule
		public void getBaiduDateBaseRule(){
			Connect.connect();
			for(int i=0;i<rules.size();i++){
				System.out.println("规则："+rules.get(i));
				Connect.insertRule(new Rule(rules.get(i).get(0),
						rules.get(i).get(1),
						rules.get(i).get(2),
						rules.get(i).get(3),
						rules.get(i).get(4),
						(spiderTime+textTime.get(i))));
						
			}
			Connect.close();
		}
		
		//加入historys中
		public void getHistorys(){
			Connect.connect();
			Connect.insertHistory(historylist);
			Connect.close();
		}
		//加入versions中
		public void getVersions(){
			Connect.connect();
			for(int i=0;i<versionlist.size();i++){
				Connect.insertVersionsAAS(versionlist);;
			}
			Connect.close();
		}
		
		//加入words中
		public void getWords(){
			System.out.println("wordlist:"+wordlist);
			System.out.println("titlelist:"+titlelist);
			Connect.connect();
			Connect.insertWords(wordlist,titlelist);
			Connect.close();
		}
		
		//加入界面列表
		public void printList(){
			ArrayList<String> baidurules=new ArrayList<String>();
			for(int i=0;i<rules.size();i++){
				baidurules.add(rules.get(i).get(0)+"-"+
						rules.get(i).get(1));
			}
			if(!baidurules.isEmpty())Start.addList(baidurules);//加入界面list中
		}
		public String getUrl() {
			return url;
		}

		public ArrayList<String> getTitlelist() {
			return titlelist;
		}

		public ArrayList<String> getWordlist() {
			return wordlist;
		}

		public ArrayList<String> getHistorylist() {
			return historylist;
		}

		public ArrayList<ArrayList<String>> getVersionmap() {
			return versionlist;
		}

		public ArrayList<String> getComparelist() {
			return comparelist;
		}

		public ArrayList<ArrayList<String>> getRules() {
			return rules;
		}

		public int getNum() {
			return num;
		}

		public int getHisnum() {
			return hisnum;
		}

		public int getTotalnum() {
			return totalnum;
		}

		public long getSpiderTime() {
			return spiderTime;
		}

		public ArrayList<Long> getTextTime() {
			return textTime;
		}
		
}
