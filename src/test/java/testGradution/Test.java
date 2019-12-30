package testGradution;

import java.util.ArrayList;

import business.Baidu;

public class Test {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Baidu b=new Baidu("http://baike.baidu.com/fenlei/环保组织",0.5,"dd");
		ArrayList<String> s=new ArrayList<String>();
		s.add("https://baike.baidu.com/item/水俣事件");
		b.getHistoryList(s);
	}

}
