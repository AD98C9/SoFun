package cn.xidianyaoyao.app.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * �������ݿ⣬�洢��ȥ����
 * 
 * @author WangTanyun
 * 
 */
public class MoregoSQLiteHelper extends SQLiteOpenHelper {
	// ���ø��๹����
	private String user_name = null;
	private static final int VERSION = 1;

	public MoregoSQLiteHelper(Context context, String name,
			SQLiteDatabase.CursorFactory factory, int version, String user_name) {
		super(context, name, factory, version);
		this.user_name = user_name;
	}

	public MoregoSQLiteHelper(Context context, String user_name) {
		this(context, "sql" + user_name, null, VERSION, "sqllcus" + user_name);// �ڶ����������ݿ���
	}

	/**
	 * �����ݿ��״δ���ʱִ�и÷�����һ�㽫������ȳ�ʼ���������ڸ÷�����ִ��. ��дonCreate����������execSQL����������
	 * */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ user_name
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,cus_id VARCHAR,res_id VARCHAR,res_name VARCHAR,res_score VARCHAR,res_addr VARCHAR,res_call VARCHAR,res_descr VARCHAR,res_lat VARCHAR,res_lon VARCHAR,count INTEGER)");
	}

	// ��������
	public void InsertData(SQLiteDatabase db, String name, String res_id,
			String res_name, String res_score, String res_addr,
			String res_call, String res_descr, String res_lat, String res_lon) {
		Cursor c = db.rawQuery("SELECT * FROM " + user_name, null);
		db.execSQL("INSERT INTO " + user_name
				+ " VALUES (NULL,?,?,?,?,?,?,?,?,?)", new Object[] {
				"sqllcus" + name, res_id, res_name, res_score, res_addr,
				res_call, res_descr, res_lat, res_lon });
	}

	// �������ݿ�ʱ����İ汾���뵱ǰ�İ汾�Ų�ͬʱ����ø÷���
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
