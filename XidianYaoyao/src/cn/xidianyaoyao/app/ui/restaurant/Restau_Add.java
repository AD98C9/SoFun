package cn.xidianyaoyao.app.ui.restaurant;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
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
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;

public class Restau_Add extends Activity {

	private ImageView mRestauAddClose;
	private ImageView mRestauAddSend;
	private EditText mRestauName;
	private EditText mRestauCall;
	private EditText mRestauAddr;

	private ProgressDialog mProgress;
	private AsyncAddTask mTask;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.restau_add);

		initView();
		setLister();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mRestauAddClose = (ImageView) findViewById(R.id.restau_add_back);
		mRestauAddSend = (ImageView) findViewById(R.id.restau_add_send);
		mRestauName = (EditText) findViewById(R.id.restau_add_name);
		mRestauCall = (EditText) findViewById(R.id.restau_add_call);
		mRestauAddr = (EditText) findViewById(R.id.restau_add_addr);
	}

	private void setLister() {
		mRestauAddClose.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			}
		});

		mRestauAddSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (add_inputIsSuccess()) {
					if (mTask != null
							&& mTask.getStatus() == AsyncAddTask.Status.RUNNING)
						mTask.cancel(true);
					mTask = new AsyncAddTask();
					mTask.execute(mRestauName.getText().toString(), mRestauCall
							.getText().toString(), mRestauAddr.getText()
							.toString(), "", "", "");
				}
			}
		});
	}

	// �ж��û������Ƿ���ȷ
	private boolean add_inputIsSuccess() {
		// ��ȡ�û��������Ϣ
		String restau_name = mRestauName.getText().toString();
		String restau_call = mRestauCall.getText().toString();
		String restau_addr = mRestauAddr.getText().toString();
		if ("".equals(restau_name)) {
			Toast.makeText(Restau_Add.this, "��ӷ��ݵ����Ʋ���Ϊ��!", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if ("".equals(restau_call)) {
			Toast.makeText(Restau_Add.this, "��ӷ��ݵĵ绰����Ϊ��!", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if ("".equals(restau_addr)) {
			Toast.makeText(Restau_Add.this, "��ӷ��ݵĵ�ַ����Ϊ��!", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	public class AsyncAddTask extends AsyncTask<String, Integer, String> {
		protected String doInBackground(String... params) {// ��̨�߳̽�Ҫ��ɵ�����
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.restauAdd(
						params[0], params[1], params[2], params[3], params[4],
						params[5]);
				JSONObject resultCode = new JSONObject(result);
				code = resultCode.getString("commonACK");
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
			if (result.equals("110110")) {
				Toast.makeText(Restau_Add.this, "��ӷ��ݳɹ�,SoFun�ᾡ����ˣ�",
						Toast.LENGTH_SHORT).show();
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			} else {
				Toast.makeText(Restau_Add.this, "������������ԣ�", Toast.LENGTH_SHORT)
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
			mProgress = new ProgressDialog(this, R.style.myProgressDialog);
			mProgress.setMessage("������ӷ���...");
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