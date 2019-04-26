# 验证码检测说明 

## 文件说明
| 序号 | 文件名称 | 说明 |
| ------ | ------ | ------ |
| 1 | java/verificationcode| 检测源码目录 |
| 2 | test/java/verificationcode/TestOCR  | 测试入口 |
| 3 | resources/verificationcode | 测试图片 |

## 配置说明
DebugUtil.java  
debugSwitch：检测过程debug图片开关  
testPath：测试输出目录  
outputPicSuffix：输出图片后缀  
inputPicName： 输入图片名称  
inputPicSuffix：输入图片后缀  


# 文字切割说明
文字切割是基于文字检测和定位后对行文本进行切割
举例说明：如test\resources\detection目录下的火车票中框出的文本就是通过文本检测后框出的结果

## 文件说明
| 序号 | 文件名称 | 说明 |
| ------ | ------ | ------ |
| 1 | java/separation| 切割代码目录 |
| 2 | test/java/separation/SeparationTest  | 测试入口 |
| 3 | resources/separation | 测试图片 |

# 2 如何使用
## 2.1 测试数据图片放在resources/separation 
输入图片为已经检测出行文本的图片

## 2.2 运行 SeparationTest的testSeparation方法

 
