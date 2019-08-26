package pls.help.livedata

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*

internal class MainActivity : AppCompatActivity() {

    private val bag = CompositeDisposable()
    private val viewModel: MainViewModel by viewModels()
    private lateinit var onStartStopSubscription: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("MyApp", "onCreate()")

        bag.add(
            viewModel.rxNavigation
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.i("MyApp", "rxNavigation in onCreate: $it")
                    useNavigationHere(it) // 1.
                }
        )

        viewModel.liveNavigation.observe(this) { event ->
            event.consume {
                Log.i("MyApp", "liveNavigation in onCreate: $it")
                // useNavigationHere(it) // 2.
            }
        }

        viewModel.currentValue.observe(this) {
            label.text = "Current value: $it"
        }
        btnChangeCounter.setOnClickListener {
            startActivity(Intent(this, ChangeCounter::class.java))
        }
    }

    private fun useNavigationHere(event: MainNavigation) {
        when (event) {
            MainNavigation.First -> Unit // TODO: add snackbar
            is MainNavigation.Second -> AlertDialog.Builder(this).apply {
                setTitle("Is not smol")
                setMessage("Is big, is: ${event.someId}")
            }.show()
        }
    }

    override fun onStart() {
        Log.i("MyApp", "onStart()")
        super.onStart()
        onStartStopSubscription = viewModel.rxNavigation
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.i("MyApp", "rxNavigation in onStart: $it")
               // useNavigationHere(it) // 3.
            }
    }

    override fun onStop() {
        Log.i("MyApp", "onStop()")
        super.onStop()
        onStartStopSubscription.dispose()
    }

    override fun onDestroy() {
        Log.i("MyApp", "onDestroy()")
        super.onDestroy()
        bag.dispose()
    }
}
