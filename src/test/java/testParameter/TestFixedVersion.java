package testParameter;

public class TestFixedVersion {
	private int totalnum;
	private int fixednum;
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		
	}
	
	public boolean isFixed(String word){
		if(word.contains("修改内容")||
				word.contains("修正")||
				word.contains("改正")||
				word.contains("改为")||
				word.contains("更正")||
				word.contains("补充完善")||
				word.contains("添加概述")||
				word.contains("编辑信息"))
		{
			return true;
		}
		return false;
		
	}
}
