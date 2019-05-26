# cuckooBill

🍕一个简单易用的记账APP

## 项目依赖

```
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation 'com.android.support:appcompat-v7:28.0.0'
    implementation 'com.android.support.constraint:constraint-layout:1.1.3'
    implementation 'org.litepal.android:core:1.4.1'
    implementation 'se.emilsjolander:stickylistheaders:2.1.1'
    implementation 'com.android.support.constraint:constraint-layout:1.0.1'
    implementation 'com.android.support:recyclerview-v7:28.0.0'
    implementation 'com.github.PhilJay:MPAndroidChart:v2.1.6'
    implementation 'com.contrarywind:Android-PickerView:4.1.6'

    implementation 'jp.wasabeef:glide-transformations:2.0.1'
    implementation 'com.github.bumptech.glide:glide:3.7.0'
    implementation 'com.github.zcweng:switch-button:0.0.3@aar'
    implementation 'com.android.support:design:28.0.0'
    implementation 'com.github.medyo:fancybuttons:1.9.0'

    implementation "com.hanks:htextview-base:0.1.6"
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
    implementation files('libs/MPAndroidChart-v3.0.0-beta2-sources.jar')
}
```

## 数据库

> Sqlite3

## 使用方法

首先,你需要使用手机号注册账户，App会自动登录注册的账户
<p>
<img src="https://ws3.sinaimg.cn/large/005BYqpggy1g3emq9oq9jj30g90swq3h.jpg" width="240">
</p>

## 分页

#### 账单默认页

<p >
<img src="https://ws3.sinaimg.cn/large/005BYqpggy1g3emqacrduj30eg0pqt9x.jpg" width="160">
<img src="https://ws3.sinaimg.cn/large/005BYqpggy1g3emq9sjwrj30ev0qfgmw.jpg" width="160">
</p>

注:👌支持滑动Item

点击一笔记账，进入详情页面，你可选择修改或者删除该笔记录

<p >
<img src="https://ws3.sinaimg.cn/large/005BYqpggy1g3emq9uckzj30ez0qm0tb.jpg" width="160">
</p>

#### 图表页
按照月份和年份,收支层面来查询账单数据，记录最大收支和总收支

<p >
<img src="https://ws3.sinaimg.cn/large/005BYqpggy1g3emqb91lpj30ez0qmq3z.jpg" width="160">
<img src="https://ws3.sinaimg.cn/large/005BYqpggy1g3emqadyxjj30eq0q7jsd.jpg" width="160">
</p>

#### 添加新一笔记账
点击中心加号，添加新一笔记账，可选择不同类型与备注记账信息来源

<p >
<img src="https://ws3.sinaimg.cn/large/005BYqpggy1g3emq9qjj4j30ej0put9h.jpg" width="160">
</p>

#### 流水页
扇形图记录每月记账流水，点击扇形图来查看记账详情

<p >
<img src="https://ws3.sinaimg.cn/large/005BYqpggy1g3emqarsazj30fd0ra0u1.jpg" width="160">
<img src="https://ws3.sinaimg.cn/large/005BYqpggy1g3emqamb19j30f60r175r.jpg" width="160">
<img src="https://ws3.sinaimg.cn/large/005BYqpggy1g3emqaclapj30f90r6q40.jpg" width="160">
</p>

#### 个人页
可在个人页设置toolbar通知，预算设置和登出

<p >
<img src="https://ws3.sinaimg.cn/large/005BYqpggy1g3emqba41zj30fb0rathp.jpg" width="160">
</p>

点击记账通知打开记账通知开关,选择提醒时间和通知信息

<p >
<img src="https://ws3.sinaimg.cn/large/005BYqpggy1g3emq9qcwhj30f70r10t4.jpg" width="160">
<img src="https://ws3.sinaimg.cn/large/005BYqpggy1g3enax4q49j30es0q9abm.jpg" width="160">
</p>
通知提示

点击预算设置打开预算开关，选择预算数目

<p >
<img src="https://ws3.sinaimg.cn/large/005BYqpggy1g3emq9qx61j30f50qz3yy.jpg" width="160">
<img src="https://ws3.sinaimg.cn/large/005BYqpggy1g3enax4r0oj30ez0qmmyi.jpg" width="160">
</p>
显示预算

        








 


