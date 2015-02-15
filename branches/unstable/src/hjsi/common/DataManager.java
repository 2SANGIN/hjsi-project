package hjsi.common;

import hjsi.game.GameState;
import hjsi.game.Tower;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

public class DataManager {
  /**
   * DB 도우미
   */
  private static AppDatabaseHelper databaseHelper;
  /**
   * DB 파일명
   */
  private static final String DB_NAME = "ElementTD.db";
  /**
   * 유저정보 테이블명
   */
  public static final String TABLE_USERDATA = "user_data";
  /**
   * 타워정보 테이블명
   */
  public static final String TABLE_TOWERINFO = "tower_info";

  public DataManager() {}

  public static String getSqlCreateTable(String tableName, ArrayList<String> attributes) {
    String sql = "create table " + tableName + " (";

    for (String attribute : attributes) {
      sql += " " + attribute + ",";
    }
    if (sql.charAt(sql.length() - 1) == ',')
      sql = sql.substring(0, sql.length() - 1);
    sql += ");";

    return sql;
  }

  /**
   * 유저 데이터를 로드한다. 파일로 저장되어 있는 DB의 버전보다 새로 입력된 DB의 버전이 더 최신일 경우 DB도우미 클래스에서 onUpdate를
   * 호출한다.
   */
  public static void loadDatabase(Context context, int version) {
    databaseHelper = new AppDatabaseHelper(context, DataManager.DB_NAME, null, version);
    // 최초 실행시 DB 생성도 같이 됨
    SQLiteDatabase db = databaseHelper.getReadableDatabase();

    Cursor cursor = db.rawQuery("select * from " + TABLE_USERDATA + " where id=1", null);
    while (cursor.moveToNext()) {
      int userWave = cursor.getInt(1);
      int userGold = cursor.getInt(2);
      int userCoin = cursor.getInt(3);
      String towers = cursor.getString(4);
      String recipes = cursor.getString(5);
      String upgrades = cursor.getString(6);
      AppManager.printDetailLog("유저정보 wave: " + userWave + ", gold: " + userGold + ", coin: " + userCoin + ", towers: "
          + towers);

      // TODO 문자열 형태의 tower 목록을 파싱해서 타워 객체를 생성하고 링크드리스트<Tower> 형태로 만든다.

      GameState.getInstance().setUserData(userWave, userGold, userCoin, new LinkedList<Tower>());
    }
    cursor.close();
    db.close();
  }

  /**
   * 게임의 정보를 저장한다.
   * 
   * @param state
   */
  public static void save() {
    SQLiteDatabase db = databaseHelper.getWritableDatabase();

    StringBuilder towers = new StringBuilder();
    for (Tower tower : GameState.getInstance().getTowers()) {
      towers.append(tower.getId()).append(',');
    }

    ContentValues values = new ContentValues();
    values.put("wave", GameState.getInstance().getWave());
    values.put("gold", GameState.getInstance().getGold() + 100);
    values.put("coin", GameState.getInstance().getCoin() + 1);
    values.put("towers", towers.toString());
    int affectedRows = db.update("user_data", values, "id=?", new String[] {"1"});
    AppManager.printDetailLog("db updated " + affectedRows + " row(s).");
    db.close();
  }

  public static void insertRecords(String tableName, ArrayList<ContentValues> values) {
    SQLiteDatabase db = databaseHelper.getWritableDatabase();
    for (ContentValues value : values) {
      long rowId = db.insert(tableName, null, value);
      AppManager.printInfoLog(rowId + "번째 레코드: " + value.toString() + " 삽입");
    }
    db.close();
  }

  /**
   * 파일로부터 데이터를 읽어서 파싱한 후 데이터베이스에 삽입할 수 있는 레코드 형태로 반환한다. assets/db/tower_spec_table.csv 같은
   * 형태의 파일만 취급한다.
   * 
   * @param fileName 데이터베이스에 입력할 데이터가 들어있는 assets/db 폴더 내의 csv 파일이름
   * @return DB 테이블에 삽입할 레코드들(ContentValues 리스트)
   */
  public static ArrayList<ContentValues> parseTable(String fileName) {
    ArrayList<ContentValues> values = new ArrayList<ContentValues>();

    try {
      String[] lines = AppManager.readTextFile(AppManager.getPathMap("db").get(fileName));
      for (int i = 0; i < lines.length; i++) {
        lines[i] = lines[i].trim();
      }

      String[] attrNames = lines[0].split(",");
      String[] attrTypes = lines[1].split(",");

      for (int i = 2; i < lines.length; i++) {
        String[] attrValues = lines[i].split(",");

        // 데이터 컬럼 수, 타입의 수와 값의 갯수가 서로 일치하는지 검사한다.
        if (attrNames.length != attrTypes.length || attrNames.length != attrValues.length) {
          AppManager.printErrorLog(fileName + "의 파일 내용이 형식에 맞지 않습니다!");
          AppManager.printErrorLog(i + "행: " + attrNames.length + ", " + attrTypes.length + ", " + attrValues.length);
        }

        ContentValues value = new ContentValues();
        // 텍스트의 한 줄에 대해서 컬럼 별로 처리를 한다.
        for (int j = 0; j < attrNames.length; j++) {
          String attrType = attrTypes[j];
          // 정수형
          if (attrType.equalsIgnoreCase("integer")) {
            value.put(attrNames[j], Integer.parseInt(attrValues[j]));
          }
          // 문자형
          else if (attrType.equalsIgnoreCase("text")) {
            value.put(attrNames[j], attrValues[j]);
          }
          // 이외의 경우
          else {
            AppManager.printErrorLog(i + "행의 " + j + "번 컬럼의 데이터 형식이 지정되지 않았습니다.");
          }
        }
        // 레코드를 ArrayList에 추가함
        values.add(value);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return values;
  }

  public static Tower createTower(int towerId) {
    String[] columns = "id,name,tier,damage,attackspeed,range".split(",");

    SQLiteDatabase db = databaseHelper.getReadableDatabase();
    Cursor cursor =
        db.query(TABLE_TOWERINFO, columns, "id=?", new String[] {String.valueOf(towerId)}, null, null, null);

    cursor.moveToNext();
    int id = cursor.getInt(0);
    String name = cursor.getString(1);
    int tier = cursor.getInt(2);
    int dmg = cursor.getInt(3);
    int atkSpeed = cursor.getInt(4);
    int range = cursor.getInt(5);

    String fileName = "tower" + id;
    Bitmap face = AppManager.getBitmap(fileName);
    if (face == null) {
      try {
        face = AppManager.readImageFile("img/towers/" + fileName + ".png", null);
        AppManager.addBitmap(fileName, face);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return new Tower(id, name, tier, dmg, atkSpeed, range, face);
  }
}
