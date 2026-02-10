package com.example.aac.data.mapper

import com.example.aac.data.remote.dto.MyInfoResponse
import com.example.aac.domain.model.MyInfo

fun MyInfoResponse.toMyInfo(): MyInfo? {
    if (!success || data == null) return null

    return MyInfo(
        id = data.id,
        nickname = data.nickname,
        email = data.email,
        accountType = data.accountType
    )
}
