package com.mktj.cn.repositories;

import com.mktj.cn.Application;
import com.mktj.cn.web.po.DeliveryAddress;
import com.mktj.cn.web.po.RealInfo;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.repositories.UserRepository;
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
@TransactionConfiguration(defaultRollback = false)
public class UserRepositoryTest extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    UserRepository repository;

    @DataProvider(name = "users")
    public static Object[][] dataProviderMethod() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setPhone("18930983718");
        user.setPassword("123456");
        user.setNickname("付凯博");
        user.setAuthorizationCode("ZYWT98B8CBWCMX65F");
        user.setEmail("101175708");
        user.setRoleType(RoleType.天使);
        user.setDisable(false);

        RealInfo realInfo=new RealInfo();
        realInfo.setRealName("付凯博");
        realInfo.setSex("1");
        realInfo.setBirthday("1995-10-26");
        realInfo.setOccupation("其他");
        realInfo.setUser(user);

        DeliveryAddress deliveryAddress=new DeliveryAddress();
        deliveryAddress.setProvince("上海");
        deliveryAddress.setCity("上海");
        deliveryAddress.setDeliveryMan("付凯博");
        deliveryAddress.setPhone("18930983718");
        deliveryAddress.setRegion("宝山区");

        List<DeliveryAddress> deliveryAddressList =new ArrayList<>();
        deliveryAddressList.add(deliveryAddress);
        user.setDeliveryAddressList(deliveryAddressList);

        user.setRealInfo(realInfo);
        users.add(user);
        return new Object[][]{
                {users}
        };
    }

    @BeforeMethod()
    public void setUp() throws Exception {
        System.out.println("-------start test user------------");
    }

    @Test(dataProvider = "users")
    public void testSave(List<User> users) {
        for (User user : users) {
            repository.save(user);
        }
    }
}