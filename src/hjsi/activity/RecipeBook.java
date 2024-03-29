package hjsi.activity;

import hjsi.common.AppManager;
import hjsi.customview.RecipeView;
import hjsi.game.Tower;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * 조합도감 액티비티
 *
 * @author 이상인
 */
public class RecipeBook extends Base {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AppManager.printSimpleLog();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipebook);

    // 뒤로가기 버튼 기능 구현
    Button back = (Button) findViewById(R.id.back);
    back.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    // 테스트용 TowerView 생성
    Tower ede = new Tower();
    ede.name = "나루토";
    ede.imgName = "naruto01";
    ede.damage = 10;
    ede.attackSpeed = 2;

    Tower ede2 = new Tower();
    ede2.name = "사쿠라";
    ede2.imgName = "naruto04";
    ede2.damage = 9;
    ede2.attackSpeed = 1;

    Tower ede3 = new Tower();
    ede3.name = "사스케";
    ede3.imgName = "naruto07";
    ede3.damage = 11;
    ede3.attackSpeed = 3;

    RecipeExpandibleAdapter recipeListAdapter = new RecipeExpandibleAdapter(this);

    ArrayList<Recipe> tmpList = makeRecipeList();
    tmpList.set(0, new Recipe(ede, ede2, ede3));
    recipeListAdapter.add("하급", tmpList);
    recipeListAdapter.add("중급", makeRecipeList());
    recipeListAdapter.add("고급", makeRecipeList());

    ExpandableListView recipeList = (ExpandableListView) findViewById(R.id.grouplist);
    recipeList.setAdapter(recipeListAdapter);
    recipeList.setGroupIndicator(null);
  }

  /**
   * 지금은 테스트용으로 대충 5개의 레시피를 갖는 리스트를 반환한다
   */
  private ArrayList<Recipe> makeRecipeList() {
    AppManager.printSimpleLog();
    ArrayList<Recipe> ar = new ArrayList<Recipe>(5);

    ar.add(new Recipe());
    ar.add(new Recipe());
    ar.add(new Recipe());
    ar.add(new Recipe());
    ar.add(new Recipe());

    return ar;
  }
}


class RecipeExpandibleAdapter extends BaseExpandableListAdapter {
  private Context context;
  private ArrayList<String> arGroup;
  private ArrayList<ArrayList<Recipe>> arChildList;

  public RecipeExpandibleAdapter(Context context) {
    this.context = context;

    arGroup = new ArrayList<String>();
    arChildList = new ArrayList<ArrayList<Recipe>>();
  }

  public void add(String groupName, ArrayList<Recipe> childList) {
    arGroup.add(groupName);
    arChildList.add(childList);
  }

  @Override
  public int getGroupCount() {
    return arGroup.size();
  }

  @Override
  public String getGroup(int groupPosition) {
    return arGroup.get(groupPosition);
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View v, ViewGroup parent) {
    TextView textName;
    TextView textNum;

    if (v == null) {
      // 새로 레이아웃을 만든다
      v = new RelativeLayout(context);
      RelativeLayout layout = (RelativeLayout) v;
      textName = new TextView(context);
      textName.setId(1);
      textName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
      textNum = new TextView(context);
      textNum.setId(2);
      textNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

      LayoutParams lpName =
          new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
              android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
      lpName.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      layout.addView(textName, lpName);

      LayoutParams lpNum =
          new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
              android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
      lpNum.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      layout.addView(textNum, lpNum);
    } else {
      textName = (TextView) v.findViewById(1);
      textNum = (TextView) v.findViewById(2);
    }

    textName.setText(getGroup(groupPosition));
    textNum.setText(getChildrenCount(groupPosition) + " / 32");

    return v;
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return arChildList.get(groupPosition).size();
  }

  @Override
  public Recipe getChild(int groupPosition, int childPosition) {
    return arChildList.get(groupPosition).get(childPosition);
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View v,
      ViewGroup parent) {
    if (v == null) {
      v = new RecipeView(context);
    }

    RecipeView recipeView = (RecipeView) v;

    recipeView.setElements(getChild(groupPosition, childPosition).arElements);

    return v;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }
}


// ExpandibleListView를 위한 자료구조
class Recipe {
  Tower arElements[] = new Tower[3];

  Recipe() {
    for (int i = 0; i < arElements.length; i++) {
      arElements[i] = new Tower();
    }
  }

  Recipe(Tower e1, Tower e2, Tower eResult) {
    arElements[0] = e1;
    arElements[1] = e2;
    arElements[2] = eResult;
  }
}
