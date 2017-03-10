# HelloAppCanNative
## 简介

为了尽可能的不影响原工程的可读性，AppCan SDK采用library工程的方式引入。AppCan相关的引擎插件以及前端开发的Widget 包，都放在此library工程里面，方便维护。

### 目录结构

以HelloAppCanNative Demo为例，介绍工程的目录结构(基于Android Studio，Eclipse也类似):

```java
├── AppCanEngine //AppCanSDK Library 工程，需要作为依赖加入到原来的应用 
│   ├── AndroidManifest.xml
│   ├── assets
│   │   ├── error //AppCanSDK 加载HTML页面出错的错误页面，可以根据需要调整样式
│   │   └── widget //前端开发的应用放到此目录
│   │       ├── config.xml //AppCanSDK 的一些配置文件，此文件必须存在
│   │       ├── index.html //HTML页面
│   ├── build.gradle
│   ├── libs //AppCanSDK 依赖的jar
│   ├── obfuscation.appcan
│   ├── proguard.cfg
│   ├── project.properties
│   ├── res
│   │   └── xml
│   │       └── plugin.xml //插件的配置文件
│   └── src
├── app  // 你的Application工程目录
│   ├── build.gradle
│   ├── libs
│   ├── proguard-rules.pro
│   └── src
│       ├── androidTest
│       ├── main
│       └── test
├── build.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradle
```



## SDK集成

### 构建工程

- 将`AppCanEngine`拷贝到工程

- 在`setting.gradle`中添加下面一行：

  ```groovy
  include ':AppCanEngine'
  ```


- 点击右上角的`Sync Now`

如果构建失败，请参考常见问题

### 相关工程配置

#### AndroidManifest.xml

aar可以直接用解压文件打开修改然后保存

##### Application 节点

```xml

      android:name="org.zywx.wbpalmstar.widgetone.WidgetOneApplication"
        android:allowBackup="false"
        android:allowClearUserData="false"
        android:debuggable="false"
        android:icon="@drawable/icon"
        android:label="@string/app_name"
```

如果有自己的Application，此段可以去掉。硬件加速开关可以根据情况进行更改

##### LoadingActivity 节点

```
<category android:name="android.intent.category.LAUNCHER" />
```

删掉此行，不然桌面会有两个应用图标

#### libs文件夹

如果原来工程中已经有了相应的jar，则删掉libs中对应的jar，如常见的`android-support-v4.jar`

#### build.gradle

`targetSDkVersion`需要设置为17以下

### 添加插件

把下载的插件压缩包解压后，得到如下目录。将所有文件拷贝或合并至`AppCanEngine`即可

```java
├── plugins  
│   ├── AndroidManifest.xml //如果有，则按照Android标准合并到AppCanEngine的AndroidManifest.xml文件
│   ├── assets //如果有，则直接拷贝到AppCanEngine的assets目录
│   ├── libs //拷贝至AppCanEngine的libs目录
│   ├── res //除了values和xml目录，其他的都可以拷贝至AppCanEngine的res目录
│   │   ├── values //以下目录都需要按照Android标准合并至对应的文件，不能替换
│   │   │   ├── attrs.xml
│   │   │   ├── colors.xml
│   │   │   ├── dimens.xml
│   │   │   ├── ids.xml
│   │   │   ├── strings.xml
│   │   │   └── styles.xml
│   │   └── xml  //需要合并至AppCanEngine 对应的plugin.xml文件，（可以参考已有的内容合并）
│   │       └── plugin.xml //插件的配置文件
│   └── src
```

### 代码调用

#### 初始化

在Application的`onCreate()`中添加如下初始化方法：

```
AppCan.getInstance().initSync(this);
```



#### 启动AppCanEngine

```java
AppCan.getInstance().start(MainActivity.this,null);	
```

需要自定义起始页可以调用：

```java
AppCan.getInstance().start(Activity activity,String indexUrl,Bundle bundle)
```

`bundle`为需要传给起始网页的参数，可以在网页中通过`uexWidget.onLoadByOtherApp()`监听获取到

#### 监听返回事件

可以调用如下方法监听返回事件

```java
AppCan.getInstance().registerFinishCallback(new OnAppCanFinishListener() {
  
     @Override
     public void onFinish(int i, String s) {
                
     }
  
});
```

### 源码调试

#### 调试引擎源码：

引擎源码地址：https://github.com/AppCanOpenSource/appcan-android

##### 1. 删除引擎jar

aar文件需要删除`classes.jar`

library工程需要删除`libs`目录下的`AppCanEngine-system(内核)-x.x.x(版本).jar`

##### 2. 添加引擎jar的对应版本源码

clone对应版本引擎jar 的源码

拷贝源码的`appcan-android/Engine/src/main/java`下的全部文件合并至library工程的`src`目录下或者`src/main/java`(视情况而定，工程的source目录)

拷贝源码的`appcan-android/Engine/src/system(内核)/java`下的全部文件合并至library工程的`src`目录下或者`src/main/java`(视情况而定，工程的source目录)

> 引擎有三种内核，crosswalk,x5,system，对应于gradle的flavor

#### 调试插件源码：

##### 1. 删除插件jar

删除libs目录下的`plugin_uexXXX(插件名).jar`

##### 2. 添加插件源码

插件源码地址：https://github.com/android-plugin

下载插件对应版本的源码，将`src`目录拷贝合并到library工程的`src`目录

插件版本可以通过`assets/info.xml`文件查看

## 常见问题

### Jar 冲突解决方案

如果AppCanEngine内置的jar与原来工程中的jar重复，请删掉AppCanEngine中对应的jar即可。如Demo中就是删除了support V4 Jar之后的。

### 混淆配置

AppCanEngine中的jar部分是已经混淆过的，AppCanEngine工程需要设置为不混淆

### AndroidManifest.xml 编译出错

有一些三方SDK如环信，极光推送中需要配置AppKey的部分，AndroidManifest.xml中是以`$XXXAppKey$`作为标识符区分的，如果报错，请将`$XXXAppKey$`替换为对应的AppKey即可

