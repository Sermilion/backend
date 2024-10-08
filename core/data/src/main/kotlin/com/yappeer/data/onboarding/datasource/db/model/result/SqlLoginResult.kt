package com.yappeer.data.onboarding.datasource.db.model.result

import java.util.UUID

sealed interface SqlLoginResult {
    data class Success(val userId: UUID) : SqlLoginResult
    data object CredentialsError : SqlLoginResult
    data object UnknownError : SqlLoginResult
}
