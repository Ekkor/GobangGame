package net.dsq.gobanggame.FiveChesse1128;

/**
 * ���� ������ ��ʼλ���
 * int[][] chessArray = new int[i][j];
 * ��ʮ�� ���𣡣�����  
 * NullPointerException ��ָ���쳣  ѧ�ῴ��ʾ
 * java.lang.ArrayIndex Out Of Bounds Exception: -1 �����±�Խ��
 * ������ -��-!! ��  �о���ɥ�������� ���˺ö�ʱ�� Ȼ��ʲôҲû����
 * ���� ����
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
	 * ��ʼ�� ������  
	 */
	public FiveChesseUI(){
		listener = new ChesseMListener();
	}

	
	/**
	 * ��һ�����棡
	 */
	public void ShowUI() {
		
		//һ��JFrame
		JFrame jf = new JFrame();
		jf.setTitle("��������Ϸ");
		jf.setDefaultCloseOperation(3);
		jf.setSize(740, 650);
		jf.setResizable(false);
		//���·��  �� ���� СӦ��ͼ��
		jf.setIconImage(new ImageIcon("src/com/dsq/Fivechesse1128/Image/qqͼ��.png").getImage());
		jf.setLocationRelativeTo(null);// ���ô��ھ���
		
		
		// �ұߵ�һ�����ep
		JPanel ep = new JPanel();
		ep.setLayout(new FlowLayout());
		ep.setBackground(Color.GRAY);
		ep.setPreferredSize(new Dimension(80,0));
		jf.add(ep, BorderLayout.EAST);
		
		//һЩ��ť �����ϼ�����
		String[] Barray = { "��ʼ��Ϸ", "������Ϸ","˫����Ϸ" ,"����", "���¿�ʼ" };
		for (int i = 0; i < Barray.length; i++) {
			
			JButton button = new JButton(Barray[i]);
			button.setPreferredSize(new Dimension(100, 40));
			button.setBackground(Color.GRAY);
			//button  �߽� false
			button.setBorderPainted(false);
			ep.add(button);		
			button.addActionListener(listener);

		}
		
		// this�����м��һ�������
		this.setBackground(Color.LIGHT_GRAY);
		jf.add(this, BorderLayout.CENTER);
		
		//�� ������ listner 
		this.addMouseListener(listener);
		jf.setVisible(true);
		listener.setJPane(this);
	}
	
	
	/**
	 * ��дpaint ����
	 */
	public void paint(Graphics g) {
		super.paint(g);
		//������
		for (int i = 0; i < LOWS; i++) {
			g.drawLine(X_START, Y_START + i * SPACING_SIZE, X_START + (LOWS - 1) * SPACING_SIZE, Y_START + i * SPACING_SIZE);
			g.drawLine(X_START + i * SPACING_SIZE, Y_START, X_START + i * SPACING_SIZE, Y_START + (LOWS - 1) * SPACING_SIZE);
		}
		//�ػ�����
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
