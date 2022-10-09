package com.asi.nav


/**
 * @ClassName NavIntent.java
 * @author usopp
 * @version 1.0.0
 * @Description 导航命令
 * @createTime 2022年09月21日 17:10:00
 */

sealed class NavIntent {

    /**
     * 返回堆栈弹出到指定目标
     * @property route 指定目标
     * @property inclusive 是否弹出指定目标
     * @constructor
     * 【"4"、"3"、"2"、"1"】 Back("2",true)->【"4"、"3"】
     * 【"4"、"3"、"2"、"1"】 Back("2",false)->【"4"、"3"、"2"】
     */
    data class Back(
        val route: String? = null,
        val inclusive: Boolean = false,
    ) : NavIntent()


    /**
     * 导航到指定目标
     * @property route 指定目标
     * @property popUpToRoute 返回堆栈弹出到指定目标
     * @property inclusive 是否弹出指定popUpToRoute目标
     * @property isSingleTop 是否是栈中单实例模式
     * @constructor
     */
    data class To(
        val route: String,
        val popUpToRoute: String? = null,
        val inclusive: Boolean = false,
        val isSingleTop: Boolean = false,
    ) : NavIntent()

    /**
     * 替换当前导航/弹出当前导航并导航到指定目的地
     * @property route 当前导航
     * @property isSingleTop 是否是栈中单实例模式
     * @constructor
     */
    data class Replace(
        val route: String,
        val isSingleTop: Boolean = false,
    ) : NavIntent()

    /**
     * 清空导航栈并导航到指定目的地
     * @property route 指定目的地
     * @constructor
     */
    data class OffAllTo(
        val route: String,
    ) : NavIntent()

}
