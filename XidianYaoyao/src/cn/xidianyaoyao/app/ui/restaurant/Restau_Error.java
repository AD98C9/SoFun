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

public class Restau_Error extends Activity {

	private ImageView mRestauErrorClose;
	private ImageView mRestauErrorSend;
	private EditText mErrorSummary;

	private ProgressDialog mProgress;
	private AsyncErrorTask mTask;

	private String sErrorSummary;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.restau_error);

		initView();
		setLister();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mRestauErrorClose = (ImageView) findViewById(R.id.restau_error_back);
		mRestauErrorSend = (ImageView) findViewById(R.id.restau_error_send);
		mErrorSummary = (EditText) findViewById(R.id.restau_error_summary);
	}

	private void setLister() {
		mRestauErrorClose.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			}
		});

		mRestauErrorSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (error_inputIsSuccess()) {
					sErrorSummary = mErrorSummary.getText().toString();
					if (mTask != null
							&& mTask.getStatus() == AsyncErrorTask.Status.RUNNING)
						mTask.cancel(true);
					mTask = new AsyncErrorTask();
					mTask.execute(getIntent().getStringExtra("RestauName"),
							sErrorSummary);
				} else {
					mErrorSummary.setText("");
				}
			}
		});
	}

	// �ж��û������Ƿ���ȷ
	private boolean error_inputIsSuccess() {
		// ��ȡ�û��������Ϣ
		String error_summary = mErrorSummary.getText().toString();
		if ("".equals(error_summary)) {
			Toast.makeText(Restau_Error.this, "���ݾ�����Ϣ����Ϊ��!", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	public class AsyncErrorTask extends AsyncTask<String, Integer, String> {
		protected String doInBackground(String... params) {// ��̨�߳̽�Ҫ��ɵ�����
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.restauCorrect(params[0], params[1]);
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
			// if (result.equals("110000")) {// �Ķ�
			Toast.makeText(Restau_Error.this, "����ɹ�", Toast.LENGTH_SHORT)
					.show();
			finish();
			overridePendingTransition(0, R.anim.roll_down);
			// } else {
			// Toast.makeText(Restau_Error.this, "������������ԣ�",
			// Toast.LENGTH_SHORT).show();
			// }
		}

		protected void onPreExecute() {
			super.onPreExecute();
			startProgressDialog();
		}

	}

	private void startProgressDialog() {
		if (mProgress == null) {
			mProgress = new ProgressDialog(this, R.style.myProgressDialog);
			mProgress.setMessage("���ھ���...");
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