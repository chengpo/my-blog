package com.monkeyapp.blog.static

import com.monkeyapp.blog.di.BlogParameters
import java.util.concurrent.CompletableFuture

class HomePageGenerator(dependencies: Dependencies): StaticResourceGenerator {
    private val blogParameters = dependencies.blogParameters()

    override fun start(): CompletableFuture<Void> {
        TODO("Not yet implemented")
    }

    interface Dependencies {
       fun blogParameters(): BlogParameters 
    }
}