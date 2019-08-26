package pls.help.livedata.domain

import io.reactivex.subjects.BehaviorSubject

object DomainState {

    private const val THRESHOLD = 3
    private val subject = BehaviorSubject.create<Value>()

    val query = subject.hide()

    private fun makeComplexLogic(value: Int) {
        val result = if (value >= THRESHOLD) {
            Value.Big(value)
        } else {
            Value.Smol(value)
        }

        subject.onNext(result)
    }

    fun increment() = makeComplexLogic(
        when (val current = subject.value) {
            is Value.Big -> current.value + 2
            is Value.Smol -> current.value + 1
            null -> 1
        }
    )
}
