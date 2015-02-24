package hjsi.activity;

import hjsi.common.AppManager;
import hjsi.common.Camera;
import hjsi.common.GameSurface;
import hjsi.game.GameMaster;
import hjsi.game.GameState;
import hjsi.game.Tower;
import hjsi.game.Unit;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ToggleButton;

public class Game extends Base implements OnClickListener {
  private boolean explicitQuit = false; // Map에서 사용한 리소스 해제 타이밍을 위한 변수

  private Button btnBook, btnPause, btnStore, btnDeploy, btnGen;
  private ToggleButton btnFF;
  private DlgSetting dlgSetting;

  /** bgm 재생 객체 */
  private MediaPlayer bgMusic;
  /** bgm 음소거 여부 */
  private boolean bgmPlaying = true;
  /** 게임을 진행하는 인게임 스레드를 가진 개체 */
  private GameMaster gameMaster;
  /** 카메라 */
  private Camera camera;
  /** 게임 정보 관리 객체 */
  GameState gState = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AppManager.printSimpleLog();
    super.onCreate(savedInstanceState);

    gState = AppManager.getInstance().getGameState();

    /*
     * 화면 비율을 구해서 카메라를 생성할 때 넘겨준다.
     */
    camera = new Camera(AppManager.getInstance().getDisplayFactor());
    /*
     * surfaceview 생성 및 등록
     */
    GameSurface gameView = new GameSurface(getApplicationContext(), camera, gState);
    setContentView(gameView);

    /*
     * 기타 버튼 UI 등록
     */
    View layout = getLayoutInflater().inflate(R.layout.activity_game, null);
    addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

    /*
     * 버튼 뷰 참조자 및 드로블 가져옴
     */
    btnBook = (Button) findViewById(R.id.btn_book);
    btnBook.setOnClickListener(this);
    btnPause = (Button) findViewById(R.id.btn_pause);
    btnPause.setOnClickListener(this);
    btnStore = (Button) findViewById(R.id.btn_store);
    btnStore.setOnClickListener(this);
    btnDeploy = (Button) findViewById(R.id.btn_deploy);
    btnDeploy.setOnClickListener(this);
    btnGen = (Button) findViewById(R.id.btn_gen);
    btnGen.setOnClickListener(this);
    btnFF = (ToggleButton) findViewById(R.id.btn_ff);
    btnFF.setOnClickListener(this);

    dlgSetting = new DlgSetting(this, this);
    dlgSetting.setCanceledOnTouchOutside(false);

    bgMusic = MediaPlayer.create(this, R.raw.bgm);
    bgMusic.setLooping(true);
    bgMusic.start();

    /* GameMaster 생성 */
    gameMaster = new GameMaster(this, gState);
    gameMaster.playGame();

    AppManager.printDetailLog(getClass().getSimpleName() + " 초기화 완료");
  }

  @Override
  protected void onPause() {
    AppManager.printSimpleLog();
    super.onStop();

    if (gameMaster != null) {
      gameMaster.pauseGame();
    }

    if (bgMusic != null) {
      bgMusic.pause();
    }
  }

  @Override
  protected void onResume() {
    AppManager.printSimpleLog();
    super.onResume();

    if (bgMusic != null) {
      bgMusic.start();
    }
  }

  @Override
  protected void onDestroy() {
    AppManager.printSimpleLog();
    super.onDestroy();

    if (gameMaster != null) {
      gameMaster.quitGame();
    }

    if (bgMusic != null) {
      bgMusic.stop();
      bgMusic.release();
    }

    if (explicitQuit) {
      /* 사용했던 리소스를 해제한다. */
      AppManager.getInstance().allRecycle();
      gState.purgeGameState(); // 게임 상태정보를 없앤다.
    }
  }

  /**
   * back 키를 누르면 옵션 메뉴가 열리도록 함
   */
  @Override
  public void onBackPressed() {
    AppManager.printSimpleLog();
    showSettingMenu();
  }

  @Override
  public void onClick(View v) {
    AppManager.printDetailLog(v.toString());

    if (v == btnBook) {
      Intent Book = new Intent(Game.this, RecipeBook.class);
      startActivity(Book);
    }
    /*
     * play 버튼
     */
    else if (v == btnPause) {
      gameMaster.pauseGame();
      showSettingMenu();
    }
    /* 재생 -> 일시정지 */

    else if (v == btnStore) {
      Intent Store = new Intent(Game.this, Store.class);
      startActivity(Store);
    }

    else if (v == btnDeploy) {
      gState.refreshArea();
      gState.onDeployMode();
    }

    else if (v == btnGen) {
      btnGen.setVisibility(View.GONE);
      gState.waveReady().start();
      gameMaster.playGame();
    }

    else if (v == btnFF) {
      if (btnFF.isChecked()) {
        GameMaster.ff = 2;
      } else {
        GameMaster.ff = 1;
      }
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    /*
     * 기본적인 View 객체에 대한 이벤트는 해당 객체가 먼저 이벤트를 받아서 처리하므로, 여기서는 카메라 및 유닛 등의 조작에 대해서만 고려한다. 다음은 터치 이벤트 중
     * 스크롤 및 핀치줌 인/아웃에 관한 동작은 카메라가 처리하도록 한다.
     */
    if (camera.touchHandler(event)) {
      AppManager.printEventLog(event);
      return true;
    }

    /*
     * 카메라가 처리할 이벤트가 아닌 경우는 보통의 클릭 동작이며, 여러가지 게임 개체에 대한 동작을 수행한다. 가장 먼저, 화면 터치 좌표를 게임월드의 좌표로 변환한다.
     */
    int logicalX = (int) ((event.getX() + camera.getX()) / camera.getScale());
    int logicalY = (int) ((event.getY() + camera.getY()) / camera.getScale());

    // 터치로 입력받은 화면상의 좌표를 게임월드 비율에 맞게 변환함
    logicalX =
        (int) (logicalX / (float) (camera.getScreenWidth() / (float) GameState.WORLD_WIDTH) + 0.5);
    logicalY =
        (int) (logicalY / (float) (camera.getScreenWidth() / (float) GameState.WORLD_WIDTH) + 0.5);

    /* 로그 출력용 코드 */
    event.setLocation(logicalX, logicalY);
    AppManager.printEventLog(event);

    int row = gState.getRow(logicalY);
    int col = gState.getColumn(logicalX);
    Tower tower = gState.getTower(row, col);
    if (tower != null) {
      AppManager.printDetailLog("타워" + tower.getId() + " 클릭 됨.");
    }
    /* 로그 출력용 코드 끝 */

    Unit unit = gState.getUnit(logicalX, logicalY);
    if (unit != null) {
      AppManager.printInfoLog(unit.toString());
    } else if (gState.checkDeployMode()) {
      gState.deployTower(logicalX, logicalY);
    }

    return super.onTouchEvent(event);
  }

  public void quitExplicitly() {
    AppManager.printSimpleLog();
    explicitQuit = true;
  }

  private void showSettingMenu() {
    AppManager.printSimpleLog();
    dlgSetting.show();
    gameMaster.pauseGame();
  }

  /**
   * 다이얼로그의 메시지를 처리하는 메소드
   */
  public void handleDialog(int msg) {
    AppManager.printSimpleLog();

    switch (msg) {
      case DlgSetting.DLG_BTN_RESUME:
        gameMaster.playGame();
        break;

      case DlgSetting.DLG_BTN_SOUND:
        if (bgmPlaying) {
          bgmPlaying = false;
          bgMusic.pause();
        } else {
          bgmPlaying = true;
          bgMusic.start();
        }
        break;

      case DlgSetting.DLG_BTN_QUIT:
        bgMusic.stop();
        bgMusic.release();
        bgMusic = null;
        quitExplicitly(); // 이번에 Game.onDestroy() 될 때 리소스 해제하라고 알림
        AppManager.getInstance().quitApp();
        break;

      default:
        AppManager.printErrorLog("Game 액티비티에 예외 메시지(" + msg + ")가 왔습니다.");
        break;
    }
  }

  /**
   * TODO handler leak 없도록 구현해야함
   */
  private final Handler spawnButtonHandler = new Handler() {
    public void handleMessage(Message msg) {
      btnGen.setVisibility(View.VISIBLE);
    }
  };

  public void readySpawnButton() {
    spawnButtonHandler.sendEmptyMessage(0);
  }
}
