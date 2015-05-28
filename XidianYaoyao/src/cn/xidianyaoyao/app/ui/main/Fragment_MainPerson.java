package cn.xidianyaoyao.app.ui.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.xidianyaoyao.app.R;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import cn.xidianyaoyao.app.ui.person.Person_changepwd;
import cn.xidianyaoyao.app.ui.person.Person_collected;
import cn.xidianyaoyao.app.ui.person.Person_friendList;
import cn.xidianyaoyao.app.ui.person.Person_history;
import cn.xidianyaoyao.app.ui.person.Person_login;
import cn.xidianyaoyao.app.ui.person.Person_morego;
import cn.xidianyaoyao.app.ui.person.Person_trace;
import cn.xidianyaoyao.app.ui.widget.BitmapTools;
import cn.xidianyaoyao.app.ui.widget.ImageTools;
import cn.xidianyaoyao.app.ui.widget.PreferencesService;
import cn.xidianyaoyao.app.utils.HttpUtils;

/**
 * �û�������Ϣ��
 * 
 * @author WangTanyun
 * 
 */
public class Fragment_MainPerson extends Fragment {

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;

	private Bitmap photoHead = null;
	private String newPhotoName;

	private LinearLayout mLoginYes;
	private RelativeLayout mLoginNo;

	private ProgressDialog mProgress;

	private View mPersonView;

	private RelativeLayout mMainPersonCollect;
	private RelativeLayout mMainPersonSee;
	private RelativeLayout mMainPersonChangePwd;
	private RelativeLayout mMainPersonFriend;
	private RelativeLayout mMainPersonRestau;
	private RelativeLayout mMainPersonTrace;

	private ImageView mPersonHeadPhoto;
	private TextView mPersoninfoName;
	private TextView mPersoninfoGender;
	private TextView mPersoninfoEmail;

	private PreferencesService preferencesService;
	private Map<String, String> params;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// savedInstanceStateΪ�����״̬��
		if (container == null)// ���������ֵΪNull,��ζ�Ÿ���Ƭ���ɼ�
			return null;
		mPersonView = (View) inflater.inflate(R.layout.fragment_main_person,
				container, false);

		initView();
		setListener();

		preferencesService = new PreferencesService(getActivity());
		params = preferencesService.cusInfo_getPreferences();
		if (params.get("cusName").equals("")) {
			mLoginYes.setVisibility(View.GONE);
			mLoginNo.setVisibility(View.VISIBLE);
		} else if (!params.get("cusName").equals("")) {
			mLoginNo.setVisibility(View.GONE);
			mLoginYes.setVisibility(View.VISIBLE);
			mPersoninfoName.setText(params.get("cusName"));
			if (params.get("cusGender").equals("1")) {
				mPersoninfoGender.setText("��");
			} else if (params.get("cusGender").equals("0")) {
				mPersoninfoGender.setText("Ů");
			}
			mPersoninfoEmail.setText(params.get("cusMailbox"));

			asyncImageLoad(mPersonHeadPhoto, HttpUtils.IP + "resources/upload/"
					+ params.get("cusHead"));// ����ͷ��
		}
		return mPersonView;
	}

	private void initView() {
		mLoginYes = (LinearLayout) mPersonView
				.findViewById(R.id.layout_login_yes);
		mLoginNo = (RelativeLayout) mPersonView
				.findViewById(R.id.layout_login_no);

		mMainPersonCollect = (RelativeLayout) mPersonView
				.findViewById(R.id.main_person_collect);
		mMainPersonSee = (RelativeLayout) mPersonView
				.findViewById(R.id.main_person_see);
		mMainPersonChangePwd = (RelativeLayout) mPersonView
				.findViewById(R.id.main_person_changepwd);
		mMainPersonFriend = (RelativeLayout) mPersonView
				.findViewById(R.id.main_person_friend);
		mMainPersonTrace = (RelativeLayout) mPersonView
				.findViewById(R.id.main_person_trace);
//		mMainPersonRestau = (RelativeLayout) mPersonView
//				.findViewById(R.id.main_person_restau);
		mPersonHeadPhoto = (ImageView) mPersonView.findViewById(R.id.photo);

		mPersoninfoName = (TextView) mPersonView.findViewById(R.id.person_id);
		mPersoninfoGender = (TextView) mPersonView
				.findViewById(R.id.person_gender);
		mPersoninfoEmail = (TextView) mPersonView
				.findViewById(R.id.person_mailbox);
	}

	private void setListener() {
		mLoginNo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivityForResult(new Intent(getActivity(),
						Person_login.class), 1);
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		mPersonHeadPhoto.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showPicturePicker(getActivity());
			}
		});
		mMainPersonCollect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent().setClass(getActivity(),
						Person_collected.class));
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		mMainPersonSee.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent().setClass(getActivity(),
						Person_history.class));
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		mMainPersonChangePwd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent().setClass(getActivity(),
						Person_changepwd.class));
				getActivity().overridePendingTransition(R.anim.roll_up,
						R.anim.roll);
			}
		});
		mMainPersonFriend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent().setClass(getActivity(),
						Person_friendList.class));
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		
		mMainPersonTrace.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent().setClass(getActivity(),
						Person_trace.class));
				getActivity().overridePendingTransition(R.anim.roll_up,
						R.anim.roll);
			}
		});
//		mMainPersonRestau.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				startActivity(new Intent().setClass(getActivity(),
//						Person_morego.class));
//				getActivity().overridePendingTransition(R.anim.slide_in_right,
//						R.anim.slide_out_left);
//			}
//		});
	}

	public void showPicturePicker(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("ѡ������ͷ��ʽ");
		builder.setItems(new String[] { "�ֻ�����", "�ֻ����" },
				new DialogInterface.OnClickListener() {
					// ������
					int REQUEST_CODE;

					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case TAKE_PICTURE:
							Uri imageUri = null;
							Intent openCameraIntent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							REQUEST_CODE = CROP;

							imageUri = Uri.fromFile(new File(Environment
									.getExternalStorageDirectory(), "photo.png"));
							// ָ����Ƭ����·����SD������image.jpgΪһ����ʱ�ļ���ÿ�����պ����ͼƬ���ᱻ�滻
							openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
									imageUri);
							startActivityForResult(openCameraIntent,
									REQUEST_CODE);
							break;

						case CHOOSE_PICTURE:
							Intent openAlbumIntent = new Intent(
									Intent.ACTION_GET_CONTENT);
							REQUEST_CODE = CROP;
							openAlbumIntent
									.setDataAndType(
											MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
											"image/*");
							startActivityForResult(openAlbumIntent,
									REQUEST_CODE);
							break;

						default:
							break;
						}
					}
				});
		builder.create().show();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			mLoginNo.setVisibility(View.GONE);
			mLoginYes.setVisibility(View.VISIBLE);

			preferencesService = new PreferencesService(getActivity());
			params = preferencesService.cusInfo_getPreferences();
			mPersoninfoName.setText(params.get("cusName"));
			if (params.get("cusGender").equals("1")) {
				mPersoninfoGender.setText("��");
			} else if (params.get("cusGender").equals("0")) {
				mPersoninfoGender.setText("Ů");
			}
			mPersoninfoEmail.setText(params.get("cusMailbox"));
			asyncImageLoad(mPersonHeadPhoto, HttpUtils.IP + "resources/upload/"
					+ params.get("cusHead"));// ����ͷ��
			return;
		} else if (resultCode == getActivity().RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				break;

			case CHOOSE_PICTURE:
				break;

			case CROP:
				Uri uri = null;
				if (data != null) {
					uri = data.getData();
				} else {
					uri = Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), "photo.png"));
				}
				cropImage(uri, 400, 400, CROP_PICTURE);
				break;

			case CROP_PICTURE:
				photoHead = null;
				Uri photoUri = data.getData();
				if (photoUri != null) {
					photoHead = BitmapFactory.decodeFile(photoUri.getPath());
				}
				if (photoHead == null) {
					Bundle extra = data.getExtras();
					if (extra != null) {
						photoHead = (Bitmap) extra.get("data");
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						photoHead.compress(Bitmap.CompressFormat.JPEG, 100,
								stream);
					}
				}
				photoHead = BitmapTools.toRoundCorner(photoHead, 20);// Բ��ͷ����ֵԽ��Բ��Խ��
				ImageTools.savePhotoToSDCard(photoHead, Environment
						.getExternalStorageDirectory().getAbsolutePath(),
						"myphoto");

				File temp = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/myphoto.png");
				if (!temp.exists()) {
					Toast.makeText(getActivity(), "ͼƬ���治�ɹ�", Toast.LENGTH_SHORT)
							.show();
					return;
				} else {
					preferencesService = new PreferencesService(getActivity());
					params = preferencesService.cusInfo_getPreferences();
					new AsyncHeadUpTask().execute(params.get("cusName"));
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * �ü�ͼƬ����ʵ��
	 * 
	 * @param uri
	 */
	public void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// �������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);

		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, requestCode);
	}

	/******************************************************/
	public class AsyncHeadUpTask extends AsyncTask<String, Integer, String> {
		// �ϴ�ͷ��
		protected String doInBackground(String... params) {
			String code = "";
			try {
				String result = XidianYaoyaoApplication.mTUploadFile
						.TUploadHeadImage(params[0]);
				JSONObject resultCode = new JSONObject(result);
				code = resultCode.getString("commonACK");
				newPhotoName = resultCode.getString("uploadFileName1");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return code;
		}

		protected void onPostExecute(String result) {
			stopProgressDialog();
			if (result.equals("111111")) {// �ϴ�ͷ��ɹ�
				mPersonHeadPhoto.setImageBitmap(photoHead);
				preferencesService = new PreferencesService(getActivity());
				params = preferencesService.cusInfo_getPreferences();
				preferencesService.save_cusInfo(params.get("cusName"),
						params.get("cusGender"), params.get("cusMailbox"),
						newPhotoName);
				Toast.makeText(getActivity(), "�ϴ�ͷ��ɹ�", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "�������Ӵ���", Toast.LENGTH_SHORT)
						.show();
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			startUpProgressDialog();
		}
	}

	/******************************************/
	private void asyncImageLoad(ImageView imageView, String path) {
		AsyncHeadDownTask mHeadDownTask = new AsyncHeadDownTask(imageView);
		mHeadDownTask.execute(path);
	}

	private class AsyncHeadDownTask extends AsyncTask<String, Integer, byte[]> {
		private ImageView imageView;

		public AsyncHeadDownTask(ImageView imageView) {
			this.imageView = imageView;
		}

		protected byte[] doInBackground(String... params) {// ���߳���ִ�е�
			try {
				return XidianYaoyaoApplication.mHttpUtils
						.getNoCacheImage(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(byte[] result) {// ���������߳�
			if (result != null && imageView != null) {

				// BitmapFactory.Options opts = new BitmapFactory.Options();
				// opts.inJustDecodeBounds = true;
				// BitmapFactory.decodeFile(imageFile, opts);
				// opts.inSampleSize = BitmapTools.computeSampleSize(opts, -1,
				// 128 * 128);
				// opts.inJustDecodeBounds = false;
				// try {
				// Bitmap bmp = BitmapFactory.decodeFile(imageFile, opts);
				// imageView.setImageBitmap(bmp);
				// } catch (OutOfMemoryError err) {
				// }

				Bitmap bitmap = null;
				try {
					bitmap = BitmapFactory.decodeByteArray(result, 0,
							result.length);
					imageView.setImageBitmap(bitmap);
				} catch (OutOfMemoryError e) {// ͼƬ̫���˾���ʾϵͳĬ�ϵ�ͷ��
				}
			}
		}

		protected void onPreExecute() {
			super.onPreExecute();
		}
	}

	/******************************************/
	private void startUpProgressDialog() {
		if (mProgress == null) {
			mProgress = new ProgressDialog(getActivity(),
					R.style.myProgressDialog);
			mProgress.setMessage("ͷ���ϴ���...");
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
