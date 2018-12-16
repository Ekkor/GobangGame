package net.dsq.gobanggame.FiveChesse1128;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 鼠标监听类 和 动作监听类 --------------------------------------- 关于人机下棋的想法：
 * 这里采用的是最简单的一种分析棋盘情况 然后根据设定的权值（hashmap<棋盘情况，权值>） 计算每一个位置的权值 选择“最优”的下棋点
 * ---------------------------------------
 * 
 * @author 1
 *
 */
public class ChesseMListener extends MouseAdapter implements ChesseConstant, ActionListener {

	private HashMap<String, Integer> map = new HashMap<String, Integer>();
	private JPanel f;
	private Graphics g;
	private ImageIcon iconyellow, iconblack;// 棋子图片
	private String mode = "双人游戏";
	private int count = 0;// count :第几步 用来一个控制棋子交替 和 悔棋

	int[] x_undo = new int[LOWS * LOWS];// 悔棋用的 记录每一步的位置
	int[] y_undo = new int[LOWS * LOWS];

	/**
	 * 1: 画板可以下棋子
	 * 
	 * 0: 画板不可以下下棋子
	 */
	private int flag = 0;

	/**
	 * 构造方法 图片 和 hashmap 初始化
	 * 
	 * @param f
	 */
	public ChesseMListener() {

		iconyellow = new ImageIcon(this.getClass().getResource("Image/hj.png"));
		iconblack = new ImageIcon(this.getClass().getResource("Image/black.png"));

		/*------------------------------------------
			这里设置 棋盘情况 和 权值 
			比较简单 ：P
		 -------------------------------------------*/
		map.put("", 0);
		map.put("0", 0);
		// 活连 防守
		map.put("1", 6);
		map.put("11", 40);
		map.put("111", 300);
		map.put("1111", 1);
		// 绵连 防守
		map.put("12", 2);
		map.put("112", 8);
		map.put("1112", 13);
		map.put("11112", 50000);
		// 活连 进攻
		map.put("2", 10);
		map.put("22", 80);
		map.put("222", 500);
		map.put("2222", 50000);
		// 绵连 进攻
		map.put("21", 5);
		map.put("221", 15);
		map.put("2221", 70);
		map.put("22221", 50000);

	}

	/**
	 * 传递JPanel
	 * 
	 * @param f
	 *            画棋子的面板
	 */
	public void setJPane(JPanel f) {
		this.f = f;
		g = f.getGraphics();
	}

	/**
	 * 鼠标按下 获取 按下的坐标 根据模式的不同分别处理
	 */
	public void mousePressed(MouseEvent e) {
		if (mode.equals("单人游戏")) {
			singleplay(e.getX(), e.getY());
		} else if (mode.equals("双人游戏")) {
			doubplay(e.getX(), e.getY());
		}
	}

	/**
	 * 单人游戏模式
	 */
	public void singleplay(int x, int y) {
		// x y 是点击点
		// x_cross,y_cross 是每一个格子的交点
		int x_cross;
		int y_cross;

		for (int i = 0; i < LOWS; i++) {
			for (int j = 0; j < LOWS; j++) {

				x_cross = X_START + i * SPACING_SIZE;// 遍历坐标
				y_cross = Y_START + j * SPACING_SIZE;//
				// 确定了点击 发生在哪个交点
				if (chessArray[i][j] == 0 && x > (x_cross - SPACING_SIZE / 2) && x < (x_cross + SPACING_SIZE / 2)
						&& y > (y_cross - SPACING_SIZE / 2) && y < (y_cross + SPACING_SIZE / 2) && flag == 1) {
					System.out.println("下在了第" + i + "行，第" + j + "列；");
					// 先记录棋盘情况！
					chessArray[i][j] = 1;
					x_undo[count] = i;
					y_undo[count] = j;
					count++;

					// 然后 在交点处画棋子啊
					g.drawImage(iconyellow.getImage(), x_cross - Chesse_SIZE / 2, y_cross - Chesse_SIZE / 2,
							Chesse_SIZE, Chesse_SIZE, null);

					// 人机对战的判断输赢比较特别
					if (win() == 1) {
						JOptionPane.showMessageDialog(f, "滑稽：我会输？");
					} else if (win() == 0) {
						// 人点击位置后 下完祺 接下来我们要自动找位置 下黑色棋子了
						int[][] weight = W();// 遍历棋盘 得到每个位置的权值 将情况储存在 weight数据中
						int[] max = max(weight);// 遍历棋盘 选择最后一个 weight数据中的最大值
												// 作为下棋点
						// 记录棋盘情况
						chessArray[max[0]][max[1]] = 2;
						g.drawImage(iconblack.getImage(), X_START + max[0] * SPACING_SIZE - Chesse_SIZE / 2,
								Y_START + max[1] * SPACING_SIZE - Chesse_SIZE / 2, Chesse_SIZE, Chesse_SIZE, null);
						x_undo[count] = max[0];
						y_undo[count] = max[1];
						count++;
					}
					if (win() == 2) {
						JOptionPane.showMessageDialog(f, "还有输给我的代码这种操作嘛");
					}
				}
			}
		}
	}

	/**
	 * 双人游戏模式 非联网
	 * 
	 * @param x
	 * @param y
	 */
	public void doubplay(int x, int y) {
		// x y 是鼠标点击点
		// x_cross,y_cross 是每一个格子的交点
		int x_cross;
		int y_cross;
		for (int i = 0; i < LOWS; i++) {
			for (int j = 0; j < LOWS; j++) {
				x_cross = X_START + i * SPACING_SIZE;// 第i个交叉点的x坐标值
				y_cross = Y_START + j * SPACING_SIZE;//
				// 这里判断你 点击的点属于哪个交点 并把交点位置 赋给x_cross 和 y_Cross
				if (flag == 1 && chessArray[i][j] == 0 && x > (x_cross - SPACING_SIZE / 2)
						&& x < (x_cross + SPACING_SIZE / 2) && y > (y_cross - SPACING_SIZE / 2)
						&& y < (y_cross + SPACING_SIZE / 2)) {
					// 记录棋盘啊 并下棋啊
					chessArray[i][j] = drawChess(x_cross, y_cross);
					x_undo[count] = i;
					y_undo[count] = j;
					// 判断输赢的代码
					if (win() == 1) {
						JOptionPane.showMessageDialog(f, "滑稽获胜");
					} else if (win() == 2) {
						JOptionPane.showMessageDialog(f, "黑子获胜");
					}
				}
			}
		}
	}

	/**
	 * 控制棋子交替画出来
	 * 
	 * @param x_cross交点x
	 * @param y_cross交点y
	 * @returnq 1：代表画的黄子 2：代表画的黑子
	 */
	public int drawChess(int x_cross, int y_cross) {
		if (count % 2 == 0) {
			g.drawImage(iconyellow.getImage(), x_cross - Chesse_SIZE / 2, y_cross - Chesse_SIZE / 2, Chesse_SIZE,
					Chesse_SIZE, null);
			count++;
			return 1;
		} else {

			g.drawImage(iconblack.getImage(), x_cross - Chesse_SIZE / 2, y_cross - Chesse_SIZE / 2, Chesse_SIZE,
					Chesse_SIZE, null);
			count++;
			return 2;
		}

	}

	/**
	 * 动作监听 实现各个按钮的功能
	 */
	public void actionPerformed(ActionEvent e) {
		// 获得按钮上的字符
		String cmd = e.getActionCommand();
		if ("开始游戏".equals(cmd) || "重新开始".equals(cmd)) {
			reset();// 重置棋盘
		}
		if ("单人游戏".equals(cmd)) {
			mode = "单人游戏";
			reset();
		}
		if ("双人游戏".equals(cmd)) {
			mode = "双人游戏";
			reset();
		}

		if ("悔棋".equals(cmd)) {
			if (count > 0) {

				if (mode == "双人游戏") {
					chessArray[x_undo[count]][y_undo[count]] = 0;
					count--;
				} else if (mode == "单人游戏") {
					chessArray[x_undo[count - 1]][y_undo[count - 1]] = 0;
					count--;
					chessArray[x_undo[count - 1]][y_undo[count - 1]] = 0;
					count--;
				} else {
					JOptionPane.showMessageDialog(f, "没得棋子啦 兄dei~");
				}
			f.repaint();
			}
		}
	}

	/**
	 * 重置操作
	 */
	public void reset() {
		flag = 1;
		count = 0;
		f.repaint();
		for (int i = 0; i < LOWS; i++) {
			for (int j = 0; j < LOWS; j++) {
				chessArray[i][j] = 0;
			}
		}
	}

	/**
	 * 遍历棋盘 得各个位置得权值
	 * 
	 * @return整个棋盘的权值情况
	 */
	public int[][] W() {
		String code = "";// 棋盘情况
		Integer value = 0;// 棋盘情况对应权值！
		int[][] weight = new int[LOWS][LOWS];// 整个棋盘的权值情况

		for (int i = 0; i < LOWS; i++) {
			for (int j = 0; j < LOWS; j++) {
				code = countR(i, j);
				value = map.get(code);
				if (value != null) {
					weight[i][j] += value;// 然后是八个方向
				}

				code = countL(i, j);
				value = map.get(code);
				if (value != null) {
					weight[i][j] += value;
				}
				code = countU(i, j);
				value = map.get(code);
				if (value != null) {
					weight[i][j] += value;
				}
				code = countD(i, j);
				value = map.get(code);
				if (value != null) {
					weight[i][j] += value;
				}
				code = countRU(i, j);
				value = map.get(code);
				if (value != null) {
					weight[i][j] += value;
				}
				code = countRD(i, j);
				value = map.get(code);
				if (value != null) {
					weight[i][j] += value;
				}
				code = countLU(i, j);
				value = map.get(code);
				if (value != null) {
					weight[i][j] += value;
				}
				code = countLD(i, j);
				value = map.get(code);
				if (value != null) {
					weight[i][j] += value;
				}

			}
		} // 遍历 完 得棋盘weight
		return weight;

	}

	/**
	 * 找最后一个 最大值weight[i][j]作为下棋位置
	 */
	public int[] max(int[][] weight) {
		int max = 0;
		int[] array = new int[2];
		for (int i = 0; i < weight.length; i++) {
			for (int j = 0; j < weight[i].length; j++) {
				if (max < weight[i][j]) {
					max = weight[i][j];
					array[0] = i;
					array[1] = j;
				}
			}
		}
		return array;
	}

	/**
	 * 判断输赢
	 * 
	 * @return 1 滑稽赢 2 黑色赢
	 */
	public int win() {
		for (int a = 2; a < LOWS + 2; a++) {// 上下
			for (int b = 2; b < LOWS - 2; b++) {
				if (chessArray[a - 2][b - 2] == 1 && chessArray[a - 2][b - 1] == 1 && chessArray[a - 2][b] == 1
						&& chessArray[a - 2][b + 1] == 1 && chessArray[a - 2][b + 2] == 1) {
					flag = 0;
					return 1;
				}
				if (chessArray[a - 2][b - 2] == 2 && chessArray[a - 2][b - 1] == 2 && chessArray[a - 2][b] == 2
						&& chessArray[a - 2][b + 1] == 2 && chessArray[a - 2][b + 2] == 2) {
					flag = 0;
					return 2;
				}
			}
		}

		for (int a = 2; a < LOWS - 2; a++) {// 左右
			for (int b = 2; b < LOWS + 2; b++) {
				if (chessArray[a - 2][b - 2] == 1 && chessArray[a - 1][b - 2] == 1 && chessArray[a][b - 2] == 1
						&& chessArray[a + 1][b - 2] == 1 && chessArray[a + 2][b - 2] == 1) {
					// JOptionPane.showMessageDialog(f, "黑子获胜");
					flag = 0;
					return 1;
				}
				if (chessArray[a - 2][b - 2] == 2 && chessArray[a - 1][b - 2] == 2 && chessArray[a][b - 2] == 2
						&& chessArray[a + 1][b - 2] == 2 && chessArray[a + 2][b - 2] == 2) {
					// JOptionPane.showMessageDialog(f, "白子获胜");
					flag = 0;
					return 2;
				}
			}
		}
		for (int a = 2; a < LOWS - 2; a++) {// 正对角
			for (int b = 2; b < LOWS - 2; b++) {
				if (chessArray[a - 2][b - 2] == 1 && chessArray[a - 1][b - 1] == 1 && chessArray[a][b] == 1
						&& chessArray[a + 1][b + 1] == 1 && chessArray[a + 2][b + 2] == 1) {// 正对角
					// JOptionPane.showMessageDialog(f, "黑子获胜");
					flag = 0;
					return 1;
				}
				if (chessArray[a - 2][b - 2] == 2 && chessArray[a - 1][b - 1] == 2 && chessArray[a][b] == 2
						&& chessArray[a + 1][b + 1] == 2 && chessArray[a + 2][b + 2] == 2) {// 正对角
					// JOptionPane.showMessageDialog(f, "白子获胜");
					flag = 0;
					return 2;
				}
			}
		}
		for (int a = 2; a < LOWS - 2; a++) {// 反对角
			for (int b = 2; b < LOWS - 2; b++) {
				if (chessArray[a + 2][b - 2] == 1 && chessArray[a + 1][b - 1] == 1 && chessArray[a][b] == 1
						&& chessArray[a - 1][b + 1] == 1 && chessArray[a - 2][b + 2] == 1) {// 反对角
					// JOptionPane.showMessageDialog(f, "黑子获胜");
					flag = 0;
					return 1;
				}
				if (chessArray[a + 2][b - 2] == 2 && chessArray[a + 1][b - 1] == 2 && chessArray[a][b] == 2
						&& chessArray[a - 1][b + 1] == 2 && chessArray[a - 2][b + 2] == 2) {// 反对角
					// JOptionPane.showMessageDialog(f, "白子获胜");
					flag = 0;
					return 2;
				}
			}
		}
		return 0;
	}

	/**
	 * 向右记录棋盘得棋子状况
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public String countR(int x, int y) {
		String code = "";
		int chess = 0;
		if (chessArray[x][y] == 0) {
			for (int i = x + 1; i < LOWS; i++) {
				if (chessArray[i][y] == 0) {
					break;
				} else if (chessArray[i][y] != 0) {
					if (chess == 0) {
						code = code + chessArray[i][y];
						chess = chessArray[i][y];
					} else if (chess == chessArray[i][y]) {// 如果和前面的颜色一只
															// 则可以继续记录并循环
						code = code + chessArray[i][y];
					} else {// 颜色变了 到此停止记录了
						code = code + chessArray[i][y];
						break;
					}
				}
			}
		}
		return code;
	}

	/**
	 * 左
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public String countL(int x, int y) {
		String code = "";
		int chess = 0;
		if (chessArray[x][y] == 0) {
			for (int i = x - 1; i > 0; i--) {
				if (chessArray[i][y] == 0) {
					break;
				} else {
					if (chess == 0) {
						code = code + chessArray[i][y];
						chess = chessArray[i][y];
					} else if (chess == chessArray[i][y]) {// 如果和前面的颜色一只
															// 则可以继续记录并循环
						code = code + chessArray[i][y];
					} else {// 颜色变了 到此停止记录了
						code = code + chessArray[i][y];
						break;
					}
				}
			}
		}
		return code;
	}
	
	/**
	 * 上
	 * @param x
	 * @param y
	 * @return
	 */
	public String countU(int x, int y) {
		String code = "";
		int chess = 0;
		if (chessArray[x][y] == 0) {
			for (int i = y + 1; i < LOWS; i++) {
				if (chessArray[x][i] == 0) {
					break;
				} else {
					if (chess == 0) {
						code = code + chessArray[x][i];
						chess = chessArray[x][i];
					} else if (chess == chessArray[x][i]) {// 如果和前面的颜色一只
															// 则可以继续记录并循环
						code = code + chessArray[x][i];
					} else {// 颜色变了 到此停止记录了
						code = code + chessArray[x][i];
						break;
					}
				}
			}
		}
		return code;
	}

	public String countD(int x, int y) {
		String code = "";
		int chess = 0;
		if (chessArray[x][y] == 0) {
			for (int i = y - 1; i > 0; i--) {
				if (chessArray[x][i] == 0) {
					break;
				} else {
					if (chess == 0) {
						code = code + chessArray[x][i];
						chess = chessArray[x][i];
					} else if (chess == chessArray[x][i]) {// 如果和前面的颜色一只
															// 则可以继续记录并循环
						code = code + chessArray[x][i];
					} else {// 颜色变了 到此停止记录了
						code = code + chessArray[x][i];
						break;
					}
				}
			}
		}

		return code;
	}

	public String countRD(int x, int y) {
		String code = "";
		int chess = 0;
		if (chessArray[x][y] == 0) {
			for (int i = 1; i < 100; i++) {
				if (x + i < LOWS && y + i < LOWS) {
					if (chessArray[x + i][y + i] == 0) {
						break;
					} else {
						if (chess == 0) {
							code = code + chessArray[x + i][y + i];
							chess = chessArray[x + i][y + i];
						} else if (chess == chessArray[x + i][y + i]) {// 如果和前面的颜色一只
																		// 则可以继续记录并循环
							code = code + chessArray[x + i][y + i];
						} else {// 颜色变了 到此停止记录了
							code = code + chessArray[x + i][y + i];
							break;
						}
					}
				}
			}
		}

		return code;
	}

	public String countRU(int x, int y) {
		String code = "";
		int chess = 0;
		if (chessArray[x][y] == 0) {
			for (int i = x + 1, j = y - 1; i < LOWS && j > 0; i++, j--) {

				if (chessArray[i][j] == 0) {
					break;
				} else {
					if (chess == 0) {
						code = code + chessArray[i][j];
						chess = chessArray[i][j];
					} else if (chess == chessArray[i][j]) {// 如果和前面的颜色一只
															// 则可以继续记录并循环
						code = code + chessArray[i][j];
					} else {// 颜色变了 到此停止记录了
						code = code + chessArray[i][j];
						break;
					}
				}
			}
		}

		return code;
	}

	public String countLU(int x, int y) {
		String code = "";
		int chess = 0;
		if (chessArray[x][y] == 0) {
			for (int i = x - 1, j = y - 1; i > 0 && j > 0; i--, j--) {
				if (chessArray[i][j] == 0) {
					break;
				} else {
					if (chess == 0) {
						code = code + chessArray[i][j];
						chess = chessArray[i][j];
					} else if (chess == chessArray[i][j]) {// 如果和前面的颜色一只
															// 则可以继续记录并循环
						code = code + chessArray[i][j];
					} else {// 颜色变了 到此停止记录了
						code = code + chessArray[i][j];
						break;
					}
				}
			}
		}
		return code;
	}

	public String countLD(int x, int y) {
		String code = "";
		int chess = 0;
		if (chessArray[x][y] == 0) {
			for (int i = x - 1, j = y + 1; i > 0 && j < LOWS; i--, j++) {
				if (chessArray[i][j] == 0) {
					break;
				} else {
					if (chess == 0) {
						code = code + chessArray[i][j];
						chess = chessArray[i][j];
					} else if (chess == chessArray[i][j]) {// 如果和前面的颜色一只
															// 则可以继续记录并循环
						code = code + chessArray[i][j];
					} else {// 颜色变了 到此停止记录了
						code = code + chessArray[i][j];
						break;
					}
				}
			}
		}
		return code;
	}
}
