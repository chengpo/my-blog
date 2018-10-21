It is always good to keep release APK small. 
So, the basic idea is locate all the unnecessary code / resources and remove them as much as you can.

The Android developer website has provided a good guideline for [reducing APK size](https://developer.android.com/topic/performance/reduce-apk-size), includes tips like:

- Use vector graphics
- Minimize and shrink libraries
- Minimize and shrink resources 

Beyond those tips, I find more methods to reduce APK size even smaller.

## Remove Unused Resources From Libraries
Libraries like "Android Support Library" and "Google Play Services", 
as they are made to serve for general purpose, they contain resources which are not used for your App.

My App only supports English so far. 
So, it is safe for me to only keep English string resources.

```
 android {
    defaultConfig {
        ...
        resConfigs "en"   // only keep the English resources
    }
 }
```

My App only uses vector graphics.
So, it is safe for me to remove all the alternative densities.

```
 android {
    defaultConfig {
        ...
        resConfigs "nodpi"   // remove high densitiy resources
    }
 }
```

## Minimize Code by R8
R8 is a new Android SDK tool to shrink and obfuscation which outputs smaller DEX file than Proguard.
It is still in preview stage, so use it on your own judgement.

Enable R8 by adding below line to gradle.properties file : 

```
android.enableR8 = true
```

## Summary
The final gradle.build file is as:

```

android {
    defaultConfig {
        ...
        resConfigs "en"   // only keep the English resources
        resConfigs "nodpi"   // remove high densitiy resources
    }
    
    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            zipAlignEnabled true
            
            debuggable false
            jniDebuggable false
            renderscriptDebuggable false
            pseudoLocalesEnabled false
            
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
       
        debug {
               applicationIdSuffix ".debug"
         }
    }
    
    ...
}

```
The final gradle.properties file is as:
```
org.gradle.jvmargs=-Xms256m -Xmx2048M -Dfile.encoding=UTF-8
org.gradle.parallel=true
org.gradle.daemon=true

android.enableR8=true

...
```
<!--eof-->