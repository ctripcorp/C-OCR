# 图片中姓名拒识判断说明 

# 1 项目介绍
## 1.1 关于拒识逻辑说明
   该项目中的拒识是针对图片中的姓名区域，校验是否曝光、遮挡以及模糊
   

## 文件说明
| 序号 | 文件名称 | 说明 |
| ------ | ------ | ------ |
| 1 | java/code| 拒识源码目录 |
| 2 | java/entity| 拒识实体类 |
| 3| java/util| 拒识核心类 |
| 4| RejectionTest  | 测试入口 |
| 5 | resources/img | 测试图片 |

# 2 如何使用
## 2.1 测试数据图片放在resources/img 
输入图片包含姓名所在的区域，且姓名区域站整个图像区域的比例不可过小

## 2.2 运行 RejectionTest下的testReject方法
其中， RejectBiz.getRejectFlag获取的结果为拒识结果，1表示拒识，0表示不拒识


