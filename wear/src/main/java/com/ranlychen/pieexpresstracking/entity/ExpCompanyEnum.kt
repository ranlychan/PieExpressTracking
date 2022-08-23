package com.ranlychen.pieexpresstracking.entity

import com.ranlychen.pieexpresstracking.R

open class ExpCompanyEnum(){

}

/**快递网**/
enum class KdwExpCompanyEnum(val companyCode: String, val companyName: String, val companyIconResId: Int) {
    aae("aae","AAE快递",R.mipmap.ic_launcher_foreground),
    aramex("aramex","Aramex快递",R.mipmap.ic_launcher_foreground),
    annengwuliu("annengwuliu","安能物流快递", R.mipmap.ic_launcher_foreground),
    huitongkuaidi("huitongkuaidi","百世汇通快递",R.mipmap.ic_launcher_foreground),
    ems("ems","EMS快递",R.mipmap.ic_launcher_foreground),
    emsguoji("emsguoji","EMS国际",R.mipmap.ic_launcher_foreground),
    fedex("fedex","FedEx（国际）",R.mipmap.ic_launcher_foreground),
    jd("jd","京东快递",R.mipmap.ic_launcher_foreground),
    lianbangkuaidi("lianbangkuaidi","联邦快递",R.mipmap.ic_launcher_foreground),
    shentong("shentong","申通快递", R.drawable.com_sto),
    shunfeng("shunfeng","顺丰速运",R.drawable.com_sf),
    tiantian("tiantian","天天快递",R.mipmap.ic_launcher_foreground),
    yuantong("yuantong","",R.drawable.com_yto),
    yunda("yunda","韵达快运",R.mipmap.ic_launcher_foreground),
    yuntongkuaidi("yuntongkuaidi","运通快递",R.mipmap.ic_launcher_foreground),
    youzhengguonei("youzhengguonei","邮政国内",R.mipmap.ic_launcher_foreground),
    youzhengguoji("youzhengguoji","邮政国际",R.mipmap.ic_launcher_foreground),
    zhongtong("zhongtong","中通快递",R.drawable.com_zto);
}
