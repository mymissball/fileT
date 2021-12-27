package com.example.demo;

import com.example.demo.dao.MachineImpl;
import com.example.demo.dbUtil.DBUtil;
import com.example.demo.entity.MachineInfo;
import com.example.demo.t.TableT;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;

@SpringBootTest
class DemoApplicationTests {



	@Autowired
	MachineImpl machineService;


	@Test
	void contextLoads() throws IOException {

		System.out.println("machineService:"+machineService);
		//System.out.println(machineService.findInfoById(6));
		///System.out.println(machineService.finaAll());
		//System.out.println(machineService.finaAll1());



		String filePath="D:\\excel\\设备维修记录表 - 12 - 副本 - 副本.xlsx";
		TableT tt=new TableT();
		tt.writeObj(filePath);
		System.out.println(tt.machineInfoList);

		for(MachineInfo info:tt.machineInfoList)
			machineService.insertToGetKey(info);

		System.out.println(machineService.findInfoById(910));

	}

	@AfterEach
	void doAfter(){
		System.out.println("test is end");
	}


	@BeforeEach
	void doBefore(){
		System.out.println("test is start");
	}
}
