package net.dsq.gobanggame.FiveChesse1128;

/**
 * 标题 计算在 起始位置里？
 * int[][] chessArray = new int[i][j];
 * 抄十遍 懂吗！！！！  
 * NullPointerException 空指针异常  学会看提示
 * java.lang.ArrayIndex Out Of Bounds Exception: -1 数组下标越界
 * 传画板 -。-!! 唉  感觉好丧啊！！！ 花了好多时间 然后什么也没作。
 * 哈哈 垃圾
 * 
 * 
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FiveChesseUI extends JPanel implements ChesseConstant {
	private ChesseMListener listener=null;

	public static void main(String[] args) {
		FiveChesseUI fcui = new FiveChesseUI();
		fcui.ShowUI();
	}

	/**
	 * 初始化 监听器  
	 */
	public FiveChesseUI(){
		listener = new ChesseMListener();
	}

	
	/**
	 * 做一个界面！
	 */
	public void ShowUI() {
		
		//一个JFrame
		JFrame jf = new JFrame();
		jf.setTitle("五子棋游戏");
		jf.setDefaultCloseOperation(3);
		jf.setSize(740, 650);
		jf.setResizable(false);
		//相对路径  和 设置 小应用图标
		jf.setIconImage(new ImageIcon("src/com/dsq/Fivechesse1128/Image/qq图标.png").getImage());
		jf.setLocationRelativeTo(null);// 设置窗口居中
		
		
		// 右边的一个面板ep
		JPanel ep = new JPanel();
		ep.setLayout(new FlowLayout());
		ep.setBackground(Color.GRAY);
		ep.setPreferredSize(new Dimension(80,0));
		jf.add(ep, BorderLayout.EAST);
		
		//一些按钮 并加上监听器
		String[] Barray = { "开始游戏", "单人游戏","双人游戏" ,"悔棋", "重新开始" };
		for (int i = 0; i < Barray.length; i++) {
			
			JButton button = new JButton(Barray[i]);
			button.setPreferredSize(new Dimension(100, 40));
			button.setBackground(Color.GRAY);
			//button  边界 false
			button.setBorderPainted(false);
			ep.add(button);		
			button.addActionListener(listener);

		}
		
		// this就是中间的一个面板啦
		this.setBackground(Color.LIGHT_GRAY);
		jf.add(this, BorderLayout.CENTER);
		
		//给 面板加上 listner 
		this.addMouseListener(listener);
		jf.setVisible(true);
		listener.setJPane(this);
	}
	
	
	/**
	 * 重写paint 方法
	 */
	public void paint(Graphics g) {
		super.paint(g);
		//画格子
		for (int i = 0; i < LOWS; i++) {
			g.drawLine(X_START, Y_START + i * SPACING_SIZE, X_START + (LOWS - 1) * SPACING_SIZE, Y_START + i * SPACING_SIZE);
			g.drawLine(X_START + i * SPACING_SIZE, Y_START, X_START + i * SPACING_SIZE, Y_START + (LOWS - 1) * SPACING_SIZE);
		}
		//重绘棋子
		for (int i = 0; i < LOWS; i++) {
			for (int j = 0; j < LOWS; j++) {
				if (chessArray[i][j] == 1) {
					g.drawImage(new ImageIcon("src/net/dsq/gobanggame/FiveChesse1128/Image/hj.png").getImage(),(X_START + i * SPACING_SIZE) - Chesse_SIZE / 2,(Y_START + j * SPACING_SIZE) - Chesse_SIZE / 2, Chesse_SIZE,Chesse_SIZE,null);
				}
				if (chessArray[i][j] == 2) {
					g.drawImage(new ImageIcon("src/net/dsq/gobanggame/FiveChesse1128/Image/black.png").getImage(),(X_START + i * SPACING_SIZE) - Chesse_SIZE / 2,(Y_START + j * SPACING_SIZE) - Chesse_SIZE / 2, Chesse_SIZE,Chesse_SIZE,null);
				}
			}
		}
	}
}
