package net.dsq.gobanggame.FiveChesse1128;
/**
 * �����ӿ�
 * @author 1
 *
 */
public interface ChesseConstant {
	/**
	 * ���� ������ ������
	 */
	public final static  int LOWS = 15;	
	/**
	 *�����ӵ���ʼλ�� x
	 */
	public final static int X_START = 42;	
	/**
	 *�����ӵ���ʼλ�� y
	 */
	public final static int Y_START = 30;	
	/**
	 * ����֮��ļ��
	 */
	public final static int SPACING_SIZE = 40;	
	/**
	 *���ӵĴ�С
	 */
	public final static int Chesse_SIZE = 40;
	/**
	 * ���������ֵ��
	 *  1 ���� �±��Ϊ yellow 
	 *   2.Ϊ black
	 *    0.Ϊδ����
	 */
	public  final static int[][] chessArray = new int[LOWS][LOWS];
}
