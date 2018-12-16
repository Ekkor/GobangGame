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
 * �������� �� ���������� --------------------------------------- �����˻�������뷨��
 * ������õ�����򵥵�һ�ַ���������� Ȼ������趨��Ȩֵ��hashmap<���������Ȩֵ>�� ����ÿһ��λ�õ�Ȩֵ ѡ�����š��������
 * ---------------------------------------
 * 
 * @author 1
 *
 */
public class ChesseMListener extends MouseAdapter implements ChesseConstant, ActionListener {

	private HashMap<String, Integer> map = new HashMap<String, Integer>();
	private JPanel f;
	private Graphics g;
	private ImageIcon iconyellow, iconblack;// ����ͼƬ
	private String mode = "˫����Ϸ";
	private int count = 0;// count :�ڼ��� ����һ���������ӽ��� �� ����

	int[] x_undo = new int[LOWS * LOWS];// �����õ� ��¼ÿһ����λ��
	int[] y_undo = new int[LOWS * LOWS];

	/**
	 * 1: �������������
	 * 
	 * 0: ���岻������������
	 */
	private int flag = 0;

	/**
	 * ���췽�� ͼƬ �� hashmap ��ʼ��
	 * 
	 * @param f
	 */
	public ChesseMListener() {

		iconyellow = new ImageIcon(this.getClass().getResource("Image/hj.png"));
		iconblack = new ImageIcon(this.getClass().getResource("Image/black.png"));

		/*------------------------------------------
			�������� ������� �� Ȩֵ 
			�Ƚϼ� ��P
		 -------------------------------------------*/
		map.put("", 0);
		map.put("0", 0);
		// ���� ����
		map.put("1", 6);
		map.put("11", 40);
		map.put("111", 300);
		map.put("1111", 1);
		// ���� ����
		map.put("12", 2);
		map.put("112", 8);
		map.put("1112", 13);
		map.put("11112", 50000);
		// ���� ����
		map.put("2", 10);
		map.put("22", 80);
		map.put("222", 500);
		map.put("2222", 50000);
		// ���� ����
		map.put("21", 5);
		map.put("221", 15);
		map.put("2221", 70);
		map.put("22221", 50000);

	}

	/**
	 * ����JPanel
	 * 
	 * @param f
	 *            �����ӵ����
	 */
	public void setJPane(JPanel f) {
		this.f = f;
		g = f.getGraphics();
	}

	/**
	 * ��갴�� ��ȡ ���µ����� ����ģʽ�Ĳ�ͬ�ֱ���
	 */
	public void mousePressed(MouseEvent e) {
		if (mode.equals("������Ϸ")) {
			singleplay(e.getX(), e.getY());
		} else if (mode.equals("˫����Ϸ")) {
			doubplay(e.getX(), e.getY());
		}
	}

	/**
	 * ������Ϸģʽ
	 */
	public void singleplay(int x, int y) {
		// x y �ǵ����
		// x_cross,y_cross ��ÿһ�����ӵĽ���
		int x_cross;
		int y_cross;

		for (int i = 0; i < LOWS; i++) {
			for (int j = 0; j < LOWS; j++) {

				x_cross = X_START + i * SPACING_SIZE;// ��������
				y_cross = Y_START + j * SPACING_SIZE;//
				// ȷ���˵�� �������ĸ�����
				if (chessArray[i][j] == 0 && x > (x_cross - SPACING_SIZE / 2) && x < (x_cross + SPACING_SIZE / 2)
						&& y > (y_cross - SPACING_SIZE / 2) && y < (y_cross + SPACING_SIZE / 2) && flag == 1) {
					System.out.println("�����˵�" + i + "�У���" + j + "�У�");
					// �ȼ�¼���������
					chessArray[i][j] = 1;
					x_undo[count] = i;
					y_undo[count] = j;
					count++;

					// Ȼ�� �ڽ��㴦�����Ӱ�
					g.drawImage(iconyellow.getImage(), x_cross - Chesse_SIZE / 2, y_cross - Chesse_SIZE / 2,
							Chesse_SIZE, Chesse_SIZE, null);

					// �˻���ս���ж���Ӯ�Ƚ��ر�
					if (win() == 1) {
						JOptionPane.showMessageDialog(f, "�������һ��䣿");
					} else if (win() == 0) {
						// �˵��λ�ú� ������ ����������Ҫ�Զ���λ�� �º�ɫ������
						int[][] weight = W();// �������� �õ�ÿ��λ�õ�Ȩֵ ����������� weight������
						int[] max = max(weight);// �������� ѡ�����һ�� weight�����е����ֵ
												// ��Ϊ�����
						// ��¼�������
						chessArray[max[0]][max[1]] = 2;
						g.drawImage(iconblack.getImage(), X_START + max[0] * SPACING_SIZE - Chesse_SIZE / 2,
								Y_START + max[1] * SPACING_SIZE - Chesse_SIZE / 2, Chesse_SIZE, Chesse_SIZE, null);
						x_undo[count] = max[0];
						y_undo[count] = max[1];
						count++;
					}
					if (win() == 2) {
						JOptionPane.showMessageDialog(f, "��������ҵĴ������ֲ�����");
					}
				}
			}
		}
	}

	/**
	 * ˫����Ϸģʽ ������
	 * 
	 * @param x
	 * @param y
	 */
	public void doubplay(int x, int y) {
		// x y ���������
		// x_cross,y_cross ��ÿһ�����ӵĽ���
		int x_cross;
		int y_cross;
		for (int i = 0; i < LOWS; i++) {
			for (int j = 0; j < LOWS; j++) {
				x_cross = X_START + i * SPACING_SIZE;// ��i��������x����ֵ
				y_cross = Y_START + j * SPACING_SIZE;//
				// �����ж��� ����ĵ������ĸ����� ���ѽ���λ�� ����x_cross �� y_Cross
				if (flag == 1 && chessArray[i][j] == 0 && x > (x_cross - SPACING_SIZE / 2)
						&& x < (x_cross + SPACING_SIZE / 2) && y > (y_cross - SPACING_SIZE / 2)
						&& y < (y_cross + SPACING_SIZE / 2)) {
					// ��¼���̰� �����尡
					chessArray[i][j] = drawChess(x_cross, y_cross);
					x_undo[count] = i;
					y_undo[count] = j;
					// �ж���Ӯ�Ĵ���
					if (win() == 1) {
						JOptionPane.showMessageDialog(f, "������ʤ");
					} else if (win() == 2) {
						JOptionPane.showMessageDialog(f, "���ӻ�ʤ");
					}
				}
			}
		}
	}

	/**
	 * �������ӽ��滭����
	 * 
	 * @param x_cross����x
	 * @param y_cross����y
	 * @returnq 1�������Ļ��� 2�������ĺ���
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
	 * �������� ʵ�ָ�����ť�Ĺ���
	 */
	public void actionPerformed(ActionEvent e) {
		// ��ð�ť�ϵ��ַ�
		String cmd = e.getActionCommand();
		if ("��ʼ��Ϸ".equals(cmd) || "���¿�ʼ".equals(cmd)) {
			reset();// ��������
		}
		if ("������Ϸ".equals(cmd)) {
			mode = "������Ϸ";
			reset();
		}
		if ("˫����Ϸ".equals(cmd)) {
			mode = "˫����Ϸ";
			reset();
		}

		if ("����".equals(cmd)) {
			if (count > 0) {

				if (mode == "˫����Ϸ") {
					chessArray[x_undo[count]][y_undo[count]] = 0;
					count--;
				} else if (mode == "������Ϸ") {
					chessArray[x_undo[count - 1]][y_undo[count - 1]] = 0;
					count--;
					chessArray[x_undo[count - 1]][y_undo[count - 1]] = 0;
					count--;
				} else {
					JOptionPane.showMessageDialog(f, "û�������� ��dei~");
				}
			f.repaint();
			}
		}
	}

	/**
	 * ���ò���
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
	 * �������� �ø���λ�õ�Ȩֵ
	 * 
	 * @return�������̵�Ȩֵ���
	 */
	public int[][] W() {
		String code = "";// �������
		Integer value = 0;// ���������ӦȨֵ��
		int[][] weight = new int[LOWS][LOWS];// �������̵�Ȩֵ���

		for (int i = 0; i < LOWS; i++) {
			for (int j = 0; j < LOWS; j++) {
				code = countR(i, j);
				value = map.get(code);
				if (value != null) {
					weight[i][j] += value;// Ȼ���ǰ˸�����
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
		} // ���� �� ������weight
		return weight;

	}

	/**
	 * �����һ�� ���ֵweight[i][j]��Ϊ����λ��
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
	 * �ж���Ӯ
	 * 
	 * @return 1 ����Ӯ 2 ��ɫӮ
	 */
	public int win() {
		for (int a = 2; a < LOWS + 2; a++) {// ����
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

		for (int a = 2; a < LOWS - 2; a++) {// ����
			for (int b = 2; b < LOWS + 2; b++) {
				if (chessArray[a - 2][b - 2] == 1 && chessArray[a - 1][b - 2] == 1 && chessArray[a][b - 2] == 1
						&& chessArray[a + 1][b - 2] == 1 && chessArray[a + 2][b - 2] == 1) {
					// JOptionPane.showMessageDialog(f, "���ӻ�ʤ");
					flag = 0;
					return 1;
				}
				if (chessArray[a - 2][b - 2] == 2 && chessArray[a - 1][b - 2] == 2 && chessArray[a][b - 2] == 2
						&& chessArray[a + 1][b - 2] == 2 && chessArray[a + 2][b - 2] == 2) {
					// JOptionPane.showMessageDialog(f, "���ӻ�ʤ");
					flag = 0;
					return 2;
				}
			}
		}
		for (int a = 2; a < LOWS - 2; a++) {// ���Խ�
			for (int b = 2; b < LOWS - 2; b++) {
				if (chessArray[a - 2][b - 2] == 1 && chessArray[a - 1][b - 1] == 1 && chessArray[a][b] == 1
						&& chessArray[a + 1][b + 1] == 1 && chessArray[a + 2][b + 2] == 1) {// ���Խ�
					// JOptionPane.showMessageDialog(f, "���ӻ�ʤ");
					flag = 0;
					return 1;
				}
				if (chessArray[a - 2][b - 2] == 2 && chessArray[a - 1][b - 1] == 2 && chessArray[a][b] == 2
						&& chessArray[a + 1][b + 1] == 2 && chessArray[a + 2][b + 2] == 2) {// ���Խ�
					// JOptionPane.showMessageDialog(f, "���ӻ�ʤ");
					flag = 0;
					return 2;
				}
			}
		}
		for (int a = 2; a < LOWS - 2; a++) {// ���Խ�
			for (int b = 2; b < LOWS - 2; b++) {
				if (chessArray[a + 2][b - 2] == 1 && chessArray[a + 1][b - 1] == 1 && chessArray[a][b] == 1
						&& chessArray[a - 1][b + 1] == 1 && chessArray[a - 2][b + 2] == 1) {// ���Խ�
					// JOptionPane.showMessageDialog(f, "���ӻ�ʤ");
					flag = 0;
					return 1;
				}
				if (chessArray[a + 2][b - 2] == 2 && chessArray[a + 1][b - 1] == 2 && chessArray[a][b] == 2
						&& chessArray[a - 1][b + 1] == 2 && chessArray[a - 2][b + 2] == 2) {// ���Խ�
					// JOptionPane.showMessageDialog(f, "���ӻ�ʤ");
					flag = 0;
					return 2;
				}
			}
		}
		return 0;
	}

	/**
	 * ���Ҽ�¼���̵�����״��
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
					} else if (chess == chessArray[i][y]) {// �����ǰ�����ɫһֻ
															// ����Լ�����¼��ѭ��
						code = code + chessArray[i][y];
					} else {// ��ɫ���� ����ֹͣ��¼��
						code = code + chessArray[i][y];
						break;
					}
				}
			}
		}
		return code;
	}

	/**
	 * ��
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
					} else if (chess == chessArray[i][y]) {// �����ǰ�����ɫһֻ
															// ����Լ�����¼��ѭ��
						code = code + chessArray[i][y];
					} else {// ��ɫ���� ����ֹͣ��¼��
						code = code + chessArray[i][y];
						break;
					}
				}
			}
		}
		return code;
	}
	
	/**
	 * ��
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
					} else if (chess == chessArray[x][i]) {// �����ǰ�����ɫһֻ
															// ����Լ�����¼��ѭ��
						code = code + chessArray[x][i];
					} else {// ��ɫ���� ����ֹͣ��¼��
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
					} else if (chess == chessArray[x][i]) {// �����ǰ�����ɫһֻ
															// ����Լ�����¼��ѭ��
						code = code + chessArray[x][i];
					} else {// ��ɫ���� ����ֹͣ��¼��
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
						} else if (chess == chessArray[x + i][y + i]) {// �����ǰ�����ɫһֻ
																		// ����Լ�����¼��ѭ��
							code = code + chessArray[x + i][y + i];
						} else {// ��ɫ���� ����ֹͣ��¼��
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
					} else if (chess == chessArray[i][j]) {// �����ǰ�����ɫһֻ
															// ����Լ�����¼��ѭ��
						code = code + chessArray[i][j];
					} else {// ��ɫ���� ����ֹͣ��¼��
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
					} else if (chess == chessArray[i][j]) {// �����ǰ�����ɫһֻ
															// ����Լ�����¼��ѭ��
						code = code + chessArray[i][j];
					} else {// ��ɫ���� ����ֹͣ��¼��
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
					} else if (chess == chessArray[i][j]) {// �����ǰ�����ɫһֻ
															// ����Լ�����¼��ѭ��
						code = code + chessArray[i][j];
					} else {// ��ɫ���� ����ֹͣ��¼��
						code = code + chessArray[i][j];
						break;
					}
				}
			}
		}
		return code;
	}
}
