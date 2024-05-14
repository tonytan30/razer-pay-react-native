# Razer Pay React Native Module

## 运行测试程序
```
cd razer-pay-react-native
npm i
cd example
npm i
npm run android
```

## android 安装
### 1. 引入molpayxdk
1. 复制molpayxdk文件夹到 android目录
2. 修改根目录下settings.gradle, 在最后添加
```
include ':molpayxdk'
```
3. 导入本地razer-pay-react-native module
修改 package.json
```
  "expo": {
    "autolinking": {
      "nativeModulesDir": "razer-pay-react-native的本地目录"
    }
  }
```

## 使用module支付
参考example/App.tsx