package cn.zlebhs.easymodbus;

import com.serotonin.modbus4j.code.DataType;

import lombok.Data;

/**
 * @ClassName: ModbusEntity
 * @Description:
 * @date: 22/7/22 10:16
 * @author hs
 * @version 1.0
 * @since JDK 1.8
 */
@Data
public class ModbusEntity {

    @ModbusField(offset = 40000, dataType = DataType.FOUR_BYTE_INT_SIGNED)
    private int val1;
    @ModbusField(offset = 40002)
    private int val2;
    @ModbusField(offset = 40004)
    private int val3;
    @ModbusField(offset = 40006)
    private int val4;

}
