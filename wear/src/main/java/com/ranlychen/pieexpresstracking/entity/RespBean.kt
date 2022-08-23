package com.ranlychen.pieexpresstracking.entity

import java.lang.reflect.Type
import java.time.chrono.JapaneseEra.values

/**快递网接口返回 www.kuaidi.com**/
data class KdwRespBean(var success: Boolean?, var reason: String?, var data: List<KdwTraceBean>?, var status: Int?){
    constructor() : this(null, null,null,null) {
    }
}

data class KdwTraceBean(var time: String, var context: String)

enum class KdwTraceLineEnum(val traceLine: Int){
    MULTI_LINE(0),    //返回单行信息
    SINGLE_LINE(1)   //返回完整多行信息
}

data class KdwNumberList(var list: List<String>?) {
    constructor() : this(null)
}

enum class KdwStatusEnum(val statusCode: Int, val statusName: String, val explanation: String){
    NO_RESULT(0, "暂无结果", "物流单号暂无结果"),
    ON_TRANSPORT(3, "在途", "快递处于运输过程中"),
    ON_PICK_UP(4, "揽件", "快递已被快递公司揽收并产生了第一条信息"),
    TRANSPORT_PROBLEM(5, "疑难", "快递邮寄过程中出现问题"),
    SIGNED(6, "签收", "收件人已签收"),
    SIGN_BACK(7, "退签", "快递因用户拒签、超区等原因退回，而且发件人已经签收"),
    ON_DELIVERY(8, "派件", "快递员正在同城派件"),
    ON_REFUND(9, "退回", "货物处于退回发件人途中");

    /*
    0:物流单号暂无结果；
    3:在途，快递处于运输过程中；
    4:揽件，快递已被快递公司揽收并产生了第一条信息；
    5:疑难，快递邮寄过程中出现问题；
    6:签收，收件人已签收；
    7:退签，快递因用户拒签、超区等原因退回，而且发件人已经签收；
    8:派件，快递员正在同城派件；
    9:退回，货物处于退回发件人途中；
    */

    companion object : EnumCompanion<Int, KdwStatusEnum>(KdwStatusEnum.values().associateBy(KdwStatusEnum::statusCode))

}

open class EnumCompanion<T, V>(private val valueMap: Map<T, V>) {
    fun fromInt(type: T) = valueMap[type]
}