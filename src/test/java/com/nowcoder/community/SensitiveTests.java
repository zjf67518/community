package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
//配置类
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter() {
        String text = "这里面可以赌博，可以嫖娼，可以开票，可以吸毒，哈哈哈！！！";
        text = sensitiveFilter.filter(text);
        System.out.println(text);

        text = "这里面可以-赌-博-，可以嫖下娼，可以开?票，可以吸,,毒，哈哈哈！！！";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }

}
