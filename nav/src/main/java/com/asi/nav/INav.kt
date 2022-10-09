package com.asi.nav

/**
 * @ClassName INav.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月22日 17:40:00
 */
interface INav {


    /**
     * 出栈
     * @param route String
     * @param inclusive Boolean
     */
    fun back(
        route: String? = null,
        inclusive: Boolean = false,
    )

    /**
     * 导航
     * @param route 目的地路由
     * @param popUpToRoute 弹出路由?
     * @param inclusive 是否也弹出popUpToRoute
     * @param isSingleTop Boolean
     */
    fun to(
        route: String,
        popUpToRoute: String? = null,
        inclusive: Boolean = false,
        isSingleTop: Boolean = false,
    )


    /**
     * 弹出当前栈并导航到
     * @param route String
     * @param isSingleTop Boolean
     */
    fun replace(
        route: String,
        isSingleTop: Boolean = false,
    )

    /**
     * 清空导航栈然后导航到route
     * @param route String
     */
    fun offAllTo(
        route: String,
    )


}
