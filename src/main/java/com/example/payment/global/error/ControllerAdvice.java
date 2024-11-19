package com.example.payment.global.error;

import com.example.payment.account.exception.AlreadyUnregisteredException;
import com.example.payment.account.exception.AlreadyExistedBalanceException;
import com.example.payment.account.exception.NotEqualAccountUserException;
import com.example.payment.account.exception.NotExistAccountException;
import com.example.payment.global.error.response.ErrorResponse;
import com.example.payment.global.exception.NotMatchPasswordException;
import com.example.payment.member.exception.AlreadyExistedFiveAccount;
import com.example.payment.member.exception.AlreadyExistedUserException;
import com.example.payment.member.exception.NotExistMemberException;
import com.example.payment.transaction.exception.NotUseAccountException;
import com.example.payment.transfer.exception.NotEnoughWithdrawalMoney;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    private static final String DTO_ERROR_MESSAGE_FORMAT = "%s 필드는 %s [ 들어온 값 : %s ]";

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequest() {
        ErrorResponse errorResponse = new ErrorResponse(
                "올바르지 않은 형식의 Request Body 입니다.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch() {
        ErrorResponse errorResponse = new ErrorResponse("올바르지 않은 데이터 타입 입니다.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleNotSupportedMethod() {
        ErrorResponse errorResponse = new ErrorResponse(
                "지원하지 않는 HTTP 메소드 요청 입니다.");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(errorResponse);
    }

    /**
     * @Valid 요건을 충족 시키기 못한 경우
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDtoField(
            final MethodArgumentNotValidException e) {
        FieldError firstFieldError = e.getFieldErrors().get(0);
        String errorMessage = String.format(DTO_ERROR_MESSAGE_FORMAT,
                firstFieldError.getField(),
                firstFieldError.getDefaultMessage(),
                firstFieldError.getRejectedValue());

        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({
            NotExistMemberException.class,
            NotExistAccountException.class,
            NotUseAccountException.class
    })
    public ResponseEntity<ErrorResponse> handleNotExist(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({
            NotMatchPasswordException.class,
            NotEqualAccountUserException.class,
    })
    public ResponseEntity<ErrorResponse> handleNotMatch(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({
            NotEnoughWithdrawalMoney.class
    })
    public ResponseEntity<ErrorResponse> handleNotEnough(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler({
            AlreadyExistedUserException.class,
            AlreadyExistedFiveAccount.class,
            AlreadyUnregisteredException.class,
            AlreadyExistedBalanceException.class,
    })
    public ResponseEntity<ErrorResponse> handleExisted(RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
