package testTool;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.dictionary.CoreSynonymDictionary;
import com.hankcs.hanlp.suggest.Suggester;

import business.MatchHanLP;

public class TestHanlp {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根 
//		MatchHanLP.find("发你发思念嗲声的发生的法师都发士大夫阿三都发送方的sdafasdfasfds4546878643136fsdaf的发达'''''''''''''..,.,.,.,.。。，。，。‘’“”“”；：", "fdasfe56df的发达    --==" );
	HanLP.Config.ShowTermNature=false;
		System.out.println(HanLP.segment("在镇压黄巢起义中有大功的节度使李克用与朱玫朱全忠因争权夺利而发生了火并事件长安再度告急"));
	}

}
