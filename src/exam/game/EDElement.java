package exam.game;

/**
 * ElementView �׽�Ʈ������ �ϳ� ����
 * 
 * @author �̻���
 */
public class EDElement
{
    public String name;
    public int    level;
    public int    type;
    public int    dmg;
    public float  rate;
    public String imgName;

    public EDElement()
    {
        name = "��";
        level = 0;
        type = 0;
        dmg = 5;
        rate = 0.5f;
        imgName = "match";
    }

    @Override
    public String toString()
    {
        String element = "<�ϱ�> " + name;

        return element;

    }
}
