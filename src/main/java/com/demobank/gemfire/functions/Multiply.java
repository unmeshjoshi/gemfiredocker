package com.demobank.gemfire.functions;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.geode.internal.InternalDataSerializer;
import org.apache.geode.internal.logging.LogService;
import org.apache.geode.pdx.PdxInstance;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

public class Multiply implements Function {
    public Multiply() {
        printCallerStack("Function constructor called from");
        LogService.getLogger().error(this);
    }

    @Override
    public boolean hasResult() {
        return true;
    }

    @Override
    public void execute(FunctionContext context) {
        //flushing classcache to make sure we do not reload old classes
        getCachedPdxParameterClass("com.gemfire.functions.MultArgs");

        LogService.getLogger().info("Flushing Class Cache");
        InternalDataSerializer.flushClassCache();

        getCachedPdxParameterClass("com.gemfire.functions.MultArgs");

        RegionFunctionContext rctx = (RegionFunctionContext) context;
        Region<Object, Object> dataSet = rctx.getDataSet();
        Object o = ((PdxInstance) context.getArguments()).getObject();
        LogService.getLogger().info(o.getClass() + " loaded from " + getClassLoaderJar(o.getClass()));
        printCallerStack("Function execution called from");
        LogService.getLogger().info("Thread context classloader is " + Thread.currentThread().getContextClassLoader());
        LogService.getLogger().error(this);
        LogService.getLogger().info(BaseArgs.class + " loaded from " + getClassLoaderJar(BaseArgs.class));
        LogService.getLogger().info(o.getClass() + " args class parent is " + o.getClass().getClassLoader().getParent());
        LogService.getLogger().info(o.getClass() + " loaded from " + getClassLoaderJar(o.getClass()));
        LogService.getLogger().info(this.getClass() + " loaded from " + getClassLoaderJar(this.getClass()));

        BaseArgs args = (BaseArgs) o;

        LogService.getLogger().info("args = " + args);

        Integer first = (Integer) ((MultArgs) args).getI1();
        Integer second = (Integer) ((MultArgs) args).getI2();

        LogService.getLogger().info("Function returning result " + this.getClass() + " loaded from " + this.getClass().getClassLoader());

        rctx.getResultSender().lastResult(first * second);
    }

    private void getCachedPdxParameterClass(String className) {
        Class<?> cachedClass = null;
        try {
            cachedClass = InternalDataSerializer.getCachedClass(className);
            LogService.getLogger().info(cachedClass + " loaded from " + getClassLoaderJar(cachedClass));

        } catch (ClassNotFoundException e) {
            LogService.getLogger().info(getStackTraceFor(e));
        }
    }

    private List<URL> getClassLoaderJar(Class clazz) {
        URL[] urls = ((URLClassLoader) clazz.getClassLoader()).getURLs();
        return Arrays.asList(urls);
    }

    @Override
    public String getId() {
        return "MULT";
    }

    @Override
    public boolean optimizeForWrite() {
        return false;
    }

    @Override
    public boolean isHA() {
        return false;
    }


    private void printCallerStack(final String message) {
        LogService.getLogger().error(getCallerStack(message));
    }

    private String getCallerStack(String message) {
        return getStackTraceFor(new Exception(message));
    }

    private String getStackTraceFor(Exception e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream p = new PrintStream(baos);
        e.printStackTrace(p);
        return new String(baos.toByteArray());
    }
}
