package phone.ktv.tootls;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import phone.ktv.R;


/**
 * @描述	     1.各种不同场景的Toast效果
 */
public class ToastUtils {
	private static Toast mToast;
	/**
	 * @des 短暂显示Toast消息
	 */
	public static void showShortToast(Context context, String message) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast, null);
		TextView text = (TextView) view.findViewById(R.id.toast_message);
		text.setText(message);
		if (mToast != null)
			mToast.cancel();
		mToast = new Toast(context);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.setGravity(Gravity.BOTTOM, 0, OtherUtils.dip2px(context,50));
		mToast.setView(view);
		mToast.show();
	}

	/**
	 * @des 长时间显示Toast消息
	 */
	public static void showLongToast(Context context, String message) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast, null);
		TextView text = (TextView) view.findViewById(R.id.toast_message);
		text.setText(message);
		if (mToast != null)
			mToast.cancel();
		mToast = new Toast(context);
		mToast.setDuration(Toast.LENGTH_LONG);
		mToast.setGravity(Gravity.BOTTOM, 0, OtherUtils.dip2px(context,50));
		mToast.setView(view);
		mToast.show();
	}
	public static void showLongToast(Context context, @StringRes int msgId) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast, null);
		TextView text = (TextView) view.findViewById(R.id.toast_message);
		text.setText(msgId);
		if (mToast != null)
			mToast.cancel();
		mToast = new Toast(context);
		mToast.setDuration(Toast.LENGTH_LONG);
		mToast.setGravity(Gravity.BOTTOM, 0, OtherUtils.dip2px(context,50));
		mToast.setView(view);
		mToast.show();
	}
}
