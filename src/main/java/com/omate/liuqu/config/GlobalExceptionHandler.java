package com.omate.liuqu.config;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();

        // Extract all error messages
        errors.put("errors", ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Required header '" + e.getHeaderName() + "' is missing");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException e) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("error", e.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        Throwable rootCause = ex.getRootCause();
        if (rootCause instanceof SQLException) {
            SQLException sqlEx = (SQLException) rootCause;
            String message = sqlEx.getMessage();

            // 尝试从异常消息中解析出违规的字段信息
            String fieldError = parseFieldErrorFromSqlExceptionMessage(message);

            String bodyOfResponse = "数据库数据完整性违规：" + fieldError;
            return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
        }

        // 如果无法解析异常消息中的字段信息
        String defaultResponse = "数据库操作失败，可能是由于不正确的数据。";
        return new ResponseEntity<>(defaultResponse, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
    private String parseFieldErrorFromSqlExceptionMessage(String message) {
        // 这里需要自定义解析逻辑，因为不同数据库的异常消息格式不同
        // 这通常需要正则表达式或字符串分析方法
        // 例如，您可能会寻找 "Column 'xxx' cannot be null" 这样的模式
        // 这是一个非常基础的示例，实际的实现可能需要更复杂
        Pattern pattern = Pattern.compile("Column '(.*?)' cannot be null");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String defaultResponse = "字段 " + matcher.group(1) + " 的值不能为空。";
            return defaultResponse;
        }
        // 返回默认消息或进一步分析
        String defaultResponse = "字段约束违规。";
        return defaultResponse;
    }


}
