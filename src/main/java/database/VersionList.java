package database;

public class VersionList {
	private String name;
	private String newurl;
	private String oldurl;
	private String compareurl;
	private long time;
	public VersionList(String name,String newurl,String oldurl,String compareurl,long time){
		this.name=name;
		this.newurl=newurl;
		this.oldurl=oldurl;
		this.compareurl=compareurl;
		this.time=time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNewurl() {
		return newurl;
	}
	public void setNewurl(String newurl) {
		this.newurl = newurl;
	}
	public String getOldurl() {
		return oldurl;
	}
	public void setOldurl(String oldurl) {
		this.oldurl = oldurl;
	}
	public String getCompareurl() {
		return compareurl;
	}
	public void setCompareurl(String compareurl) {
		this.compareurl = compareurl;
	}
	public long getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
}
