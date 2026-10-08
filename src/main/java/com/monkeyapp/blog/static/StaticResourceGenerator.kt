package com.monkeyapp.blog.static

import java.util.concurrent.CompletableFuture

interface StaticResourceGenerator {
  fun start(): CompletableFuture<Void>
}  