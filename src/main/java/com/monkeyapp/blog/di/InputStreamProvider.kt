package com.monkeyapp.blog.di

import org.jvnet.hk2.annotations.Contract
import org.jvnet.hk2.annotations.Service
import java.io.File
import java.io.InputStream

@Contract
interface InputStreamProvider {
    fun streamOf(path: String):  InputStream
}

@Service
class InputStreamProviderImpl : InputStreamProvider {
    override fun streamOf(path: String) = File(path).inputStream()
}