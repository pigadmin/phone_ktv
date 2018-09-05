package phone.ktv.tootls;

import android.view.View;

import phone.ktv.R;
import phone.ktv.views.BtmDialog;

/**
 * AlertDialog工具类
 */
public class AlertDialogHelper {
    /**
     * @param dialog          dialog
     * @param isState         方法是否暴露下外部
     * @param canle           Dialog显示时,返回键是否生效
     * @param confirmlistener 确定事件
     * @param cancelistener   取消事件
     */
    public static void BtmDialogDerive1(final BtmDialog dialog, boolean isState, boolean canle, View.OnClickListener confirmlistener, View.OnClickListener cancelistener) {
        if (isState) {
            dialog.cancel.setOnClickListener(cancelistener);
        } else {
            dialog.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        dialog.setCancelable(canle);
        dialog.confirm.setOnClickListener(confirmlistener);
        dialog.show();
    }

    /**
     * @param dialog          dialog
     * @param isState         方法是否暴露下外部
     * @param confirmlistener 确定事件
     */
    public static void BtmDialogDerive2(final BtmDialog dialog, boolean isState, boolean canle, View.OnClickListener confirmlistener) {
        if (isState) {
            dialog.confirm.setOnClickListener(confirmlistener);
        } else {
            dialog.confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        dialog.setCancelable(canle);
        dialog.cancel.setVisibility(View.GONE);
        dialog.confirm.setBackgroundResource(R.drawable.selector_back3_shape);
        dialog.show();
    }
}
