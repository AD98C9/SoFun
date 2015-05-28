package cn.xidianyaoyao.app.ui.restaurant;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.person.Person_login;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;
import cn.xidianyaoyao.app.utils.HttpUtils;

public class Fragment_Restau_Dish extends Fragment {

	private View mDishView;
	private Bundle bundle;
	private File file;

	private ImageView mDishImage;
	private TextView mDishPrice;
	private TextView mDishScore;
	private TextView mDishTaste;
	private TextView mDishNutrition;
	private RelativeLayout mCollectNoLayout;
	private RelativeLayout mCollectYesLayout;

	private RelativeLayout mEvaluateSee;
	private RelativeLayout mMsgSend;
	private RelativeLayout mShare;

	private ProgressDialog mProgressAdd = null;
	private ProgressDialog mProgressDelete = null;

	private PreferencesService preferencesService;
	private Map<String, String> params;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// savedInstanceStateΪ�����״̬��
		if (container == null)// ���������ֵΪNull,��ζ�Ÿ���Ƭ���ɼ�
			return null;

		mDishView = (View) inflater.inflate(R.layout.fragment_restau_dish,
				container, false);

		initView();
		setListener();

		return mDishView;
	}

	private void initView() {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// �ж�����SD��
			file = new File(Environment.getExternalStorageDirectory(),// SD��·��
					"xidianyaoyao_cache/dishImageCache");
			if (!file.exists())
				file.mkdirs();
		}

		mDishImage = (ImageView) mDishView.findViewById(R.id.dish_image);
		mDishPrice = (TextView) mDishView.findViewById(R.id.dish_price);
		mDishScore = (TextView) mDishView.findViewById(R.id.dish_score);
		mDishTaste = (TextView) mDishView.findViewById(R.id.dish_taste);
		mDishNutrition = (TextView) mDishView.findViewById(R.id.dish_nutrition);

		mCollectNoLayout = (RelativeLayout) mDishView
				.findViewById(R.id.Layout_collect_no);
		mCollectYesLayout = (RelativeLayout) mDishView
				.findViewById(R.id.Layout_collect_yes);

		bundle = this.getArguments();
		asyncImageLoad(mDishImage, HttpUtils.IP + "resources/images/dishes/"
				+ bundle.getString("DishImage"));
		mDishPrice.setText("���ۣ�" + bundle.getString("DishPrice"));
		mDishScore.setText("���֣�" + bundle.getString("DishScore"));
		mDishTaste.setText("��ζ��" + bundle.getString("DishTaste"));
		mDishNutrition.setText("Ӫ����" + bundle.getString("DishNutrition"));

		mEvaluateSee = (RelativeLayout) mDishView
				.findViewById(R.id.dish_evaluateSee);
		mMsgSend = (RelativeLayout) mDishView.findViewById(R.id.dish_message);
		mShare = (RelativeLayout) mDishView.findViewById(R.id.dish_share);

		preferencesService = new PreferencesService(getActivity());
		params = preferencesService.cusInfo_getPreferences();

		if (params.get("cusName").equals("")) {
			mCollectYesLayout.setVisibility(View.GONE);
			mCollectNoLayout.setVisibility(View.VISIBLE);
		} else if (!params.get("cusName").equals("")) {
			new AsyIsOrNotCollectTast().execute(params.get("cusName"),
					bundle.getString("DishId"), bundle.getString("RestauId"));
		}
	}

	private void setListener() {
		mEvaluateSee.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("DishId", bundle.getString("DishId"));
				intent.putExtra("DishName", bundle.getString("DishName"));
				intent.putExtra("RestauId", bundle.getString("RestauId"));
				intent.putExtra("RestauName", bundle.getString("RestauName"));
				intent.putExtra("RestauScore", bundle.getString("RestauScore"));
				intent.putExtra("RestauAddr", bundle.getString("RestauAddr"));
				intent.putExtra("RestauCall", bundle.getString("RestauCall"));
				intent.putExtra("RestauDescr", bundle.getString("RestauDescr"));
				intent.putExtra("RestauLat", bundle.getString("RestauLat"));
				intent.putExtra("RestauLon", bundle.getString("RestauLon"));
				intent.setClass(getActivity(), Dish_EvaluateSee.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		mMsgSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SENDTO);
				intent.setData(Uri.parse("smsto:"));
				// ��λ�ú͵绰���˴��ο�ʳ��ҡҡ
				intent.putExtra("sms_body",
						"����֪ͨ��[" + bundle.getString("RestauName") + "]��("
								+ bundle.getString("DishName")
								+ ")����Ŷ��һ��ȥ�԰ɣ�����#SoFun#���Ƽ���");
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		// ȡ���ղ�
		mCollectYesLayout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				preferencesService = new PreferencesService(getActivity());
				params = preferencesService.cusInfo_getPreferences();
				new AsyncDeleteTask().execute(params.get("cusName"),
						bundle.getString("DishId"),
						bundle.getString("RestauId"));
			}
		});

		// ����ղ�
		mCollectNoLayout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				preferencesService = new PreferencesService(getActivity());
				params = preferencesService.cusInfo_getPreferences();
				if (params.get("cusName").equals("")) {
					Toast.makeText(getActivity(), "�ף��㻹û��¼��!",
							Toast.LENGTH_SHORT).show();
					startActivityForResult(new Intent(getActivity(),
							Person_login.class), 1);
					getActivity().overridePendingTransition(
							R.anim.slide_in_right, R.anim.slide_out_left);
				} else if (!params.get("cusName").equals("")) {
					new AsyncAddTask().execute(params.get("cusName"),
							bundle.getString("DishId"),
							bundle.getString("RestauId"));
				}
			}
		});
		mShare.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new AsyncImageShareTask().execute(HttpUtils.IP
						+ "resources/images/dishes/"
						+ bundle.getString("DishImage"));
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 1:// ��¼�ɹ�
				// �ж����Ƿ��ղع�
			preferencesService = new PreferencesService(getActivity());
			params = preferencesService.cusInfo_getPreferences();
			new AsyIsOrNotCollectTast2().execute(params.get("cusName"),
					bundle.getString("DishId"), bundle.getString("RestauId"));
			break;
		default:
			break;
		}
	}

	// �Ƿ��ղ�
	public class AsyIsOrNotCollectTast extends
			AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.isOrNoCollect(params[0], params[1], params[2]);
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
			if (result.equals("100101")) {// �Ѿ��ղ�
				mCollectNoLayout.setVisibility(View.GONE);
				mCollectYesLayout.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "�Ѿ��ղ�", Toast.LENGTH_SHORT)
						.show();
			} else if (result.equals("100110")) {// ��δ�ղ�
				mCollectYesLayout.setVisibility(View.GONE);
				mCollectNoLayout.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "��δ�ղ�", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "��������޷��ж��Ƿ��ղأ������ԣ�",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// �Ƿ��ղ�
	public class AsyIsOrNotCollectTast2 extends
			AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.isOrNoCollect(params[0], params[1], params[2]);
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
			if (result.equals("100101")) {// �Ѿ��ղ�
				mCollectNoLayout.setVisibility(View.GONE);
				mCollectYesLayout.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "�Ѿ��ղ�", Toast.LENGTH_SHORT)
						.show();
			} else if (result.equals("100110")) {// ��δ�ղ�
				mCollectYesLayout.setVisibility(View.GONE);
				mCollectNoLayout.setVisibility(View.VISIBLE);
				preferencesService = new PreferencesService(getActivity());
				params = preferencesService.cusInfo_getPreferences();
				new AsyncAddTask().execute(params.get("cusName"),
						bundle.getString("DishId"),
						bundle.getString("RestauId"));
			} else {
				Toast.makeText(getActivity(), "��������޷��ж��Ƿ��ղأ������ԣ�",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	// ɾ���ղ�
	public class AsyncDeleteTask extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {// ��̨�߳̽�Ҫ��ɵ�����
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.personNotCollect(params[0], params[1], params[2]);
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
			stopProgressDelete();
			if (result.equals("100011")) {
				mCollectYesLayout.setVisibility(View.GONE);
				mCollectNoLayout.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "ɾ���ղسɹ�", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "������������ԣ�", Toast.LENGTH_SHORT)
						.show();
			}
		}

		protected void onPreExecute() {// �÷�������ִ�к�̨��ʱ����ǰ�����ã���ʼ������ʵ��������
			super.onPreExecute();
			startProgressDelete();
		}
	}

	// ����ղ�
	public class AsyncAddTask extends AsyncTask<String, Integer, String> {

		protected String doInBackground(String... params) {// ��̨�߳̽�Ҫ��ɵ�����
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mHttpUtils
						.personCollect(params[0], params[1], params[2]);
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
			stopProgressAdd();
			if (result.equals("100001")) {
				mCollectNoLayout.setVisibility(View.GONE);
				mCollectYesLayout.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "�ղسɹ�", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "������������ԣ�", Toast.LENGTH_SHORT)
						.show();
			}
		}

		protected void onPreExecute() {// �÷�������ִ�к�̨��ʱ����ǰ�����ã���ʼ������ʵ��������
			super.onPreExecute();
			startProgressAdd();
		}
	}

	private void startProgressDelete() {
		if (mProgressDelete == null) {
			mProgressDelete = new ProgressDialog(getActivity(),
					R.style.myProgressDialog);
			mProgressDelete.setMessage("����ɾ���ղ�...");
		}
		mProgressDelete.show();
	}

	private void startProgressAdd() {
		if (mProgressAdd == null) {
			mProgressAdd = new ProgressDialog(getActivity(),
					R.style.myProgressDialog);
			mProgressAdd.setMessage("��������ղ�...");
		}
		mProgressAdd.show();
	}

	private void stopProgressDelete() {
		if (mProgressDelete != null) {
			mProgressDelete.dismiss();
			mProgressDelete = null;
		}
	}

	private void stopProgressAdd() {
		if (mProgressAdd != null) {
			mProgressAdd.dismiss();
			mProgressAdd = null;
		}
	}

	private void asyncImageLoad(ImageView imageView, String path) {
		AsyncDishImageTask mDishImageTask = new AsyncDishImageTask(imageView);
		mDishImageTask.execute(path);
	}

	private class AsyncDishImageTask extends AsyncTask<String, Integer, Uri> {
		private ImageView imageView;

		public AsyncDishImageTask(ImageView imageView) {
			this.imageView = imageView;
		}

		protected Uri doInBackground(String... params) {// ���߳���ִ�е�
			try {
				return XidianYaoyaoApplication.mHttpUtils.getCacheImage(
						params[0], file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Uri result) {// ���������߳�
			if (result != null && imageView != null) {
				imageView.setImageURI(result);
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
		}
	}

	private class AsyncImageShareTask extends AsyncTask<String, Integer, Uri> {
		protected Uri doInBackground(String... params) {// ���߳���ִ�е�
			try {
				return XidianYaoyaoApplication.mHttpUtils.getCacheImage(
						params[0], file);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Uri result) {// ���������߳�
			String content = "";// ����ʱ��Ӧ��ͼƬ������˵��
			Intent shareIntent = new Intent(Intent.ACTION_SEND);

			if (result != null) {
				shareIntent.putExtra(Intent.EXTRA_STREAM, result);
				shareIntent.setType("image/*");
				// ���û�ѡ�����ʱʹ��sms_bodyȡ������
				shareIntent.putExtra("sms_body", content);
			} else {
				shareIntent.setType("text/plain"); // ���ļ�����Ϊ���ı�����ʽ
			}
			shareIntent.putExtra(Intent.EXTRA_TEXT,
					"#SoFun#" + "[" + bundle.getString("RestauName") + "]��("
							+ bundle.getString("DishName") + ")ζ������Ŷ��");// content
			startActivity(Intent.createChooser(shareIntent, "�����罻"));
		}

		protected void onPreExecute() {
			super.onPreExecute();
		}
	}
}
