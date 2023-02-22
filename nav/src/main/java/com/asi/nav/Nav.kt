package com.asi.nav

/**
 * @ClassName Nav.java
 * @author usopp
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年09月22日 17:13:00
 */


object Nav : INav {

    private fun navigate(destination: NavIntent) {
        NavChannel.navigate(destination)
    }

    override fun back(route: String?, inclusive: Boolean) {
        navigate(
            NavIntent.Back(
                route = route,
                inclusive = inclusive,
            )
        )
    }


    override fun to(
        route: String,
        popUpToRoute: String?,
        inclusive: Boolean,
        isSingleTop: Boolean,
    ) {
        navigate(
            NavIntent.To(
                route = route,
                popUpToRoute = popUpToRoute,
                inclusive = inclusive,
                isSingleTop = isSingleTop,
            )
        )
    }

    override fun replace(route: String, isSingleTop: Boolean) {
        navigate(
            NavIntent.Replace(
                route = route,
                isSingleTop = isSingleTop,
            )
        )

    }

    override fun offAllTo(route: String) {
        navigate(NavIntent.OffAllTo(route))
    }


}
