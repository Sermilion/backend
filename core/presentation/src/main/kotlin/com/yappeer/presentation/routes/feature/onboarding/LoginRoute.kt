package com.yappeer.presentation.routes.feature.onboarding

import com.yappeer.domain.onboarding.model.value.ValueValidationException
import com.yappeer.domain.onboarding.repository.OnboardingRepository
import com.yappeer.domain.onboarding.security.UserAuthenticationService
import com.yappeer.presentation.routes.model.param.LoginParams
import com.yappeer.presentation.routes.model.result.ErrorResponse
import com.yappeer.presentation.routes.model.ui.TokenUiModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingCall
import kotlinx.datetime.Clock
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory

internal const val LoginRoute = "/login"
private const val ErrorTypeInvalidCredentials = "InvalidCredentials"

suspend fun Route.loginRoute(call: RoutingCall) {
    val onboardingRepository: OnboardingRepository by inject()
    val userAuthenticationService: UserAuthenticationService by inject()

    val logger = LoggerFactory.getLogger(LoginRoute)
    val params = call.receive<LoginParams>()

    try {
        val passwordHash = onboardingRepository
            .findPassword(params.emailValue)
            ?: return call.respond(HttpStatusCode.BadRequest, createInvalidCredentialsError())
        val user = onboardingRepository.findUser(params.emailValue)

        val passwordMatches = userAuthenticationService.verifyPassword(params.passwordValue, passwordHash)
        if (passwordMatches && user != null) {
            val accessToken = userAuthenticationService.generateAccessToken(user.id)
            val refreshToken = userAuthenticationService.generateRefreshToken(user.id)
            val response = TokenUiModel(accessToken = accessToken, refreshToken = refreshToken)
            onboardingRepository.updateLastLogin(user.id, Clock.System.now())
            call.respond(HttpStatusCode.OK, response)
        } else {
            call.respond(HttpStatusCode.BadRequest, createInvalidCredentialsError())
        }
    } catch (e: ValueValidationException) {
        logger.error("Validation error for value type ${e.valueType}", e)
        call.respond(HttpStatusCode.BadRequest, createInvalidCredentialsError())
    }
}

private fun createInvalidCredentialsError(): ErrorResponse {
    return ErrorResponse(
        code = ErrorTypeInvalidCredentials,
        details = emptyList(),
    )
}
