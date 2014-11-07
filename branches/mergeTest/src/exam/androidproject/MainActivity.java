package exam.androidproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity
{
    // ��Ƽ��Ƽ �� ����� ���� ��û�ڵ� ���
    private static final int ACT_NEWGAME  = 0;
    private static final int ACT_CONTINUE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void mOnClick(View v)
    {
        switch (v.getId())
        {
        case R.id.gamestart_btn:
            Intent intent1 = new Intent(MainActivity.this, Picturebook.class);
            startActivityForResult(intent1, ACT_NEWGAME);
            break;
        case R.id.continue_btn:
            Intent intent2 = new Intent(MainActivity.this, Continue.class);
            startActivityForResult(intent2, ACT_CONTINUE);
            break;

        case R.id.exit_btn:
            // view�� alert �̸� �˾����� �� ��ư�� ������ �˾�â�� �ߴ� ����
            new AlertDialog.Builder(MainActivity.this).setTitle("��������") // �˾�â Ÿ��Ʋ��
                    .setMessage("�����Ͻðڽ��ϱ�?")  // �˾�â ����
                    .setPositiveButton("��������", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // TODO Auto-generated method stub
                            finish();
                        }
                    }).setNeutralButton("�ݱ�", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dlg, int sumthin)
                        {
                            // �ݱ� ��ư�� ������ �ƹ��͵� ���ϰ� �ݱ� ������ �׳� ���
                        }
                    }).show(); // �˾�â ������
            break;
        }
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
        case ACT_NEWGAME:
        case ACT_CONTINUE:
            if (resultCode == RESULT_OK)
            {
                Intent intentForActMap = new Intent(getApplicationContext(), Map.class);
                startActivity(intentForActMap);
            }

            break;
        }
    }
}
