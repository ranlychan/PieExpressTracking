package com.ranlychen.pieexpresstracking.adapter;


import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ranlychen.pieexpresstracking.R;
import com.ranlychen.pieexpresstracking.entity.KdwRespBean;
import com.ranlychen.pieexpresstracking.entity.PiePackageItemBean;
import com.ranlychen.pieexpresstracking.factory.PackageCompanyIconFactory;
import com.ranlychen.pieexpresstracking.factory.PackageItemOverviewFactory;

import java.util.List;

public class KdwPackageInfoAdapter extends BaseQuickAdapter<PiePackageItemBean<KdwRespBean>, BaseViewHolder> {

    PackageItemOverviewFactory<KdwRespBean> overviewFactory = new PackageItemOverviewFactory<>();
    PackageCompanyIconFactory<KdwRespBean> iconFactory = new PackageCompanyIconFactory<>();

    /**
     * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
     * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
     */
    public KdwPackageInfoAdapter(List<PiePackageItemBean<KdwRespBean>> list) {
        super(R.layout.list_item, list);
    }

    /**
     * 在此方法中设置item数据
     */
    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, PiePackageItemBean<KdwRespBean> piePackageItemBean) {
        baseViewHolder.setText(R.id.item_name, piePackageItemBean.getLocalInfoBean().getMarkName())
                .setText(R.id.item_exp_num, piePackageItemBean.getLocalInfoBean().getLogisticCode())
                .setText(R.id.item_exp_info, overviewFactory.getPackageItemOverview(piePackageItemBean.getOnlineInfoBean()));
        baseViewHolder.setImageResource(R.id.item_exp_icon, iconFactory.getComIconResId(piePackageItemBean));
    }
}