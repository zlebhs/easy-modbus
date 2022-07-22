# easy-modbus
easy-modbus基于modbus4j开发
* 通过对ModbusMaster的代理，提供一些更为简便的操作方法，并增加了异步读写方法
## 使用
maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.zlebhs</groupId>
    <artifactId>easy-modbus</artifactId>
    <version>1.0.0</version>
</dependency>

```
示例
```java
// 正常的创建Modbus
ModbusMaster master = new ModbusFactory().createTcpMaster(param, true);
// 包装
ModbusMasterPlus masterPlus = ModbusMasterPlus.wrap(master);
// 正常使用即可
masterPlus.init();

// 可以用注解的方式进行读取
ModbusEntity entity =masterPlus.read(slaveId, ModbusEntity.class);
class ModbusEntity {
    @ModbusField(offset = 40000, dataType = DataType.FOUR_BYTE_INT_SIGNED)
    private int val1;
    @ModbusField(offset = 40002)
    private int val2;
    @ModbusField(offset = 40004)
    private int val3;
    @ModbusField(offset = 40006)
    private int val4;
}

// 异步读
masterPlus.read(slaveId, ModbusEntity.class, new ExecuteCallback<ModbusEntity>() {
    @Override
    public void success(ModbusEntity response) {
        System.out.println(response);
    }

    @Override
    public void error(Exception e) {
        e.printStackTrace();
    }
});

// 正常的读写
ReadHoldingRegistersResponse response1 = masterPlus.readHoldingRegisters(1, 40000, 2);
WriteRegistersResponse response2 = masterPlus.writeRegisters(1, 40000, new short[]{1, 2, 3, 4});

```


