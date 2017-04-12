package com.zjwocai.qundui.activity.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jph.takephoto.app.TakePhotoFragment;
import com.threeti.teamlibrary.widgets.CommTitleBar;

import java.io.Serializable;

/**
 * Fragment基础类
 */

@SuppressWarnings("WeakerAccess")
public abstract class MyBaseFragment extends TakePhotoFragment {
    protected View mRoot;
    protected Bundle mBundle;
    protected boolean isVisible;
    public CommTitleBar title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        initBundle(mBundle);
    }

    /**
     * initTitle:初始化标题. <br/>
     *
     * @param id
     * @author BaoHang
     */
    public void initTitle(int id) {
        if (title == null) {
            title = new CommTitleBar(getActivity());
        }
        title.setTitle(id);
    }


    /**
     * initTitle:初始化标题. <br/>
     *
     * @param name
     * @author BaoHang
     */
    public void initTitle(String name) {
        if (title == null) {
            title = new CommTitleBar(getActivity());
        }
        title.setTitle(name);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRoot != null) {
            ViewGroup parent = (ViewGroup) mRoot.getParent();
            if (parent != null)
                parent.removeView(mRoot);
        } else {
            mRoot = inflater.inflate(getLayoutId(), container, false);
            onBindViewBefore(mRoot);
            if (savedInstanceState != null)
                onRestartInstance(savedInstanceState);
            initWidget(mRoot);
            //            initData();
        }
        return mRoot;
    }


    protected void onBindViewBefore(View root) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mBundle = null;
    }

    protected abstract int getLayoutId();

    protected void initBundle(Bundle bundle) {

    }

    protected void initWidget(View root) {

    }

    //    protected void initData() {
    //
    //    }

    protected <T extends View> T findView(int viewId) {
        return (T) mRoot.findViewById(viewId);
    }

    protected <T extends Serializable> T getBundleSerializable(String key) {
        if (mBundle == null) {
            return null;
        }
        return (T) mBundle.getSerializable(key);
    }


    protected void onRestartInstance(Bundle bundle) {

    }
}