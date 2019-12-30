package testTool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

public class TestWebMagic implements PageProcessor{
	private Site site=Site.me().setRetryTimes(3).setSleepTime(100);;
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
//	    httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
//	    new Proxy("101.101.101.101",8888)
//	    ,new Proxy("102.102.102.102",8888)));
		Spider.create(new TestWebMagic()).addUrl("http://baike.baidu.com/fenlei/社会心理学").addPipeline(new ConsolePipeline()).thread(3).run();
//		Spider.create(new TestWebMagic()).addUrl("https://www.sogou.com/sogou?query=文化&pid=sogou-wsse-17737832ac17be52&duppid=1&insite=baike.sogou.com&sugsuv=004C51C30E7BFF185CBED7EA905E1716&sut=1161&sugtime=1556031115766&lkt=0%2C0%2C0&s_from=result_up&sst0=1556031115766&cid=&page=4&ie=utf8&w=01029901&dr=1").addPipeline(new ConsolePipeline()).thread(3).run();
	}

	public Site getSite() {
		// TODO 自动生成的方法存根
		return site;
	}
	private int magicn=0;
	public void process(Page page) {
		// TODO 自动生成的方法存根
		long start=System.currentTimeMillis();
		processBaidu(page);
		long end=System.currentTimeMillis();
		magicn+=(end-start);
		System.out.println("webmagic:"+magicn);
		long jst=System.currentTimeMillis();
		try {
			Document doc=Jsoup.connect("http://baike.baidu.com/fenlei/社会心理学").get();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		long jend=System.currentTimeMillis();
		System.out.println("Jsoup:"+(jend-jst));
//		processSougou(page);
	}
	
	//爬百度
	private void processBaidu(Page page){
		if(page.getUrl().toString().contains("fenlei")){
			page.putField("list",page.getHtml().css("div.photo>a").regex("href=\"([^\"]*)\"").all());
			page.addTargetRequests(page.getHtml().css("div.photo>a").regex("href=\"([^\"]*)\"").all());
		}else if(page.getUrl().toString().contains("view")){
			List<String> historylist=new ArrayList<String>();
			historylist.add(page.getHtml().css("dl.side-box").links().get());
			page.putField("historylist",historylist);
			page.addTargetRequests(historylist);
		}else if(page.getUrl().toString().contains("historylist")){
			page.putField("result", page.getHtml());
		}
	}
	//搜狗
	private void processSougou(Page page){
		{
			page.putField("history",page.getHtml().css("h3.vrTitle").all());
		}
	}

}
