package cn.zlebhs.easymodbus;

import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: ModbusField
 * @Description: Modbus属性注解
 * @date: 22/7/22 9:46
 * @author hs
 * @version 1.0
 * @since JDK 1.8
 * @see RegisterRange
 * @see DataType
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModbusField {

    int range() default RegisterRange.HOLDING_REGISTER;
    int offset() default 0;
    int dataType() default DataType.FOUR_BYTE_INT_SIGNED;
    int bit() default 0;
    int registerCount() default 1;
    String charset() default "utf-8";

}
