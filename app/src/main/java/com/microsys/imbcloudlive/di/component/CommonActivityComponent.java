package com.microsys.imbcloudlive.di.component;


import com.microsys.imbcloudlive.di.ActivityScope;

import dagger.Component;

/**
 * ============================
 * 作    者：mlx
 * 创建日期：2018/1/16.
 * 描    述：通用Activity
 * 修改历史：
 * ===========================
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface CommonActivityComponent {

}
