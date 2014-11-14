package exam.androidproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import exam.customwidget.GoodsView;

/**
 * @author �̻���
 * 
 */
public class Store extends Activity implements View.OnClickListener
{
    private static final int LOW     = 0;
    private static final int MIDDLE  = 1;
    private static final int HIGH    = 2;
    private static final int SPECIAL = 3;
    private static final int LEGEND  = 4;
    private static final int REPAIR  = LEGEND + 1;
    private static final int UPGRADE = REPAIR + 1;
    private static final int REBUILD = UPGRADE + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_store);

        // �ڷΰ��� ��ư ��� ����
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        initTabElement();
        initTabTower();
    }

    private void initTabTower()
    {
        LinearLayout vg = (LinearLayout) findViewById(R.id.store_tab2_layout);

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.rightMargin = 30;

        GoodsView gv = new GoodsView(this, this, REPAIR);
        gv.setProperties("Ÿ�� ü�� ȸ��", "��", 3000);
        gv.txtCaption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        vg.addView(gv, lp);

        gv = new GoodsView(this, this, UPGRADE);
        gv.setProperties("�ִ� ü�� ����", "��", 10000);
        gv.txtCaption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        vg.addView(gv, lp);

        gv = new GoodsView(this, this, REBUILD);
        gv.setProperties("Ÿ�� ��Ǽ�", "��", 20000);
        gv.txtCaption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        vg.addView(gv, lp);
    }

    private void initTabElement()
    {
        LinearLayout vg = (LinearLayout) findViewById(R.id.store_tab1_layout);

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.rightMargin = 30;

        GoodsView gv = new GoodsView(this, this, LOW);
        gv.setProperties("�ϱ�", "G", 1000);
        vg.addView(gv, lp);

        gv = new GoodsView(this, this, MIDDLE);
        gv.setProperties("�߱�", "G", 2500);
        vg.addView(gv, lp);

        gv = new GoodsView(this, this, HIGH);
        gv.setProperties("���", "G", 5000);
        vg.addView(gv, lp);

        gv = new GoodsView(this, this, SPECIAL);
        gv.setProperties("Ư��", "G", 10000);
        vg.addView(gv, lp);

        gv = new GoodsView(this, this, LEGEND);
        gv.setProperties("����", "G", 20000);
        vg.addView(gv, lp);
    }

    /**
     * ������ ���� �ٲٴ� �޼ҵ�
     */
    public void switchTab(View v)
    {
        findViewById(R.id.store_tab1).setVisibility(View.INVISIBLE);
        findViewById(R.id.store_tab2).setVisibility(View.INVISIBLE);
        findViewById(R.id.store_tab3).setVisibility(View.INVISIBLE);

        switch (v.getId())
        {
        case R.id.store_btn_elementbox:
            findViewById(R.id.store_tab1).setVisibility(View.VISIBLE);
            break;
        case R.id.store_btn_tower:
            findViewById(R.id.store_tab2).setVisibility(View.VISIBLE);
            break;
        case R.id.store_btn_cashonly:
            findViewById(R.id.store_tab3).setVisibility(View.VISIBLE);
            break;
        }
    }

    @Override
    public void onClick(View v)
    {
        GoodsView gv = (GoodsView) v;
        Log.i("info", "caption: " + gv.getCaption());

        switch (gv.getId())
        {
        case LOW:
            Log.i("info", "value: " + gv.getValue());
            break;

        case MIDDLE:
            break;

        case HIGH:
            break;
        }
    }
}
