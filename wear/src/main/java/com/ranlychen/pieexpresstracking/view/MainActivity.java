package com.ranlychen.pieexpresstracking.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.ranlychen.pieexpresstracking.BaseSpeechActivity;
import com.ranlychen.pieexpresstracking.R;
import com.ranlychen.pieexpresstracking.adapter.KdwPackageInfoAdapter;
import com.ranlychen.pieexpresstracking.entity.KdwRespBean;
import com.ranlychen.pieexpresstracking.entity.PiePackageItem;
import com.ranlychen.pieexpresstracking.network.AbsRxSubscriber;
import com.ranlychen.pieexpresstracking.service.PackageService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseSpeechActivity {


    private View headView;
    private View footView;
    private Intent aboutpage;
    private PieAddItemDialog additem;
    private int editviewname;

    /**new**/
    private RecyclerView rvPackageList;
    private KdwPackageInfoAdapter packageListAdapter;
    private List<PiePackageItem<KdwRespBean>> packageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void bindView() {
        rvPackageList = findViewById(R.id.rv_package_list);

        final LayoutInflater inflater = LayoutInflater.from(this);
        headView = inflater.inflate(R.layout.view_header, null, false);
        footView = inflater.inflate(R.layout.view_footer, null, false);
    }

    @Override
    protected void onViewBound() {
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
            public boolean onItemLongClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                return false;
            }
        });
        packageListAdapter.addHeaderView(headView);
        packageListAdapter.addFooterView(footView);

        rvPackageList.setLayoutManager(new LinearLayoutManager(this));
        rvPackageList.setAdapter(packageListAdapter);
    }

    @Override
    protected void initData() {
        aboutpage = new Intent(MainActivity.this, AppInfoActivity.class);
        PackageService.queryLocalPackageDetailList(new AbsRxSubscriber<List<PiePackageItem<KdwRespBean>>>() {
            @Override
            public void onNext(List<PiePackageItem<KdwRespBean>> kdwRespBeans) {
                ToastUtils.showLong("本地数据加载成功");
                packageListAdapter.setDiffNewData(kdwRespBeans);
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
    public void footadd(View v) {
        additem = new PieAddItemDialog(MainActivity.this);
        additem.setCancel(new PieAddItemDialog.IOnCancelListener() {
            @Override
            public void onCancel(PieAddItemDialog dialog) {
                Toast.makeText(MainActivity.this,getString(R.string.addfail),Toast.LENGTH_SHORT).show();
            }
        });
        additem.setConfirm(new PieAddItemDialog.IOnConfirmListener() {
            @Override
            public void onConfirm(boolean isSuccess) {

            }
        });
        additem.setVoiceListenerForName(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("VoiceInputStart,Name");
                startVoiceInput();
            }
        });
        additem.setVoiceListenerForNo(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("VoiceInputStart,No");
                startVoiceInput();
            }
        });
        additem.show();

    }

    public void footUpDate(View v) {
        Toast.makeText(MainActivity.this, "刷新成功！",Toast.LENGTH_SHORT).show();
    }

    public void logo(View v) {
        startActivity(aboutpage);
    }

    @Override
    public void onRecognitionSuccess(String s) {
        additem.setEditViewText(editviewname,s);
    }

    @Override
    public void onRecognitionFailed() {

    }
}