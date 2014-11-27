package exam.androidproject;

/**
 * ����
 * - ���� ��ġ ������ ��� �ִ� 2���� �迭
 * ���
 * - ���� ��ġ ������ ��� �ִ� 6 by 8 2���� �Լ�
 * - LEFTTOP�� ��ǥ ����
 * - ���� ������ ���� ũ�� ����
 * 
 * @author HJ
 *
 */
public class ElementMatrix
{
    private static final int EMPTY      = 0;            // 0: �� �ڸ�
    private static final int RESERVED   = 1;            // 1: Ÿ���� �̹� ��ġ�Ǿ� ����
    private static final int RESTRICTED = 2;            // 2: ��ġ �Ұ���

    public int[][]           matrix     = new int[6][8];

    /**
     * �迭 �ʱ�ȭ
     */
    public ElementMatrix()
    {
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                matrix[i][j] = 0;
    }

    /**
     * ��� �ִ��� Ȯ���ϴ� �Լ�
     */
    public boolean isEmpty(int x, int y)
    {
        if (matrix[x][y] == EMPTY)
            return true;
        else
            return false;
    }

    /**
     * ��ġ�ϴ� �Լ�
     * 
     * @return �ڸ��� �ְų� ��ġ�Ұ��� ��� false
     *         �ƴϸ� RESERVED�� �ٲٰ� true
     */
    public boolean deployElement(int x, int y)
    {
        if (matrix[x][y] == RESERVED || matrix[x][y] == RESTRICTED)
            return false;
        else
        {
            matrix[x][y] = RESERVED;
            return true;
        }
    }

}
