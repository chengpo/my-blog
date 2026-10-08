package com.monkeyapp.blog.static

import com.monkeyapp.blog.di.*
import com.monkeyapp.blog.StaticUrl
import org.slf4j.LoggerFactory
import org.slf4j.Logger

class WebsiteBuilder(
   private val baseDir: String,
    private val appVersion: String 
) {
    private val logger: Logger = LoggerFactory.getLogger(WebsiteBuilder::class.java)   
    
    private val rootScope: RootScope = StaticRootScopeImpl()
    
    fun build() {
        logger.info("Generating static website in $baseDir with app version $appVersion ...")
        
        val postBuilder = PostResourceBuilder(rootScope.visitorScope().postController())
        postBuilder.build()
        
        val pageBuilder = PageResourceBuilder(rootScope.visitorScope().pageController())
        pageBuilder.build()
        
        // rootScope.visitorScope().feedController()
            
    }
}