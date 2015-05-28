package cn.xidianyaoyao.app.ui.shake;

import java.io.File;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.data.AdapterDishShake;
import cn.xidianyaoyao.app.data.DataDishShake;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.sqlite.HistorySQLiteHelper;
import cn.xidianyaoyao.app.ui.restaurant.RestauDish_info;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;

public class shaked_dish extends Activity {

	private ImageView mDishShakedBack;// ����

	private ListView mDishShakelistView;
	private File mShakeDishImageCache;
	private List<DataDishShake> data;

	private AdapterDishShake adapter;

	private PreferencesService preferencesService;
	private Map<String, String> params;
	private SQLiteDatabase db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shaked_dish);

		initView();
		setLister();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		/**
		 * listview����
		 */
		AnimationSet set = new AnimationSet(false);
		Animation animation = new AlphaAnimation(0, 1); // ���ƽ���͸���Ķ���Ч��
		animation.setDuration(250); // ����ʱ�������
		set.addAnimation(animation); // ���붯������
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 1);

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// �ж�����SD��
			mShakeDishImageCache = new File(
					Environment.getExternalStorageDirectory(),// SD��·��
					"xidianyaoyao_cache/dishImageCache");
			if (!mShakeDishImageCache.exists())
				mShakeDishImageCache.mkdirs();
		}

		mDishShakedBack = (ImageView) findViewById(R.id.dish_shaked_back);
		mDishShakelistView = (ListView) findViewById(R.id.dish_shaked_listView);
		mDishShakelistView.setLayoutAnimation(controller); // ListView ���ö���Ч��

		data = (List<DataDishShake>) getIntent().getSerializableExtra("data");
		adapter = new AdapterDishShake(this, data, mShakeDishImageCache);
		mDishShakelistView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void setLister() {
		mDishShakedBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			}
		});
		// �����б��¼�����
		mDishShakelistView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent();
				intent.putExtra("DishId", data.get(position).getDish_id());
				intent.putExtra("DishImage", data.get(position).getDish_image());
				intent.putExtra("DishName", data.get(position).getDish_name());
				intent.putExtra("DishScore", data.get(position).getDish_score());
				intent.putExtra("DishPrice", data.get(position).getDish_price());
				intent.putExtra("DishTaste", data.get(position).getDish_taste());
				intent.putExtra("DishNutrition", data.get(position)
						.getDish_nutrition());
				intent.putExtra("RestauId", data.get(position).getRestau_id());
				intent.putExtra("RestauName", data.get(position)
						.getRestau_name());
				intent.putExtra("RestauScore", data.get(position)
						.getRestau_score());
				intent.putExtra("RestauAddr", data.get(position)
						.getRestau_addr());
				intent.putExtra("RestauCall", data.get(position)
						.getRestau_call());
				intent.putExtra("RestauDescr", data.get(position)
						.getRestau_descr());
				intent.putExtra("RestauLat", data.get(position).getRestau_lat());
				intent.putExtra("RestauLon", data.get(position).getRestau_lon());
				intent.setClass(shaked_dish.this, RestauDish_info.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);

				preferencesService = new PreferencesService(shaked_dish.this);
				params = preferencesService.cusInfo_getPreferences();
				if (!params.get("cusName").equals("")) {
					HistorySQLiteHelper helper = new HistorySQLiteHelper(
							shaked_dish.this, params.get("cusName"));
					db = helper.getWritableDatabase();// ��ȡ�ɶ�д�����ݿ�
					helper.InsertData(db, params.get("cusName"),
							data.get(position).getDish_id(), data.get(position)
									.getDish_image(), data.get(position)
									.getDish_name(), data.get(position)
									.getDish_score(), data.get(position)
									.getDish_price(), data.get(position)
									.getDish_taste(), data.get(position)
									.getDish_nutrition(), data.get(position)
									.getRestau_id(), data.get(position)
									.getRestau_name(), data.get(position)
									.getRestau_score(), data.get(position)
									.getRestau_addr(), data.get(position)
									.getRestau_call(), data.get(position)
									.getRestau_descr(), data.get(position)
									.getRestau_lat(), data.get(position)
									.getRestau_lon());
				}
			}
		});
	}
}