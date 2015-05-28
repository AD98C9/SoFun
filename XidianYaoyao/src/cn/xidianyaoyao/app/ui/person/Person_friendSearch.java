package cn.xidianyaoyao.app.ui.person;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;

public class Person_friendSearch extends Activity {

	private ImageView mFriendSearchBack;
	private ImageView mFriendSearchBtn;
	private EditText mFriendSearchName;

	private AsyncFriendSearchTask mTask;
	private ProgressDialog mProgress;

	private String sFriendImage;
	private String sFriendName;
	private String sFriendGender;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_friendsearch);

		initView();
		setLister();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mFriendSearchBack = (ImageView) findViewById(R.id.friendsearch_back);
		mFriendSearchBtn = (ImageView) findViewById(R.id.friendsearch_btn);
		mFriendSearchName = (EditText) findViewById(R.id.friendsearch_name);
	}

	private void setLister() {
		mFriendSearchBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			}
		});
		mFriendSearchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (search_inputIsSuccess()) {
					if (mTask != null
							&& mTask.getStatus() == AsyncFriendSearchTask.Status.RUNNING)
						mTask.cancel(true);
					mTask = new AsyncFriendSearchTask();
					mTask.execute(mFriendSearchName.getText().toString());
				} else {
					mFriendSearchName.setText("");
				}
			}
		});

	}

	// �ж��û������Ƿ���ȷ
	private boolean search_inputIsSuccess() {
		// ��ȡ�û��������Ϣ
		String search_name = mFriendSearchName.getText().toString();
		if ("".equals(search_name)) {
			Toast.makeText(Person_friendSearch.this, "�����Ի����Ʋ���Ϊ��!",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public class AsyncFriendSearchTask extends
			AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {// ��̨�߳̽�Ҫ��ɵ�����
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.friendSearch(params[0]);
				JSONObject resultCode = new JSONObject(result);
				String cusInfo = resultCode.getString("list");
				code = resultCode.getString("commonACK");
				if (code.equals("111000")) {
					JSONArray resultCus = new JSONArray(cusInfo);
					JSONObject object = resultCus.getJSONObject(0);
					sFriendImage = object.getString("headimage");
					sFriendName = object.getString("cus_id");
					sFriendGender = object.getString("gender");
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
			if (result.equals("111000")) {
//				Toast.makeText(Person_friendSearch.this, "�����ɹ�",
//						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("FriendImage", sFriendImage);
				intent.putExtra("FriendName", sFriendName);
				intent.putExtra("FriendGender", sFriendGender);
				intent.setClass(Person_friendSearch.this,
						Person_friendSee.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else if (result.equals("110111")) {
				Toast.makeText(Person_friendSearch.this, "�˺Ų����ڣ�",
						Toast.LENGTH_SHORT).show();
				mFriendSearchName.setText("");
			} else {
				Toast.makeText(Person_friendSearch.this, "������������ԣ�",
						Toast.LENGTH_SHORT).show();
			}
		}

		protected void onPreExecute() {// �÷�������ִ�к�̨��ʱ����ǰ�����ã���ʼ������ʵ��������
			super.onPreExecute();
			startProgressDialog();
		}
	}

	private void startProgressDialog() {
		if (mProgress == null) {
			mProgress = new ProgressDialog(this, R.style.myProgressDialog);
			mProgress.setMessage("������������...");
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
