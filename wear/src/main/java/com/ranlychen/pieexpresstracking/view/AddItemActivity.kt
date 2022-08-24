package com.ranlychen.pieexpresstracking.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.SnackbarUtils.dismiss
import com.blankj.utilcode.util.ToastUtils
import com.ranlychen.pieexpresstracking.R
import com.ranlychen.pieexpresstracking.databinding.ActivityAddItemBinding
import com.ranlychen.pieexpresstracking.entity.KdwExpCompanyEnum
import com.ranlychen.pieexpresstracking.entity.KdwRespBean
import com.ranlychen.pieexpresstracking.entity.PiePackageItemBean
import com.ranlychen.pieexpresstracking.network.AbsRxSubscriber
import com.ranlychen.pieexpresstracking.service.LocalPackageDataService
import com.ranlychen.pieexpresstracking.service.PackageService
import com.ranlychen.pieexpresstracking.utils.Const
import com.ranlychen.pieexpresstracking.utils.L2UReplacementTransformationMethod
import com.ranlychen.pieexpresstracking.utils.StringUtil
import com.ranlychen.pieexpresstracking.view.base.BaseBindingSpeechRecogActivity

class AddItemActivity : BaseBindingSpeechRecogActivity<ActivityAddItemBinding>() {

    override fun initContentView(): Int {
        return R.layout.activity_add_item
    }

    override fun initView() {
        binding.btnConfirm.setOnClickListener(this::onConfirmClick)
        binding.btnCancel.setOnClickListener(this::onCancelClick)
        binding.edtMarkName.setTransformationMethod(L2UReplacementTransformationMethod())
        binding.civVoiceMarkName.setOnClickListener(this::onVoiceInputMarkNameClick)
        binding.civVoiceExpNumber.setOnClickListener(this::onVoiceInputExpNumberClick)
    }

    override fun initData() {

    }

    private fun onConfirmClick(view: View) {
        var markName: String = binding.edtMarkName.text.toString().trim()
        val expNumber = StringUtil.toUpperCase(binding.edtExpNumber.text.toString().trim())
        val companyName: String = binding.spCompanyList.selectedItem.toString()
        val companyCode: String
        val companyEnum = KdwExpCompanyEnum.fromString(companyName)

        companyCode = if (companyEnum != null) {
            companyEnum.companyCode
        } else {
            ToastUtils.showLong("请选择正确的快递公司")
            return
        }

        //默认备注名
        if (TextUtils.isEmpty(markName)) {
            markName = resources.getString(R.string.default_mark_name)
        }

        if (TextUtils.isEmpty(expNumber)) {
            ToastUtils.showLong("快递单号不能为空")
            return
        }

        if (TextUtils.isEmpty(companyCode)) {
            ToastUtils.showLong("请选择快递公司")
            return
        }

        try {
            Log.d(TAG, "markName:${markName},expNumber:${expNumber},companyName:${companyName},companyCode:${companyCode}")
            PackageService.queryPackageDetail(
                markName,
                companyCode,
                expNumber,
                object : AbsRxSubscriber<PiePackageItemBean<KdwRespBean>>() {
                    override fun onNext(item: PiePackageItemBean<KdwRespBean>) {

                        LocalPackageDataService.saveKdwPackageData(item.localInfoBean, object : AbsRxSubscriber<Boolean>(){
                            override fun onNext(t: Boolean?) {
                                addItemSuccess()
                            }

                            override fun onError(throwable: Throwable?) {
                                super.onError(throwable)
                                addItemFailed("本地缓存失败")
                                Log.d(TAG,throwable.toString())
                            }
                        })
                    }

                    override fun onError(throwable: Throwable) {
                        super.onError(throwable)
                        addItemFailed("快递查询失败")
                    }
                })

        } catch (e: Exception) {
            e.printStackTrace()
        }
        dismiss()
    }

    private fun addItemSuccess(){
        val intent: Intent = Intent()
        val bundle: Bundle  = Bundle()
//        bundle.putString("result", "data")
        setResult(Const.ACTIVITY_RESULT_CODE.ITEM_ADD_SUCCESS, intent.putExtras(bundle))
        finish()
        ToastUtils.showLong("添加成功")
    }

    private fun addItemFailed(reason: String){
        val intent: Intent = Intent()
        val bundle: Bundle  = Bundle()
//        bundle.putString("result", "data")
        setResult(Const.ACTIVITY_RESULT_CODE.ITEM_ADD_FAIL, intent.putExtras(bundle))
        finish()
        ToastUtils.showLong("添加失败:$reason")
    }

    private fun onCancelClick(view: View) {
        finish()
        ToastUtils.showShort("已取消添加")
    }

    private fun onVoiceInputMarkNameClick(view: View) {
        startVoiceInput(object : OnRecognitionCompleteListener {
            override fun onRecognitionSuccess(result: String?) {
                result?.let {
                    binding.edtMarkName.setText(result)
                }
            }

            override fun onRecognitionFailed() {
                ToastUtils.showShort("识别失败")
            }

        })
    }

    private fun onVoiceInputExpNumberClick(view: View) {
        startVoiceInput(object : OnRecognitionCompleteListener {
            override fun onRecognitionSuccess(result: String?) {
                result?.let {
                    binding.edtExpNumber.setText(result)
                }
            }

            override fun onRecognitionFailed() {
                ToastUtils.showShort("识别失败")
            }

        })
    }
}