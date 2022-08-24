package com.ranlychen.pieexpresstracking.view.base;


/**
 * 拥有生命周期的对象接口，提供对{@link LifeCycleManager}的管理
 *
 * @author carlosliu on 2019/1/11.
 */
public interface LifeCycleHolder {
    /**
     * 获取holder所持有的lifeCycleManager对象
     *
     * @return 当前对象的LifeCycleManager
     */
    LifeCycleManager getLifecycleManager();
}
