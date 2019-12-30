package business;

import java.util.ArrayList;

public interface SpiderMaker {
	public void run();	//运行
	public ArrayList<String> getWordList(String url);	//输入分类链接，获取词条名称而后构建链接
	public ArrayList<String> getHistoryList();	//直接获取历史列表链接集
	public ArrayList<String> getFixedVersion(ArrayList<String> urls);	//直接获取合适历史版本链接集
	public ArrayList<ArrayList<String>> matchRules(ArrayList<String> urls);	//输入合适历史版本链接集，获取规则集
	public void seeResults();	//查看结果
	public void seeNum();	//查看数据
}
