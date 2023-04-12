package com.ruoyi.web.controller.monitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import com.ruoyi.common.core.domain.ResponseEntity;
import com.ruoyi.system.domain.dto.SysCacheDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;

/**
 * 缓存监控
 *
 * @author ruoyi
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/monitor/cache")
public class CacheController {
    private final RedisTemplate<String, String> redisTemplate;

    private final static List<SysCacheDTO> caches = new ArrayList<>();

    static {
        caches.add(new SysCacheDTO(CacheConstants.LOGIN_TOKEN_KEY, "用户信息"));
        caches.add(new SysCacheDTO(CacheConstants.SYS_CONFIG_KEY, "配置信息"));
        caches.add(new SysCacheDTO(CacheConstants.SYS_DICT_KEY, "数据字典"));
        caches.add(new SysCacheDTO(CacheConstants.CAPTCHA_CODE_KEY, "验证码"));
        caches.add(new SysCacheDTO(CacheConstants.REPEAT_SUBMIT_KEY, "防重提交"));
        caches.add(new SysCacheDTO(CacheConstants.RATE_LIMIT_KEY, "限流处理"));
        caches.add(new SysCacheDTO(CacheConstants.PWD_ERR_CNT_KEY, "密码错误次数"));
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping()
    public AjaxResult getInfo() throws Exception {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection ->
                connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::dbSize);

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        Set<String> propertyNames = Optional.ofNullable(commandStats).map(Properties::stringPropertyNames)
                .orElseGet(Collections::emptySet);
        propertyNames.forEach(key -> {
            if (commandStats == null) {
                return;
            }
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return AjaxResult.success(result);
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getNames")
    public ResponseEntity<List<SysCacheDTO>> cache() {
        return ResponseEntity.successful(caches);
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getKeys/{cacheName}")
    public ResponseEntity<Set<String>> getCacheKeys(@PathVariable String cacheName) {
        Set<String> cacheKeys = redisTemplate.keys(cacheName + "*");
        return ResponseEntity.successful(cacheKeys);
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    public ResponseEntity<SysCacheDTO> getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey) {
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);
        SysCacheDTO sysCache = new SysCacheDTO(cacheName, cacheKey, cacheValue);
        return ResponseEntity.successful(sysCache);
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheName/{cacheName}")
    public ResponseEntity<Void> clearCacheName(@PathVariable String cacheName) {
        Collection<String> cacheKeys = Optional.ofNullable(redisTemplate.keys(cacheName + "*"))
                .orElseGet(Collections::emptySet);
        return ResponseEntity.deduce(() -> redisTemplate.delete(cacheKeys));
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheKey/{cacheKey}")
    public ResponseEntity<Void> clearCacheKey(@PathVariable String cacheKey) {
        return ResponseEntity.deduce(() -> redisTemplate.delete(cacheKey));
    }

    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @DeleteMapping("/clearCacheAll")
    public ResponseEntity<Void> clearCacheAll() {
        Collection<String> cacheKeys = Optional.ofNullable(redisTemplate.keys("*"))
                .orElseGet(Collections::emptySet);
        return ResponseEntity.deduce(() -> redisTemplate.delete(cacheKeys));
    }
}
