package com.yappeer.presentation.routes.model.mapper

import com.yappeer.domain.content.model.FollowersResult
import com.yappeer.presentation.routes.model.mapper.UserResponseMapper.toUiModel
import com.yappeer.presentation.routes.model.ui.FollowersUiModel

internal object FollowersResponseMapper {

    fun FollowersResult.Data.toUiModel(): FollowersUiModel {
        return FollowersUiModel(
            users = this.users.map { it.toUiModel() },
            totalUserCount = this.totalUserCount,
            pagesCount = this.pagesCount,
            currentPage = this.currentPage,
        )
    }
}