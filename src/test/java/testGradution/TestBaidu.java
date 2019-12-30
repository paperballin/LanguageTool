package testGradution;
import java.util.ArrayList;

import business.Baidu;

public class TestBaidu {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Baidu bai=new Baidu("http://baike.baidu.com/fenlei/各国艺术",0.7,"各国艺术");
		bai.run();
		bai.seeResults();
		
	}

}
