package database;

public class Rule {
	private String newword;
	private String oldword;
	private String sentence;
	private String newurl;
	private String oldurl;
	private long time;
	
	public Rule(String newword, String oldword, String sentence, String newurl, String oldurl, long time) {
		this.newword = newword;
		this.oldword = oldword;
		this.sentence = sentence;
		this.newurl = newurl;
		this.oldurl = oldurl;
		this.time = time;
	}
	public String getNewword() {
		return newword;
	}
	public void setNewword(String newword) {
		this.newword = newword;
	}
	public String getOldword() {
		return oldword;
	}
	public void setOldword(String oldword) {
		this.oldword = oldword;
	}
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
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
	public long getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
}
