package cn.xidianyaoyao.app.ui.person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.data.DataSearch;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;

public class Person_trace extends Activity {

	private ImageView mEattraceBack;
	private ImageView mEattraceSee;
	private EditText mEattraceZao;
	private EditText mEattraceWu;
	private EditText mEattraceWan;
	private TextView mtraceReault;

	private ProgressDialog mProgress;
	private AsyncTraceTask mTask;
	private AsyncTraceTask2 mTask2;
	private AsyncTraceTask3 mTask3;
	private List<DataSearch> dataDish;
	private List<DataSearch> dataDish2;
	private List<DataSearch> dataDish3;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_trace);

		initView();
		setLister();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mEattraceBack = (ImageView) findViewById(R.id.eat_trace_back);
		mEattraceSee = (ImageView) findViewById(R.id.eat_trace_see);
		mEattraceZao = (EditText) findViewById(R.id.eat_trace_zao);
		mEattraceWu = (EditText) findViewById(R.id.eat_trace_wu);
		mEattraceWan = (EditText) findViewById(R.id.eat_trace_wan);
		mtraceReault = (TextView) findViewById(R.id.trace_summary);
	}

	private void setLister() {
		mEattraceBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			}
		});

		mEattraceSee.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (trace_inputIsSuccess()) {
					// if (mTask != null
					// && mTask.getStatus() == AsyncTraceTask.Status.RUNNING)
					// mTask.cancel(true);
					mTask = new AsyncTraceTask();
					mTask2 = new AsyncTraceTask2();
					mTask3 = new AsyncTraceTask3();
					mTask.execute(mEattraceZao.getText().toString());
					mTask2.execute(mEattraceWu.getText().toString());
					mTask3.execute(mEattraceWan.getText().toString());
				}
			}
		});
	}

	// �ж��û������Ƿ���ȷ
	private boolean trace_inputIsSuccess() {
		// ��ȡ�û��������Ϣ
		String trace_zao = mEattraceZao.getText().toString();
		String trace_wu = mEattraceWu.getText().toString();
		String trace_wan = mEattraceWan.getText().toString();
		if ("".equals(trace_zao)) {
			Toast.makeText(Person_trace.this, "������벻��Ϊ��!", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if ("".equals(trace_wu)) {
			Toast.makeText(Person_trace.this, "������벻��Ϊ��!", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if ("".equals(trace_wan)) {
			Toast.makeText(Person_trace.this, "������벻��Ϊ��!", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	public class AsyncTraceTask extends AsyncTask<String, Integer, String> {
		protected String doInBackground(String... params) {// ��̨�߳̽�Ҫ��ɵ�����
			String code = "";
			dataDish = new ArrayList<DataSearch>();
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.search(params[0]);
				JSONObject resultString = new JSONObject(result);
				String resultDish = resultString.getString("dishes");
				if (!resultDish.equals("null")) {
					code = "1111";
					JSONArray resultCode = new JSONArray(resultDish);
					for (int i = 0; i < resultCode.length(); i++) {
						JSONObject object = resultCode.getJSONObject(i);
						DataSearch dr = new DataSearch(
								object.getString("taste"),
								object.getString("nutrition"));
						dataDish.add(dr);
					}
				} else if (resultDish.equals("null")) {
					code = "2222";// ������
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
			} else if (result.equals("2222")) {
			} else {
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			startProgressDialog();
		}

	}

	private void startProgressDialog() {
		if (mProgress == null) {
			mProgress = new ProgressDialog(this, R.style.myProgressDialog);
			mProgress.setMessage("������ʳ����...");
		}
		mProgress.show();
	}

	private void stopProgressDialog() {
		if (mProgress != null) {
			mProgress.dismiss();
			mProgress = null;
		}
	}

	public class AsyncTraceTask2 extends AsyncTask<String, Integer, String> {
		protected String doInBackground(String... params) {// ��̨�߳̽�Ҫ��ɵ�����
			String code = "";
			dataDish2 = new ArrayList<DataSearch>();
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.search(params[0]);
				JSONObject resultString = new JSONObject(result);
				String resultDish = resultString.getString("dishes");
				if (!resultDish.equals("null")) {
					code = "1111";
					JSONArray resultCode = new JSONArray(resultDish);
					for (int i = 0; i < resultCode.length(); i++) {
						JSONObject object = resultCode.getJSONObject(i);
						DataSearch dr = new DataSearch(
								object.getString("taste"),
								object.getString("nutrition"));
						dataDish2.add(dr);
					}
				} else if (resultDish.equals("null")) {
					code = "2222";// ������
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
			if (result.equals("1111")) {
			} else if (result.equals("2222")) {
			} else {
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
		}

	}

	public class AsyncTraceTask3 extends AsyncTask<String, Integer, String> {
		protected String doInBackground(String... params) {// ��̨�߳̽�Ҫ��ɵ�����
			String code = "";
			dataDish3 = new ArrayList<DataSearch>();
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.search(params[0]);
				JSONObject resultString = new JSONObject(result);
				String resultDish = resultString.getString("dishes");
				if (!resultDish.equals("null")) {
					code = "1111";
					JSONArray resultCode = new JSONArray(resultDish);
					for (int i = 0; i < resultCode.length(); i++) {
						JSONObject object = resultCode.getJSONObject(i);
						DataSearch dr = new DataSearch(
								object.getString("taste"),
								object.getString("nutrition"));
						dataDish3.add(dr);
					}
				} else if (resultDish.equals("null")) {
					code = "2222";// ������
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
			if (result.equals("1111")) {
				Toast.makeText(Person_trace.this, "���ٳɹ���", Toast.LENGTH_SHORT)
						.show();
				mtraceReault.setVisibility(View.VISIBLE);
				mtraceReault.setText("�ף��������ʳ��Ϣ��\n���[��ζ��"+dataDish.get(0).getDish_taste()+"][Ӫ����"
				+dataDish.get(0).getDish_nutrition()+"]\n���[��ζ��"+dataDish2.get(0).getDish_taste()+"][Ӫ����"
				+dataDish2.get(0).getDish_nutrition()+"]\n���[��ζ��"+dataDish3.get(0).getDish_taste()+"][Ӫ����"
				+dataDish3.get(0).getDish_nutrition()+"]\n\n����SoFun����ʳ����");
			} else if (result.equals("2222")) {
				Toast.makeText(Person_trace.this, "�����Ʒ�����޷����٣�",
						Toast.LENGTH_SHORT).show();
			} else {
				mtraceReault.setVisibility(View.GONE);
				Toast.makeText(Person_trace.this, "������������ԣ�",
						Toast.LENGTH_SHORT).show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
		}

	}
}