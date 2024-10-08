package com.yappeer.domain.onboarding.model.value

class ValueValidationException(
    val valueType: String,
) : Exception("Validation for value type $valueType failed.")
