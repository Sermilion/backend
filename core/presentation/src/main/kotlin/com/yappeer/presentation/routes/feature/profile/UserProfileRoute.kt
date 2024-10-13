package com.yappeer.presentation.routes.feature.profile

import com.yappeer.domain.onboarding.model.value.ValueValidationException
import com.yappeer.domain.onboarding.repository.OnboardingRepository
import com.yappeer.presentation.routes.model.mapper.ResponseMapper.toUiModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingCall
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import java.util.UUID

internal const val UserProfileRoute = "/user_profile"
private const val UserIdParam = "user_id"

suspend fun Route.userProfileRoute(call: RoutingCall) {
    val onboardingRepository: OnboardingRepository by inject()

    val logger = LoggerFactory.getLogger(SelfProfileRoute)

    val userId = call.request.queryParameters[UserIdParam]?.let {
        UUID.fromString(it)
    }

    try {
        if (userId != null) {
            val result = onboardingRepository.findUser(userId)

            if (result != null) {
                val profile = result.toUiModel()
                call.respond(HttpStatusCode.OK, profile)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        } else {
            val message = "User not found."
            logger.info(message)
            call.respond(HttpStatusCode.NotFound, message)
        }
    } catch (e: ValueValidationException) {
        val message = "Validation error for value type ${e.valueType}"
        logger.error(message, e)
        call.respond(HttpStatusCode.BadRequest, message)
    }
}