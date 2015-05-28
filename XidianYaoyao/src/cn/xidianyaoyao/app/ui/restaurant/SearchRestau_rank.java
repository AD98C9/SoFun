package cn.xidianyaoyao.app.ui.restaurant;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.data.AdapterRankRestau;
import cn.xidianyaoyao.app.data.DataRankRestau;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.widget.LoadingProgressDialog;

/**
 * 
 * ����ǰ20�����ݵ�����
 * 
 * @author WangTanyun
 * 
 */
public class SearchRestau_rank extends Activity {

	private int setRestauLocation;// ÿ��ˢ�µ���ǰһ���ɼ�item
	private int topRestauOffset;// ÿ��ˢ�µ���ǰһ���ɼ�item���������ƫ����

	private ImageView mSearchRestauBack;
	private ImageView mSearchRestauRefresh;
	private View mSeeMoreView; // ���ظ������ݵ�Footer
	private View mLoadingView; // ���ظ������ݵ�Footer
	private LinearLayout mFooterSeeMore;// Footer��Ĳ���

	private LoadingProgressDialog mProgress = null;
	private ListView mRestauRanklistView;
	private AdapterRankRestau mRestauAdapter;

	private int mRestauRequestTimes = 0;// �������
	private int mRestauLimit = 5;// ÿҳ��ʾ��item������

	private boolean mToBottom = false;
	private int mRankRestauNumber = 0;// ���������
	private List<DataRankRestau> dataRestau;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_restau_rank);

		initView();
		setListener();

		new AsyncRestauRankTask().execute(String.valueOf(mRestauRequestTimes),
				String.valueOf(mRestauLimit));

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mSearchRestauBack = (ImageView) findViewById(R.id.search_restau_rank_back);
		mSearchRestauRefresh = (ImageView) findViewById(R.id.search_restau_rank_refresh);
		mRestauRanklistView = (ListView) findViewById(R.id.rankRestau_listView);

		mSeeMoreView = getLayoutInflater().inflate(
				R.layout.widget_footer_seemore, null);
		mLoadingView = getLayoutInflater().inflate(
				R.layout.widget_footer_loading, null);
		mFooterSeeMore = (LinearLayout) mSeeMoreView
				.findViewById(R.id.footer_seeMore);

		dataRestau = new ArrayList<DataRankRestau>();
		mRestauRanklistView.setOnScrollListener(new ScrollListener());
	}

	private void setListener() {
		mSearchRestauBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
		});

		mSearchRestauRefresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dataRestau.clear();
				mRestauRanklistView.removeFooterView(mSeeMoreView);
				mRestauRequestTimes = 0;
				mRankRestauNumber = 0;
				new AsyncRestauRankTask().execute(
						String.valueOf(mRestauRequestTimes),
						String.valueOf(mRestauLimit));
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
				intent.setClass(SearchRestau_rank.this, Restau_info.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		mFooterSeeMore
				.setOnClickListener(new android.view.View.OnClickListener() {
					public void onClick(View v) {
						mRestauRanklistView.removeFooterView(mSeeMoreView);
						mRestauRequestTimes++;
						new AsyncRestauRankTask2().execute(
								String.valueOf(mRestauRequestTimes),
								String.valueOf(mRestauLimit));
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
				setRestauLocation = mRestauRanklistView
						.getFirstVisiblePosition();
				topRestauOffset = mRestauRanklistView.getChildAt(0).getTop();
			}
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
				mRestauAdapter = new AdapterRankRestau(SearchRestau_rank.this,
						dataRestau);
				mRestauRanklistView.addFooterView(mSeeMoreView);
				mRestauRanklistView.setAdapter(mRestauAdapter);
				mRestauAdapter.notifyDataSetChanged();
				mRestauRanklistView.setSelectionFromTop(setRestauLocation,
						topRestauOffset);
			} else if (result.equals("1111")) {
				Toast.makeText(SearchRestau_rank.this, "���ݻ�ȡ��ϣ�",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(SearchRestau_rank.this, "������������ԣ�",
						Toast.LENGTH_SHORT).show();
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
				// AdapterRankRestau(SearchRestau_rank.this,dataRestau);
				mRestauRanklistView.addFooterView(mSeeMoreView);
				// mRestauRanklistView.setAdapter(mRestauAdapter);
				mRestauAdapter.notifyDataSetChanged();
				mRestauRanklistView.setSelectionFromTop(setRestauLocation,
						topRestauOffset);
			} else if (result.equals("1111")) {
				Toast.makeText(SearchRestau_rank.this, "���ݻ�ȡ��ϣ�",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(SearchRestau_rank.this, "������������ԣ�",
						Toast.LENGTH_SHORT).show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			mRestauRanklistView.addFooterView(mLoadingView);
		}
	}

	private void startProgressDialog() {
		if (mProgress == null) {
			mProgress = LoadingProgressDialog
					.createDialog(SearchRestau_rank.this);
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
