package testParameter;

import org.apache.commons.lang3.StringUtils;

public class TestSimilarity {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String s1="我爱中国，中国是我的祖国。今年过节回家看看。我思故我在。";
		String s2="我爱中华，中国是我的中国。今年过节常回家看看。我思我在。";
		
		for(String s:s1.split("。")){
			for(String l:s2.split("。")){
				double jsim=StringUtils.getJaroWinklerDistance("s","l");
				System.out.println(s+"\n"+l+jsim);
			}
		}
	}

}
