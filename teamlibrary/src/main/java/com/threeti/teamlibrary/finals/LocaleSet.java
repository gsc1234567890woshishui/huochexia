/**
 * Project Name:ThreeTiProject
 * Package Name:com.threeti.threetiproject.finals
 * File Name:LocaleSet.java
 * Function: TODO 〈一句话功能简述〉. <br/>
 * Description: TODO 〈功能详细描述〉. <br/>
 * Date:2014年10月9日下午10:31:43
 * Copyright:Copyright (c) 2014, 翔傲科技（上海）有限公司 All Rights Reserved.
 */
package com.threeti.teamlibrary.finals;

import java.util.Locale;

/**
 * ClassName:LocaleSet
 * Function:TODO ADD FUNCTION.〈一句话功能简述〉. <br/>
 * Description: TODO 〈功能详细描述，包括界面的上层以及下层的逻辑关系〉. <br/>
 * Date:2014年10月9日
 *
 * @since [产品/模块版本] （可选）
 * @author BaoHang
 * @version
 */
public interface LocaleSet {
    /**
     * default lange of the app
     */
    public static final Locale DEFAULT = Locale.TRADITIONAL_CHINESE;
    /**
     * Locale constant for zh_CN.
     */
    public static final Locale SIMPLE = Locale.SIMPLIFIED_CHINESE;

    /**
     * Locale constant for zh_TW.
     */
    public static final Locale TRADITION = Locale.TRADITIONAL_CHINESE;
    /**
     * Locale constant for en.
     */
    public static final Locale ENGLISH = Locale.ENGLISH;

    /**
     * Locale constant for ja.
     */
    public static final Locale JAPAN = Locale.JAPANESE;
}
