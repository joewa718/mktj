package com.mktj.cn.repositories;

import com.mktj.cn.Application;
import com.mktj.cn.web.repositories.UserSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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