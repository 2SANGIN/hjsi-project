package exam.customwidget;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import exam.game.EDElement;

/**
 * ������ ������ �����ִ� ��<br/>
 * <br/>
 * <<<�� ��>>><br/>
 * Ŭ�� ���� ���� �����ؾ���.
 * 
 * @author �̻���
 */
public class ElementView extends RelativeLayout
{
    /* ������ (������ �ʱ�ȭ�� �ݵ�� init()�� ���ؼ� �ؾ��Ѵ�!) */
    public ElementView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(null);
    }

    public ElementView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(null);
    }

    public ElementView(Context context)
    {
        super(context);
        init(null);
    }

    /* static ��� ���� */
    static private HashMap<String, RoundedDrawable> arDrawable;     // ���ҽ� ������

    /* ��Ÿ ��� ���� */
    private EDElement                               mElement;       // ������Ʈ ��ü
    private int                                     viewWidth;      // �� ��ü�� ���� ũ��
    private int                                     viewHeight;     // �� ��ü�� ���� ũ��
    private int                                     elementIconSize; // ȭ�� �ػ�, ����ϴ� ��Ȳ�� �°� ũ�⸦ ���ؾ���.
    private int                                     detailLevel;    // �ڼ������� ����
    private LayoutParams                            mParam;         // �� ���� ���̾ƿ� �Ӽ�

    /* ���̾ƿ� ���� ��ҵ� */
    private ImageView                               mElementIcon;   // ������Ʈ �������� ������
    private TextView                                mName;          // ������Ʈ ��ް� �̸��� ǥ����
    private TextView                                mDmg;           // ������Ʈ ���ݷ��� ǥ����
    private TextView                                mRate;          // ������Ʈ ���ݼӵ��� ǥ����

    /* Identifiers of views */
    private enum viewId
    {
        ICON, NAME, DAMAGE, RATE, LAST;

        @SuppressWarnings("unused")
        public int getIndex()
        {
            return this.ordinal();
        }

        public int getId()
        {
            return this.ordinal() + 1;
        }
    }

    /* static �ʱ�ȭ */
    static
    {
        arDrawable = new HashMap<String, RoundedDrawable>();
    }

    private void init(EDElement element)
    {
        // element�� null�̸� �⺻�� Element�� �Ҵ��Ѵ�. ���� �� ����� �⺻�� Element �� ���� ����.
        if (element != null)
            mElement = element;
        else
            mElement = new EDElement();

        // ���ο� ���� �並 �����ϰ� id ���� �Ҵ�����
        mElementIcon = new ImageView(getContext());
        mElementIcon.setId(viewId.ICON.getId());
        mName = new TextView(getContext());
        mName.setId(viewId.NAME.getId());
        mDmg = new TextView(getContext());
        mDmg.setId(viewId.DAMAGE.getId());
        mRate = new TextView(getContext());
        mRate.setId(viewId.RATE.getId());

        // ��Ÿ ��� ������ �ʱ�ȭ��.
        viewWidth = Math.max(280, getWidth());
        viewHeight = Math.max(0, getHeight());
        elementIconSize = 180;
        detailLevel = 0;

        // ElementView ��ü�� ���̾ƿ� �Ӽ��� �����Ѵ�. (ũ��)
        mParam = new LayoutParams(Math.max(viewWidth, elementIconSize), Math.max(viewHeight, elementIconSize));
        setLayoutParams(mParam);
        setPadding(10, 10, 10, 10);
        setBackgroundColor(Color.argb(128, 255, 0, 0));// �׽�Ʈ�� ����

        // element�� �� �Ӽ� ������ view�� ���� ǥ���Ѵ�.
        updateText();
        // �׸��� ���̴� ���·� �ʱ�ȭ��
        changeDetailLevel(detailLevel);

        // ���� ���̾ƿ��� ������.
        designInnerLayout();
    }

    /**
     * ElementView ������ ���̾ƿ��� �������Ѵ�.
     */
    private void designInnerLayout()
    {
        // ������ ǥ�úκ� ���̾ƿ�
        LayoutParams lpIcon = new LayoutParams(elementIconSize, elementIconSize);
        lpIcon.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lpIcon.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        addView(mElementIcon, lpIcon);

        LayoutParams lpName = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams lpDmg = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams lpRate = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        // �̸�, ��� ǥ�úκ� ���̾ƿ�
        // �������� �ؿ� �ٿ��� ǥ��
        lpName.addRule(RelativeLayout.BELOW, viewId.ICON.getId());
        addView(mName, lpName);

        // ���ݷ� ǥ�úκ� ���̾ƿ�
        // �̸�, ��� �ؿ� ǥ��
        lpDmg.addRule(RelativeLayout.BELOW, viewId.NAME.getId());
        addView(mDmg, lpDmg);

        // ���ݼӵ� ǥ�úκ� ���̾ƿ�
        // ���ݷ� �ؿ� ǥ��
        lpRate.addRule(RelativeLayout.BELOW, viewId.DAMAGE.getId());
        addView(mRate, lpRate);
    }

    public LayoutParams getParams()
    {
        return mParam;
    }

    public int getIconSize()
    {
        return elementIconSize;
    }

    /**
     * ElementView�� ������ EDElement�� �Ҵ��Ͽ� �� �並 �����ϴ� �����鿡�� �����͸� ������.
     * 
     * @param element
     *            �� �信 ������ ����
     */
    public void setElement(EDElement element)
    {
        if (element == null)
            Log.e("null error", "EDElement ��Ұ� null ����!");
        else
            mElement = element;

        RoundedDrawable roundIcon;

        // drawable�� ������ ���� ������ ���� ������ �ʰ� ������ ����.
        // ���ϸ� ����Ʈ�信�� ���ɸ�..
        if (arDrawable.containsKey(mElement.imgName))
        {
            roundIcon = arDrawable.get(mElement.imgName);
        }
        else
        {
            // ���� �̸����� ���ҽ� ID ���ؼ� ��Ʈ�� ������ �����
            int drawableId = getResources().getIdentifier(mElement.imgName, "drawable", "exam.androidproject");
            Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId);

            // �׽�Ʈ������ ������ �ٸ� �̹��� ���� ���ϱ� ũ�� �����ؾ���.
            if (bm.getWidth() != elementIconSize || bm.getHeight() != elementIconSize)
                bm = Bitmap.createScaledBitmap(bm, elementIconSize, elementIconSize, true);

            roundIcon = new RoundedDrawable(bm);
            // �� �� ������ ��κ� ��ü�� �� ������ �־ ������. ��Ȱ���� ���� ��ġ
            arDrawable.put(mElement.imgName, roundIcon);
        }

        mElementIcon.setImageDrawable(roundIcon);

        // �ٲ� �͵� ����
        updateText();
        updateHeight();
    }

    /**
     * Element�� �Ӽ��� �����ִ� ������ �ٲ۴�.<br/>
     * �����ִ� ���� �ٲ�Ƿ� ���� ũ�⵵ �ٲ��.
     * 
     * @param detailLevel
     *            0~2 ������ ���� �����Ѵ�.
     *            <ul>
     *            <li>0: �׸��� �����ش�.</li>
     *            <li>1: ���, �̸����� �����ش�.</li>
     *            <li>2: ���ݷ�, ���ݼӵ����� �����ش�.</li>
     *            </ul>
     */
    public void changeDetailLevel(int detailLevel)
    {
        this.detailLevel = detailLevel;
        switch (detailLevel)
        {
        case 0:
            mName.setVisibility(GONE);
            mDmg.setVisibility(GONE);
            mRate.setVisibility(GONE);
            break;

        case 1:
            mName.setVisibility(VISIBLE);
            mDmg.setVisibility(GONE);
            mRate.setVisibility(GONE);
            break;

        case 2:
            mName.setVisibility(VISIBLE);
            mDmg.setVisibility(VISIBLE);
            mRate.setVisibility(VISIBLE);
            break;
        }

        updateHeight();
    }

    /**
     * ���� ���� �ִ� EDElement�� �������� View�� �����Ѵ�.
     */
    private void updateText()
    {
        mName.setText(mElement.toString());
        mDmg.setText("DMG: " + String.valueOf(mElement.dmg));
        mRate.setText("RATE: " + String.valueOf(mElement.rate));
    }

    /**
     * ���� ũ�⸦ ������ ����Ͽ� ElementView�� ũ�⸦ ������.
     */
    private void updateHeight()
    {
        mParam.height = Math.max(viewHeight, elementIconSize + (mName.getLineHeight() * 2) * detailLevel);
    }
}
