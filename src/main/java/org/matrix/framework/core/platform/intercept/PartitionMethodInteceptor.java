package org.matrix.framework.core.platform.intercept;

import java.lang.reflect.Method;
import javax.inject.Inject;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.matrix.framework.core.collection.Key;
import org.matrix.framework.core.platform.exception.MatrixException;
import org.matrix.framework.core.platform.web.session.SessionContext;
import org.springframework.util.StringUtils;

public class PartitionMethodInteceptor implements MethodInterceptor {

    private SessionContext sessionContext;
    
    private PartitionContext partitionContext;
    
    private static final  String DBGROUP = "MATRIX-DBGROUP";
    
    private static final  String VENDORCODE = "MATRIX-VENDORCODE";
    
    private static final  String VENDORPARTITION = "MATRIX-VENDORPARTITION";
    
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        final Method method = methodInvocation.getMethod();
        String dbGroup = sessionContext.get(Key.stringKey(DBGROUP));
        String vendorCode = sessionContext.get(Key.stringKey(VENDORCODE));
        String vendorPartition = sessionContext.get(Key.stringKey(VENDORPARTITION)); 
        if (!StringUtils.hasText(dbGroup)) throw new MatrixException("数据库分库组名[" + DBGROUP + "]不能为空.");
        if (!StringUtils.hasText(vendorCode)) throw new MatrixException("供应商代码[" + VENDORCODE + "]不能为空.");
            
        if (!StringUtils.hasText(vendorPartition)) throw new MatrixException("供应商表分区[" + VENDORPARTITION + "]不能为空");
        
        partitionContext.setInitialized(true);
        partitionContext.setDbGroup(dbGroup);
        partitionContext.setVendorCode(vendorCode);
        partitionContext.setVendorPartition(Integer.valueOf(vendorPartition));
        return methodInvocation.proceed();
    }

    @Inject
    public void setPartitionContext(PartitionContext partitionContext) {
        this.partitionContext = partitionContext;
    }

    @Inject
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

}
