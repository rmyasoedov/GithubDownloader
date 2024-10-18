package com.instrument.githubdownloader.ui

import androidx.fragment.app.Fragment
import com.instrument.githubdownloader.dagger.MainActivitySubcomponent

open class BaseFragment : Fragment() {

    protected val subComponent: MainActivitySubcomponent by lazy {
        (requireActivity() as MainActivity).mainActivitySubcomponent
    }
}