package com.ruoyi.common.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.base.BaseException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 通用返回结果
 *
 * @author DokiYolo
 * @since 2023-03-14
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseEntity<T> {
    private int code = HttpStatus.SUCCESS;
    private String message;
    private T data;

    public static <M> ResponseEntity<M> success() {
        return new ResponseEntity<>();
    }

    public static <M> ResponseEntity<M> error() {
        return new ResponseEntity<>();
    }

    public static <M> ResponseEntity<M> successful(M data) {
        return ResponseEntity.<M>success().setData(data);
    }

    public static <M> ResponseEntity<M> warn(String message) {
        return ResponseEntity.<M>error().setCode(HttpStatus.WARN).setMessage(message);
    }

    public static <M> ResponseEntity<M> failed(int code, String message) {
        return ResponseEntity.<M>error().setCode(code).setMessage(message);
    }

    public static <M> ResponseEntity<M> failed(String message) {
        return ResponseEntity.<M>error().setCode(HttpStatus.ERROR).setMessage(message);
    }

    public static <M> ResponseEntity<M> deduce(Runnable runnable) {
        return deduce(runnable, "处理失败", e -> log.error("处理失败", e));
    }

    public static <Void> ResponseEntity<Void> deduce(Runnable runnable, String errorMsg) {
        return deduce(runnable, errorMsg, e -> log.error(errorMsg, e));
    }

    public static <M> ResponseEntity<M> deduce(Runnable runnable, Consumer<Exception> consumer) {
        return deduce(runnable, "处理失败", consumer);
    }

    public static <M> ResponseEntity<M> deduce(Runnable runnable, String errorMsg, Consumer<Exception> consumer) {
        try {
            runnable.run();
            return ResponseEntity.success();
        } catch (BaseException | ConstraintViolationException e) {
            throw e;
        } catch (Exception e) {
            if (Objects.nonNull(consumer)) {
                consumer.accept(e);
            }
            return ResponseEntity.failed(errorMsg);
        }
    }
}
