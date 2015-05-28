package cn.xidianyaoyao.app.ui.person;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.widget.SendMail;

public class Person_find_pwd extends Activity {

	private ImageView mFindpwdBack;// ���һ�������淵��
	private EditText mFindpwdUser;
	private Button mFindPwdSumbit;

	private AsyncFindpwdTask mTask;
	private ProgressDialog mProgress;

	private String FUserStr;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_find_pwd);

		initView();
		setLister();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mFindpwdBack = (ImageView) findViewById(R.id.find_pwd_back);
		mFindpwdUser = (EditText) findViewById(R.id.find_pwd_user);
		mFindPwdSumbit = (Button) findViewById(R.id.find_pwd_sumbit);
	}

	private void setLister() {
		mFindpwdBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			}
		});

		mFindPwdSumbit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (findpwd_inputIsSuccess()) {
					FUserStr = mFindpwdUser.getText().toString();
					if (mTask != null
							&& mTask.getStatus() == AsyncFindpwdTask.Status.RUNNING)
						mTask.cancel(true);
					mTask = new AsyncFindpwdTask();
					mTask.execute(FUserStr);
				} else {
					mFindpwdUser.setText("");
				}
			}
		});
	}

	// �ж��û������Ƿ���ȷ
	private boolean findpwd_inputIsSuccess() {
		String username = mFindpwdUser.getText().toString().trim();
		if ("".equals(username)) {
			Toast.makeText(Person_find_pwd.this, "�˺Ų���Ϊ��!", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	public class AsyncFindpwdTask extends AsyncTask<String, Integer, String> {
		protected String doInBackground(String... params) {// ��̨�߳̽�Ҫ��ɵ�����
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.find_pwd(params[0]);
				JSONObject resultCode = new JSONObject(result);
				code = resultCode.getString("commonACK");
				if (!resultCode.getString("customer").equals("null")) {
					JSONObject cusInfo = new JSONObject(
							resultCode.getString("customer"));
					String pwd = cusInfo.getString("passwd");
					String email = cusInfo.getString("email");
					SendMail.sendEmail(email, "SoFun�һ�����", "����SoFun�˺ŵ����룺 " + pwd);
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
			if (result.equals("101001")) {
				Toast.makeText(Person_find_pwd.this, "�һ�����ɹ���\n�ѷ��͵����ע�����䣡",
						Toast.LENGTH_SHORT).show();
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			} else if (result.equals("100111")) {
				Toast.makeText(Person_find_pwd.this, "�˺Ų����ڣ�",
						Toast.LENGTH_SHORT).show();
				mFindpwdUser.setText("");
			} else {
				Toast.makeText(Person_find_pwd.this, "������������ԣ�",
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
			mProgress.setMessage("�����һ�����...");
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
