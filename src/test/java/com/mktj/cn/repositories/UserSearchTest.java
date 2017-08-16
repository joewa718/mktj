package com.mktj.cn.repositories;

import com.mktj.cn.Application;
import com.mktj.cn.web.po.DeliveryAddress;
import com.mktj.cn.web.po.RealInfo;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.repositories.UserRepository;
import com.mktj.cn.web.repositories.UserSearch;
import com.mktj.cn.web.util.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes=Application.class)
public class UserSearchTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    UserSearch userSearch;

    @BeforeMethod()
    public void setUp() throws Exception {
        System.out.println("-------start test user search------------");
    }

    @Test
    public void testSave() {
        List list = userSearch.search("18930983718");
        System.out.print(list);
    }
}