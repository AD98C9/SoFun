package cn.xidianyaoyao.app.ui.widget;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import cn.xidianyaoyao.app.logic.XidianYaoyaoApplication;

public class BitmapTools {
	/**
	 * ����ͼƬ��Ҫ�Ŀ�Ⱥ͸߶�
	 * 
	 * @param f
	 *            ���õĿ�� height ���õĸ߶�
	 */
	public static Bitmap changeBitmapWH(Bitmap bitmap, float f, float g) {
		int originalWidth = 0, originalHeight = 0;
		if (bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {

		} else {
			originalWidth = bitmap.getWidth();
			originalHeight = bitmap.getHeight();
			// ����ѹ����
			float scaleWidth = (float) f / originalWidth;
			float scaleHeight = (float) g / originalHeight;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, originalWidth,
					originalHeight, matrix, true);
		}
		return bitmap;
	}

	/**
	 * 
	 * @param bitmap
	 *            Ҫת��ΪԲ�ǵ�ͼƬbitmap
	 * @param pixels
	 *            Բ�ǵĴ�С���� ��ֵԽ��Բ��Խ��,����Ϊ4
	 * @return ������Բ��ͼƬ
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * ת��ͼƬΪԲ��
	 * 
	 * @param Bitmap
	 *            ��Ҫ�����ͼƬ �����ΪԲ�� bitmap
	 * 
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap, int width, int height) {
		Bitmap changeBitmap = changeBitmapWH(bitmap, width, height);
		Log.i("tag", "ͼƬ�Ŀ�ߣ�" + width + "--" + height);
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		roundPx = width / 2;
		top = 0;
		bottom = width;
		left = 0;
		right = width;
		height = width;
		dst_left = 0;
		dst_top = 0;
		dst_right = width;
		dst_bottom = width;
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(changeBitmap, src, dst, paint);
		return output;
	}

	/**
	 * �ϳ�����bitmap����
	 * 
	 * @param background
	 *            ������ʽ
	 * @param foreground
	 *            ǰ�渲�ǵ���ʽ
	 * @return �ϳɽ��bitmap
	 */
	public static Bitmap combineDrawable(Bitmap background, Bitmap foreground) {
		if (background == null) {
			return null;
		}
		int bgWidth = background.getWidth();
		int bgHeight = background.getHeight();
		int fgWidth = foreground.getWidth();
		int fgHeight = foreground.getHeight();
		Bitmap newmap = Bitmap
				.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(newmap);
		canvas.drawBitmap(background, 0, 0, null);
		canvas.drawBitmap(foreground, (bgWidth - fgWidth) / 2,
				(bgHeight - fgHeight) / 2 - 5
						* XidianYaoyaoApplication.phoneScale, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return newmap;
	}

	/**
	 * ��̬����ǡ����inSampleSize
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	/**
	 * �����ԭʼͼƬ�ĳ��ȺͿ��
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
