package com.ranlychen.pieexpresstracking.adapter;


import android.content.Context;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ranlychen.pieexpresstracking.R;
import com.ranlychen.pieexpresstracking.entity.KdwExpCompanyEnum;
import com.ranlychen.pieexpresstracking.entity.KdwRespBean;
import com.ranlychen.pieexpresstracking.entity.PiePackageItem;

import java.util.ArrayList;
import java.util.List;


public class CompanySpinnerAdapter extends BaseQuickAdapter<KdwExpCompanyEnum, BaseViewHolder> {

    public CompanySpinnerAdapter(@Nullable List<KdwExpCompanyEnum> data) {
        super(R.layout.item_spinner_company, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, KdwExpCompanyEnum kdwExpCompanyEnum) {
        baseViewHolder.setText(R.id.tv_company_name, kdwExpCompanyEnum.getCompanyName());
    }
}

