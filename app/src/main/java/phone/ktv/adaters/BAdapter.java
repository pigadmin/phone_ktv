package phone.ktv.adaters;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @des BaseAdapter抽取的基类
 */
public abstract class BAdapter<T> extends BaseAdapter {

    private List<T> list;
    private int layoutId;
    private LayoutInflater inflate;


    public BAdapter(Context context, int layoutId, List<T> list) {
        this.inflate = LayoutInflater.from(context);
        this.layoutId = layoutId;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflate.inflate(layoutId, null);
        }
        onInitView(convertView, position);
        return convertView;
    }

    /**
     * @param convertView 加载的视图
     * @param position
     * @des 视图和数据的绑定
     */
    public abstract void onInitView(View convertView, int position);


    public <E extends View> E get(View view, int resId) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(resId);
        if (childView == null) {
            childView = view.findViewById(resId);
            viewHolder.put(resId, childView);
        }
        return (E) childView;
    }

    public List<T> getAllData() {

        return list;
    }
}
