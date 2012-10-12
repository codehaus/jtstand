package com.jtstand.processors;

import com.jtstand.AbstractTestSequenceInstanceProcessor;
import com.jtstand.TestSequenceInstance;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class TestSequenceSimplePrint extends AbstractTestSequenceInstanceProcessor {

    private static final org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(TestSequenceSimplePrint.class.getName());
    public static final String REPORT_FILE_PATH = "REPORT_FILE_PATH";
    static final Format df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");

    public void printReport(TestSequenceInstance seq, File file) {
        FileOutputStream fos = null;
        Properties props = new Properties();

        props.setProperty("project.url", seq.getTestProject().getCreator().getSubversionUrl());
        props.setProperty("project.revision", Long.toString(seq.getTestProject().getCreator().getRevision()));
        props.setProperty("sequence.url", seq.getTestSequence().getCreator().getSubversionUrl());
        props.setProperty("sequence.revision", Long.toString(seq.getTestSequence().getCreator().getRevision()));
        props.setProperty("serialNumber", seq.getSerialNumber());
        props.setProperty("employeeNumber", seq.getEmployeeNumber());
        props.setProperty("station", seq.getTestStation().getHostName());
        props.setProperty("fixture", seq.getTestFixtureName());
        props.setProperty("partNumber", seq.getPartNumber());
        props.setProperty("partRevision", seq.getPartRevision());
        props.setProperty("testType", seq.getTestTypeName());
        props.setProperty("testTime", df.format(seq.getCreateDate()));
        props.setProperty("status", seq.getStatus().statusString);
        if (TestSequenceInstance.SequenceStatus.FAILED.equals(seq.getStatus())) {
            props.setProperty("failureStep", seq.getFailureStepPath());
            props.setProperty("failureCode", seq.getFailureCode());
        }
        try {
            fos = new FileOutputStream(file);
            props.store(fos,
                    "created by "
                    + getClass().getCanonicalName()
                    + " version "
                    + getClass().getPackage().getImplementationVersion());

        } catch (Exception ex) {
            log.error("Exception", ex);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    log.error("Exception", ex);
                }
            }
        }
    }

    @Override
    public void process(TestSequenceInstance seq) {
        String defaultFilePath = System.getProperty("user.home") + File.separator + ".jtstand" + File.separator + "report.txt";
        printReport(seq, new File(seq.getPropertyString(REPORT_FILE_PATH, defaultFilePath)));
    }
}
