package cn.xidianyaoyao.app.ui.person;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;

public class Person_regist extends Activity {

	private ImageView mRegistBack;// ��ע����淵��
	private EditText mRegistUser;
	private EditText mRegistPwd;
	private EditText mRegistPwdagain;
	private EditText mRegistEmail;
	private Button mRegister;
	private RadioGroup mCheckGender;

	private AsyncRegisterTask mTask;
	private ProgressDialog mProgress;

	private String sGender = "";// �Ա�

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_regist);

		initView();
		setLister();

		XidianYaoyaoApplication.getInstance().addActivity(this);
	}

	private void initView() {
		mRegistBack = (ImageView) findViewById(R.id.regist_back);
		mRegistUser = (EditText) findViewById(R.id.regist_user);
		mRegistPwd = (EditText) findViewById(R.id.regist_pwd);
		mRegistPwdagain = (EditText) findViewById(R.id.regist_again_pwd);
		mRegistEmail = (EditText) findViewById(R.id.regist_mail);
		mCheckGender = (RadioGroup) findViewById(R.id.check_gender);
		mRegister = (Button) findViewById(R.id.regist_sumbit);
	}

	private void setLister() {
		mRegistBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			}
		});
		mRegister.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				register_verifyFormat();
			}
		});

		mCheckGender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.check_nan:// ������1��Ů����0
					sGender = "1";
					break;
				case R.id.check_nv:
					sGender = "0";
					break;
				}
			}
		});
	}

	private void register_verifyFormat() {
		String user = mRegistUser.getText().toString();
		String password = mRegistPwd.getText().toString();
		String again = mRegistPwdagain.getText().toString();
		String email = mRegistEmail.getText().toString();

		if ("".equals(user))
			Toast.makeText(this, "�˺Ų���Ϊ��!", Toast.LENGTH_SHORT).show();
		else if ("".equals(password))
			Toast.makeText(this, "���벻��Ϊ��!", Toast.LENGTH_SHORT).show();
		else if ("".equals(again))
			Toast.makeText(this, "ȷ�����벻��Ϊ��!", Toast.LENGTH_SHORT).show();
		else if ("".equals(email))
			Toast.makeText(this, "ע�����䲻��Ϊ��!", Toast.LENGTH_SHORT).show();
		else if (sGender.equals(""))
			Toast.makeText(this, "û��ѡ���Ա�", Toast.LENGTH_SHORT).show();
		else {
			if (!pwdIsSame(password, again)) {
				Toast.makeText(this, "�����������벻һ��!", Toast.LENGTH_SHORT).show();
				mRegistPwd.setText("");
				mRegistPwdagain.setText("");
			} else if (!isEmailMode(email)) {
				Toast.makeText(this, "�����ʽ����!", Toast.LENGTH_SHORT).show();
				mRegistEmail.setText("");
			} else if (isChinese(user)) {
				Toast.makeText(this, "�˺Ų���Ϊ����!", Toast.LENGTH_SHORT).show();
				mRegistUser.setText("");
			} else if (isChinese(password)) {
				Toast.makeText(this, "���벻��Ϊ����!", Toast.LENGTH_SHORT).show();
				mRegistPwd.setText("");
				mRegistPwdagain.setText("");
			} else {// ִ���첽ע��
				if (mTask != null
						&& mTask.getStatus() == AsyncRegisterTask.Status.RUNNING)
					mTask.cancel(true);
				mTask = new AsyncRegisterTask();
				mTask.execute(user, password, email, sGender);
			}
		}
	}

	private boolean pwdIsSame(String first, String second) {
		if (first.equals(second)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isEmailMode(String emailStr) {
		String emailMode = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(emailMode);
		Matcher m = p.matcher(emailStr);
		return m.matches();
	}

	private boolean isChinese(String Str) {
		String strMode = "^[\u4e00-\u9fa5]{0,}$";//ֻ���Ǻ���
		Pattern p = Pattern.compile(strMode);
		Matcher m = p.matcher(Str);
		return m.matches();
	}

	public class AsyncRegisterTask extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {// ��̨�߳̽�Ҫ��ɵ�����
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils.regist(
						params[0], params[1], params[2], params[3]);
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
			if (result.equals("111000")) {
				Toast.makeText(Person_regist.this, "ע��ɹ�", Toast.LENGTH_SHORT)
						.show();
				finish();
				overridePendingTransition(0, R.anim.roll_down);
			} else if (result.equals("111001")) {
				Toast.makeText(Person_regist.this, "�˺���ע�ᣡ", Toast.LENGTH_SHORT)
						.show();
				mRegistUser.setText("");
				mRegistPwd.setText("");
				mRegistPwdagain.setText("");
				mRegistEmail.setText("");
			} else if (result.equals("111010")) {
				Toast.makeText(Person_regist.this, "������ע�ᣡ", Toast.LENGTH_SHORT)
						.show();
				mRegistPwd.setText("");
				mRegistPwdagain.setText("");
				mRegistEmail.setText("");
			} else {
				Toast.makeText(Person_regist.this, "������������ԣ�",
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
			mProgress.setMessage("����ע��...");
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
