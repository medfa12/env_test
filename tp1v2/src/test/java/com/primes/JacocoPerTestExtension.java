package com.primes;

import org.jacoco.agent.rt.IAgent;
import org.jacoco.agent.rt.RT;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class JacocoPerTestExtension implements BeforeEachCallback, AfterEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        IAgent agent = RT.getAgent();
        agent.setSessionId(context.getUniqueId());
        agent.reset();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        IAgent agent = RT.getAgent();
        byte[] data = agent.getExecutionData(false);
        Path dir = Path.of("target", "jacoco-per-test");
        Files.createDirectories(dir);
        String name = context.getRequiredTestMethod().getDeclaringClass().getSimpleName()
            + "_" + context.getRequiredTestMethod().getName() + ".exec";
        Path file = dir.resolve(name);
        try (FileOutputStream out = new FileOutputStream(file.toFile())) {
            out.write(data);
        }
    }
}
