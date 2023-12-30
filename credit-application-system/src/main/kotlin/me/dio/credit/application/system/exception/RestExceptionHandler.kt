package me.dio.credit.application.system.exception

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidException(ex: MethodArgumentNotValidException): ResponseEntity<ExceptionDetails>{
       val errors: MutableMap<String, String?> = HashMap()
        ex.bindingResult.allErrors.stream().forEach {
            error: ObjectError ->
                val fieldName: String = (error as FieldError).field
                val errorMessage: String? = error.defaultMessage
                errors[fieldName] = errorMessage
        }
        return ResponseEntity(
            ExceptionDetails(
                title = "Bad Request Exception, Invalid Fields",
                status = HttpStatus.BAD_REQUEST.value(),
                timestamp = java.time.LocalDateTime.now(),
                exception = ex.javaClass.name,
                details = errors
                ),
            HttpStatus.BAD_REQUEST
        )

    }


    @ExceptionHandler(DataAccessException::class)
    fun handleValidException(ex: DataAccessException): ResponseEntity<ExceptionDetails>{
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ExceptionDetails(
                title = "Data Integrity Violation Exception",
                status = HttpStatus.CONFLICT.value(),
                timestamp = java.time.LocalDateTime.now(),
                exception = ex.javaClass.name,
                details = mutableMapOf(ex.cause.toString() to ex.message)
            )
        )

    }

    @ExceptionHandler(BusinessException::class)
    fun handleValidException(ex: BusinessException): ResponseEntity<ExceptionDetails>{
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ExceptionDetails(
                title = "Bad Request Exception, Invalid Fields",
                status = HttpStatus.BAD_REQUEST.value(),
                timestamp = java.time.LocalDateTime.now(),
                exception = ex.javaClass.name,
                details = mutableMapOf(ex.cause.toString() to ex.message)
            )
        )

    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleValidException(ex: IllegalArgumentException): ResponseEntity<ExceptionDetails>{
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ExceptionDetails(
                title = "Bad Request Exception, Invalid Fields",
                status = HttpStatus.BAD_REQUEST.value(),
                timestamp = java.time.LocalDateTime.now(),
                exception = ex.javaClass.name,
                details = mutableMapOf(ex.cause.toString() to ex.message)
            )
        )

    }
}