package phone.ktv.adaters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import phone.ktv.fragments.AlreadyFragment;
import phone.ktv.fragments.RankingListFragment;
import phone.ktv.fragments.SongDeskFragment;

public class TabAdater extends FragmentPagerAdapter {

    public static final int oneTab = 0;
    public static final int twoTab = 1;
    public static final int threeTab = 2;
    public static Map<Integer, Fragment> cahceTab = new HashMap<>();
    private String tabTitle[];

    public TabAdater(String[] tabTitle, FragmentManager fm) {
        super(fm);
        this.tabTitle = tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = createTabFragment(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return tabTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }

    public static Fragment createTabFragment(int position) {
        Fragment baseTab = null;
        if (cahceTab.containsKey(position)) {
            baseTab = cahceTab.get(position);
            return baseTab;
        }
        switch (position) {
            case oneTab:
                baseTab = new SongDeskFragment();
                break;
            case twoTab:
                baseTab = new RankingListFragment();
                break;
            case threeTab:
                baseTab = new AlreadyFragment();
                break;
            default:
                break;
        }
        cahceTab.put(position, baseTab);
        return baseTab;
    }
}
