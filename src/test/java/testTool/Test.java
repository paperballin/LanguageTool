package testTool;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.BreadthCrawler;

public class Test extends BreadthCrawler{

	public Test(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
		// TODO 自动生成的构造函数存根
	}

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		long start=System.currentTimeMillis();
		 
		long end=System.currentTimeMillis();
		System.out.println(end-start);
	}

	public void visit(Page arg0, CrawlDatums arg1) {
		// TODO 自动生成的方法存根
		Test crawler = new Test("crawl",true);
        crawler.addSeed("http://www.xinhuanet.com/");  
        crawler.addRegex("http://www.xinhuanet.com/.*");     
        /*网页、图片、文件被存储在download文件夹中*/
//        crawler.setRoot("download");
        /*进行深度为5的爬取*/
        try {
			crawler.start(5);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
