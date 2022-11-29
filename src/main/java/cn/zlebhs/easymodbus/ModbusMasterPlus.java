package cn.zlebhs.easymodbus;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadCoilsResponse;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;
import com.serotonin.modbus4j.msg.WriteCoilRequest;
import com.serotonin.modbus4j.msg.WriteCoilResponse;
import com.serotonin.modbus4j.msg.WriteCoilsRequest;
import com.serotonin.modbus4j.msg.WriteCoilsResponse;
import com.serotonin.modbus4j.msg.WriteRegisterRequest;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.experimental.Delegate;

/**
 * @ClassName: ModbusMasterPlus
 * @Description: modbus主机代理
 * @date: 22/7/21 15:13
 * @author hs
 * @version 1.0
 * @since JDK 1.8
 */
public class ModbusMasterPlus {

    @Delegate
    private ModbusMaster master;
    private ExecutorService worker;

    private ModbusMasterPlus(ModbusMaster master, ExecutorService worker) {
        this.master = master;
        this.worker = worker;
    }

    public static ModbusMasterPlus wrap(ModbusMaster master, ExecutorService worker) {
        return new ModbusMasterPlus(master, worker);
    }

    public static ModbusMasterPlus wrap(ModbusMaster master) {
        return wrap(master, Executors.newSingleThreadExecutor());
    }

    // 读
    public ReadCoilsResponse readCoils(int slaveId, int startOffset, int numberOfBits) throws ModbusTransportException {
        ReadCoilsRequest request = new ReadCoilsRequest(slaveId, startOffset, numberOfBits);
        return (ReadCoilsResponse) send(request);
    }

    public ReadDiscreteInputsResponse readDiscreteInputs(int slaveId, int startOffset, int numberOfBits) throws ModbusTransportException {
        ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(slaveId, startOffset, numberOfBits);
        return (ReadDiscreteInputsResponse) send(request);
    }

    public ReadHoldingRegistersResponse readHoldingRegisters(int slaveId, int startOffset, int numberOfRegisters) throws ModbusTransportException {
        ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, startOffset, numberOfRegisters);
        return (ReadHoldingRegistersResponse) send(request);
    }

    public ReadInputRegistersResponse readInputRegisters(int slaveId, int startOffset, int numberOfRegisters) throws ModbusTransportException {
        ReadInputRegistersRequest request = new ReadInputRegistersRequest(slaveId, startOffset, numberOfRegisters);
        return (ReadInputRegistersResponse) send(request);
    }

    // 写
    public WriteCoilResponse writeCoil(int slaveId, int writeOffset, boolean writeValue) throws ModbusTransportException {
        WriteCoilRequest request = new WriteCoilRequest(slaveId, writeOffset, writeValue);
        return (WriteCoilResponse) send(request);
    }

    public WriteRegisterResponse writeRegister(int slaveId, int writeOffset, int writeValue) throws ModbusTransportException {
        WriteRegisterRequest request = new WriteRegisterRequest(slaveId, writeOffset, writeValue);
        return (WriteRegisterResponse) send(request);
    }

    public WriteCoilsResponse writeCoils(int slaveId, int startOffset, boolean[] bdata) throws ModbusTransportException {
        WriteCoilsRequest request = new WriteCoilsRequest(slaveId, startOffset, bdata);
        return (WriteCoilsResponse) send(request);
    }

    public WriteRegistersResponse writeRegisters(int slaveId, int startOffset, short[] sdata) throws ModbusTransportException {
        WriteRegistersRequest request = new WriteRegistersRequest(slaveId, startOffset, sdata);
        return  (WriteRegistersResponse) send(request);
    }

    // 异步读写
    public void send(ModbusRequest request, ExecuteCallback<ModbusResponse> callback) {
        executeAsync(() -> send(request), callback);
    }

    public <T> void getValue(BaseLocator<T> locator, ExecuteCallback<T> callback) {
        executeAsync(() -> getValue(locator), callback);
    }

    public <T> void setValue(BaseLocator<T> locator, Object value, ExecuteCallback<Boolean> callback) {
        executeAsync(() -> {
            setValue(locator, value);
            return true;
        }, callback);
    }

    public void scanForSlaveNodes(ExecuteCallback<List<Integer>> callback) {
        executeAsync(this::scanForSlaveNodes, callback);
    }

    public void testSlaveNode(int node, ExecuteCallback<Boolean> callback) {
        executeAsync(() -> testSlaveNode(node), callback);
    }

    public void readCoils(int slaveId, int startOffset, int numberOfBits, ExecuteCallback<ReadCoilsResponse> callback) {
        executeAsync(() -> readCoils(slaveId, startOffset, numberOfBits), callback);
    }

    public void readDiscreteInpus(int slaveId, int startOffset, int numberOfBits, ExecuteCallback<ReadDiscreteInputsResponse> callback) {
        executeAsync(() -> readDiscreteInputs(slaveId, startOffset, numberOfBits), callback);
    }

    public void readHoldingRegisters(int slaveId, int startOffset, int numberOfRegisters, ExecuteCallback<ReadHoldingRegistersResponse> callback) {
        executeAsync(() -> readHoldingRegisters(slaveId, startOffset, numberOfRegisters), callback);
    }

    public void readInputRegisters(int slaveId, int startOffset, int numberOfRegisters, ExecuteCallback<ReadInputRegistersResponse> callback) {
        executeAsync(() -> readInputRegisters(slaveId, startOffset, numberOfRegisters), callback);
    }

    public void writeCoil(int slaveId, int writeOffset, boolean writeValue, ExecuteCallback<WriteCoilResponse> callback) {
        executeAsync(() -> writeCoil(slaveId, writeOffset, writeValue), callback);
    }

    public void writeRegister(int slaveId, int writeOffset, int writeValue, ExecuteCallback<WriteRegisterResponse> callback) {
        executeAsync(() -> writeRegister(slaveId, writeOffset, writeValue), callback);
    }

    public void writeCoils(int slaveId, int startOffset, boolean[] bdata, ExecuteCallback<WriteCoilsResponse> callback) {
        executeAsync(() -> writeCoils(slaveId, startOffset, bdata), callback);
    }

    public void writeRegisters(int slaveId, int startOffset, short[] sdata, ExecuteCallback<WriteRegistersResponse> callback) {
        executeAsync(() -> writeRegisters(slaveId, startOffset, sdata), callback);
    }

    public <T> void executeAsync(Callable<T> task, ExecuteCallback<T> callback) {
        worker.execute(() -> {
            try {
                T response = task.call();
                if (callback != null) callback.success(response);
            } catch (Exception e) {
                if (callback != null) callback.error(e);
            }
        });
    }

    /**
     * 用注解的方式读
     * 比较简单的实现方法，利用了modbus4j的{@link BatchRead}
     * 注意：这里没有用setXxxx方法注入属性！
     *
     * @param slaveId 从机号
     * @param clazz   需要读取的类
     * @param <T>
     * @return 类的实例
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ErrorResponseException
     * @throws ModbusTransportException
     */
    public <T> T read(int slaveId, Class<T> clazz) throws IllegalAccessException, InstantiationException, ErrorResponseException, ModbusTransportException {
        BatchRead<Integer> batchRead = new BatchRead<>();
        Map<Integer, Field> fieldMap = new HashMap<>();
        T obj = clazz.newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            ModbusField anno = field.getAnnotation(ModbusField.class);
            if (anno == null) continue;
            int offset = anno.offset();
            fieldMap.put(offset, field);
            batchRead.addLocator(offset,
                    BaseLocator.createLocator(
                            slaveId, anno.range(), offset, anno.dataType(), anno.bit(),
                            anno.registerCount(), Charset.forName(anno.charset())));
        }
        BatchResults<Integer> results = send(batchRead);
        fieldMap.forEach((k, v) -> {
            v.setAccessible(true);
            try {
                v.set(obj, results.getValue(k));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return obj;
    }

    public <T> void read(int slaveId, Class<T> clazz, ExecuteCallback<T> callback) {
        executeAsync(() -> read(slaveId, clazz), callback);
    }

}
