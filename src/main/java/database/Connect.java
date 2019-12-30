package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Connect{
	//jdbc驱动和数据库url
	static final String JDBC_DRIVER="com.mysql.cj.jdbc.Driver";	
	static final String DB_URL="jdbc:mysql://localhost:3306/graduation?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
	
	//数据库用户名和密码
	static final String USER="root";
	static final String PASS="Linlin.1996.ok";
	static Connection conn=null;
	static Statement stmt=null;
	
	//连接数据库
	public static void connect(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn=DriverManager.getConnection(DB_URL,USER,PASS);
			stmt=conn.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	
	//执行某个sql语句
		public static void execute(String sql){
	        boolean rs;
			try {
				rs = stmt.execute(sql);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
		//插入wordlist
		public static void insertWordList(WordList wordlist) {
			// TODO 自动生成的方法存根
			int i = 0;
			String sql=" insert into wordlist (name,type,enclo,url,historyurl,time) values(?,?,?,?,?,?)";
		    PreparedStatement pstmt;
		    try {
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        pstmt.setString(1, wordlist.getName());
		        pstmt.setString(2, wordlist.getType());
		        pstmt.setString(3, wordlist.getEnclo());
		        pstmt.setString(4, wordlist.getUrl());
		        pstmt.setString(5, wordlist.getHistoryurl());
		        pstmt.setLong(6, wordlist.getTime());
		        i = pstmt.executeUpdate();
		        pstmt.close();
		    } catch (SQLException e) {
		    	System.out.println("插入wordlist错误");
		    }
		}
		//插入versionlist
		public static void insertVersionList(VersionList versionlist) {
			// TODO 自动生成的方法存根
			int i = 0;
			String sql=" insert into versionlist (name,newurl,oldurl,compareurl,time) values(?,?,?,?,?)";
		    PreparedStatement pstmt;
		    try {
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        pstmt.setString(1, versionlist.getName());
		        pstmt.setString(2, versionlist.getNewurl());
		        pstmt.setString(3, versionlist.getOldurl());
		        pstmt.setString(4, versionlist.getCompareurl());
		        pstmt.setLong(5, versionlist.getTime());
		        i = pstmt.executeUpdate();
		        pstmt.close();
		    } catch (SQLException e) {
		       System.out.println("插入versionlist错误");
		    }
		}
		
		//插入rules
		public static void insertRule(Rule rule) {
			// TODO 自动生成的方法存根
			int i = 0;
			String sql=" insert into rule (newword,oldword,sentence,newurl,oldurl,time) values(?,?,?,?,?,?)";
		    PreparedStatement pstmt;
		    try {
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        pstmt.setString(1, rule.getNewword());
		        pstmt.setString(2, rule.getOldword());
		        pstmt.setString(3, rule.getSentence());
		        pstmt.setString(4, rule.getNewurl());
		        pstmt.setString(5, rule.getOldurl());
		        pstmt.setLong(6, rule.getTime());
		        i = pstmt.executeUpdate();
		        pstmt.close();
		    } catch (SQLException e) {
		    	System.out.println("插入rule错误");
		    }
		}
		
		//单个表测试用，rules表
		public static void insertRules(ArrayList<String> s) {
			int i = 0;
			String sql=" insert into rules (rulename) values(?)";
		    PreparedStatement pstmt;
		    try {
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        for(int j=0;j<s.size();j++){
//		        	pstmt.setInt(1, j);
			        pstmt.setString(1, s.get(j));
			        i = pstmt.executeUpdate();
		        }
		        pstmt.close();
		    } catch (SQLException e) {
		    	System.out.println("插入rules错误");
		    }
		}
		
		//插入historys表
		public static void insertHistory(ArrayList<String> s) {
			int i = 0;
			String sql=" insert into historys (history) values(?)";
		    PreparedStatement pstmt;
		    try {
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        for(int j=0;j<s.size();j++){
//		        	pstmt.setInt(1, j);
			        pstmt.setString(1, s.get(j));
			        i = pstmt.executeUpdate();
		        }
		        pstmt.close();
		    } catch (SQLException e) {
		    	System.out.println("插入Historys错误");
		    }
		}
		//插入words表
		public static void insertWords(ArrayList<String> s,ArrayList<String> l) {
			int i = 0;
			String sql=" insert into words (word,title) values(?,?)";
		    PreparedStatement pstmt;
		    try {
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        for(int j=0;j<s.size();j++){
//		        	pstmt.setInt(1, j);
			        pstmt.setString(1, s.get(j));
			        pstmt.setString(2,l.get(j));
			        i = pstmt.executeUpdate();
		        }
		        pstmt.close();
		    } catch (SQLException e) {
		    	System.out.println("插入words错误");
		    }
		}
		
		//加入versions
		public static void insertVersionsAAS(ArrayList<ArrayList<String>> s) {
			int i = 0;
			String sql=" insert into versions (newversion,oldversion) values(?,?)";
		    PreparedStatement pstmt;
		    try {
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        for(int j=0;j<s.size();j++){
			        pstmt.setString(1, s.get(j).get(0));
			        pstmt.setString(2, s.get(j).get(1));
			        i = pstmt.executeUpdate();
		        }
		        pstmt.close();
		    } catch (SQLException e) {
		    	System.out.println("插入versions错误");
		    }
		}
		public static void insertVersionsAS(ArrayList<String> s) {
			int i = 0;
			String sql=" insert into versions (newversion,oldversion) values(?,?)";
		    PreparedStatement pstmt;
		    try {
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        for(int j=0;j<s.size()-1;j++){
			        pstmt.setString(1, s.get(j));
			        pstmt.setString(2, s.get(j+1));
			        i = pstmt.executeUpdate();
		        }
		        pstmt.close();
		    } catch (SQLException e) {
		    	System.out.println("插入versions错误");
		    }
		}
		//查
		public static void select(String sql) {
			// TODO 自动生成的方法存根
			PreparedStatement pstmt;
		    try {
		        pstmt = (PreparedStatement)conn.prepareStatement(sql);
		        ResultSet rs = pstmt.executeQuery();
		        int col = rs.getMetaData().getColumnCount();
		        System.out.println("============================");
		        while (rs.next()) {
		            for (int i = 1; i <= col; i++) {
		                System.out.print(rs.getString(i) + "\t");
		                if ((i == 2) && (rs.getString(i).length() < 8)) {
		                    System.out.print("\t");
		                }
		             }
		            System.out.println("");
		        }
		            System.out.println("============================");
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
		
		//关闭连接
			public static void close(){
				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}

}
