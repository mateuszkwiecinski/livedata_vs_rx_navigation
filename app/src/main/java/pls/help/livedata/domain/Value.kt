package pls.help.livedata.domain

sealed class Value {

    abstract val value: Int

    data class Big(override val value: Int) : Value()

    data class Smol(override val value: Int) : Value()
}
