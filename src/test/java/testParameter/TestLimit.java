package testParameter;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestLimit {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		int limit=100; 
		for(;limit<200;limit+=50){
			int index=1;
			int num=0;
			boolean next;
			long start=System.currentTimeMillis();
			try{
				Document doc;
				do{
//					System.out.println("index:"+index);
					String url="http://baike.baidu.com/fenlei/中国历史"
							+ "?limit="+limit+"&index="+index+"&offset=30#gotoList";
					doc=Jsoup.connect(url).timeout(20000).get();
			//			System.out.println(doc);
					Elements links=doc.select("div.photo");
			//			System.out.println(links);
					for(Element link:links){
						String a=link.childNode(0).attr("href");
						String title=link.child(0).attr("title");
	//					System.out.println("url:"+a+"   title:"+title);
						num++;
					}
					index++;
					next=doc.select("div.clearfix").next().hasText();
				}while(next);
				long end=System.currentTimeMillis();
				System.out.println("limit:"+limit+"   num:"+num+"   index:"+(index-2)+"   time:"+(end-start)+"ms");
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
	}

}
