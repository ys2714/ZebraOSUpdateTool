package com.zebra.emdk_kotlin_wrapper.zdm

import androidx.annotation.Keep

@Keep
object ZDMTokenStore {

    private var dwQueryAccessToken: String = ""
    private var dwRuntimeAccessToken: String = ""
    private var dwNotificationAccessToken: String = ""
    private var dwConfigAccessToken: String = ""

    @Keep
    fun saveToken(scope: ZDMConst.DelegationScope, accessToken: String?) {
        if (accessToken == null) return
        when (scope) {
            ZDMConst.DelegationScope.SCOPE_DW_QUERY_API -> {
                dwQueryAccessToken = accessToken
            }
            ZDMConst.DelegationScope.SCOPE_DW_RUNTIME_API -> {
                dwRuntimeAccessToken = accessToken
            }
            ZDMConst.DelegationScope.SCOPE_DW_NOTIFICATION_API -> {
                dwNotificationAccessToken = accessToken
            }
            ZDMConst.DelegationScope.SCOPE_DW_CONFIG_API -> {
                dwConfigAccessToken = accessToken
            }
        }
    }

    @Keep
    fun getToken(scope: ZDMConst.DelegationScope) : String {
        when (scope) {
            ZDMConst.DelegationScope.SCOPE_DW_QUERY_API -> {
                return dwQueryAccessToken
            }
            ZDMConst.DelegationScope.SCOPE_DW_RUNTIME_API -> {
                return dwRuntimeAccessToken
            }
            ZDMConst.DelegationScope.SCOPE_DW_NOTIFICATION_API -> {
                return dwNotificationAccessToken
            }
            ZDMConst.DelegationScope.SCOPE_DW_CONFIG_API -> {
                return dwConfigAccessToken
            }
        }
    }
}