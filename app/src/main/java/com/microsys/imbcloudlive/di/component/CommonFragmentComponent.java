package com.microsys.imbcloudlive.di.component;


import com.microsys.imbcloudlive.di.FragmentScope;

import dagger.Component;

/**
 * ============================
 * 作    者：mlx
 * 创建日期：2018/1/22.
 * 描    述：通用fragment
 * 修改历史：
 * ===========================
 */

@FragmentScope
@Component(dependencies = AppComponent.class)
public interface CommonFragmentComponent {
}
