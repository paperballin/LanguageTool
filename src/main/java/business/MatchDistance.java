package business;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class MatchDistance {
	private String right;
	private String wrong;
	private Multimap<String,String> mat=ArrayListMultimap.create();
	
	//构造函数
	public MatchDistance(String right,String wrong){
		this.right=right;
		this.wrong=wrong;
	}
	
	//运行
	public void run(){
		testJaro();
	}
	
	//Jaro距离
	private void testJaro(){
		String[] sr=right.split("。");
		String[] sw=wrong.split("。");
		for(String r:sr){
			double jmax=0;
			double fmax=0;
			double lmax=100;
			String rightsentence=null;
			String wrongsentence=null;
			for(String w:sw){
				double jaro=StringUtils.getJaroWinklerDistance(r, w);	//jarowinkler距离
				double fuzzy=StringUtils.getFuzzyDistance(r, w, Locale.CHINA);	//相同字符算法
				double levene=StringUtils.getLevenshteinDistance(r, w);		//莱文斯坦距离
//				if(jaro>=jmax){
//					jmax=jaro;
//					rightsentence=r;
//					wrongsentence=w;
////					System.out.println("Jaro:"+r+"/"+w+jaro);
//				}
//				if(fuzzy>=fmax){
//					fmax=fuzzy;
//					rightsentence=r;
//					wrongsentence=w;
////					System.out.println("Fuzzy:"+r+"/"+w+fuzzy);
//				}
				if(levene<=lmax){
					lmax=levene;
					rightsentence=r;
					wrongsentence=w;
//					System.out.println("Levene:"+r+"/"+w+levene);
				}
			}
			if(rightsentence!=null&&wrongsentence!=null)mat.put(rightsentence, wrongsentence);
		}
		System.out.println(mat);
	}
}
