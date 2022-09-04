package com.example.dmsalesapi.exceptions

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CentralExceptionHandler {
    @ExceptionHandler
    fun handleIllegalStateException(ex: DuplicateNameException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            HttpStatus.BAD_REQUEST.value(),
            ex.message
        )

        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }
}

class DuplicateNameException(message: String) : Exception(message)