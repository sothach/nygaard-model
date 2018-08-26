package org.seefin.nygaard.model.identifiers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class BadSchemaTest {
    @Value("${bad.schema.file}")
    private File badSchemaFile;

    @Test(expected = IllegalArgumentException.class)
    public void
    testInvalidSchema()
            throws IOException {
        Assert.assertTrue(
                "Test schema file exists [" + badSchemaFile.getCanonicalPath() + "]",
                badSchemaFile.exists());
        try {
            MSISDNScheme.loadSchemeFromFile(badSchemaFile);
            MSISDN.parse("+2687239010");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().startsWith(MSISDNScheme.INVALID_SCHEMA_PART_LENGTH));
            throw e;
        } finally {
            MSISDNScheme.resetScheme();
        }
    }
}
