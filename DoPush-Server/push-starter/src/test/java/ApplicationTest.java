import com.sadalsuud.push.domain.support.mq.SendMqService;
import com.sadalsuud.push.DoPushApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Description
 * @Author sadalsuud
 * @Blog www.sadalsuud.cn
 * @Date 10/12/2023
 * @Package PACKAGE_NAME
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoPushApplication.class)
public class ApplicationTest {
    @Resource
    private SendMqService sendMqService;

    @Test
    public void test() {
        System.out.println(sendMqService);
    }
}
