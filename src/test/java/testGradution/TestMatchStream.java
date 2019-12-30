package testGradution;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import business.MatchHanLP;

public class TestMatchStream {
	private static String right=null;
	private static String wrong=null;
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String path1="D:\\test\\1.txt";
		String path2="D:\\test\\2.txt";
		File file1=new File(path1);
		File file2=new File(path2);
		if(file1.isFile()&&file1.exists()&&file2.isFile()&&file2.exists()){
			try {
				InputStreamReader isr1=new InputStreamReader(new FileInputStream(file1),"utf-8");
				BufferedReader br1=new BufferedReader(isr1);
				InputStreamReader isr2=new InputStreamReader(new FileInputStream(file2),"utf-8");
				BufferedReader br2=new BufferedReader(isr2);
				String line1=null;
				String line2=null;
				while((line1=br1.readLine())!=null&&(line2=br2.readLine())!=null){
					right+=line1;
					wrong+=line2;
				}
				br1.close();
				br2.close();
			} catch (UnsupportedEncodingException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}else{
			System.out.println("无法找到");
		}
		
		MatchHanLP.find(right,wrong,0.6,"","");
	}

}
