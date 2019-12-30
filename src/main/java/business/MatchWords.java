package business;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

public class MatchWords{
	private String text;
	private String text1;
	private String text2;
	private List<Term> words1;					//句子一分词结果
	private List<Term> words2;					//句子二分词结果
	private String filterwords1;				//String类型的句子一分词过滤结果
	private String filterwords2;				//String类型的句子二分词过滤结果
	private ArrayList<String> dividewords1;		//ArrayList存储filterword1结果
	private ArrayList<String> dividewords2;		//ArrayList存储filterwords2结果
	private ArrayList<ArrayList<String>> results;
	private int size;						
	
	//构造函数
	public MatchWords(String text,String ctext){
		this.text=ctext;
		text1=text.replaceAll("\\p{P}","");
		text2=ctext.replaceAll("\\p{P}","");
		words1 = HanLP.segment(text1);
		words2 = HanLP.segment(text2);
		filterwords1=words1.toString().replace("[","").replace("]","").replace(" ","").trim();
		filterwords2=words2.toString().replace("[","").replace("]","").replace(" ","").trim();
		dividewords1=new ArrayList<String>(Arrays.asList(filterwords1.split(",")));
		dividewords2=new ArrayList<String>(Arrays.asList(filterwords2.split(",")));
		dividewords1=changeNumtoChinese(dividewords1);
		dividewords2=changeNumtoChinese(dividewords2);
//		if(dividewords1.size()!=dividewords2.size()){
//			fixDivideWords(dividewords1,dividewords2);
//		}
		size=dividewords1.size();
		results=new ArrayList<ArrayList<String>>();
	}
	
	//匹配句子中不同的词语
	public ArrayList<ArrayList<String>> matchWords() {
		if(dividewords1.size()!=dividewords2.size())return results;	//分词结果长度不相等，不进行具体分析
		for(int i=0;i<size;i++){
			String a=dividewords1.get(i).trim();
			String b=dividewords2.get(i).trim();
			ArrayList<String> s=new ArrayList<String>();
			if(a.equals(b)==false&&
			   isContainChinese(a)&&
			   isContainChinese(b)&&
			   !results.contains(a)&&
			   !results.contains(b)&&
			   a!=null&&
			   b!=null&& 
			   a.length()!=1&&
			   b.length()!=1
			   ){
				s.add(a);
				s.add(b);
				s.add(this.text);
				results.add(s);
//				System.out.println("已加入规则组");
			}
		}
		return results;
	}

	//查看去标点符号的文本
	public void printText(){
		System.out.println("text1:"+text1);
		System.out.println("text2:"+text2);
	}
	
	//查看分词结果
	public void printWords(){
		System.out.println("words1:"+words1);
		System.out.println("words2:"+words2);
	}
	
	//查看过滤结果
	public void printFilterWords(){
		System.out.println("filterwords1:"+filterwords1);
		System.out.println("filterwords2:"+filterwords2);
	}
	
	//查看过滤分词组
	public void printDivideWords(){
		System.out.println("dividewords1:"+dividewords1);
		System.out.println("dividewords2:"+dividewords2);
	}
	
	//返回过滤分词组
	public ArrayList<String> getDidvideWords1(){
		return dividewords1;
	}
	public ArrayList<String> getDivideWords2(){
		return dividewords2;
	}
	
	//查看规则结果
	public void printResults(){
		System.out.println(results);
	}
	
	//判断是否为中文字符串
	public boolean isContainChinese(String word){
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(word);
        if (m.find()) {
            return true;
        }
        return false;
	}
	
	//分词长度不同时处理分词链表
	public void fixDivideWords(ArrayList<String> one,ArrayList<String> two){
		for(int i=0;i<one.size();i++){
			for(int j=i;j<two.size();j++){
				if(one.get(i).equals(two.get(j))){
					one.remove(i);
					two.remove(j);
				}
			}
		}
		System.out.println("one:"+one);
		System.out.println("two:"+two);
		ArrayList<String> fitwords=new ArrayList<String>();
		int start=0;
		int end =0;
		if(one.size()>two.size()){
			for(int i=0;i<two.size();i++){
				end=start+two.get(i).length();
				fitwords.add(dividewords1.toString().replace(" ","").replace(",","").replace("[","").replace("]","").substring(start, end));
				start=end;
			}
			dividewords1=fitwords;
		}else if(one.size()<two.size()){
			String temp=dividewords2.toString().replace(" ","").replace(",","").replace("[","").replace("]","");
			System.out.println("temp:"+temp);
			for(int i=0;i<one.size();i++){
				end=start+one.get(i).length();
				System.out.println("start:"+start+"   end:"+end);
				System.out.println(temp.substring(start, end));
				fitwords.add(temp.substring(start, end));
				start=end;
			}
			dividewords2=fitwords;
		}
	}
	
	//阿拉伯数字变中文
	public ArrayList<String> changeNumtoChinese(ArrayList<String> s){
		Pattern rex=Pattern.compile("[0-9]");
		for(int i=0;i<s.size();i++){
			ArrayList<String> digit=new ArrayList<String>();	//位数List
			ArrayList<String> num=new ArrayList<String>();		//数字List
			String chinese=new String();//最终结果
			String word=new String(s.get(i));
			Matcher m=rex.matcher(s.get(i));
			if(m.find()){
				String numword=word.replaceAll("[^0-9]","").replaceAll(" ","").trim();
//				System.out.println(numword.length());
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
				//异常处理
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
				s.set(i,word);
			}
		}
		return s;
	}
	
	
}