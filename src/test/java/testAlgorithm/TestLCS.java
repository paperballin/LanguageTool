package testAlgorithm;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.hankcs.hanlp.suggest.Suggester;

public class TestLCS {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Document doc;
		try {
			doc = Jsoup.connect("https://baike.baidu.com/historydiff/%E9%92%A9%E7%9B%B2%E8%9B%87/3193221/113811152/113113460").get();
			Elements left=doc.select("div.divleft");
			Elements right=doc.select("div.divright");
			String x = left.text();
			String y = right.text();
			long lcsstart=System.currentTimeMillis();
			for(String l:x.split("。")){
				for(String r:x.split("。")){
					try{
//					SimilarDegree(l,r);
					System.out.println(SimilarDegree(l,r));
					}catch(Exception e){
						System.out.println("error");
					}
				}
			}
			long lcsend=System.currentTimeMillis();
			long suggesterstart=System.currentTimeMillis();
			Suggester suggester=new Suggester();
			for(String l:x.split("。")){
				suggester.addSentence(l);
				
			}
			for(String r:x.split("。")){
				suggester.suggest(r, 1);
			}
			long suggesterend=System.currentTimeMillis();
			System.out.println(lcsend-lcsstart);
			System.out.println(suggesterend-suggesterstart);
//			getLCS(x,y);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	//
	public static double SimilarDegree(String strA, String strB){   
        String newStrA = removeSign(strA);    
        String newStrB = removeSign(strB);
        //用较大的字符串长度作为分母，相似子串作为分子计算出字串相似度
        int temp = Math.max(newStrA.length(), newStrB.length());    
        int temp2 = longestCommonSubstring(newStrA, newStrB).length();   
        return temp2 * 1.0 / temp;    
    }  
  
    
    /*
     * 将字符串的所有数据依次写成一行
     * */
    public static String removeSign(String str) {   
        StringBuffer sb = new StringBuffer(); 
        //遍历字符串str,如果是汉字数字或字母，则追加到ab上面
        for (char item : str.toCharArray())   
            if (charReg(item)){    
                sb.append(item);  
            }  
        return sb.toString();  
    }  
  
    
    /*
     * 判断字符是否为汉字，数字和字母，
     * 因为对符号进行相似度比较没有实际意义，故符号不加入考虑范围。
     * */
    public static boolean charReg(char charValue) {    
        return (charValue >= 0x4E00 && charValue <= 0X9FA5) || (charValue >= 'a' && charValue <= 'z')
        		|| (charValue >= 'A' && charValue <= 'Z')  || (charValue >= '0' && charValue <= '9');    
    }    
  
    
    /*
     * 求公共子串，采用动态规划算法。
     * 其不要求所求得的字符在所给的字符串中是连续的。
     * 
     * */
    public static String longestCommonSubstring(String strA, String strB) {   
        char[] chars_strA = strA.toCharArray();
        char[] chars_strB = strB.toCharArray(); 
        int m = chars_strA.length;   
        int n = chars_strB.length; 
        
        /*
         * 初始化矩阵数据,matrix[0][0]的值为0，
         * 如果字符数组chars_strA和chars_strB的对应位相同，则matrix[i][j]的值为左上角的值加1，
         * 否则，matrix[i][j]的值等于左上方最近两个位置的较大值，
         * 矩阵中其余各点的值为0.
        */
        int[][] matrix = new int[m + 1][n + 1];   
        for (int i = 1; i <= m; i++) {  
            for (int j = 1; j <= n; j++) {    
                if (chars_strA[i - 1] == chars_strB[j - 1])   
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;    
                else   
                    matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);   
            }   
        }
        /*
         * 矩阵中，如果matrix[m][n]的值不等于matrix[m-1][n]的值也不等于matrix[m][n-1]的值，
         * 则matrix[m][n]对应的字符为相似字符元，并将其存入result数组中。
         * 
         */
        char[] result = new char[matrix[m][n]];    
        int currentIndex = result.length - 1;   
        while (matrix[m][n] != 0) {   
            if (matrix[n] == matrix[n - 1])  
                n--;   
            else if (matrix[m][n] == matrix[m - 1][n])    
                m--;   
            else {   
                result[currentIndex] = chars_strA[m - 1];   
                currentIndex--;  
                n--;   
                m--;  
            }  
        }    
       return new String(result);   
    }  
    
    
    /*
     * 结果转换成百分比形式 
     * */   
    public static String similarityResult(double resule){    
        return  NumberFormat.getPercentInstance(new Locale( "en ", "US ")).format(resule);   
    } 
}


