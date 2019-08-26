package pls.help.livedata

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import pls.help.livedata.domain.DomainState
import pls.help.livedata.domain.Value

internal class MainViewModel : ViewModel() {

    val bag = CompositeDisposable()
    val liveNavigation = MutableLiveData<Event<MainNavigation>>()
    val rxNavigation = PublishSubject.create<MainNavigation>()

    val currentValue =  MutableLiveData<Int>()

    init {
        bag.add(
            DomainState.query
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    currentValue.value = it.value
                    val event = when (it) {
                        is Value.Smol -> MainNavigation.First
                        is Value.Big -> MainNavigation.Second(it.value)
                    }
                    Log.i("MyApp", "Emitting $event")
                    liveNavigation.value = Event(event)
                    rxNavigation.onNext(event)
                }
        )
    }

    override fun onCleared() = bag.clear()
}

sealed class MainNavigation {
    object First : MainNavigation()
    data class Second(val someId: Int) : MainNavigation()
}
