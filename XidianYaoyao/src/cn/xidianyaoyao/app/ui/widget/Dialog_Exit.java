package cn.xidianyaoyao.app.ui.widget;

import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class Dialog_Exit extends Dialog{
	
	private Context mContext;
	private Builder mExit;
	
	public Dialog_Exit(Context context) {
		super(context);
		mContext = context;
		mExit = new AlertDialog.Builder(mContext);
		DialogSettings();
	}
	
	private void DialogSettings() {
		mExit.setMessage("ȷ���˳�SoFun��?");
		mExit.setPositiveButton("�˳�", mExitListener);
		mExit.setNegativeButton("ȡ��", mExitListener);
	}
	
	public void show(){
		mExit.show();
	}
	
	OnClickListener mExitListener = new OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch(which){
			case -1://ȷ��
				XidianYaoyaoApplication.getInstance().exit();
				break;
			case -2://ȡ��
				dialog.cancel();
				break;
			}
		}
	};
}
