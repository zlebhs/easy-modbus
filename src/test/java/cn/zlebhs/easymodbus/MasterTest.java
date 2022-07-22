package cn.zlebhs.easymodbus;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;

import org.junit.Test;

import java.util.Arrays;

/**
 * @ClassName: MasterTest
 * @Description:
 * @date: 22/7/22 9:18
 * @author hs
 * @version 1.0
 * @since JDK 1.8
 */
public class MasterTest {

    @Test
    public void test() throws Exception{

        IpParameters param = new IpParameters();
        param.setHost("127.0.0.1");
        param.setEncapsulated(true);

        ModbusMasterPlus master = ModbusMasterPlus.wrap(new ModbusFactory().createTcpMaster(param, true));
        master.init();
        ReadHoldingRegistersResponse response1 = master.readHoldingRegisters(1, 40000, 2);
        WriteRegistersResponse response2 = master.writeRegisters(1, 40000, new short[]{1, 2, 3, 4});
        System.out.println(response1);
        System.out.println(response2);

        master.readHoldingRegisters(1, 40000, 2, new ExecuteCallback<ReadHoldingRegistersResponse>() {
            @Override
            public void success(ReadHoldingRegistersResponse response) {
                System.out.println(response);
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
            }
        });

        ModbusEntity entity = master.read(1, ModbusEntity.class);
        System.out.println(entity);

        master.read(1, ModbusEntity.class, new ExecuteCallback<ModbusEntity>() {
            @Override
            public void success(ModbusEntity response) {
                System.out.println(response);
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
            }
        });
    }

}
