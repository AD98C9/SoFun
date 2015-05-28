package cn.xidianyaoyao.app.sqlite;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * �������ݿ⣬�洢�����ʷ
 * 
 * @author TangWenda
 * 
 */
public class HistorySQLiteHelper extends SQLiteOpenHelper {
	// ���ø��๹����
	private String user_name = null;
	private static final int VERSION = 1;

	public HistorySQLiteHelper(Context context, String name,
			SQLiteDatabase.CursorFactory factory, int version, String user_name) {
		super(context, name, factory, version);
		this.user_name = user_name;
	}

	public HistorySQLiteHelper(Context context, String user_name) {
		this(context, "sql" + user_name, null, VERSION, "sqlcus" + user_name);// �ڶ����������ݿ���
	}

	/**
	 * �����ݿ��״δ���ʱִ�и÷�����һ�㽫������ȳ�ʼ���������ڸ÷�����ִ��. ��дonCreate����������execSQL����������
	 * */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ user_name
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,cus_id VARCHAR,time VARCHAR,dish_id VARCHAR,dish_image VARCHAR,dish_name VARCHAR,dish_score VARCHAR,dish_price VARCHAR,dish_taste VARCHAR,dish_nutrition VARCHAR,res_id VARCHAR,res_name VARCHAR,res_score VARCHAR,res_addr VARCHAR,res_call VARCHAR,res_descr VARCHAR,res_lat VARCHAR,res_lon VARCHAR)");
	}

	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}

	// �������ݣ����ֻ�ܴ�10��������10��ʱ������ɾ����ǰ����
	public void InsertData(SQLiteDatabase db, String name, String dish_id,
			String dish_image, String dish_name, String dish_score,
			String dish_price, String dish_taste, String dish_nutrition,
			String res_id, String res_name, String res_score, String res_addr,
			String res_call, String res_descr, String res_lat, String res_lon) {
		Cursor c = db.rawQuery("SELECT * FROM " + user_name, null);
		if (c.getCount() < 10) {
			db.execSQL("INSERT INTO " + user_name
					+ " VALUES (NULL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { "sqlcus" + name, getDateTime(), dish_id,
							dish_image, dish_name, dish_score, dish_price,
							dish_taste, dish_nutrition, res_id, res_name,
							res_score, res_addr, res_call, res_descr, res_lat,
							res_lon });
		} else {
			db.execSQL("delete from " + user_name
					+ " where _id=(select _id from " + user_name
					+ " ORDER BY time limit 1)");
			db.execSQL("INSERT INTO " + user_name
					+ " VALUES (NULL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { "sqlcus" + name, getDateTime(), dish_id,
							dish_image, dish_name, dish_score, dish_price,
							dish_taste, dish_nutrition, res_id, res_name,
							res_score, res_addr, res_call, res_descr, res_lat,
							res_lon });
		}
	}

	// �������
	public void DeleteData(SQLiteDatabase db) {
		db.execSQL("delete from " + user_name);
	}

	// �������ݿ�ʱ����İ汾���뵱ǰ�İ汾�Ų�ͬʱ����ø÷���
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
