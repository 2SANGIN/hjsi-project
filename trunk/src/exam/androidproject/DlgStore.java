package exam.androidproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import exam.customwidget.ElementView;
import exam.game.EDElement;

public class DlgStore extends Dialog implements OnClickListener
{

    private View page1;
    private View page2;

    public DlgStore(Context context)
    {
        super(context);
    }

    public DlgStore(Context context, int theme)
    {
        super(context, theme);
    }

    public DlgStore(Context context, boolean cancelable, OnCancelListener cancelListener)
    {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // ���̾�α� ���� ǥ�ø� ���ش�
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // ���̾�α� ����� ���ش� (�׵θ��� ����)
        setContentView(R.layout.dialog_store);

        // ���Ÿ� ����ϰų� ���� �� "��� ����", "��ġ"�� ������ �ʴ� ��츦 ���� ������
        setOnCancelListener(new OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                // ���� ��ư�� �ٽ� ���� �� �ְ� �˾����� ǥ���� â�� �ٲ��ִ� ��
                page1.setVisibility(View.VISIBLE);
                page2.setVisibility(View.INVISIBLE);
            }
        });

        page1 = findViewById(R.id.dlg_store_goods_page1); // ���� ���� Ȯ��â
        page2 = findViewById(R.id.dlg_store_goods_page2); // ������ ���� ǥ�� �� ��� �������� ��ġ���� ���� â

        // �� ��ư��(�� ���� â�� �� ���� ��ư)�� �����ʸ� ����Ѵ�.
        ((TextView) findViewById(R.id.dlg_store_btn_buy)).setOnClickListener(this);
        ((TextView) findViewById(R.id.dlg_store_btn_cancel)).setOnClickListener(this);
        ((TextView) findViewById(R.id.dlg_store_btn_morebuy)).setOnClickListener(this);
        ((TextView) findViewById(R.id.dlg_store_btn_arrangement)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.dlg_store_btn_buy: // ������ ���� �������� Ȯ���ϴ� â�� ���� ��ư
            page1.setVisibility(View.INVISIBLE);   // ���� â�� �����
            page2.setVisibility(View.VISIBLE);     // ���� â�� ������

            // TODO ���� ���� �޼ҵ带 �ۼ��Ѵ�

            // �׽�Ʈ������ �⺻ element ���� �信 �����Ѵ�.
            ElementView ev = (ElementView) findViewById(R.id.dlg_store_goods_element);
            ev.setElement(new EDElement());
            ev.changeDetailLevel(1);
            break;

        case R.id.dlg_store_btn_cancel: // ��� ��ư�� ������ ���
            hide();
            break;

        case R.id.dlg_store_btn_morebuy: // �� �����Ϸ��� ���
            // TODO ���� element ���� ���� �޼ҵ带 �����ϸ� �ɵ�.
            break;

        case R.id.dlg_store_btn_arrangement: // ������ ���Ҹ� �ٷ� ��ġ�Ϸ��� ����
            // TODO ���� �����ؼ� ���� ���Ҹ� �ʿ� �ٷ� ��ġ�� �� �ְ� ó���Ѵ�
            break;
        }
    }
}
