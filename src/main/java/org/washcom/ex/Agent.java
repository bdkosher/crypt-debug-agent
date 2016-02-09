package org.washcom.ex;

import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author Joe Wolf
 */
public class Agent {

    private static final AgentBuilder.RawMatcher TYPE_MATCHER = new AgentBuilder.RawMatcher() {
        @Override
        public boolean matches(TypeDescription typeDescription, ClassLoader classLoader, 
                Class<?> classBeingRedefined, ProtectionDomain protectionDomain) {
            return typeDescription.getCanonicalName().startsWith("javax.crypto.spec");
        }

    };

    private static Transformer generateTransformer(final LoggingInterceptor interceptor) {
        return new Transformer() {

            @Override
            public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription td) {
                log("Building logger interceptor transform.");
                try {
                    return builder
                            .method(ElementMatchers.named("getEncoded"))
                            .intercept(MethodDelegation.to(interceptor, "instance")
                                    .andThen(SuperMethodCall.INSTANCE));
                } catch (Exception ex) {
                    log("Failed to initialize builder.", ex);
                    return null;
                }
            }

        };
    }

    public static void premain(String args, Instrumentation instrumentation) {
        System.setProperty("org.washcom.ex.Agent", "true"); // quick validation the Agent was applied
        new AgentBuilder.Default()
                .type(TYPE_MATCHER)
                .transform(generateTransformer(new LoggingInterceptor()))
                .installOn(instrumentation);
        log("Agent initialized!");
    }

    private static void log(String msg) {
        System.out.println("*** " + msg);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private static void log(String msg, Throwable t) {
        log(msg);
        t.printStackTrace();
    }

}
