package com.example.emptyapp.core.network.error

import com.example.emptyapp.core.common.result.ApiError
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.serialization.SerializationException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class NetworkErrorMapperTest {

    @Test
    fun `unknown host maps to no network`() {
        assertEquals(ApiError.NoNetwork, NetworkErrorMapper.map(UnknownHostException("offline")))
    }

    @Test
    fun `socket timeout maps to timeout`() {
        assertEquals(ApiError.Timeout, NetworkErrorMapper.map(SocketTimeoutException("slow")))
    }

    @Test
    fun `serialization failure maps to serialization`() {
        val error = NetworkErrorMapper.map(SerializationException("bad json"))

        assertEquals(ApiError.Serialization("bad json"), error)
    }

    @Test
    fun `unexpected throwable maps to unknown`() {
        val cause = IllegalStateException("boom")

        assertEquals(ApiError.Unknown("boom", cause), NetworkErrorMapper.map(cause))
    }

    @Test
    fun `http 400 maps to bad request with the server message`() {
        val error = NetworkErrorMapper.map(httpException(400, """{"message":"Invalid input"}"""))

        assertEquals(ApiError.BadRequest("Invalid input"), error)
    }

    @Test
    fun `http 401 maps to unauthorized`() {
        assertEquals(ApiError.Unauthorized, NetworkErrorMapper.map(httpException(401, null)))
    }

    @Test
    fun `http 403 maps to forbidden`() {
        assertEquals(ApiError.Forbidden, NetworkErrorMapper.map(httpException(403, null)))
    }

    @Test
    fun `http 404 maps to not found`() {
        assertEquals(ApiError.NotFound("gone"), NetworkErrorMapper.map(httpException(404, """{"message":"gone"}""")))
    }

    @Test
    fun `http 500 maps to server with code and message`() {
        val error = NetworkErrorMapper.map(httpException(503, """{"message":"unavailable"}"""))

        assertEquals(ApiError.Server(503, "unavailable"), error)
    }

    @Test
    fun `http 500 without a body still maps to server`() {
        val error = NetworkErrorMapper.map(httpException(500, ""))

        assertEquals(ApiError.Server(500, null), error)
    }

    @Test
    fun `non json error body is surfaced as text`() {
        val error = NetworkErrorMapper.map(httpException(400, "plain failure"))

        assertEquals(ApiError.BadRequest("plain failure"), error)
    }

    private fun httpException(code: Int, body: String?): HttpException {
        val responseBody = (body ?: "").toResponseBody("application/json".toMediaType())
        return HttpException(Response.error<Any>(code, responseBody))
    }
}
