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



## 常见问题

### Jar 冲突解决方案

如果AppCanEngine内置的jar与原来工程中的jar重复，请删掉AppCanEngine中对应的jar即可。如Demo中就是删除了support V4 Jar之后的。

### 混淆配置

AppCanEngine中的jar部分是已经混淆过的，AppCanEngine工程需要设置为不混淆

### AndroidManifest.xml 编译出错

有一些三方SDK如环信，极光推送中需要配置AppKey的部分，AndroidManifest.xml中是以`$XXXAppKey$`作为标识符区分的，如果报错，请将`$XXXAppKey$`替换为对应的AppKey即可

