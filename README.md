# AsktaoPackageDataHelper
问道封包读写工具

封装常用工具类

新写项目直接套用即可 1.0.1

```
<dependency>
  <groupId>io.github.devzwy</groupId>
  <artifactId>asktao_data_helper</artifactId>
  <version>1.0.1</version>
</dependency>
```

- 增加测试用例
```
src/test/java/io/github/devzwy/ExtKtTest.kt
```

```
/**
     * 对明文的密码加密
     */
    fun encryptGamePassword(account: String, password: String) =
        Md5Utils.hash("${account}${Md5Utils.hash(password).uppercase()}20070201").uppercase()

    /**
     * 计算账号表签名
     * [account] 传入明文账号
     * [encryptPassword] 加密后的密码
     * [privilege] 权限
     * [goldCoin] 金元宝数量
     * [silverCoin] 银元宝数量
     */
    fun getAccountCheckSum(account: String, encryptPassword: String, privilege: Long, goldCoin: Long, silverCoin: Long) = Md5Utils.hash(
        "${account}${encryptPassword}${toHexStr(privilege)}${toHexStr(goldCoin)}${
            toHexStr(silverCoin)
        }ABCDEF"
    ).uppercase()
```
