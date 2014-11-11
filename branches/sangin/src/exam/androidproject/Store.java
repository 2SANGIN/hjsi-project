package exam.androidproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author �̻���
 * 
 */
public class Store extends Activity
{
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
}
