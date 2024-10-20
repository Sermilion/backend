package com.yappeer.presentation.plugins

import com.yappeer.presentation.routes.feature.onboarding.LoginRoute
import com.yappeer.presentation.routes.feature.onboarding.RefreshTokenRoute
import com.yappeer.presentation.routes.feature.onboarding.RegistrationRoute
import com.yappeer.presentation.routes.feature.onboarding.loginRoute
import com.yappeer.presentation.routes.feature.onboarding.refreshTokenRoute
import com.yappeer.presentation.routes.feature.onboarding.registrationRoute
import com.yappeer.presentation.routes.feature.profile.FollowersUsersRoute
import com.yappeer.presentation.routes.feature.profile.FollowingTagsRoute
import com.yappeer.presentation.routes.feature.profile.FollowingUserRoute
import com.yappeer.presentation.routes.feature.profile.SelfProfileRoute
import com.yappeer.presentation.routes.feature.profile.UserProfileRoute
import com.yappeer.presentation.routes.feature.profile.followersUsersRoute
import com.yappeer.presentation.routes.feature.profile.followingTagsRoute
import com.yappeer.presentation.routes.feature.profile.followingUsersRoute
import com.yappeer.presentation.routes.feature.profile.selfProfileRoute
import com.yappeer.presentation.routes.feature.profile.userProfileRoute
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.resources.Resources
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    install(Resources)
    configureAuthentication()
    configureErrorHandling()

    routing {
        openAPI(path = "openapi", swaggerFile = "config/openapi/documentation.yaml")
        post(LoginRoute) { loginRoute(call) }
        post(RegistrationRoute) { registrationRoute(call) }

        authenticate(AuthenticationIdentifier) {
            get(UserProfileRoute) { userProfileRoute(call) }
            get(SelfProfileRoute) { selfProfileRoute(call) }
            get(RefreshTokenRoute) { refreshTokenRoute(call) }
            post(FollowersUsersRoute) { followersUsersRoute(call) }
            post(FollowingUserRoute) { followingUsersRoute(call) }
            post(FollowingTagsRoute) { followingTagsRoute(call) }
        }
    }
}
