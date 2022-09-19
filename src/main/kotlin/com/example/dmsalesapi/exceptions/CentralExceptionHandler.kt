package com.example.dmsalesapi.exceptions

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

    @ExceptionHandler
    fun handleIdNotFoundException(ex: IdNotFoundException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            HttpStatus.NOT_FOUND.value(),
            ex.message
        )

        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleFieldNotProvidedException(ex: FieldNotProvidedException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            HttpStatus.BAD_REQUEST.value(),
            ex.message
        )

        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleIncorrectFieldValue(ex: IncorrectFieldValueException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            HttpStatus.BAD_REQUEST.value(),
            ex.message
        )

        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleEmployeeNotFoundException(ex: EmployeeNotFoundException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            HttpStatus.BAD_REQUEST.value(),
            ex.message
        )

        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleTokenNotFoundException(ex: TokenNotFoundException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            HttpStatus.UNAUTHORIZED.value(),
            ex.message
        )

        return ResponseEntity(errorMessage, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler
    fun handleTokenMalformedException(ex: TokenMalformedException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            HttpStatus.UNAUTHORIZED.value(),
            ex.message
        )

        return ResponseEntity(errorMessage, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler
    fun handleUnauthorizedException(ex: UnauthorizedException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            HttpStatus.UNAUTHORIZED.value(),
            ex.message
        )

        return ResponseEntity(errorMessage, HttpStatus.UNAUTHORIZED)
    }
}

class DuplicateNameException(message: String) : Exception(message)
class IdNotFoundException(message: String) : Exception(message)

class FieldNotProvidedException(message: String) : Exception(message)
class IncorrectFieldValueException(message: String) : Exception(message)

class EmployeeNotFoundException(message: String) : Exception(message)

class TokenNotFoundException(message:String) : Exception(message)
class TokenMalformedException(message:String) : Exception(message)

class UnauthorizedException(message:String) : Exception(message)