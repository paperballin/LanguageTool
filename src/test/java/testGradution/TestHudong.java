package testGradution;
import business.Hudong;

public class TestHudong {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Hudong h=new Hudong("http://fenlei.baike.com/历史人物/list/",0.6,"历史人物");
		h.run();
		h.seeResults();
		h.seeNum();
	}

}
