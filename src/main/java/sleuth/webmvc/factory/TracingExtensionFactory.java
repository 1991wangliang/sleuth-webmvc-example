package sleuth.webmvc.factory;

import brave.Tracing;
import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import com.alibaba.dubbo.common.extension.ExtensionFactory;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/14
 *
 * @author ujued
 */
public class TracingExtensionFactory implements ExtensionFactory {

    @Override
    public <T> T getExtension(Class<T> type, String name) {
        if (type != Tracing.class) {
            return null;
        }
        return (T) Tracing.newBuilder()
                .propagationFactory(ExtraFieldPropagation.newFactory(B3Propagation.FACTORY,"groupId"))
                .build();
    }
}
