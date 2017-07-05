# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\SetupFile\Java\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#相关资料
#----App研发录-ProGuard技术详解----
#按照代码字面量进行混淆
# 代码混淆压缩比，在0~7之间，默认为5，一般不需要改
-optimizationpasses 5
#混淆时不使用大小写混合，混淆后的类名为小写
-dontusemixedcaseclassnames
#指定不去忽略非公共的库的类  //意为忽略公共的库的类
-dontskipnonpubliclibraryclasses
#指定不去忽略非公共的库的类的成员 //意为忽略公共的库的类成员
-dontskipnonpubliclibraryclassmembers
#不做预校验，preverify是proguard的4个步骤之一
#Android不需要preverify,去掉这一步可加快混淆速度
-dontpreverify
#有了verbose这句话，混淆后就会生成映射文件
#包含有类名->混淆后类名的映射关系
#然后使用printmapping指定映射文件的名称
-verbose
-printmapping proguardMapping.txt
#指定混淆时采用的算法，后面的参数是一个过滤器
#这个过滤器是谷歌推荐的算法，一般不改变
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护代码中的Annotation不被混淆  //no
-keepattributes *Annotation*
#避免混淆泛型
#这在Json实体映射时非常重要，比如fastJson //no
-keepattributes Signature
#抛出异常时保留代码行号，在第6章异常分析中我们提到过
-keepattributes SrouceFile,LineNumberTable

#保留所有本地的原生方法不被混淆 //no
-keepclasseswithmembernames class * {
    native <methods>;
}
#保留了继承自Activity、Application这些类的子类
#因为这些子类都有可能被外部调用
#比如说，第一行就保证了所有Activity的子类不要被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
#如果有引用android-support-v4.jar包，可以添加下面这行
-keep public class com.tuniu.app.ui.fragment.** { *; }
#保留在Activity中的方法参数是view的方法，
#从而我们在layout里面编写onClick就不会被影响
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
#枚举类不能被混淆
-keepclassmembers enum *{
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#保留自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context,android.util.AttributeSet);
    public <init>(android.content.Context,android.util.AttributeSet,int);
}
#保留Parcelable序列化的类不被混淆
-keep class * implements adnroid.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
#保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#保留R(资源)下面的所有类及其方法的指令
-keep class **.R$* {*;}
#对于带有回调函数onXXEvent的，不能被混淆
-keepclassmembers class * {
    void *(**on*Event);
}

-ignorewarnings                # 抑制警告
#==================自定义混淆=====================

#---------------------------------1.实体类---------------------------------
#这句非常重要
-keep class com.tcl.base.model.** {*;}



#---------------------------------2.第三方包-------------------------------
#glide
-dontwarn  com.bumptech.glide.**
-keep class com.bumptech.glide.** { *;}

# Router
-keep class com.chenenyu.router.** {*;}
-keep class * implements com.chenenyu.router.RouteInterceptor {*;}

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#fastJson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }

#gson
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.** { *;}
-keep class com.google.**{*;}

#java
-dontwarn java.lang.invoke.**
-keep class java.lang.invoke.** { *;}

#awt
-dontwarn  java.awt.**
-keep class java.awt.** { *;}

#RX
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-keep class com.ogaclejapan.smarttablayout.**{*;}
-keep class com.pnikosis.materialishprogress.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}

#autolayout
-dontwarn com.zhy.autolayout.**
-keep class com.zhy.autolayout.**{*;}

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

#utilcode
-dontwarn com.blankj.utilcode.utils.**
-keep class com.blankj.utilcode.utils.** { *; }

#superadapter
-dontwarn org.byteam.superadapter.**
-keep class org.byteam.superadapter.** { *; }

#---------------------------------3.与js互相调用的类------------------------




#---------------------------------4.反射相关的类和方法-----------------------





#-----------------------------------------------------------------------------
