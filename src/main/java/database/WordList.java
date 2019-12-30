package database;

public class WordList {
	private String name;
	private String type;
	private String enclo;
	private String url;
	private String historyurl;
	private long time;
	
	public WordList(String name,String type,String enclo,String url,String historyurl,long time){
		this.name=name;
		this.type=type;
		this.enclo=enclo;
		this.url=url;
		this.historyurl=historyurl;
		this.time=time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getEnclo() {
		return enclo;
	}
	public void setEnclo(String enclo) {
		this.enclo = enclo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHistoryurl() {
		return historyurl;
	}
	public void setHistoryurl(String historyurl) {
		this.historyurl = historyurl;
	}
	public long getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
}
