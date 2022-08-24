package com.ranlychen.pieexpresstracking.entity

import com.ranlychen.pieexpresstracking.R


/**快递网**/
enum class KdwExpCompanyEnum(val companyCode: String, val companyName: String, val companyIconResId: Int) {
    aae("aae","AAE快递", R.drawable.ic_full_cancel),
    aramex("aramex","Aramex快递", R.drawable.ic_full_cancel),
    annengwuliu("annengwuliu","安能物流快递", R.drawable.ic_full_cancel),
    huitongkuaidi("huitongkuaidi","百世汇通快递",R.drawable.ic_full_cancel),
    ems("ems","EMS快递",R.drawable.ic_full_cancel),
    emsguoji("emsguoji","EMS国际",R.drawable.ic_full_cancel),
    fedex("fedex","FedEx（国际）",R.drawable.ic_full_cancel),
    jd("jd","京东快递",R.drawable.ic_full_cancel),
    lianbangkuaidi("lianbangkuaidi","联邦快递",R.drawable.ic_full_cancel),
    shentong("shentong","申通快递", R.drawable.com_sto),
    shunfeng("shunfeng","顺丰速运",R.drawable.com_sf),
    tiantian("tiantian","天天快递",R.drawable.ic_full_cancel),
    yuantong("yuantong","圆通快递",R.drawable.com_yto),
    yunda("yunda","韵达快运",R.drawable.ic_full_cancel),
    yuntongkuaidi("yuntongkuaidi","运通快递",R.drawable.ic_full_cancel),
    youzhengguonei("youzhengguonei","邮政国内",R.drawable.ic_full_cancel),
    youzhengguoji("youzhengguoji","邮政国际",R.drawable.ic_full_cancel),
    zhongtong("zhongtong","中通快递",R.drawable.com_zto);

    companion object : EnumCompanion<String, KdwExpCompanyEnum>(KdwExpCompanyEnum.values().associateBy(KdwExpCompanyEnum::companyName))
}
