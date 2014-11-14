package exam.customwidget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import exam.androidproject.R;
import exam.game.EDElement;

/**
 * ListView�� ���� �� ��¥�� View. <br/>
 * �� ������ ���չ��� �����ִ� ������ �Ѵ�.
 * 
 * @author �̻���
 * 
 */
public class RecipeView extends RelativeLayout implements View.OnClickListener
{
    /* ������ */
    public RecipeView(Context context)
    {
        super(context);
        init();
    }

    public RecipeView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public RecipeView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    /* static ��� ���� */
    private static Drawable drawablePlus;
    private static Drawable drawableEqual;
    private static boolean  flag             = false;

    /* ��� ���� */
    private int             detailLevel      = 1;                 // Element�� �̸����� ���̴� ���¸� ��Ÿ��

    /* Views of layout */
    private ImageView       mImgPlus;
    private ImageView       mImgEqual;
    /**
     * ������ �Ǵ� ����� �� ���� Element�� ����� Element �� ���� ��� ������. <br/>
     * <ul>
     * <b>index ����</b>
     * <li>0: ���� ��� Element</li>
     * <li>1: ������ ��� Element</li>
     * <li>2: ������ ��� Element</li>
     * </ul>
     */
    private ElementView     arElementViews[] = new ElementView[3];

    /* Identifiers of views */
    private enum viewId
    {
        LHS1, LHS2, RESULT, IMG_PLUS, IMG_EQUAL, LAST;

        public int getIndex()
        {
            return this.ordinal();
        }

        public int getId()
        {
            return this.ordinal() + 1;
        }
    }

    private void init()
    {
        // ���̾ƿ��� ���� �並 �����ϰ� id ���� �Ҵ�����
        mImgPlus = new ImageView(getContext());
        mImgPlus.setId(viewId.IMG_PLUS.getId());
        mImgEqual = new ImageView(getContext());
        mImgEqual.setId(viewId.IMG_EQUAL.getId());

        // ElementView���� �̸����� ���̴� ���·� �ʱ�ȭ�Ѵ�.
        for (int i = 0; i < arElementViews.length; i++)
        {
            arElementViews[i] = new ElementView(getContext());
            arElementViews[i].setId(viewId.LHS1.getId() + i);
            arElementViews[i].changeDetailLevel(detailLevel);
        }

        // ���ϱ�, ��ȣ ���ҽ� �ҷ���
        if (flag == false)
        {
            // ��ü ��ü �߿��� ��ü�� ó�� ���� �� �� ������.
            drawablePlus = getResources().getDrawable(R.drawable.recipeview_plus_img);
            drawableEqual = getResources().getDrawable(R.drawable.recipeview_equal_img);
            flag = true;
        }
        mImgPlus.setImageDrawable(drawablePlus);
        mImgEqual.setImageDrawable(drawableEqual);

        setPadding(10, 10, 10, 10);

        // ���̾ƿ� ����
        designInnerLayout();

        // onClick �޼ҵ带 �����
        setOnClickListener(this);
    }

    /**
     * RecipeView ������ ���̾ƿ��� �������Ѵ�.
     */
    private void designInnerLayout()
    {
        int plusSize = 120;

        // ���̾ƿ� �Ķ���͸� �ʱ�ȭ
        LayoutParams lp[] = new LayoutParams[viewId.LAST.getIndex()];

        /* RecipeView�� �� view�� ��Ͻ�Ŵ */
        // ���� ��� Element ����
        int idx = viewId.LHS1.getIndex();
        lp[idx] = arElementViews[idx].getParams();
        lp[idx].addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        addView(arElementViews[idx]);

        // + �̹��� ����
        idx = viewId.IMG_PLUS.getIndex();
        lp[idx] = new LayoutParams(plusSize, plusSize);
        lp[idx].addRule(RelativeLayout.RIGHT_OF, viewId.LHS1.getId());
        lp[idx].addRule(RelativeLayout.ALIGN_TOP, viewId.LHS1.getId());
        lp[idx].setMargins(10, 0, 10, 0);
        lp[idx].topMargin = (arElementViews[viewId.LHS1.getId()].getIconSize() - plusSize) / 2;
        addView(mImgPlus, lp[idx]);

        // ������ ��� Element ����
        idx = viewId.LHS2.getIndex();
        lp[idx] = arElementViews[idx].getParams();
        lp[idx].addRule(RelativeLayout.RIGHT_OF, viewId.IMG_PLUS.getId());
        addView(arElementViews[idx]);

        // = �̹��� ����
        idx = viewId.IMG_EQUAL.getIndex();
        lp[idx] = new LayoutParams(plusSize, plusSize);
        lp[idx].addRule(RelativeLayout.RIGHT_OF, viewId.LHS2.getId());
        lp[idx].addRule(RelativeLayout.ALIGN_TOP, viewId.LHS2.getId());
        lp[idx].setMargins(10, 0, 10, 0);
        lp[idx].topMargin = (arElementViews[viewId.LHS2.getId()].getIconSize() - plusSize) / 2;
        addView(mImgEqual, lp[idx]);

        // ���հ�� Element ����
        idx = viewId.RESULT.getIndex();
        lp[idx] = arElementViews[idx].getParams();
        lp[idx].addRule(RelativeLayout.RIGHT_OF, viewId.IMG_EQUAL.getId());
        addView(arElementViews[idx]);
    }

    /**
     * @param arElements
     *            �ϳ��� ����Ʈ�� �׸�(O+O=O)�� �� EDElement ��ü 3���� �迭
     */
    public void setElements(EDElement[] arElements)
    {
        for (int i = 0; i < 3; i++)
        {
            arElementViews[i].setElement(arElements[i]);
        }
    }

    @Override
    public void onClick(View v)
    {
        if (detailLevel == 1)
            detailLevel = 2;
        else
            detailLevel = 1;

        // �� ���� Element�� �ڼ������� ���¸� ���� �����Ѵ�.
        for (ElementView ev : arElementViews)
        {
            ev.changeDetailLevel(detailLevel);
        }
    }
}
