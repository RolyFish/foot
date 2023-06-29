//package com.example.solution.common.util;
//
//
//import org.springframework.http.HttpStatus;
//
///**
// * 安全服务工具类
// *
// *
// * @date 2021/12/15
// */
//public class SecurityUtils
//{
//
//
//    /**
//     * 未登录用户
//     */
//    public static final String  PERMISSION_NOT_LOGIN = "permission.check.not.login";
//
//    /**
//     * 签名鉴权
//     */
//    public static final String  PERMISSION_INVALID_SIGN = "permission.check.invalid.sign";
//
//    /**
//     * 当前请求已过期
//     */
//    public static final String  PERMISSION_REQUEST_EXPIRED = "permission.check.request.expired";
//    /**
//     * 用户ID
//     **/
//    public static Long getUserId()
//    {
//        try
//        {
//            return getLoginUser().getUserId();
//        }
//        catch (Exception e)
//        {
//            throw new ServiceException(MessageUtils.message(PERMISSION_NOT_LOGIN), HttpStatus.UNAUTHORIZED);
//        }
//    }
//
//    /**
//     * 获取部门ID
//     **/
//    public static Long getDeptId()
//    {
//        try
//        {
//            return getLoginUser().getDeptId();
//        }
//        catch (Exception e)
//        {
//            throw new ServiceException(MessageUtils.message(PERMISSION_NOT_LOGIN), HttpStatus.UNAUTHORIZED);
//        }
//    }
//
//    /**
//     * 获取用户账户
//     **/
//    public static String getUsername()
//    {
//        try
//        {
//            return getLoginUser().getUsername();
//        }
//        catch (Exception e)
//        {
//            throw new ServiceException(MessageUtils.message(PERMISSION_NOT_LOGIN), HttpStatus.UNAUTHORIZED);
//        }
//    }
//
//    /**
//     * 获取用户关联客户号
//     **/
//    public static String getCustomerCode()
//    {
//        try
//        {
//            return getLoginUser().getUser().getCustomerCode();
//        }
//        catch (Exception e)
//        {
//            throw new ServiceException(MessageUtils.message(PERMISSION_NOT_LOGIN), HttpStatus.UNAUTHORIZED);
//        }
//    }
//
//    /**
//     * 获取用户关联客户id
//     **/
//    public static Long getCustomerId()
//    {
//        try
//        {
//            return getLoginUser().getUser().getCustomerId();
//        }
//        catch (Exception e)
//        {
//            throw new ServiceException(MessageUtils.message(PERMISSION_NOT_LOGIN), HttpStatus.UNAUTHORIZED);
//        }
//    }
//
//    /**
//     * 获取用户
//     **/
//    public static LoginUser getLoginUser()
//    {
//        try
//        {
//            return (LoginUser) getAuthentication().getPrincipal();
//        }
//        catch (Exception e)
//        {
//            throw new ServiceException(MessageUtils.message(PERMISSION_NOT_LOGIN), HttpStatus.UNAUTHORIZED);
//        }
//    }
//
//    /**
//     * 获取Authentication
//     */
//    public static Authentication getAuthentication()
//    {
//        return SecurityContextHolder.getContext().getAuthentication();
//    }
//
//    /**
//     * 生成BCryptPasswordEncoder密码
//     *
//     * @param password 密码
//     * @return 加密字符串
//     */
//    public static String encryptPassword(String password)
//    {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        return passwordEncoder.encode(password);
//    }
//
//    /**
//     * 判断密码是否相同
//     *
//     * @param rawPassword 真实密码
//     * @param encodedPassword 加密后字符
//     * @return 结果
//     */
//    public static boolean matchesPassword(String rawPassword, String encodedPassword)
//    {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        return passwordEncoder.matches(rawPassword, encodedPassword);
//    }
//
//    /**
//     * 是否为管理员
//     *
//     * @param userId 用户ID
//     * @return 结果
//     */
//    public static boolean isAdmin(Long userId)
//    {
//        return userId != null && 1L == userId;
//    }
//}
