package net.dsq.gobanggame.FiveChesse1128;
/**
 * 常数接口
 * @author 1
 *
 */
public interface ChesseConstant {
	/**
	 * 行数 和列数 都是它
	 */
	public final static  int LOWS = 15;	
	/**
	 *画格子的起始位置 x
	 */
	public final static int X_START = 42;	
	/**
	 *画格子的起始位置 y
	 */
	public final static int Y_START = 30;	
	/**
	 * 格子之间的间隔
	 */
	public final static int SPACING_SIZE = 40;	
	/**
	 *棋子的大小
	 */
	public final static int Chesse_SIZE = 40;
	/**
	 * 棋盘数组的值：
	 *  1 代表 下标出为 yellow 
	 *   2.为 black
	 *    0.为未下子
	 */
	public  final static int[][] chessArray = new int[LOWS][LOWS];
}
