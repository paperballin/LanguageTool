package testGradution;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;

import business.MatchDistance;
import business.MatchHanLP;

public class TestMatchString {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String line="你好啊，今天天气不错。是啊是啊，开心。嗯明天见。为了瓦坎达。瓦斯罗达";
		String line1="你好呵呵，今天天气不错。是吧，还行。唉天天见。为了部落。瓦斯咔哒";
		String text0="Hello,my friend,I love you";
		String text1="Hello,my father,I see you";
		System.out.println(HanLP.segment(text0.replaceAll("[a-zA-Z]","")));
		System.out.println(HanLP.segment(text1));
//		MatchHanLP m=new MatchHanLP(line,line1);
//		m.find();
		MatchDistance mj=new MatchDistance(line,line1);
		mj.run();
		
	}

}
