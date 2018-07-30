package phone.ktv.tootls;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import org.xutils.common.util.DensityUtil;


/**
 * @author quan zi
 * @time 2016/9/1 16:50
 * @des 1.PupopWindow的工具类
 */
public class PupopWindowUtils {

//    /**
//     * @des 显示一个指定视图下的pw的内容列表
//     */
//    public static PopupWindow showPw(Context context, ListView list, View view, String[] data) {
//        PopupWindow popupWindow = new PopupWindow(view, DensityUtil.dip2px(context, 130), LinearLayout.LayoutParams.WRAP_CONTENT);
//        popupWindow.setContentView(list);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.pw_text, data);
//        list.setAdapter(adapter);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.setFocusable(true);
//        popupWindow.showAsDropDown(view, DensityUtil.dip2px(context, -80), 0);
//        return popupWindow;
//    }
}
