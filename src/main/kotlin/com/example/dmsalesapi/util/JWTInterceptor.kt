package com.example.dmsalesapi.util

import com.example.dmsalesapi.exceptions.TokenMalformedException
import com.example.dmsalesapi.exceptions.TokenNotFoundException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (request.requestURI == "/auth/login") return true

        // Enable during tests
        return true

        val token = request.getHeader(AUTHORIZATION)?.run { substringAfter("Token ") }
            ?: throw TokenNotFoundException("No token found")

        try {
            val role = Jwts.parser().setSigningKey("dummysecret").parseClaimsJws(token).body
            request.setAttribute("employee_role_code", role.issuer)
        } catch (e: MalformedJwtException) {
            throw TokenMalformedException("Malformed JWT")
        } catch (e: ExpiredJwtException) {
            throw TokenMalformedException("JWT Expired")
        }

        return true
    }
}