package com.example.emptyapp.core.common.result

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ApiResultTest {

    @Test
    fun `map transforms a success value`() {
        val result: ApiResult<Int> = ApiResult.Success(2)

        val mapped = result.map { it * 10 }

        assertEquals(ApiResult.Success(20), mapped)
    }

    @Test
    fun `map keeps a failure untouched`() {
        val error = ApiError.Timeout
        val result: ApiResult<Int> = ApiResult.Failure(error)

        val mapped = result.map { it * 10 }

        assertEquals(ApiResult.Failure(error), mapped)
    }

    @Test
    fun `fold selects the success branch`() {
        val value = ApiResult.Success("ok").fold(
            onSuccess = { "success:$it" },
            onFailure = { "failure" },
        )

        assertEquals("success:ok", value)
    }

    @Test
    fun `fold selects the failure branch`() {
        val value = ApiResult.Failure(ApiError.Server(500)).fold(
            onSuccess = { "success" },
            onFailure = { "failure:${it.javaClass.simpleName}" },
        )

        assertEquals("failure:Server", value)
    }

    @Test
    fun `onSuccess runs only for success`() {
        var invoked = false
        ApiResult.Success(1).onSuccess { invoked = true }
        ApiResult.Failure(ApiError.NoNetwork).onSuccess { invoked = false }

        assertEquals(true, invoked)
    }

    @Test
    fun `onFailure runs only for failure`() {
        var captured: ApiError? = null
        ApiResult.Success(1).onFailure { captured = it }
        assertNull(captured)

        ApiResult.Failure(ApiError.Forbidden).onFailure { captured = it }
        assertEquals(ApiError.Forbidden, captured)
    }

    @Test
    fun `getOrNull returns data or null`() {
        assertEquals(5, ApiResult.Success(5).getOrNull())
        assertNull(ApiResult.Failure(ApiError.Unknown()).getOrNull())
    }

    @Test
    fun `getOrElse falls back on failure`() {
        assertEquals(7, ApiResult.Failure(ApiError.Timeout).getOrElse { 7 })
        assertEquals(1, ApiResult.Success(1).getOrElse { 7 })
    }

    @Test
    fun `errorOrNull exposes the failure`() {
        assertEquals(ApiError.Unauthorized, ApiResult.Failure(ApiError.Unauthorized).errorOrNull())
        assertNull(ApiResult.Success(1).errorOrNull())
    }
}
