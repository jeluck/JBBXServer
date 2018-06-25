package com.jbb.server.core.dao;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jbb.server.common.Home;
import com.jbb.server.core.domain.IouOperationRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class IouOperationRecordDaoTest {
    public static final int TESTFROMUSERID = 1000000;
    public static final int TESTTOUSERID = 1000003;
    public static final String TESTIOUCODE = "TESTIOU201801201218";
    
    @Autowired
    private IouOperationRecordDao operationRecordsDao;

    @BeforeClass
    public static void oneTimeSetUp() {
        Home.getHomeDir();
    }
    
    @Test
    public void testAddOperationRecord() {
        IouOperationRecord operationRecord = new IouOperationRecord(TESTIOUCODE,TESTFROMUSERID,TESTTOUSERID,"确认还款",null);
        operationRecordsDao.insertIouOperationRecord(operationRecord);
    }

    @Test
    public void testSearchOperationRecord() {
        List<IouOperationRecord> list = operationRecordsDao.searchIouOperationRecords(TESTIOUCODE, TESTFROMUSERID,null,null,null);
        for (IouOperationRecord operationRecord : list) {
            System.out.println(operationRecord);
        }
    }
}
