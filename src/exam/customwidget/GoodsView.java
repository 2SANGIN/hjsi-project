package exam.customwidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import exam.androidproject.R;

public class GoodsView extends LinearLayout
{
    public GoodsView(Context context, OnClickListener l, int goodsId)
    {
        super(context);
        setOnClickListener(l);
        this.goodsId = goodsId;
        init();
    }

    public GoodsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public GoodsView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    /* Ŭ���� ���� �ڿ� static ���� */
    private static Bitmap    bmpTreasure;      // ���� ���� �̹����� ������ �������� �Ἥ static���� �ص״µ�,
                                                // ���߿� ��޺��� �ٸ� �̹����� ���� static���� �ϸ� �ȵ�
    private static boolean   flag      = false; // ó���� �� �� ������ �ؼ� ���� ���� �̹����� �ε� ���״��� üũ

    /* view id (������ �ʿ�� ����) */
    private static final int IMG_GOODS = 0;
    private static final int TXT_NAME  = 1;
    private static final int TXT_VALUE = 2;
    /* view */
    private ImageView        imgGoods;         // ��ǰ �̹���
    public TextView          txtCaption;       // ��ǰ��
    private int              goodsId;          // ��ǰ ������ ������ �ĺ��� (�ϱ� �������� Ÿ�� ��Ǽ����� ��)
    private TextView         txtValue;         // ���� ǥ�ÿ� �ؽ�Ʈ
    private String           valueUnit = "G";  // ���� ����
    private int              value     = 1000; // ��ǰ ����

    private void init()
    {
        imgGoods = new ImageView(getContext());
        imgGoods.setId(IMG_GOODS); // ���� ������ �� ���µ� �ϴ� ���. ������ view�� ��������

        /* �� �並 �� ���� �� ������ٸ� ���� ���� �̹����� �ҷ��ͼ� static ������ ���� */
        if (flag == false)
        {
            bmpTreasure = BitmapFactory.decodeResource(getResources(), R.drawable.goodsview_treasurebox_img);
            bmpTreasure = Bitmap.createScaledBitmap(bmpTreasure, 400, 300, true);
            flag = true;
        }
        imgGoods.setImageBitmap(bmpTreasure);

        // ������ �� ����
        txtCaption = new TextView(getContext());
        txtCaption.setId(TXT_NAME);
        txtValue = new TextView(getContext());
        txtValue.setId(TXT_VALUE);

        // �� �Ӽ��̳� ��ġ �� �ڼ��� ����
        designInnerLayout();
    }

    private void designInnerLayout()
    {
        DisplayMetrics screen = getResources().getDisplayMetrics(); // ȭ�� ũ�� ���ϱ� ���� ��ü

        setBackgroundColor(Color.DKGRAY);
        setOrientation(LinearLayout.VERTICAL);
        setWeightSum(5); // ���Ͼ� ���̾ƿ��� ũ�⸦ 5�� ���, ������ ���ϵ� ����� ũ�⸦ 5���� 1�̳� 5���� 2�� ���� ������ ����

        float scaleWidth = 1f / 4f; // �ϳ��� ������ ��ư�� ȭ�鿡�� �����ϴ� ����

        LayoutParams lp = new LayoutParams((int) (screen.widthPixels * scaleWidth), screen.heightPixels * 3 / 5);
        lp.weight = 3; // �θ� ���� weightSum ������ �����ϴ� ���� (imgGoods�� 3/5)
        addView(imgGoods, lp);

        lp = new LayoutParams((int) (screen.widthPixels * scaleWidth), screen.heightPixels * 1 / 5);
        lp.weight = 1;
        txtCaption.setText("�ϱ�");
        txtCaption.setBackgroundColor(Color.RED);
        txtCaption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        txtCaption.setGravity(Gravity.CENTER);
        addView(txtCaption, lp);

        lp = new LayoutParams((int) (screen.widthPixels * scaleWidth), screen.heightPixels * 1 / 5);
        lp.weight = 1;
        txtValue.setText(String.valueOf(value) + valueUnit); // ���� + "����" �������� ǥ����
        txtValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        txtValue.setGravity(Gravity.CENTER);
        addView(txtValue, lp);
    }

    // �̹��� ������ ��ǰ��, ����, ȭ����� ����
    public void setProperties(String caption, String valueUnit, int value)
    {
        setCaption(caption);
        setValueUnit(valueUnit);
        setValue(value);
    }

    public void setCaption(String caption)
    {
        txtCaption.setText(caption);
    }

    public void setValue(int value)
    {
        this.value = value;
        txtValue.setText(String.valueOf(value) + valueUnit);
    }

    public void setValueUnit(String unit)
    {
        valueUnit = unit;
        txtValue.setText(String.valueOf(value) + valueUnit);
    }

    public String getCaption()
    {
        return txtCaption.getText().toString();
    }

    public int getValue()
    {
        return value;
    }

    public int getGoodsId()
    {
        return goodsId;
    }
}
