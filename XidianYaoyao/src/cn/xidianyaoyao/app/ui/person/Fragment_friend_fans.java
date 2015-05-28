package cn.xidianyaoyao.app.ui.person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.data.AdapterFriend;
import cn.xidianyaoyao.app.data.DataFriend;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.widget.LoadingProgressDialog;

public class Fragment_friend_fans extends Fragment {

	private View mFansView;
	private Bundle bundle;

	private LoadingProgressDialog mProgressLoad = null;

	private ListView mFanslistView;
	private LinearLayout mFansEmpty;
	private AdapterFriend adapter;
	private List<DataFriend> data;
	private boolean mToBottom = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// savedInstanceStateΪ�����״̬��
		if (container == null)// ���������ֵΪNull,��ζ�Ÿ���Ƭ���ɼ�
			return null;

		mFansView = (View) inflater.inflate(R.layout.fragment_person_fans,
				container, false);

		initView();
		setLister();

		bundle = this.getArguments();
		new AsyncFansTask().execute(bundle.getString("FriendName"));

		return mFansView;
	}

	private void initView() {
		mFanslistView = (ListView) mFansView.findViewById(R.id.fans_listView);
		mFansEmpty = (LinearLayout) mFansView.findViewById(R.id.fans_empty);

		data = new ArrayList<DataFriend>();
		mFanslistView.setOnScrollListener(new ScrollListener());
	}

	private void setLister() {
		// �����б��¼�����
		mFanslistView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("FriendName", data.get(position)
						.getFriend_name());
				intent.putExtra("FriendImage", data.get(position)
						.getFriend_image());
				intent.putExtra("FriendGender", data.get(position)
						.getFriend_gender());
				intent.setClass(getActivity(), Person_friendSee.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
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
			if (scrollState == SCROLL_STATE_IDLE && mToBottom) {
			}
		}
	}

	public class AsyncFansTask extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.fansShow(params[0]);

				JSONObject resultCode = new JSONObject(result);
				String followsInfo = resultCode.getString("list");
				if (!followsInfo.equals("null")) {// ������
					code = "1111";
					JSONArray follows = new JSONArray(followsInfo);
					for (int i = 0; i < follows.length(); i++) {
						JSONObject object = follows.getJSONObject(i);
						DataFriend dr = new DataFriend(
								object.getString("headimage"),
								object.getString("cus_id"),
								object.getString("gender"));
						data.add(dr);
					}
				} else {
					code = "0000";// û�й�ע
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
			stopProgressLoad();
			if (result.equals("1111")) {
				adapter = new AdapterFriend(getActivity(), data);
				mFanslistView.setAdapter(adapter);
			} else if (result.equals("0000")) {
				mFanslistView.setVisibility(View.GONE);
				mFansEmpty.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(getActivity(), "������������ԣ�", Toast.LENGTH_SHORT)
						.show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			startProgressLoad();
		}
	}

	private void startProgressLoad() {
		if (mProgressLoad == null) {
			mProgressLoad = LoadingProgressDialog.createDialog(getActivity());
			mProgressLoad.setMessage("Ŭ��������...");
		}
		mProgressLoad.show();
	}

	private void stopProgressLoad() {
		if (mProgressLoad != null) {
			mProgressLoad.dismiss();
			mProgressLoad = null;
		}
	}
}
