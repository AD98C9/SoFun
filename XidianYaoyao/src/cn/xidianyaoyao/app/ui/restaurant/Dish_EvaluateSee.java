package cn.xidianyaoyao.app.ui.restaurant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.data.AdapterDishEvaulate;
import cn.xidianyaoyao.app.data.DataDishEvaluate;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.person.Person_evaluate;
import cn.xidianyaoyao.app.ui.person.Person_friendSee;
import cn.xidianyaoyao.app.ui.person.Person_login;
import cn.xidianyaoyao.app.ui.widget.LoadingProgressDialog;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;

public class Dish_EvaluateSee extends Activity {

	private int setLocation;// ÿ��ˢ�µ���ǰһ���ɼ�item
	private int topOffset;// ÿ��ˢ�µ���ǰһ���ɼ�item���������ƫ����

	private View mSeeMoreView; // ���ظ������ݵ�Footer
	private View mLoadingView; // ���ظ������ݵ�Footer
	private LinearLayout mFooterSeeMore;// Footer��Ĳ���

	private LoadingProgressDialog mProgress = null;
	private ImageView mEvaluateAdd;
	private ImageView mEvaluateBack;
	private TextView mEvaluateTitle;
	private ListView mEvaluatelistView;
	private AdapterDishEvaulate adapter;
	private LinearLayout mEvaluateEmpty;

	private int mRequestTimes = 0;// �������
	private int mLimit = 6;// ÿҳ��ʾ����
	private boolean mToBottom = false;// �����Ƿ��˵ײ�

	private List<DataDishEvaluate> data;
	private PreferencesService preferencesService;
	private Map<String, String> params;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dish_evaluate_see);

		initView();
		setLister();

		new AsyncDishEvaluateTask().execute(getIntent()
				.getStringExtra("DishId"),
				getIntent().getStringExtra("RestauId"), String
						.valueOf(mRequestTimes), String.valueOf(mLimit));

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mEvaluateBack = (ImageView) findViewById(R.id.dishEvaluate_back);
		mEvaluateAdd = (ImageView) findViewById(R.id.dishEvaluate_add);
		mEvaluateTitle = (TextView) findViewById(R.id.dishEvaluate_title);
		mEvaluateTitle.setText(getIntent().getStringExtra("DishName") + "-"
				+ getIntent().getStringExtra("RestauName"));

		mEvaluateEmpty = (LinearLayout) findViewById(R.id.dish_empty_layout);
		mEvaluatelistView = (ListView) findViewById(R.id.dish_evau_listview);

		mSeeMoreView = getLayoutInflater().inflate(
				R.layout.widget_footer_seemore, null);
		mLoadingView = getLayoutInflater().inflate(
				R.layout.widget_footer_loading, null);
		mFooterSeeMore = (LinearLayout) mSeeMoreView
				.findViewById(R.id.footer_seeMore);

		data = new ArrayList<DataDishEvaluate>();
		mEvaluatelistView.setOnScrollListener(new ScrollListener());
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 1:// ��¼�ɹ�
			Intent intent = new Intent();
			intent.putExtra("DishId", getIntent().getStringExtra("DishId"));
			intent.putExtra("RestauId", getIntent().getStringExtra("RestauId"));
			intent.setClass(Dish_EvaluateSee.this, Person_evaluate.class);
			startActivity(intent);
			overridePendingTransition(R.anim.roll_up, R.anim.roll);
			break;
		default:
			break;
		}
	}

	public void setLister() {
		mEvaluateBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});
		mEvaluateAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				preferencesService = new PreferencesService(
						Dish_EvaluateSee.this);
				params = preferencesService.cusInfo_getPreferences();
				if (params.get("cusName").equals("")) {
					Toast.makeText(Dish_EvaluateSee.this, "�ף��㻹û��¼��!",
							Toast.LENGTH_SHORT).show();
					startActivityForResult(new Intent(Dish_EvaluateSee.this,
							Person_login.class), 1);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				} else if (!params.get("cusName").equals("")) {
					Intent intent = new Intent();
					intent.putExtra("DishId",
							getIntent().getStringExtra("DishId"));
					intent.putExtra("RestauId",
							getIntent().getStringExtra("RestauId"));
					intent.putExtra("RestauName",
							getIntent().getStringExtra("RestauName"));
					intent.putExtra("RestauScore",
							getIntent().getStringExtra("RestauScore"));
					intent.putExtra("RestauAddr",
							getIntent().getStringExtra("RestauAddr"));
					intent.putExtra("RestauCall",
							getIntent().getStringExtra("RestauCall"));
					intent.putExtra("RestauDescr",
							getIntent().getStringExtra("RestauDescr"));
					intent.putExtra("RestauLat",
							getIntent().getStringExtra("RestauLat"));
					intent.putExtra("RestauLon",
							getIntent().getStringExtra("RestauLon"));
					intent.setClass(Dish_EvaluateSee.this,
							Person_evaluate.class);
					startActivity(intent);
					overridePendingTransition(R.anim.roll_up, R.anim.roll);
				}
			}
		});

		// �����б��¼�����
		mEvaluatelistView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("FriendImage", data.get(position).getHead());
				intent.putExtra("FriendName", data.get(position).getName());
				intent.putExtra("FriendGender", data.get(position).getGender());
				intent.setClass(Dish_EvaluateSee.this, Person_friendSee.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		mFooterSeeMore
				.setOnClickListener(new android.view.View.OnClickListener() {
					public void onClick(View v) {
						mEvaluatelistView.removeFooterView(mSeeMoreView);
						mRequestTimes++;
						new AsyncDishEvaluateTask2().execute(getIntent()
								.getStringExtra("DishId"), getIntent()
								.getStringExtra("RestauId"), String
								.valueOf(mRequestTimes), String.valueOf(mLimit));
					}
				});
	}

	private final class ScrollListener implements OnScrollListener {

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
				setLocation = mEvaluatelistView.getFirstVisiblePosition();
				topOffset = mEvaluatelistView.getChildAt(0).getTop();
			}
		}
	}

	/*************************************************/
	public class AsyncDishEvaluateTask extends
			AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.evaluateShow(params[0], params[1], params[2],
								params[3]);

				JSONObject resultCode = new JSONObject(result);
				int totalItems = Integer.parseInt(resultCode
						.getString("totalProperty"));// ������
				if (totalItems == 0) {
					code = "0000";// ������������
				} else {
					String commentInfo = resultCode.getString("comments");
					if (!commentInfo.equals("null")) {// ������
						code = "1111";
						JSONArray resultComments = new JSONArray(commentInfo);
						for (int i = 0; i < resultComments.length(); i++) {
							JSONObject object = resultComments.getJSONObject(i);
							DataDishEvaluate dr = new DataDishEvaluate(
									object.getString("commenttime"),
									object.getString("headimage"),
									object.getString("cus_id"),
									object.getString("gender"),
									object.getString("score"),
									object.getString("descr"));
							data.add(dr);
						}
					} else {
						code = "2222";// ���ݻ�ȡ���
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return code;
		}

		protected void onPostExecute(String result) {
			stopProgressDialog();
			if (result.equals("1111")) {
				adapter = new AdapterDishEvaulate(Dish_EvaluateSee.this, data);
				mEvaluatelistView.addFooterView(mSeeMoreView);
				mEvaluatelistView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				mEvaluatelistView.setSelectionFromTop(setLocation, topOffset);
			} else if (result.equals("0000")) {
				mEvaluatelistView.setVisibility(View.GONE);
				mEvaluateEmpty.setVisibility(View.VISIBLE);
			} else if (result.equals("2222")) {
				Toast.makeText(Dish_EvaluateSee.this, "���ݻ�ȡ��ϣ�",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Dish_EvaluateSee.this, "������������ԣ�",
						Toast.LENGTH_SHORT).show();
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			startProgressDialog();
		}
	}

	private void startProgressDialog() {
		if (mProgress == null) {
			mProgress = LoadingProgressDialog.createDialog(this);
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

	public class AsyncDishEvaluateTask2 extends
			AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.evaluateShow(params[0], params[1], params[2],
								params[3]);

				JSONObject resultCode = new JSONObject(result);
				int totalItems = Integer.parseInt(resultCode
						.getString("totalProperty"));// ������
				if (totalItems == 0) {
					code = "0000";// ������������
				} else {
					String commentInfo = resultCode.getString("comments");
					if (!commentInfo.equals("null")) {// ������
						code = "1111";
						JSONArray resultComments = new JSONArray(commentInfo);
						for (int i = 0; i < resultComments.length(); i++) {
							JSONObject object = resultComments.getJSONObject(i);
							DataDishEvaluate dr = new DataDishEvaluate(
									object.getString("commenttime"),
									object.getString("headimage"),
									object.getString("cus_id"),
									object.getString("gender"),
									object.getString("score"),
									object.getString("descr"));
							data.add(dr);
						}
					} else {
						code = "2222";// ���ݻ�ȡ���
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return code;
		}

		protected void onPostExecute(String result) {
			mEvaluatelistView.removeFooterView(mLoadingView);
			if (result.equals("1111")) {
				// adapter = new AdapterDishEvaulate(Dish_EvaluateSee.this,
				// data);
				mEvaluatelistView.addFooterView(mSeeMoreView);
				// mEvaluatelistView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				mEvaluatelistView.setSelectionFromTop(setLocation, topOffset);
			} else if (result.equals("0000")) {
				mEvaluatelistView.setVisibility(View.GONE);
				mEvaluateEmpty.setVisibility(View.VISIBLE);
			} else if (result.equals("2222")) {
				Toast.makeText(Dish_EvaluateSee.this, "���ݻ�ȡ��ϣ�",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Dish_EvaluateSee.this, "������������ԣ�",
						Toast.LENGTH_SHORT).show();
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			mEvaluatelistView.addFooterView(mLoadingView);
		}
	}
}
