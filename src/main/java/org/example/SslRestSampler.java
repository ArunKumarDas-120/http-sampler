package org.example;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import static org.example.Constants.*;

public class SslRestSampler extends AbstractJavaSamplerClient implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(SslRestSampler.class);
    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = new Arguments();
        defaultParameters.addArgument(KEY_STORE ,EMPTY);
        defaultParameters.addArgument(KEY_PASSWORD ,EMPTY);
        defaultParameters.addArgument(TRUST_STORE ,EMPTY);
        defaultParameters.addArgument(TRUST_STORE_PASSWORD ,EMPTY);
        defaultParameters.addArgument(URL ,EMPTY);
        return defaultParameters;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();
        String  keyStorePath = context.getParameter(KEY_STORE);
        String keyStorePass = context.getParameter(KEY_PASSWORD);
        String trustStorePath = context.getParameter(TRUST_STORE);
        String trustStorePass = context.getParameter(TRUST_STORE_PASSWORD);
        String url = context.getParameter(URL);
        Config.configure(keyStorePath,keyStorePass.toCharArray(),trustStorePath,trustStorePass.toCharArray(),true);
        Config.hit(url);
        sampleResult.sampleEnd();
        sampleResult.setSuccessful(Boolean.TRUE);
        sampleResult.setResponseCodeOK();
        return sampleResult;
    }
}
