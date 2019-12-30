package surface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JTextArea;
import javax.swing.JProgressBar;
import javax.swing.JList;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import business.Select;
import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Start {

	private JFrame frmLanguage;
	private static JTextArea textArea = new JTextArea();//信息栏
	private static JProgressBar progressBar = new JProgressBar();//进度条
	private static JList<String> list = new JList<String>();//结果列表
	private static int num=0;
	private static double rate=0.6;
	private static ArrayList<String> baidurules=new ArrayList<String>();
	private static ArrayList<String> hudongrules=new ArrayList<String>();
	private static ArrayList<String> sougourules=new ArrayList<String>();
	private Select select=new Select();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Start window = new Start();
					window.frmLanguage.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Start() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLanguage = new JFrame();
		frmLanguage.setTitle("Language中文用语更新");
		frmLanguage.setBounds(100, 100, 672, 476);
		frmLanguage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLanguage.getContentPane().setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("宋体", Font.PLAIN, 13));
		menuBar.setBounds(0, 1, 653, 27);
		frmLanguage.getContentPane().add(menuBar);
		
		JMenu mnNewMenu = new JMenu("开始");
		mnNewMenu.setFont(new Font("宋体", Font.PLAIN, 13));
		menuBar.add(mnNewMenu);
		
		JMenuItem menuItem_2 = new JMenuItem("退出");
		menuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		JMenuItem menuItem_1 = new JMenuItem("导出规则");
		mnNewMenu.add(menuItem_1);
		
		mnNewMenu.add(menuItem_2);
		
		JMenu mnNewMenu_1 = new JMenu("选项");
		mnNewMenu_1.setFont(new Font("宋体", Font.PLAIN, 13));
		menuBar.add(mnNewMenu_1);
		
		JMenu mnNewMenu_4 = new JMenu("设置相似度阈值");
		mnNewMenu_1.add(mnNewMenu_4);
		
		JMenuItem menuItem = new JMenuItem("0.3");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Start.rate=0.3;
				JOptionPane.showMessageDialog(frmLanguage,"设置完成","相似度",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnNewMenu_4.add(menuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("0.4");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Start.rate=0.4;
				JOptionPane.showMessageDialog(frmLanguage,"设置完成","相似度",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnNewMenu_4.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("0.5");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Start.rate=0.5;
				JOptionPane.showMessageDialog(frmLanguage,"设置完成","相似度",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnNewMenu_4.add(mntmNewMenuItem_2);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("0.6");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Start.rate=0.6;
				JOptionPane.showMessageDialog(frmLanguage,"设置完成","相似度",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnNewMenu_4.add(mntmNewMenuItem_3);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("0.7");
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Start.rate=0.7;
				JOptionPane.showMessageDialog(frmLanguage,"设置完成","相似度",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnNewMenu_4.add(mntmNewMenuItem_4);
		
		JMenuItem mntmNewMenuItem_5 = new JMenuItem("0.8");
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Start.rate=0.8;
				JOptionPane.showMessageDialog(frmLanguage,"设置完成","相似度",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnNewMenu_4.add(mntmNewMenuItem_5);
		
		JMenuItem mntmNewMenuItem_6 = new JMenuItem("0.9");
		mntmNewMenuItem_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frmLanguage,"设置完成","相似度",JOptionPane.INFORMATION_MESSAGE);
				Start.rate=0.9;
			}
		});
		mnNewMenu_4.add(mntmNewMenuItem_6);
		
		JMenu mnNewMenu_2 = new JMenu("帮助");
		mnNewMenu_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(frmLanguage,"输入分类后按开始进行规则获取\n"+
						"在选项中可以设置参数","帮助",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnNewMenu_2.setFont(new Font("宋体", Font.PLAIN, 13));
		menuBar.add(mnNewMenu_2);
		
		JMenu mnNewMenu_3 = new JMenu("关于");
		mnNewMenu_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(frmLanguage,"作者：林子源","关于",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnNewMenu_3.setFont(new Font("宋体", Font.PLAIN, 13));
		menuBar.add(mnNewMenu_3);
		
		JLabel label = new JLabel("输入分类：");
		label.setFont(new Font("宋体", Font.PLAIN, 13));
		label.setBounds(19, 36, 72, 15);
		frmLanguage.getContentPane().add(label);
		
		final JEditorPane editorPane = new JEditorPane();
		editorPane.setBounds(84, 34, 341, 21);
		frmLanguage.getContentPane().add(editorPane);
		
		final JCheckBox checkBox = new JCheckBox("百度百科");
		checkBox.setFont(new Font("宋体", Font.PLAIN, 13));
		checkBox.setBounds(84, 59, 80, 23);
		frmLanguage.getContentPane().add(checkBox);
		
		JLabel label_1 = new JLabel("选择百科：");
		label_1.setFont(new Font("宋体", Font.PLAIN, 13));
		label_1.setBounds(19, 63, 72, 15);
		frmLanguage.getContentPane().add(label_1);
		
		final JCheckBox checkBox_1 = new JCheckBox("互动百科");
		checkBox_1.setFont(new Font("宋体", Font.PLAIN, 13));
		checkBox_1.setBounds(166, 59, 80, 23);
		frmLanguage.getContentPane().add(checkBox_1);
		
		final JCheckBox checkBox_2 = new JCheckBox("搜狗百科");
		checkBox_2.setFont(new Font("宋体", Font.PLAIN, 13));
		checkBox_2.setBounds(248, 59, 80, 23);
		frmLanguage.getContentPane().add(checkBox_2);
		
		final JCheckBox checkBox_3 = new JCheckBox("存入数据库");
		checkBox_3.setFont(new Font("宋体", Font.PLAIN, 13));
		checkBox_3.setBounds(490, 59, 103, 23);
		frmLanguage.getContentPane().add(checkBox_3);
		
		final JButton btnNewButton = new JButton("开始");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(editorPane.getText().isEmpty()){
					JOptionPane.showMessageDialog(frmLanguage,"请输入分类","分类错误",JOptionPane.ERROR_MESSAGE);
				}else if(!isContainChinese(editorPane.getText())){
					JOptionPane.showMessageDialog(frmLanguage,"请输入中文分类","分类错误",JOptionPane.ERROR_MESSAGE);
				}else if(!checkBox.isSelected()&&!checkBox_1.isSelected()&&!checkBox_2.isSelected()){
					JOptionPane.showMessageDialog(frmLanguage,"请选择百科","百科错误",JOptionPane.ERROR_MESSAGE);
				}else{
					new Thread(){
						public void run(){
							Start.setProgress(0);select.run(checkBox.isSelected(),checkBox_1.isSelected(),checkBox_2.isSelected(),editorPane.getText(),checkBox_3.isSelected());
							select.setRate(Start.rate);
							Start.baidurules=select.getBaiduRules();
							Start.hudongrules=select.getHudongrules();
							Start.sougourules=select.getSougourules();
						}
					}.start();
				}
			}
		});
		btnNewButton.setFont(new Font("宋体", Font.PLAIN, 13));
		btnNewButton.setBounds(490, 32, 93, 23);
		frmLanguage.getContentPane().add(btnNewButton);
		
		textArea.setBounds(153, 121, 478, 269);
		frmLanguage.getContentPane().add(textArea);
		
		
		progressBar.setBounds(18, 400, 613, 25);
		frmLanguage.getContentPane().add(progressBar);
		
		JLabel label_3 = new JLabel("信息栏");
		label_3.setFont(new Font("宋体", Font.PLAIN, 13));
		label_3.setBounds(355, 96, 54, 15);
		frmLanguage.getContentPane().add(label_3);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(19, 121, 129, 269);
		scrollPane.setViewportView(list);
		frmLanguage.getContentPane().add(scrollPane);
		
		list.setBounds(19, 121, 108, 269);
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == comboBox) {
					int index = comboBox.getSelectedIndex();
					switch (index) {
					case 1:
					//这里可以做别的事情，这样写只是为了告诉你有这个方法。
					//box.getSelectedItem() 的返回值是 object
						if(!baidurules.isEmpty())Start.addList(baidurules);
						Start.setText(comboBox.getSelectedItem().toString());
						break;
					case 2:
						if(!hudongrules.isEmpty())Start.addList(hudongrules);
						Start.setText(comboBox.getSelectedItem().toString());
						break;
					case 3:
						if(!sougourules.isEmpty())Start.addList(sougourules);
						Start.setText(comboBox.getSelectedItem().toString());
					break;
					case 0:
						Start.setText(comboBox.getSelectedItem().toString());
						break;
					}
				}
			}
		});
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"结果列表", "百度百科结果", "互动百科结果", "搜狗百科结果"}));
		comboBox.setToolTipText("");
		comboBox.setBounds(19, 90, 129, 27);
		frmLanguage.getContentPane().add(comboBox);
		frmLanguage.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{menuBar, mnNewMenu, mnNewMenu_1, mnNewMenu_2, mnNewMenu_3, label, editorPane, label_1, checkBox, checkBox_1, checkBox_2, btnNewButton, list, label_3, textArea, progressBar}));
	}
	//设置信息栏
	public static void setText(String s){
		if(num<15){
			textArea.append(s+"\n"); 
			num++;
		}else{
			textArea.setText(" ");;
			textArea.append(s+"\n");
			num=0;
		}
	}
	//设置进度条参数
	public static void setProgress(int num){
		progressBar.setStringPainted(true);
		progressBar.setValue(num);
	}
	//添加list中
	public static void addList(ArrayList<String> results){
		 DefaultListModel<String> dlm = new DefaultListModel<String>();
		 for(int i=0;i<results.size();i++)dlm.addElement(results.get(i));
		 list.setModel(dlm);
	}
	//匹配中文
	private  boolean isContainChinese(String text){
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(text);
        if (m.find()) {
            return true;
        }
        return false;
	}
}
