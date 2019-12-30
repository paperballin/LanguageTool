package testGradution;
import business.Sougou;

public class TestSougou {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Sougou s=new Sougou("https://www.sogou.com/sogou?query=历史&pid=sogou-wsse-17737832ac17be52&duppid=1&insite=baike.sogou.com&sugsuv=1554710539248452&sut=1621&sugtime=1554712427540&lkt=0%2C0%2C0&s_from=result_up&sst0=1554712427540&cid=&page=1&ie=utf8&w=01029901&dr=1",0.6,"历史");
		s.run();
		s.seeResults();
	}

}
