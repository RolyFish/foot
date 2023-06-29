package com.example.solution.common.util;

import cn.hutool.core.util.StrUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 获取i18n资源文件
 *
 *
 * @date 2021/12/15
 */
public class MessageUtils
{
    /**
     * 根据消息键和参数 获取消息 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args)
    {
        return message(code, getCurrentLocale(), args);
    }

    public static String message(String code, Locale locale, Object... args)
    {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, locale);
    }

    /**
     * 获取当前上下文Locale语言
     *
     * @return Locale，默认zh-CN
     */
    public static Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }

    /**
     * 本地化为中文语言
     *
     * @return 默认为zh-CN,true
     */
    public static boolean isZhCnLocale() {
        return Locale.SIMPLIFIED_CHINESE.equals(getCurrentLocale());
    }

    /**
     * 是否为中文语言
     * @param locale 语言
     * @return 默认为zh-CN,true
     */
    public static boolean isZhCnLocale(Locale locale) {
        return Locale.SIMPLIFIED_CHINESE.equals(locale);
    }

    /**
     * 根据消息键获取消息,获取不到返回原始键值
     *
     * @param code 消息键
     * @return 获取国际化翻译值
     */
    public static String getMessageOrDefault(String code)
    {
        if (StrUtil.isNotEmpty(code)) {
            String message;
            try {
                message = message(code);
            } catch (NoSuchMessageException e) {
                message = code;
            }
            return message;
        } else {
            return code;
        }
    }
}
