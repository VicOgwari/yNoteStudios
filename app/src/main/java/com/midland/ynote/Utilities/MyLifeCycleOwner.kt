package com.midland.ynote.Utilities

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class MyLifeCycleOwner(): LifecycleOwner {

    private val mLifecycleRegistry: LifecycleRegistry by lazy {LifecycleRegistry(this)}

    init {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    fun stop(){
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }
    fun start(){
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }
    override fun getLifecycle(): Lifecycle = mLifecycleRegistry
}