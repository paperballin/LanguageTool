package testGradution;
import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TestHtmlUnit {

	public static void main(String[] args) {
		WebClient webclient=new WebClient(BrowserVersion.BEST_SUPPORTED);
		HtmlPage page;
		HtmlDivision div;
		for(int i=0;i<10;i++){
			try {
				page=webclient.getPage("https://baike.sogou.com/ShowHistory.e?sp=l4422971");
				System.out.println(page.asText());
			} catch (FailingHttpStatusCodeException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		webclient.close();
	}

}
