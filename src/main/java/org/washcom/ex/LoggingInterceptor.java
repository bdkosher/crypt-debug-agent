package org.washcom.ex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.xml.bind.DatatypeConverter;
import net.bytebuddy.implementation.bind.annotation.Origin;

/**
 * @author Joe Wolf
 */
public class LoggingInterceptor {

    private Object instance;

    public void intercept(@Origin Method method) {
        try {
            String methodName = method.getName();
            log("Intercepted " + methodName + " call on instance" + (instance == null ? "null" : instance.getClass()));
            Object result = method.invoke(instance);
            log("Return value for " + methodName + " is " + valueOf(result));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            log("***Unable to invoke method " + method + " on " + instance, ex);
        }
    }

    private static String valueOf(Object obj) {
        if (obj == null) {
            return "~null~";
        } else if (obj instanceof byte[]) {
            byte[] bytes = (byte[]) obj;
            return DatatypeConverter.printHexBinary(bytes) + "(" + bytes.length + " bytes total)";
        } else {
            return obj.toString();
        }
    }

    private static void log(String msg) {
        System.out.println("+++ " + msg);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private static void log(String msg, Throwable t) {
        log(msg);
        t.printStackTrace();
    }

}
