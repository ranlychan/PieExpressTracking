package com.ranlychen.pieexpresstracking.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
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
import com.ranlychen.pieexpresstracking.view.base.BaseBindingActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {

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
                new KdwDetailDialog(MainActivity.this, packageList.get(position)).show();
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
        binding.rvPackageList.post(new Runnable() {
            @Override
            public void run() {
                LocalPackageDataService.queryAllKdwPackageDataList(new AbsRxSubscriber<List<PiePackageItemBean<KdwRespBean>>>() {
                    @Override
                    public void onNext(List<PiePackageItemBean<KdwRespBean>> piePackageItemBeans) {
                        ToastUtils.showLong("本地数据加载成功");
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
        });
    }

    /**
     * 不用监听器实现按钮点击事件监听
     * 添加快递信息按钮
     */
    public void onAddBtnClick(View v) {
        Intent addItemPageIntent = new Intent(MainActivity.this, AddItemActivity.class);
        startActivity(addItemPageIntent);
    }


    public void onLogoClick(View v) {
        Intent aboutPageIntent = new Intent(MainActivity.this, AppInfoActivity.class);
        startActivity(aboutPageIntent);
    }
}