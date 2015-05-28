package cn.xidianyaoyao.app.ui.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.data.AdapterRankDish;
import cn.xidianyaoyao.app.data.AdapterRankRestau;
import cn.xidianyaoyao.app.data.DataRankDish;
import cn.xidianyaoyao.app.data.DataRankRestau;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.sqlite.HistorySQLiteHelper;
import cn.xidianyaoyao.app.ui.restaurant.RestauDish_info;
import cn.xidianyaoyao.app.ui.restaurant.Restau_info;
import cn.xidianyaoyao.app.ui.widget.LoadingProgressDialog;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;

/**
 * 
 * ����ǰ50�����ˣ�ǰ20�����ݵ�����
 * 
 * @author WangTanyun
 * 
 */
public class Fragment_MainRank extends Fragment {

	private int setDishLocation;// ÿ��ˢ�µ���ǰһ���ɼ�item
	private int topDishOffset;// ÿ��ˢ�µ���ǰһ���ɼ�item���������ƫ����
	private int setRestauLocation;// ÿ��ˢ�µ���ǰһ���ɼ�item
	private int topRestauOffset;// ÿ��ˢ�µ���ǰһ���ɼ�item���������ƫ����

	private View mSeeMoreView; // ���ظ������ݵ�Footer
	private View mLoadingView; // ���ظ������ݵ�Footer
	private LinearLayout mFooterSeeMore;// Footer��Ĳ���

	private File mRankDishImageCache;

	private RadioGroup mRankGroup;
	private RadioButton mRankDish;
	private RadioButton mRankRestau;
	private LinearLayout mRankDishLay;
	private LinearLayout mRankRestauLay;

	private LoadingProgressDialog mProgress = null;
	private View mRankView;

	private ListView mDishRanklistView;
	private ListView mRestauRanklistView;
	private AdapterRankDish mDishAdapter;
	private AdapterRankRestau mRestauAdapter;

	private int mDishRequestTimes = 0;// �������
	private int mDishLimit = 10;// ÿҳ��ʾ��item������
	private int mRestauRequestTimes = 0;// �������
	private int mRestauLimit = 5;// ÿҳ��ʾ��item������

	private boolean mToBottom = false;
	private int mRankDishNumber = 0;// ���������
	private int mRankRestauNumber = 0;// ���������

	private List<DataRankDish> dataDish;
	private List<DataRankRestau> dataRestau;

	private PreferencesService preferencesService;
	private Map<String, String> params;
	private SQLiteDatabase db;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// savedInstanceStateΪ�����״̬��
		if (container == null)// ���������ֵΪNull,��ζ�Ÿ���Ƭ���ɼ�
			return null;
		mRankView = (View) inflater.inflate(R.layout.fragment_main_rank,
				container, false);

		initView();
		setListener();

		new AsyncDishRankTask().execute(String.valueOf(mDishRequestTimes),
				String.valueOf(mDishLimit));

		return mRankView;
	}

	private void initView() {
		mRankGroup = (RadioGroup) mRankView.findViewById(R.id.rank_group);
		mRankDish = (RadioButton) mRankView.findViewById(R.id.rank_dish);
		mRankRestau = (RadioButton) mRankView
				.findViewById(R.id.rank_restaurant);
		mRankDishLay = (LinearLayout) mRankView.findViewById(R.id.rankDish);
		mRankRestauLay = (LinearLayout) mRankView.findViewById(R.id.rankRestau);

		mDishRanklistView = (ListView) mRankView
				.findViewById(R.id.rankDish_listView);
		mRestauRanklistView = (ListView) mRankView
				.findViewById(R.id.rankRestau_listView);

		mSeeMoreView = getActivity().getLayoutInflater().inflate(
				R.layout.widget_footer_seemore, null);
		mLoadingView = getActivity().getLayoutInflater().inflate(
				R.layout.widget_footer_loading, null);
		mFooterSeeMore = (LinearLayout) mSeeMoreView
				.findViewById(R.id.footer_seeMore);

		dataDish = new ArrayList<DataRankDish>();
		dataRestau = new ArrayList<DataRankRestau>();

		mDishRanklistView.setOnScrollListener(new ScrollListener1());
		mRestauRanklistView.setOnScrollListener(new ScrollListener2());

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// �ж�����SD��
			mRankDishImageCache = new File(
					Environment.getExternalStorageDirectory(),// SD��·��
					"xidianyaoyao_cache/dishImageCache");
			if (!mRankDishImageCache.exists())
				mRankDishImageCache.mkdirs();
		}
	}

	private void setListener() {

		mDishRanklistView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				preferencesService = new PreferencesService(getActivity());
				params = preferencesService.cusInfo_getPreferences();

				Intent intent = new Intent();
				intent.putExtra("DishId", dataDish.get(position).getDish_id());
				intent.putExtra("DishImage", dataDish.get(position)
						.getDish_image());
				intent.putExtra("DishName", dataDish.get(position)
						.getDish_name());
				intent.putExtra("DishScore", dataDish.get(position)
						.getDish_score());
				intent.putExtra("DishPrice", dataDish.get(position)
						.getDish_price());
				intent.putExtra("DishTaste", dataDish.get(position)
						.getDish_taste());
				intent.putExtra("DishNutrition", dataDish.get(position)
						.getDish_nutrition());
				intent.putExtra("RestauId", dataDish.get(position)
						.getRestau_id());
				intent.putExtra("RestauName", dataDish.get(position)
						.getRestau_name());
				intent.putExtra("RestauScore", dataDish.get(position)
						.getRestau_score());
				intent.putExtra("RestauAddr", dataDish.get(position)
						.getRestau_addr());
				intent.putExtra("RestauCall", dataDish.get(position)
						.getRestau_call());
				intent.putExtra("RestauDescr", dataDish.get(position)
						.getRestau_descr());
				intent.putExtra("RestauLat", dataDish.get(position)
						.getRestau_lat());
				intent.putExtra("RestauLon", dataDish.get(position)
						.getRestau_lon());
				intent.setClass(getActivity(), RestauDish_info.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);

				preferencesService = new PreferencesService(getActivity());
				params = preferencesService.cusInfo_getPreferences();
				if (!params.get("cusName").equals("")) {
					HistorySQLiteHelper helper = new HistorySQLiteHelper(getActivity(),
							params.get("cusName"));
					db = helper.getWritableDatabase();// ��ȡ�ɶ�д�����ݿ�
					helper.InsertData(db, params.get("cusName"),
							dataDish.get(position).getDish_id(),
							dataDish.get(position).getDish_image(), dataDish
									.get(position).getDish_name(), dataDish
									.get(position).getDish_score(), dataDish
									.get(position).getDish_price(), dataDish
									.get(position).getDish_taste(), dataDish
									.get(position).getDish_nutrition(),
							dataDish.get(position).getRestau_id(), dataDish
									.get(position).getRestau_name(), dataDish
									.get(position).getRestau_score(), dataDish
									.get(position).getRestau_addr(), dataDish
									.get(position).getRestau_call(), dataDish
									.get(position).getRestau_descr(), dataDish
									.get(position).getRestau_lat(), dataDish
									.get(position).getRestau_lon());
				}
			}
		});

		mRestauRanklistView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("RestauId", dataRestau.get(position)
						.getRestau_id());
				intent.putExtra("RestauName", dataRestau.get(position)
						.getRestau_name());
				intent.putExtra("RestauScore", dataRestau.get(position)
						.getRestau_score());
				intent.putExtra("RestauAddr", dataRestau.get(position)
						.getRestau_addr());
				intent.putExtra("RestauCall", dataRestau.get(position)
						.getRestau_call());
				intent.putExtra("RestauDescr", dataRestau.get(position)
						.getRestau_descr());
				intent.putExtra("RestauLat", dataRestau.get(position)
						.getRestau_lat());
				intent.putExtra("RestauLon", dataRestau.get(position)
						.getRestau_lon());
				intent.setClass(getActivity(), Restau_info.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		mFooterSeeMore
				.setOnClickListener(new android.view.View.OnClickListener() {
					public void onClick(View v) {
						if (mRankDish.isChecked()) {
							mDishRanklistView.removeFooterView(mSeeMoreView);
							mDishRequestTimes++;
							new AsyncDishRankTask2().execute(
									String.valueOf(mDishRequestTimes),
									String.valueOf(mDishLimit));
						} else if (mRankRestau.isChecked()) {
							mRestauRanklistView.removeFooterView(mSeeMoreView);
							mRestauRequestTimes++;
							new AsyncRestauRankTask2().execute(
									String.valueOf(mRestauRequestTimes),
									String.valueOf(mRestauLimit));
						}

					}
				});

		mRankGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rank_dish:
					dataRestau.clear();
					mRestauRanklistView.removeFooterView(mSeeMoreView);// �������У���Ȼ�пո����
					mDishRequestTimes = 0;
					mRankDishNumber = 0;
					mRankRestauLay.setVisibility(View.GONE);
					mRankDishLay.setVisibility(View.VISIBLE);
					new AsyncDishRankTask().execute(
							String.valueOf(mDishRequestTimes),
							String.valueOf(mDishLimit));
					break;
				case R.id.rank_restaurant:
					dataDish.clear();
					mDishRanklistView.removeFooterView(mSeeMoreView);
					mRestauRequestTimes = 0;
					mRankRestauNumber = 0;
					mRankDishLay.setVisibility(View.GONE);
					mRankRestauLay.setVisibility(View.VISIBLE);
					new AsyncRestauRankTask().execute(
							String.valueOf(mRestauRequestTimes),
							String.valueOf(mRestauLimit));
					break;
				}
			}
		});
	}

	private final class ScrollListener1 implements OnScrollListener {

		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			int lastVisibleItemIndex = firstVisibleItem + visibleItemCount - 1;// ��ȡ���һ���ɼ�Item������(0-based)
			if (totalItemCount - 1 == lastVisibleItemIndex) {
				mToBottom = true;
			} else {
				mToBottom = false;
			}
		}

		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == SCROLL_STATE_IDLE && mToBottom) {// SCROLL_STATE_IDLE������ֹ
				setDishLocation = mDishRanklistView.getFirstVisiblePosition();
				topDishOffset = mDishRanklistView.getChildAt(0).getTop();
			}
		}
	}

	private final class ScrollListener2 implements OnScrollListener {

		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			int lastVisibleItemIndex = firstVisibleItem + visibleItemCount - 1;// ��ȡ���һ���ɼ�Item������(0-based)
			if (totalItemCount - 1 == lastVisibleItemIndex) {
				mToBottom = true;
			} else {
				mToBottom = false;
			}
		}

		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == SCROLL_STATE_IDLE && mToBottom) {// SCROLL_STATE_IDLE������ֹ
				setRestauLocation = mRestauRanklistView
						.getFirstVisiblePosition();
				topRestauOffset = mRestauRanklistView.getChildAt(0).getTop();
			}
		}
	}

	/*************************************************/
	public class AsyncDishRankTask extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.dishRank(
						params[0], params[1]);
				JSONObject dish_object = new JSONObject(result);
				String dishString = dish_object.getString("list");
				if (!dishString.equals("null")) {
					JSONArray resultCode = new JSONArray(dishString);
					for (int i = 0; i < resultCode.length(); i++) {
						JSONObject object = resultCode.getJSONObject(i);
						DataRankDish dr = new DataRankDish(
								String.valueOf(++mRankDishNumber), // �����ţ���1��ʼ
								object.getString("dis_id"),
								object.getString("imageUrl"),
								object.getString("name"),
								object.getString("dishscore"),
								object.getString("price"),
								object.getString("taste"),
								object.getString("nutrition"),
								object.getString("res_id"),
								object.getString("resname"),
								object.getString("restscore"),
								object.getString("addr"),
								object.getString("telephone"),
								object.getString("restdescr"),
								object.getString("lat"),
								object.getString("lng"));
						dataDish.add(dr);
						code = "0000";// ������
					}
				} else {
					code = "1111";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return code;
		}

		protected void onPostExecute(String result) {
			stopProgressDialog();
			if (result.equals("0000")) {
				mDishAdapter = new AdapterRankDish(getActivity(), dataDish,
						mRankDishImageCache);
				mDishRanklistView.addFooterView(mSeeMoreView);
				mDishRanklistView.setAdapter(mDishAdapter);
				mDishAdapter.notifyDataSetChanged();
				mDishRanklistView.setSelectionFromTop(setDishLocation,
						topDishOffset);
			} else if (result.equals("1111")) {
				Toast.makeText(getActivity(), "��ȡ������ϣ�", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "������������ԣ�", Toast.LENGTH_SHORT)
						.show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			startProgressDialog();
		}
	}

	public class AsyncDishRankTask2 extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.dishRank(
						params[0], params[1]);
				JSONObject dish_object = new JSONObject(result);
				String dishString = dish_object.getString("list");
				if (!dishString.equals("null")) {
					JSONArray resultCode = new JSONArray(dishString);
					for (int i = 0; i < resultCode.length(); i++) {
						JSONObject object = resultCode.getJSONObject(i);
						DataRankDish dr = new DataRankDish(
								String.valueOf(++mRankDishNumber), // �����ţ���1��ʼ
								object.getString("dis_id"),
								object.getString("imageUrl"),
								object.getString("name"),
								object.getString("dishscore"),
								object.getString("price"),
								object.getString("taste"),
								object.getString("nutrition"),
								object.getString("res_id"),
								object.getString("resname"),
								object.getString("restscore"),
								object.getString("addr"),
								object.getString("telephone"),
								object.getString("restdescr"),
								object.getString("lat"),
								object.getString("lng"));
						dataDish.add(dr);
						code = "0000";// ������
					}
				} else {
					code = "1111";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return code;
		}

		protected void onPostExecute(String result) {
			mDishRanklistView.removeFooterView(mLoadingView);
			if (result.equals("0000")) {
				// mDishAdapter = new AdapterRankDish(getActivity(),
				// dataDish,mRankDishImageCache);
				mDishRanklistView.addFooterView(mSeeMoreView);
				// mDishRanklistView.setAdapter(mDishAdapter);
				mDishAdapter.notifyDataSetChanged();
				mDishRanklistView.setSelectionFromTop(setDishLocation,
						topDishOffset);
			} else if (result.equals("1111")) {
				Toast.makeText(getActivity(), "��ȡ������ϣ�", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "������������ԣ�", Toast.LENGTH_SHORT)
						.show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			mDishRanklistView.addFooterView(mLoadingView);
		}
	}

	/*************************************************/
	public class AsyncRestauRankTask extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.restauRank(
						params[0], params[1]);
				JSONObject restau_object = new JSONObject(result);
				String restauString = restau_object.getString("list");
				if (!restauString.equals("null")) {
					JSONArray resultCode = new JSONArray(restauString);
					for (int i = 0; i < resultCode.length(); i++) {
						JSONObject object = resultCode.getJSONObject(i);
						DataRankRestau dr = new DataRankRestau(
								String.valueOf(++mRankRestauNumber), // �����ţ���1��ʼ
								object.getString("res_id"),
								object.getString("resname"),
								object.getString("restscore"),
								object.getString("addr"),
								object.getString("telephone"),
								object.getString("restdescr"),
								object.getString("lat"),
								object.getString("lng"));
						dataRestau.add(dr);
						code = "0000";// ������
					}
				} else {
					code = "1111";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return code;
		}

		protected void onPostExecute(String result) {
			stopProgressDialog();
			if (result.equals("0000")) {
				mRestauAdapter = new AdapterRankRestau(getActivity(),
						dataRestau);
				mRestauRanklistView.addFooterView(mSeeMoreView);
				mRestauRanklistView.setAdapter(mRestauAdapter);
				mRestauAdapter.notifyDataSetChanged();
				mRestauRanklistView.setSelectionFromTop(setRestauLocation,
						topRestauOffset);
			} else if (result.equals("1111")) {
				Toast.makeText(getActivity(), "���ݻ�ȡ��ϣ�", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "������������ԣ�", Toast.LENGTH_SHORT)
						.show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			startProgressDialog();
		}
	}

	public class AsyncRestauRankTask2 extends
			AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.restauRank(
						params[0], params[1]);
				JSONObject restau_object = new JSONObject(result);
				String restauString = restau_object.getString("list");
				if (!restauString.equals("null")) {
					JSONArray resultCode = new JSONArray(restauString);
					for (int i = 0; i < resultCode.length(); i++) {
						JSONObject object = resultCode.getJSONObject(i);
						DataRankRestau dr = new DataRankRestau(
								String.valueOf(++mRankRestauNumber), // �����ţ���1��ʼ
								object.getString("res_id"),
								object.getString("resname"),
								object.getString("restscore"),
								object.getString("addr"),
								object.getString("telephone"),
								object.getString("restdescr"),
								object.getString("lat"),
								object.getString("lng"));
						dataRestau.add(dr);
						code = "0000";// ������
					}
				} else {
					code = "1111";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return code;
		}

		protected void onPostExecute(String result) {
			mRestauRanklistView.removeFooterView(mLoadingView);
			if (result.equals("0000")) {
				// mRestauAdapter = new
				// AdapterRankRestau(getActivity(),dataRestau);
				mRestauRanklistView.addFooterView(mSeeMoreView);
				// mRestauRanklistView.setAdapter(mRestauAdapter);
				mRestauAdapter.notifyDataSetChanged();
				mRestauRanklistView.setSelectionFromTop(setRestauLocation,
						topRestauOffset);
			} else if (result.equals("1111")) {
				Toast.makeText(getActivity(), "���ݻ�ȡ��ϣ�", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "������������ԣ�", Toast.LENGTH_SHORT)
						.show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			mRestauRanklistView.addFooterView(mLoadingView);
		}
	}

	/************************************************/

	private void startProgressDialog() {
		if (mProgress == null) {
			mProgress = LoadingProgressDialog.createDialog(getActivity());
			mProgress.setMessage("Ŭ��������...");
		}
		mProgress.show();
	}

	private void stopProgressDialog() {
		if (mProgress != null) {
			mProgress.dismiss();
			mProgress = null;
		}
	}
}
