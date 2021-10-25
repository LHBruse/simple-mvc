package lhbruse;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * @author: LHB
 * @createDate: 2021/10/21
 * @version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)  // Junit提供的扩展接口，这里指定使用SpringJUnit4ClassRunner作为Junit测试环境
@ContextConfiguration(classes = App.class)  // 加载配置文件
public class BaseJunit4Test {
}
