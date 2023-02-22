
## Changelog
- 1.1.0
    增加导航动画

Jetpack Compose Clean Navigation

## 引入
```
implementation 'io.github.yuexunshi:Nav:1.1.0'
```



[Navigation 组件](https://developer.android.com/guide/navigation)支持 [Jetpack Compose](https://developer.android.com/jetpack/compose) 应用。我们可以在利用 Navigation 组件的基础架构和功能，在可组合项之间导航。然而，在项目中使用之后，我发现这个组件真的不好用：

- 耦合：导航需要持有**NavHostController**，在可组合函数中，必须传递**NavHostController**才能导航，导致所有需要导航的可组合函数都要持有**NavHostController**的引用。传递`callback`也是同样的问题。
- 重构和封装变得困难：有的项目并不是一个全新的 Compose 项目，而是部分功能重写，在这种情况下，很难将NavHostController 提供给这些可组合项。
- 跳转功能麻烦，许多时候并不是单纯的导航到下一个页面，可能伴随 `replace`、`pop`、清除导航栈等，需要大量代码实现。
- `ViewModel`等非可组合函数不能获取**NavHostController**。
- 拼接路由名麻烦：导航组件的路由如果传递参数的话，需要按照规则拼接。

看了很多关于如何实现导航的讨论，并且找到了一些非常棒的库，[appyx](https://github.com/bumble-tech/appyx)、[compose-router](https://github.com/zsoltk/compose-router)、[Decompose](https://github.com/arkivanov/Decompose)、[compose-backstack](https://github.com/rjrjr/compose-backstack)和使用者最多的[compose-destinations](https://github.com/raamcosta/compose-destinations)，但是都不能满足我，毕竟导航是重中之重，所以就准备对 Navigation 组件改造，封装一个方便使用的组件库。

## Jetpack Compose Clean Navigation

如果使用单例或者`Hilt`提供一个单例的自定义导航器，每个`ViewModel`和`Compose`里均可以直接使用，通过调用导航器的函数，实现导航到不同的屏幕。所有导航事件能收集在一起，这样就不需要传递回调或传递`navController`给其他屏幕。达到下面一句话的简洁用法，就问你香不香？

```
            AppNav.to(ThreeDestination("来自Two"))
            AppNav.replace(ThreeDestination("replace来自Two"))
            AppNav.back()
```

实现一个自定义导航器，首先用接口声明出需要的函数，一般来说，前两个出栈、导航函数就可以满足应用中需要的场景，后面两个函数的功能也可以用前两个函数实现出来，但是参数略多，另外实际使用的场景也很多，为了简洁，利用后面两个函数扩展一下：

```kotlin
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

```



`AppNav`实现了上面的四个导航功能。非常简单，因为要用单例，这里使用`object`，其中只是多了一个私有函数，发送导航意图，：

```kotlin
object AppNav : INav {

    private fun navigate(destination: NavIntent) {
        NavChannel.navigate(destination)
    }

    override fun back(route: String?, inclusive: Boolean) {
        navigate(NavIntent.Back(
            route = route,
            inclusive = inclusive,
        ))
    }


    override fun to(
        route: String,
        popUpToRoute: String?,
        inclusive: Boolean,
        isSingleTop: Boolean,
    ) {
        navigate(NavIntent.To(
            route = route,
            popUpToRoute = popUpToRoute,
            inclusive = inclusive,
            isSingleTop = isSingleTop,
        ))
    }

    override fun replace(route: String, isSingleTop: Boolean) {
        navigate(NavIntent.Replace(
            route = route,
            isSingleTop = isSingleTop,
        ))

    }

    override fun offAllTo(route: String) {
        navigate(NavIntent.OffAllTo(route))
    }


}
```

`NavIntent`就是导航的意图，和导航器的每个函数对应，同导航器一样，两个函数足以，多的两个函数同样是为了简洁：

```kotlin
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

```

要实现在多个地方(`ViewMdeol`、可组合函数)发送和集中在一个地方接收处理导航命令，就要使用 Flow 或者`Channel`实现，这里使用`Channel`，同样是`object`，如果使用`Hilt`的话，可以提供出去一个单例：

```kotlin
internal object NavChannel {

    private val channel = Channel<NavIntent>(
        capacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    internal var navChannel = channel.receiveAsFlow()

    internal fun navigate(destination: NavIntent) {
        channel.trySend(destination)
    }
}
```

实现接收并执行对应功能：

```kotlin
fun NavController.handleComposeNavigationIntent(intent: NavIntent) {
    when (intent) {
        is NavIntent.Back -> {
            if (intent.route != null) {
                popBackStack(intent.route, intent.inclusive)
            } else {
                currentBackStackEntry?.destination?.route?.let {
                    popBackStack()
                }
            }
        }
        is NavIntent.To -> {
            navigate(intent.route) {
                launchSingleTop = intent.isSingleTop
                intent.popUpToRoute?.let { popUpToRoute ->
                    popUpTo(popUpToRoute) { inclusive = intent.inclusive }
                }
            }
        }
        is NavIntent.Replace -> {
            navigate(intent.route) {
                launchSingleTop = intent.isSingleTop
                currentBackStackEntry?.destination?.route?.let {
                    popBackStack()
                }
            }
        }

        is NavIntent.OffAllTo -> navigate(intent.route) {
            popUpTo(0)
        }
    }
}
```

自定义`NavHost`和`composable`. `NavigationEffects`只需收集`navigationChannel`并导航到所需的屏幕。这里可以看到，它很干净干净，我们不必传递任何回调或`navController`.

```kotlin
@Composable
fun NavigationEffect(
    startDestination: String, builder: NavGraphBuilder.() -> Unit,
) {
    val navController = rememberNavController()
    val activity = (LocalContext.current as? Activity)
    val flow = NavChannel.navChannel
    LaunchedEffect(activity, navController, flow) {
        flow.collect {
            if (activity?.isFinishing == true) {
                return@collect
            }
            navController.handleComposeNavigationIntent(it)
            navController.backQueue.forEachIndexed { index, navBackStackEntry ->
                Log.e(
                    "NavigationEffects",
                    "index:$index=NavigationEffects: ${navBackStackEntry.destination.route}",
                )
            }
        }
    }
    NavHost(
        navController = navController,
        startDestination = startDestination,
        builder = builder
    )
}
```

## 使用

导航封装了完成，还有一步就是参数拼接，最初的实现是使用者自己实现：

```kotlin
sealed class Screen(
    path: String,
    val arguments: List<NamedNavArgument> = emptyList(),
) {
    val route: String = path.appendArguments(arguments)

    object One : Screen("one")
    object Two : Screen("two")
    object Four : Screen("four", listOf(
        navArgument("user") {
            type = NavUserType()
            nullable = false
        }
    )) {
        const val ARG = "user"
        fun createRoute(user: User): String {
            return route.replace("{${arguments.first().name}}", user.toString())
        }
    }

    object Three : Screen("three",
        listOf(navArgument("channelId") { type = NavType.StringType })) {
        const val ARG = "channelId"
        fun createRoute(str: String): String {
            return route.replace("{${arguments.first().name}}", str)
        }
    }
}
```

有点就是密封类实现路由声明，有约束。后来考虑到使用约定的命名约束，减少客户端样板代码，就声明了一个接口，`appendArguments`是拼接参数的扩展方法，无需自己手动拼接：

```kotlin
abstract class Destination(
    path: String,
    val arguments: List<NamedNavArgument> = emptyList(),
) {
    val route: String = if (arguments.isEmpty()) path else path.appendArguments(arguments)
}

private fun String.appendArguments(navArguments: List<NamedNavArgument>): String {
    val mandatoryArguments = navArguments.filter { it.argument.defaultValue == null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "/", prefix = "/") { "{${it.name}}" }
        .orEmpty()
    val optionalArguments = navArguments.filter { it.argument.defaultValue != null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "&", prefix = "?") { "${it.name}={${it.name}}" }
        .orEmpty()
    return "$this$mandatoryArguments$optionalArguments"
}
```

客户端这样声明路由，就继承`Destination`，命名采用`page`+`Destination`：

```kotlin
object OneDestination : Destination("one")
object TwoDestination : Destination("two")

object ThreeDestination : Destination("three",
    listOf(navArgument("channelId") { type = NavType.StringType })) {
    const val ARG = "channelId"
    operator fun invoke(str: String): String = route.replace("{${arguments.first().name}}", str)
}


object FourDestination : Destination("four", listOf(
    navArgument("user") {
        type = NavUserType()
        nullable = false
    }
)) {
    const val ARG = "user"
    operator fun invoke(user: User): String =
        route.replace("{${arguments.first().name}}", user.toString())
}

object FiveDestination : Destination("five",
    listOf(navArgument("age") { type = NavType.IntType },
        navArgument("name") { type = NavType.StringType })) {
    const val ARG_AGE = "age"
    const val ARG_NAME = "name"
    operator fun invoke(age: Int, name: String): String =
        route.replace("{${arguments.first().name}}", "$age")
            .replace("{${arguments.last().name}}", name)
}
```

### 传递普通参数，String、Int

使用`navArgument`生命参数名和类型，然后用传参替换对应的参数名，这里使用`invoke`简化写法：

```kotlin
object ThreeDestination : Destination("three",
    listOf(navArgument("channelId") { type = NavType.StringType })) {
    const val ARG = "channelId"
    operator fun invoke(str: String): String = route.replace("{${arguments.first().name}}", str)
}
```

### 传递多个参数

用传参去去替换路由里面对应的参数名。

```kotlin
object FiveDestination : Destination("five",
    listOf(navArgument("age") { type = NavType.IntType },
        navArgument("name") { type = NavType.StringType })) {
    const val ARG_AGE = "age"
    const val ARG_NAME = "name"
    operator fun invoke(age: Int, name: String): String =
        route.replace("{${arguments.first().name}}", "$age")
            .replace("{${arguments.last().name}}", name)
}
```



### 传递序列化参数

 DataBean 要序列化，这里用了两个注解，`Serializable`是因为使用了`kotlinx.serialization`，如果使用 Gson 则不需要，重写`toString`是因为拼接参数的时候可以直接用。

```kotlin
@Parcelize
@kotlinx.serialization.Serializable
data class User(
    val name: String,
    val phone: String,
) : Parcelable{
    override fun toString(): String {
        return Uri.encode(Json.encodeToString(this))
    }
}
```

然后自定义`NavType`：

```kotlin
class NavUserType : NavType<User>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): User? =
        bundle.getParcelable(key)

    override fun put(bundle: Bundle, key: String, value: User) =
        bundle.putParcelable(key, value)

    override fun parseValue(value: String): User {
        return Json.decodeFromString(value)
    }

    override fun toString(): String {
        return Uri.encode(Json.encodeToString(this))
    }

}
```

传递自定义的`NavType`：

```kotlin
object FourDestination : Destination("four", listOf(
    navArgument("user") {
        type = NavUserType()
        nullable = false
    }
)) {
    const val ARG = "user"
    operator fun invoke(user: User): String =
        route.replace("{${arguments.first().name}}", user.toString())
}
```

### 导航

看下现在的导航是有多简单：

```kotlin
   Button(onClick = {
            AppNav.to(TwoDestination.route)
        }) {
            Text(text = "去TwoScreen")
        }
        Button(onClick = {
            AppNav.to(ThreeDestination("来自首页"))
        }) {
            Text(text = "去ThreeScreen")
        }
        Button(onClick = {
            AppNav.to(FourDestination(User("来着首页", "110")))
        }) {
            Text(text = "去FourScreen")
        }
        Button(onClick = {
            AppNav.to(FiveDestination(20, "来自首页"))
        }) {
            Text(text = "去FiveScreen")
        }
```

![untitled.gif](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/494e2b1b58c44a22b02061d35c096d87~tplv-k3u1fbpfcp-watermark.image?)

完成上述操作后，我们已经能够在模块化应用程序中实现 Jetpack Compose 导航。并且使我们能够集中导航逻辑，在这样做的同时，我们可以看到一系列优势：

-   我们不再需要将 NavHostController 传递给我们的可组合函数，消除了我们的功能模块依赖于 Compose Navigation 依赖项的需要，同时还简化了我们的构造函数以进行测试。
-   我们添加了对于`ViewModel`中进行导航的支持，可以在普通函数中进行导航。
-   简化了替换、出栈等操作，一句话简单实现。

Compose 中的导航仍处于早期阶段，随着官方的改进，也许我们会不需要封装，但是目前来说我对自己实现的这种方法很满意。
