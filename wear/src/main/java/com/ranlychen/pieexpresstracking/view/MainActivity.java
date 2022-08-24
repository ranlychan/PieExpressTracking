package com.ranlychen.pieexpresstracking.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.ranlychen.pieexpresstracking.R;
import com.ranlychen.pieexpresstracking.adapter.KdwPackageInfoAdapter;
import com.ranlychen.pieexpresstracking.databinding.ActivityMainBinding;
import com.ranlychen.pieexpresstracking.entity.KdwRespBean;
import com.ranlychen.pieexpresstracking.entity.PiePackageItemBean;
import com.ranlychen.pieexpresstracking.network.AbsRxSubscriber;
import com.ranlychen.pieexpresstracking.service.LocalPackageDataService;
import com.ranlychen.pieexpresstracking.utils.Const;
import com.ranlychen.pieexpresstracking.view.base.BaseLiteBindingActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseLiteBindingActivity<ActivityMainBinding> {

    private List<PiePackageItemBean<KdwRespBean>> packageList;

    private KdwPackageInfoAdapter packageListAdapter;

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        packageList = new ArrayList<>();

        packageListAdapter = new KdwPackageInfoAdapter(packageList);

        packageListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                new KdwDetailDialog(
                        MainActivity.this,
                        packageList.get(position),
                        new NotifyItemChangedListener() {
                            @Override
                            public void notifyItemChanged() {
                                initData();
                            }
                        }).show();
            }
        });

        packageListAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                return false;
            }
        });

        try {
            final LayoutInflater inflater = LayoutInflater.from(this);
            View headView = inflater.inflate(R.layout.view_header, null, false);
            View footView = inflater.inflate(R.layout.view_footer, null, false);
            packageListAdapter.addHeaderView(headView);
            packageListAdapter.addFooterView(footView);
        } catch (Exception e){
            e.printStackTrace();
        }

        binding.rvPackageList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPackageList.setAdapter(packageListAdapter);
    }

    @Override
    public void initData() {
        if(this.isFinishing() || this.isDestroyed()){
            return;
        }
        LocalPackageDataService.queryAllKdwPackageDataList(new AbsRxSubscriber<List<PiePackageItemBean<KdwRespBean>>>() {
            @Override
            public void onNext(List<PiePackageItemBean<KdwRespBean>> piePackageItemBeans) {
                packageList.clear();
                packageList.addAll(piePackageItemBeans);
                binding.rvPackageList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        packageListAdapter.notifyDataSetChanged();
                    }
                }, 500);

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                ToastUtils.showLong("本地数据加载失败");
            }
        });
    }

    /**
     * 不用监听器实现按钮点击事件监听
     * 添加快递信息按钮
     */
    public void onAddBtnClick(View v) {
        Intent addItemPageIntent = new Intent(MainActivity.this, AddItemActivity.class);
        startActivityForResult(addItemPageIntent, Const.ACTIVITY_REQUEST_CODE.ITEM_ADD);
    }


    public void onLogoClick(View v) {
        Intent aboutPageIntent = new Intent(MainActivity.this, AppInfoActivity.class);
        startActivity(aboutPageIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == Const.ACTIVITY_REQUEST_CODE.ITEM_ADD) {
            if(resultCode == Const.ACTIVITY_RESULT_CODE.ITEM_ADD_SUCCESS){
                initData();
            } else if(resultCode == Const.ACTIVITY_RESULT_CODE.ITEM_ADD_FAIL){

            } else if(resultCode == Const.ACTIVITY_RESULT_CODE.ITEM_ADD_CANCEL){

            }
        }
    }

    interface NotifyItemChangedListener{
        void notifyItemChanged();
    }
}