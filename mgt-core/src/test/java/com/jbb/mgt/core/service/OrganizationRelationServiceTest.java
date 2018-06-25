package com.jbb.mgt.core.service;

import com.jbb.mgt.core.dao.OrganizationRelationDao;
import com.jbb.mgt.core.domain.OrganizationRelation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/core-application-config.xml", "/datasource-config.xml"})
public class OrganizationRelationServiceTest {

    @Autowired
    OrganizationRelationDao dao;

    @Test
    public void deleteByPrimaryKey() throws Exception {
        OrganizationRelation record = new OrganizationRelation(1,null);
        int i = dao.deleteByPrimaryKey(record);
        assertTrue(i>0);
    }

    @Test
    public void insert() throws Exception {
        OrganizationRelation record = new OrganizationRelation(1,2);
        dao.insert(record);
        List<OrganizationRelation> organizationRelations = dao.selectOrgRelationByOrgId(1);
        assertNotNull(organizationRelations);
    }


    @Test
    public void selectOrgRelationByOrgId() throws Exception {
        List<OrganizationRelation> organizationRelations = dao.selectOrgRelationByOrgId(1);
        assertNotNull(organizationRelations);
    }

}