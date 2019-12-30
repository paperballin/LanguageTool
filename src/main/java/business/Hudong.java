package business;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import database.Connect;
import database.Rule;
import database.VersionList;
import database.WordList;
import surface.Start;

public class Hudong implements Runnable,SpiderMaker{
	private String url=null;//分类链接
	private String type=null;//分类名
	private ArrayList<String> titlelist;//存储词条名称
	private ArrayList<String> wordlist;//存储词条链接
	private ArrayList<String> historylist;//存储历史链接
	private ArrayList<String> fixedversionlist;//存储合适版本链接
	private ArrayList<ArrayList<String>> rules;//	存储规则
	private int wordnum;
	private int totalnum;
	private int versionnum;
	private int rulenum;
	private double rate=0;
	private long spiderTime=0;
	private ArrayList<Long> textTime;
	
	//构造函数
	public Hudong(String url,double rate,String type){
		this.url=url;
		this.type=type;
		this.rate=rate;
		this.titlelist=new ArrayList<String>();
		this.wordlist=new ArrayList<String>();
		this.historylist=new ArrayList<String>();
		this.fixedversionlist=new ArrayList<String>();
		this.rules=new ArrayList<ArrayList<String>>();
		this.wordnum=0;
		this.totalnum=0;
		this.versionnum=0;
		this.rulenum=0;
		this.textTime=new ArrayList<Long>();
	}
	
	//运行
	public void run(){
		long crawlstart=System.currentTimeMillis();
		getWordList(url);Start.setProgress(20);
		getFixedVersion(historylist);Start.setProgress(40);
		long crawlend=System.currentTimeMillis();
		long matchstart=System.currentTimeMillis();
		matchRules(fixedversionlist);
		long matchend=System.currentTimeMillis();
		seeNum();
		spiderTime=(crawlend-crawlstart);
		System.out.println("爬虫时间："+(crawlend-crawlstart));
		System.out.println("文本时间："+(matchend-matchstart));
		Start.setProgress(80);
	}
	
	//获取词条名称及其链接
	public ArrayList<String> getWordList(String url){
		try {
			Document doc=Jsoup.connect(url).timeout(20000).get();
			Elements links=doc.getElementsByTag("dd");
			for(Element link:links){
				wordnum++;
				titlelist.add(link.text());
				wordlist.add("http://www.baike.com/wiki/"+link.text());
				historylist.add("http://www.baike.com/wikdoc/sp/qr/history/list.do?doc_title="+link.text());
				Start.setText("词条："+link.text());
				Start.setText("词条链接："+"http://www.baike.com/wiki/"+link.text());
				Start.setText("历史链接："+"http://www.baike.com/wikdoc/sp/qr/history/list.do?doc_title="+link.text());
				System.out.println("词条："+link.text());
				System.out.println("词条链接："+"http://www.baike.com/wiki/"+link.text());
				System.out.println("历史链接："+"http://www.baike.com/wikdoc/sp/qr/history/list.do?doc_title="+link.text());
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return wordlist;
	}
	
	//获取历史版本列表
	public ArrayList<String> getHistoryList(){
		return historylist;
		
	}
	//获取合适版本
	public ArrayList<String> getFixedVersion(ArrayList<String> urls){
		for(int i=0;i<urls.size();i++){
			Element page = null;
			String url=urls.get(i);
			do{
				try {
					Document doc=Jsoup.connect(url).timeout(20000).get();
					page=doc.getElementById("flipover");
					Elements inputs=doc.getElementsByAttributeValue("name", "selectHis");//获取修改原因为“编辑正文”的历史版本
					for(Element input:inputs){
						String compileurl=input.nextSibling().attr("href");		//获取“编辑正文”url
						fixedversionlist.add(compileurl);
						versionnum++;
						Start.setText(compileurl);
						System.out.println(compileurl);
					}
					totalnum+=25;
					if(page!=null){
						if(page.text().contains("下一页")){
							Elements links=page.getElementsMatchingOwnText("下一页");
							for(Element link:links){
								url="http://www.baike.com"+link.attr("href");
							}
						}
					}
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}while(page!=null&&page.text().contains("下一页"));
		}	
		return fixedversionlist;
	}
	
	//文本分析
	public ArrayList<ArrayList<String>> matchRules(ArrayList<String> urls){
		if(urls.size()==1)return rules;//只有一个链接
		long textstart=0;
		long textend=0;
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
				//获取新版本中的标题内容列表
				ncontent=ndoc.getElementById("content").text();	//新版本文本
				Elements nheads=ndoc.getElementsByClass("content_h2");
				ArrayList<String> nheadlist=new ArrayList<String>();
				for(Element head:nheads){
					nheadlist.add(head.text());
				}
				//获取旧版本中的标题内容列表
				ocontent=odoc.getElementById("content").text();	//旧版本文本
				Elements oheads=odoc.getElementsByClass("content_h2");
				ArrayList<String> oheadlist=new ArrayList<String>();
				for(Element head:oheads){
					oheadlist.add(head.text());
				}
				//将同一模块的内容
				textstart=System.currentTimeMillis();
				System.out.println("新版本链接："+urls.get(i));
				System.out.println("旧版本链接："+urls.get(i+1));
				for(int j=0;j<nheadlist.size()-1;j++){
					for(int k=0;k<oheadlist.size()-1;k++){
						if(nheadlist.get(j).equals(oheadlist.get(k))){	//模块标题相同
							int nstart=ncontent.indexOf(nheadlist.get(j));//新版本模块起始位置
							int nend=ncontent.indexOf(nheadlist.get(j+1));//新版本模块结束位置
							int ostart=ocontent.indexOf(oheadlist.get(k));//旧版本模块起始位置
							int oend=ocontent.indexOf(oheadlist.get(k+1));//旧版本模块结束位置
							String nmodule="";
							String omodule="";
							nmodule=ncontent.substring(nstart, nend);
							omodule=ocontent.substring(ostart, oend);
							if(nmodule!=null&&omodule!=null){
								addRules(MatchHanLP.find(nmodule,omodule,rate,urls.get(i),urls.get(i+1)));
							}
						}
					}
				}
				textend=System.currentTimeMillis();
				textTime.add((textend-textstart));
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		return rules;
	}
	
	//显示结果
	public void seeResults(){
		
	}
	
	//显示数据
	public void seeNum(){
		Start.setText("词条数："+wordnum);
		Start.setText("版本总数："+totalnum);
		Start.setText("合适版本数："+versionnum);
		Start.setText("规则数："+rules.size());
		System.out.println(url);
		System.out.println("词条数："+wordnum);
		System.out.println("版本总数："+totalnum);
		System.out.println("合适版本数："+versionnum);
		System.out.println("规则数："+rules.size());
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ArrayList<String> getTitlelist() {
		return titlelist;
	}

	public void setTitlelist(ArrayList<String> titlelist) {
		this.titlelist = titlelist;
	}

	public ArrayList<String> getWordlist() {
		return wordlist;
	}

	public void setWordlist(ArrayList<String> wordlist) {
		this.wordlist = wordlist;
	}

	public ArrayList<String> getHistorylist() {
		return historylist;
	}

	public void setHistorylist(ArrayList<String> historylist) {
		this.historylist = historylist;
	}

	public ArrayList<String> getFixedversionlist() {
		return fixedversionlist;
	}

	public void setFixedversionlist(ArrayList<String> fixedversionlist) {
		this.fixedversionlist = fixedversionlist;
	}

	public ArrayList<ArrayList<String>> getRules() {
		return rules;
	}

	public void setRules(ArrayList<ArrayList<String>> rules) {
		this.rules = rules;
	}

	public int getWordnum() {
		return wordnum;
	}

	public void setWordnum(int wordnum) {
		this.wordnum = wordnum;
	}

	public int getTotalnum() {
		return totalnum;
	}

	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}

	public int getVersionnum() {
		return versionnum;
	}

	public void setVersionnum(int versionnum) {
		this.versionnum = versionnum;
	}

	public int getRulenum() {
		return rulenum;
	}

	public void setRulenum(int rulenum) {
		this.rulenum = rulenum;
	}
	
	//添加rule
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
						for(int j=0;j<fixedversionlist.size()-2;j+=2){
							Connect.insertVersionList(new VersionList(titlelist.get(i), 
									fixedversionlist.get(j),
									fixedversionlist.get(j+1),
									null, 
									textTime.get(i)));
						}
					}catch(IndexOutOfBoundsException e){
//							e.printStackTrace();
						continue;//词条没有合适版本的，继续下一条
					}
				}
				Connect.close();
			}
			
		//加入数据库rule中
		public void getBaiduDateBaseRule(){
			Connect.connect();
			for(int i=0;i<rules.size();i++){
				Connect.insertRule(new Rule(rules.get(i).get(0),rules.get(i).get(1),rules.get(i).get(2),rules.get(i).get(3),rules.get(i).get(4),(spiderTime+textTime.get(i))));
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
			ArrayList<String> surfacerule=new ArrayList<String>();
			for(int i=0;i<rules.size();i++){
				surfacerule.add(rules.get(i).get(0)+"-"+
						rules.get(i).get(1));
			}
			if(!surfacerule.isEmpty())Start.addList(surfacerule);//加入界面list中
		}
	
}
