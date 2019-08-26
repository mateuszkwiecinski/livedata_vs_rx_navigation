package pls.help.livedata

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        @Synchronized private set

    fun consume(consumer: (T) -> Unit) {
        if (!hasBeenHandled){
            hasBeenHandled = true
            consumer(content)
        }
    }

    fun value(): T = content
}
