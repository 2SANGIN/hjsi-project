package hjsi.game;

import hjsi.common.AppManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Tower extends Unit implements Attackable {
  public String name;
  /**
   * 공격력
   */
  public int damage;
  /**
   * 공격속도
   */
  public int attackSpeed = 2000;
  /**
   * 사정거리
   */
  public int range;
  /**
   * 등급
   */
  private int type;
  public String imgName;

  private long beforeTime = System.currentTimeMillis();

  private static final int PRIMITIVE = 1;
  private static final int BASIC = 2;
  private static final int SPECIAL = 3;
  private static final int MIGHTY = 4;
  private static final int TOP = 5;
  private static final int LEGEND = 6;
  private static final int HIDDEN = 7;

  public Tower() {
    super();
    name = "불";
    type = 0;
    damage = 5;
    imgName = "element_match";

  }

  public Tower(int x, int y, Bitmap face) {
    super(x, y, face);
    range = 400;
  }

  @Override
  public void action() {
    // TODO Auto-generated method stub

  }

  @Override
  public void touch() {
    // TODO Auto-generated method stub

  }

  @Override
  public void draw(Canvas canvas) {
    super.draw(canvas);
    showRange(range, canvas);
  }

  @Override
  public void attack() {
    // TODO Auto-generated method stub
    if (System.currentTimeMillis() - beforeTime > attackSpeed)
      beforeTime = System.currentTimeMillis();
    else
      return;
    for (int i = 0; i < GameState.getInstance().Mobs.size(); i++) {
      if (GameState.getInstance().Mobs.get(i).created == false)
        continue;

      else if ((int) Math.sqrt(Math.pow(GameState.getInstance().Mobs.get(i).cntrX - this.cntrX, 2)
          + Math.pow(GameState.getInstance().Mobs.get(i).cntrY - this.cntrY, 2)) <= range) {
        GameState.getInstance().projs.add(new Projectile(cntrX, cntrY, damage, i, AppManager
            .getInstance().getBitmap("proj1")));
        break;
      }
    }

  }
}
