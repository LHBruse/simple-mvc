package lhbruse.handler.exception;

import lhbruse.annotation.ControllerAdvice;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 该类用于表示被ControllerAdvice标注的类
 *
 * @author: LHB
 * @createDate: 2021/9/23
 * @version: 1.0
 */
public class ControllerAdviceBean {

    private String beanName;

    private Class<?> beanType;

    private Object bean;

    public ControllerAdviceBean(String beanName, Object bean) {
        this.beanName = beanName;
        this.bean = bean;
        this.beanType = bean.getClass();
    }

    /**
     * 从容器中找出被ControllerAdvice标注的所有类
     */
    public static List<ControllerAdviceBean> findAnnotatedBeans(ApplicationContext context) {
        Map<String, Object> beanMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, Object.class);
        return beanMap.entrySet().stream()
                .filter(entry -> context.findAnnotationOnBean(entry.getKey(), ControllerAdvice.class) != null)
                .map(entry -> new ControllerAdviceBean(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

    }

    public String getBeanName() {
        return beanName;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public Object getBean() {
        return bean;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ControllerAdviceBean)) {
            return false;
        }
        ControllerAdviceBean that = (ControllerAdviceBean) o;
        return Objects.equals(bean, that.bean);
    }

    @Override
    public int hashCode() {
        return this.bean.hashCode();
    }
}
