package com.zjwocai.qundui.util;

import android.content.Context;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zjwocai.qundui.R;

/**
 * 下拉刷新工具类
 * Created by NieLiQin on 2016/7/31.
 */
public class RefreshUtil {
    public static void setPullText(Context context, PullToRefreshBase lv) {
        ILoadingLayout startLoading = lv.getLoadingLayoutProxy(
                true, false);
        startLoading.setPullLabel(context.getString(R.string.pull_to_refresh_pull_label));
        startLoading.setRefreshingLabel(context.getString(R.string.pull_to_refresh_refreshing_label));
        startLoading.setReleaseLabel(context.getString(R.string.pull_to_refresh_release_label));

        ILoadingLayout endLoading = lv.getLoadingLayoutProxy(false,
                true);
        endLoading.setPullLabel(context.getString(R.string.pull_to_refresh_footer_pull_label));
        endLoading.setRefreshingLabel(context.getString(R.string.pull_to_refresh_footer_refreshing_label));
        endLoading.setReleaseLabel(context.getString(R.string.pull_to_refresh_footer_release_label));
    }
}
