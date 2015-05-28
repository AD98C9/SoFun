package cn.xidianyaoyao.app.ui.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.data.DataAllDB;
import cn.xidianyaoyao.app.data.DataSearch;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.restaurant.SearchDish;
import cn.xidianyaoyao.app.ui.restaurant.SearchDish_rank;
import cn.xidianyaoyao.app.ui.restaurant.SearchRestau;
import cn.xidianyaoyao.app.ui.restaurant.SearchRestau_rank;

public class Fragment_MainSearch extends Fragment {

	private View mSearchView;

	private String[] mDBallName;

	private AutoCompleteTextView mSearchEdit;
	private ImageView mSearchBtn;
	private ImageView mDeleteText;
	private RelativeLayout mSearchDish;
	private RelativeLayout mSearchRestau;

	private ProgressDialog mProgress = null;
	private AsyncSearchTask mTask;
	private String mSearchText;
	private List<DataSearch> dataDish;
	private List<DataSearch> dataRestau;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// savedInstanceStateΪ�����״̬��
		if (container == null)// ���������ֵΪNull,��ζ�Ÿ���Ƭ���ɼ�
			return null;
		mSearchView = (View) inflater.inflate(R.layout.fragment_main_search,
				container, false);

		initView();
		setListener();

		return mSearchView;
	}

	private void initView() {
		mSearchDish = (RelativeLayout) mSearchView
				.findViewById(R.id.main_search_dish);
		mSearchRestau = (RelativeLayout) mSearchView
				.findViewById(R.id.main_search_restau);
		mSearchBtn = (ImageView) mSearchView.findViewById(R.id.search_submit);
		mDeleteText = (ImageView) mSearchView
				.findViewById(R.id.search_deleteText);
		mDBallName = DataAllDB.getData();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.widget_search_down, mDBallName);
		mSearchEdit = (AutoCompleteTextView) mSearchView
				.findViewById(R.id.search_edit);
		mSearchEdit.setAdapter(adapter);
	}

	private void setListener() {
		mDeleteText.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mSearchEdit.setText("");
			}
		});
		mSearchEdit.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					mDeleteText.setVisibility(View.GONE);
				} else {
					mDeleteText.setVisibility(View.VISIBLE);
				}
			}
		});
		mSearchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (search_inputIsSuccess()) {
					mSearchText = mSearchEdit.getText().toString().trim();
					if (mTask != null
							&& mTask.getStatus() == AsyncSearchTask.Status.RUNNING)
						mTask.cancel(true);
					mTask = new AsyncSearchTask();
					mTask.execute(mSearchText);
				}
			}
		});

		mSearchDish.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SearchDish_rank.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		mSearchRestau.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SearchRestau_rank.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}

	private boolean search_inputIsSuccess() {
		String searchStr = mSearchEdit.getText().toString().trim();
		if ("".equals(searchStr)) {
			Toast.makeText(getActivity(), "�ף��㻹û������!", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	public class AsyncSearchTask extends AsyncTask<String, Integer, String> {
		protected String doInBackground(String... params) {// ��̨�߳̽�Ҫ��ɵ�����
			String code = "";
			dataDish = new ArrayList<DataSearch>();
			dataRestau = new ArrayList<DataSearch>();
			int mRankDishNumber = 0;
			int mRankRestauNumber = 0;
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.search(params[0]);
				JSONObject resultString = new JSONObject(result);
				String resultDish = resultString.getString("dishes");
				String resultRestau = resultString.getString("rests");
				if (!resultRestau.equals("null")) {
					code = "0000";// �ѷ���
					JSONArray resultCode = new JSONArray(resultRestau);
					for (int i = 0; i < resultCode.length(); i++) {
						JSONObject object = resultCode.getJSONObject(i);
						DataSearch dr = new DataSearch(
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
					}
				} else if (!resultDish.equals("null")) {
					code = "1111";// �ѷ���
					JSONArray resultCode = new JSONArray(resultDish);
					for (int i = 0; i < resultCode.length(); i++) {
						JSONObject object = resultCode.getJSONObject(i);
						DataSearch dr = new DataSearch(
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
						Log.i("datadish", String.valueOf(dataDish.size()));
					}
				} else if (resultRestau.equals("null")
						&& resultDish.equals("null")) {
					code = "2222";// ������
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return code;
		}

		protected void onPostExecute(String result) {
			stopProgressDialog();
			if (result.equals("0000")) {
				Toast.makeText(getActivity(), "�����ɹ���", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent();
				intent.putExtra("data", (ArrayList<DataSearch>) dataRestau);
				intent.setClass(getActivity(), SearchRestau.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else if (result.equals("1111")) {
				Toast.makeText(getActivity(), "�����ɹ���", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent();
				intent.putExtra("data", (ArrayList<DataSearch>) dataDish);
				intent.setClass(getActivity(), SearchDish.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else if (result.equals("2222")) {
				Toast.makeText(getActivity(), "���ݿ�û�д����ݣ�", Toast.LENGTH_SHORT)
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

	private void startProgressDialog() {
		if (mProgress == null) {
			mProgress = new ProgressDialog(getActivity(),
					R.style.myProgressDialog);
			mProgress.setMessage("��������...");
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
