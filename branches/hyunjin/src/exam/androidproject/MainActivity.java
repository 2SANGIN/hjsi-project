package exam.androidproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import exam.androidproject.R;

public class MainActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.gamestart_btn).setOnClickListener(mClickListener);
        findViewById(R.id.continue_btn).setOnClickListener(mClickListener);
        findViewById(R.id.exit_btn).setOnClickListener(mClickListener);
    }

    Button.OnClickListener mClickListener = new View.OnClickListener()
                                          {

                                              @Override
                                              public void onClick(View v)
                                              {
                                                  // TODO Auto-generated method stub
                                                  {
                                                      switch (v.getId())
                                                      {
                                                      case R.id.gamestart_btn:
                                                          Intent intent1 = new Intent(MainActivity.this, NewGame.class);
                                                          startActivity(intent1);
                                                          break;
                                                      case R.id.continue_btn:
                                                          Intent intent2 = new Intent(MainActivity.this, Continue.class);
                                                          startActivity(intent2);
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
                                                                          System.exit(0);
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
                                              }
                                          };

}
