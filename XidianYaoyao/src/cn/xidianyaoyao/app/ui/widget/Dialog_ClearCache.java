package cn.xidianyaoyao.app.ui.widget;

import java.io.File;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.widget.Toast;

public class Dialog_ClearCache extends Dialog {

	private Context mContext;
	private Builder mDelete;

	public Dialog_ClearCache(Context context) {
		super(context);
		mContext = context;
		mDelete = new AlertDialog.Builder(mContext);
		DialogSettings();
	}

	private void DialogSettings() {
		mDelete.setTitle("ȷ���������?");
		mDelete.setMessage("����ͼƬ������Խ�ʡ�ֻ�����");
		mDelete.setIcon(android.R.drawable.ic_dialog_info);
		mDelete.setPositiveButton("���", mClearCacheListener);
		mDelete.setNegativeButton("ȡ��", mClearCacheListener);
	}

	public void show() {
		mDelete.show();
	}

	OnClickListener mClearCacheListener = new OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {

			switch (which) {
			case -1:// ���
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {// �ж�SD���Ƿ��������
					File xidianyaoyao_cache = new File(
							Environment.getExternalStorageDirectory(),
							"xidianyaoyao_cache");
					DeleteFile(xidianyaoyao_cache);
				} else {
					Toast.makeText(mContext, "SD���ز�������", Toast.LENGTH_SHORT)
							.show();
				}
			case -2:// ��ʧ
				dialog.cancel();
				break;
			}
		}
	};

	// �ݹ�ɾ�������е����ж���
	public void DeleteFile(File file) {
		if (!file.exists()) {
			Toast.makeText(mContext, "ͼƬ���治���ڣ�", Toast.LENGTH_SHORT).show();
			return;
		} else {
			if (file.isFile())// ��һ���ļ�
			{
				file.delete();
				Toast.makeText(mContext, "���ͼƬ����ɹ���", Toast.LENGTH_SHORT)
						.show();
				return;
			} else if (file.isDirectory())// ��һ��Ŀ¼
			{
				File childFiles[] = file.listFiles();
				if (childFiles == null || childFiles.length == 0) {
					file.delete();
					Toast.makeText(mContext, "���ͼƬ����ɹ���", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				for (File f : childFiles) {
					DeleteFile(f);
				}
				file.delete();
				Toast.makeText(mContext, "���ͼƬ����ɹ���", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
}
