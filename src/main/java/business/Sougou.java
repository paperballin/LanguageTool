package business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import database.Connect;
import database.Rule;
import database.VersionList;
import database.WordList;
import surface.Start;

public class Sougou implements Runnable,SpiderMaker{
	private String url=null;
	private String type=null;//分类名
	private ArrayList<String> titlelist=new ArrayList<String>();//词条名称
	private ArrayList<String> numberlist=new ArrayList<String>();//词条编号
	private ArrayList<String> wordlist=new ArrayList<String>();//词条链接
	private ArrayList<String> historylist=new ArrayList<String>();//历史版本链接
	private ArrayList<String> fixedversionlist=new ArrayList<String>();//合适版本链接列表
	private ArrayList<ArrayList<String>> rules=new ArrayList<ArrayList<String>>();//规则链接列表
	private int wordnum=0;//词条数
	private int fixnum=0;//合适版本数
	private double rate=0;
	private ArrayList<Long> textTime;
	private long spiderTime=0;
	
	//构造函数
	public Sougou(String url,double rate,String type){
		this.rate=rate;
		this.url=url;
	}
	//运行
	public void run(){
		long crawlstart=System.currentTimeMillis();
		if(url!=null){
			getWordList(url);
		}else return;
		if(!numberlist.isEmpty()){
			getHistoryList();
		}else return;
		if(!historylist.isEmpty()){
			getFixedVersion(historylist);
		}else return;
		long crawlend=System.currentTimeMillis();
		if(!fixedversionlist.isEmpty()){
			matchRules(fixedversionlist);
		}else return;
//		seeResult();
	}
	//获取词条
	public ArrayList<String> getWordList(String url){
		for(int page=1;page<12;page++){//循环实现翻页操作
			try {
				String pageurl=url.replace("page=1", "page="+page);//更改页数
				Document doc=Jsoup.connect(pageurl).timeout(20000).get();
				Elements links=doc.getElementsByClass("vrwrap");//词条模块
				for(Element link:links){
					if(link.text().contains(" - 搜狗百科")){	//判断为百科类词条
						titlelist.add(link.getElementsByClass("vrTitle").text());//根据词条class都有vrTitle这个值找到
						wordnum++;
					}
					Elements snapshot=link.getElementsByClass("fb");
					for(Element snap:snapshot){	//快照模块
						String inf=snap.child(1).attr("href");
						int start=inf.indexOf("%2Fv");
						int over=inf.indexOf(".htm");
						if(start!=-1){
							String number=inf.substring(start+4, over);
							numberlist.add(number);//词条编号
						}
					}
				}
				
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		return wordlist;
	}
	//获取合适版本
	public ArrayList<String> getFixedVersion(ArrayList<String> urls){
		for(int i=0;i<urls.size();i++){
			Document doc=setConnection(historylist.get(i));
//			System.out.println(doc.text());
			if(doc==null)return null;
			Elements json=doc.getElementsByTag("script");
			System.out.println(json.text());
			Elements links=doc.getElementsByClass("edition-list");
			for(Element link:links){
				Elements nodes=link.children();
				for(Element node:nodes){
					String modifyreason=node.getElementsByClass("column column4").text();//根据第四列的修改原因
					if(modifyreason.equals("修改基本信息"))
					{
						fixedversionlist.add("https://baike.sogou.com"+node.getElementsByClass("column column2").select("a[href]").attr("href"));//获取位于第二列的链接
						fixnum++;
					}
				}
			}
		}
		return fixedversionlist;
	}
	//获取规则
	public ArrayList<ArrayList<String>> matchRules(ArrayList<String> urls){
		if(urls.size()==1)return rules;
		for(int i=0;i<urls.size()-2;i+=2){
			String ncontent=null;
			String ocontent=null;
			try {
				Document ndoc=Jsoup.connect(urls.get(i)).ignoreContentType(true)
						.userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)")
						.timeout(5000)
						.get();
				Document odoc=Jsoup.connect(urls.get(i+1)).ignoreContentType(true)
						.userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)")
						.timeout(5000)
						.get();
				//判断是否属于同一词条
				if(!ndoc.getElementsByTag("title").text().equals(odoc.getElementsByTag("title").text()))continue;
				//获取新版本文本中的章节标题内容列表
				ncontent=ndoc.getElementById("lemma_content").text();//新版本文本
				Elements nheads=ndoc.getElementsByClass("h3");
				ArrayList<String> nheadlist=new ArrayList<String>();
				for(Element head:nheads){
					nheadlist.add(head.text());
				}
				//获取旧版本文本中的章节标题内容列表
				ocontent=odoc.getElementById("lemma_content").text();	//旧版本文本
				Elements oheads=odoc.getElementsByClass("h3");
				ArrayList<String> oheadlist=new ArrayList<String>();
				for(Element head:oheads){
					oheadlist.add(head.text());
				}
				//将同一模块的内容
				for(int j=0;j<nheadlist.size()-1;j++){
					for(int k=0;k<oheadlist.size()-1;k++){
						if(nheadlist.get(j).equals(oheadlist.get(k))){	//模块标题相同
							int nstart=ncontent.indexOf(nheadlist.get(j));//新版本模块起始位置
							int nend=ncontent.indexOf(nheadlist.get(j+1));//新版本模块结束位置
							int ostart=ocontent.indexOf(oheadlist.get(k));//旧版本模块起始位置
							int oend=ocontent.indexOf(oheadlist.get(k+1));//旧版本模块结束位置
							String nmodule=ncontent.substring(nstart, nend);
							String omodule=ocontent.substring(ostart, oend);
							if(nmodule!=null&&omodule!=null){
								addRules(MatchHanLP.find(nmodule,omodule,rate,urls.get(i),urls.get(i+1)));
							}
						}
					}
				}
			
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		return rules;
	}
	//显示结果
	public void seeResults(){
		for(int i=0;i<titlelist.size();i++){
			System.out.println("词条："+titlelist.get(i));
			Start.setText("词条："+titlelist.get(i));
		}
		for(int i=0;i<numberlist.size();i++){
			System.out.println("词条代号："+numberlist.get(i));
			Start.setText("词条代号："+numberlist.get(i));
		}
		for(int i=0;i<fixedversionlist.size();i++){
			System.out.println("合适版本："+fixedversionlist.get(i));
			Start.setText("合适版本："+fixedversionlist.get(i));
		}
		for(int i=0;i<rules.size();i++){
			System.out.println("规则："+rules.get(i));
			Start.setText("规则："+rules.get(i));
		}
	}
	
	//代理池连接设置
	private Document setConnection(String url){
		Document doc=null;
		Map<String,Integer> proxylist=getProxyList();//获取代理池
		for(Map.Entry<String,Integer> entry:proxylist.entrySet()){
			Connection conn=Jsoup.connect("https://baike.sogou.com/ShowHistory.e?sp="+numberlist.get(1))
					.timeout(60000)
					.ignoreContentType(true)
	                .ignoreHttpErrors(true)
	                .timeout(1000 * 30)
	                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
	                .header("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
	                .header("accept-encoding","gzip, deflate, br")
	                .header("accept-language","zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
	                .proxy(entry.getKey(), entry.getValue());
			try {
				doc = conn.get();//连上了跳出for循环，返回 
				
				if(doc!=null){
					Start.setText("已找到有效ip");
					break;
				}
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				Start.setText("ip失效，正在使用新ip");
				e.printStackTrace();
			}
		}
		if(doc==null)Start.setText("无法连接该分类");
		return doc;
		
	}
	
	//ip池
	private Map<String,Integer> getProxyList(){
		Map<String,Integer> proxylist=new HashMap<String,Integer>();
		proxylist.put("163.125.156.205", 8888);
		proxylist.put("211.162.70.229",3128);
		proxylist.put("119.101.105.202",9999);
		proxylist.put("123.161.19.208", 9797);
		proxylist.put("14.155.115.146", 9000);
		proxylist.put("14.20.235.120", 808);
		proxylist.put("114.239.3.192", 808);
		proxylist.put("110.52.235.62", 9999);
		proxylist.put("113.121.22.1",9999);
		proxylist.put("113.121.22.16", 9999);
		proxylist.put("171.37.152.24", 8123);
		return proxylist;
	}
	
	public void seeNum() {
		// TODO 自动生成的方法存根
		System.out.println("词条数目："+wordnum);
		System.out.println("合适版本数："+fixnum);
	}
	public ArrayList<String> getHistoryList() {
		// TODO 自动生成的方法存根
		for(int i=0;i<numberlist.size();i++){
			historylist.add("https://baike.sogou.com/ShowHistory.e?sp="+numberlist.get(i));
		}
		return null;
	}
	
	//添加rule
		private ArrayList<ArrayList<String>> addRules(ArrayList<ArrayList<String>> s){
			for(int i=0;i<s.size();i++){
				rules.add(s.get(i));
			}
			return rules;
		}
		public String getUrl() {
			return url;
		}
		public ArrayList<String> getTitlelist() {
			return titlelist;
		}
		public ArrayList<String> getNumberlist() {
			return numberlist;
		}
		public ArrayList<String> getWordlist() {
			return wordlist;
		}
		public ArrayList<String> getHistorylist() {
			return historylist;
		}
		public ArrayList<String> getFixedversionlist() {
			return fixedversionlist;
		}
		public ArrayList<ArrayList<String>> getRules() {
			return rules;
		}
		public int getWordnum() {
			return wordnum;
		}
		public int getFixnum() {
			return fixnum;
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
							for(int j=0;j<fixedversionlist.size()-2;j+=2){
								Connect.insertVersionList(new VersionList(titlelist.get(i), 
										fixedversionlist.get(j),
										fixedversionlist.get(j+1),
										null, 
										textTime.get(i)));
							}
						}catch(IndexOutOfBoundsException e){
//								e.printStackTrace();
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
					for(int i=0;i<fixedversionlist.size();i++){
						Connect.insertVersionsAS(fixedversionlist);;
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
}
