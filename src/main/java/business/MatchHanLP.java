package business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.CoreSynonymDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.suggest.Suggester;

import surface.Start;

public class MatchHanLP {
	private static Suggester suggester;
	private static List<String> rs=new ArrayList<String>();
	private static Multimap<String, String> mat = ArrayListMultimap.create();//	匹配句子图
	private static ArrayList<ArrayList<String>> rules=new ArrayList<ArrayList<String>>();//规则结果
	private static String newurl=null;
	private static String oldurl=null;
	private static int matchnum=0;
	private static int rightnum=0;
	private static double rate=0;
	
	//寻找规则
	public static ArrayList<ArrayList<String>> find(String right,String wrong,double rate,String newurl,String oldurl){
		MatchHanLP.rate=rate;
		MatchHanLP.newurl=newurl;
		MatchHanLP.oldurl=oldurl;
		suggester=new Suggester();
		right=parse(right);//文本预处理
		wrong=parse(wrong);//文本预处理
		insertSuggester(right);//将新版本的句子插入suggester
		compareSuggester(wrong);//将旧版本的句子加入MultipleMap
//		Start.setText(mat);
		getResult();
		seeResults();
//		seeNum();
		return rules;
		
	}
	
	//文本预处理
	private static String parse(String text){
		Pattern pattern = Pattern.compile("[0-9]|[\\u4e00-\\u9fa5]|\\，|\\。|\\、|\\；|\\“|\\”\\》\\《");  //匹配中文和部分中文符号
		Matcher matcher = pattern.matcher(text);  
		List<String> str=new ArrayList<String>();
		while (matcher.find()) { 
			text=matcher.group();
			str.add(text);
		}
		for(String t:str.toArray(new String[str.size()])){
			text+=t;
		}
		text=text.replace("标注", "").replace("内链","");//百度百科网页文本的特殊字符
		text=text.replace("正文 - ","");//互动百科网页文本的特殊字符
//			Start.setText(text);
		return text;
	}
		
	//添加如入Suggester
	private static void insertSuggester(String text){
		String[] str=text.split("。|，");
		for(String t:str){
			rs.add(t);
			suggester.addSentence(t);
		}
	}
	
	//分析比较Suggester相似句
	private static void compareSuggester(String text){
		String[] str=text.split("。|，");
		double weight=0;//相似距离
		for(String oldt:str){
			List<String> sl=new ArrayList<String>();
			sl=suggester.suggest(oldt, 1);	//智能推荐
			String newt=null;
			if(!sl.isEmpty()){
				newt=sl.get(0);//找一个最相近的语句	
				weight=StringUtils.getJaroWinklerDistance(oldt, newt);//利用JaroWinkler作为权重
				if(weight!=1&&weight>rate){
					Start.setText("newr:"+oldt+"\n"+"oldr:"+newt+"\n"+"weight:"+weight);
					mat.put(oldt, newt);
					matchnum++;
				}
//				if(weight!=1&&weight>0.6)rightnum++;//测试文本相似度阈值
			}
		}
	}
	
	//获取比对结果
	private static void getResult(){
		List<Term> newsegment=new ArrayList<Term>();
		List<Term> oldsegment=new ArrayList<Term>();
		String newsentence=null;
		for(Entry<String, String> entry: mat.entries())
        {
			newsentence=entry.getKey();//更新句子
			newsegment=HanLP.segment(entry.getKey());//更新句子分词结果
			oldsegment=HanLP.segment(entry.getValue().toString().replace("[","").replace("]",""));	//将Term类型的错误词变String类型，去除”[]“
			for(int i=0;i<newsegment.size();i++){
				for(int j=0;j<oldsegment.size();j++){
					Term newterm=newsegment.get(i);
					Term oldterm=oldsegment.get(j);
					Nature nature=newterm.nature;//相同的那个词性（直接用新版本的词性即可）
					if(oldterm.nature.equals(nature)&&isFixedNature(nature)){	//词性相同且为合适的词性
						if(nature.startsWith("m")){		//如果是数字就变中文
							newterm.word=changeNumtoChinese(newterm.word);
							oldterm.word=changeNumtoChinese(oldterm.word);
						}
						double sim=CoreSynonymDictionary.similarity(newterm.word, oldterm.word);//词语语义相似度
						if(sim==1&&!newterm.word.equals(oldterm.word)){		//相似度为1且词语不同
							if(!isExistentRule(newterm.word,oldterm.word)){		//判断是否存在已有规则
								ArrayList<String> s=new ArrayList<String>();
								s.add(newterm.word);
								s.add(oldterm.word);
								s.add(newsentence);
								s.add(newurl);
								s.add(oldurl);
								rules.add(s);
							}
						}
					}
				}
			}
        }
//		Start.setText(results);
	}
	
	
	//阿拉伯数字变中文
	private static String changeNumtoChinese(String s){
		Pattern rex=Pattern.compile("[0-9]");
		ArrayList<String> digit=new ArrayList<String>();//位数List
		ArrayList<String> num=new ArrayList<String>();//数字List
		String chinese=new String();//最终结果
		String word=new String(s);//未过滤文本
		Matcher m=rex.matcher(s);
		if(m.find()){
			String numword=word.replaceAll("[^0-9]","").replaceAll(" ","").trim();//过滤只剩下数字的文本
//			Start.setText(numword.length());
			//位数变中文
			switch(numword.length()){
			case 5:digit.add("万");
			case 4:digit.add("千");
			case 3:digit.add("百");
			case 2:digit.add("十");
			case 1:digit.add("");break;
			default:return s;
			}
			//数字变中文
			for(int j=0;j<numword.length();j++){
				int n=Integer.parseInt(String.valueOf(numword.charAt(j)));
				switch(n){
				case 0:num.add("零");break;
				case 1:num.add("一");break;
				case 2:num.add("二");break;
				case 3:num.add("三");break;
				case 4:num.add("四");break;
				case 5:num.add("五");break;
				case 6:num.add("六");break;
				case 7:num.add("七");break;
				case 8:num.add("八");break;
				case 9:num.add("九");break;
				default:return s;
				}
			}
			//异常处理（对零的处理）
			int flag=0;
			for(int k=num.size()-1;k>=0;k--){
				if(num.get(k)!="零"){
					flag=1;
					continue;
				}
				else if(num.get(k)=="零"&&k==0)break;
				else if(num.get(k)=="零"&&flag==0){
					num.remove(k);
					digit.remove(k);
				}else if(num.get(k)=="零"&&flag==1){
					digit.set(k,"");
				}
			}
			//赋值给chinese
			for(int l=0;l<digit.size();l++){
				chinese+=num.get(l)+digit.get(l);
			}
			word=word.replaceAll(numword,chinese);
			s=word;
		}
		return s;
	}
	
	//匹配合适的词性
	private static boolean isFixedNature(Nature nature){
		ArrayList<String> naturedic=new ArrayList<String>();
		naturedic.add("an");	//名形词
		naturedic.add("g");		//学术词	
		naturedic.add("n");		//名词（可识别人名、地名等）
		naturedic.add("i");		//成语
		naturedic.add("l");		//习用语
		naturedic.add("t");		//时间词
		naturedic.add("s");		//处所词
		naturedic.add("m");		//数量词
		for(int i=0;i<naturedic.size();i++){
			if(nature.startsWith(naturedic.get(i)))return true;
		}
		return false;
		
	}
	//判断是否存在已有规则
	private static boolean isExistentRule(String newterm,String oldterm){
		for(int i=0;i<rules.size();i++){
			if(rules.get(i).get(0).equals(newterm)&&rules.get(i).get(1).equals(oldterm)){
				return true;
			}
		}
		return false;
	}
	//查看结果
	private static void seeResults(){
		if(!mat.isEmpty()&&!rules.isEmpty()){
			for(Entry<String, String> entry:mat.entries()){
				Start.setText("mat: "+"\n"+"left:"+entry.getKey()+"\n"+"right:"+entry.getValue());
			}
			for(int i=0;i<rules.size();i++){ 
				Start.setText(rules.get(i).get(0));
				Start.setText(rules.get(i).get(1));
				Start.setText(rules.get(i).get(2));
			}
		}else{
			Start.setText("无结果，请先执行getResult()");
		}
	}
	public static void seeNum(){
		System.out.println("matchnum:"+matchnum);
		System.out.println("rightnum:"+rightnum);
	}
	public static int getMatchnum() {
		return matchnum;
	}

	public static int getRightnum() {
		return rightnum;
	}
	
}
