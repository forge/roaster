package org.jboss.forge.test.parser.java;

import static org.junit.Assert.*;

import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.Import;
import org.jboss.forge.parser.java.impl.ImportImpl;
import org.junit.Test;

public class ImportEqualityTest {
    
    private static final String DEFAULT_IMPORT_PACKAGE = "org.jboss.forge.parser.java";
    private static final String DEFAULT_IMPORT_CLASS = "Import";
    private static final String DEFAULT_IMPORT = DEFAULT_IMPORT_PACKAGE + "." + DEFAULT_IMPORT_CLASS;

    @Test
    public void testImportEqualsAnotherInstance() throws Exception {
        Import firstImport = buildImport(DEFAULT_IMPORT);
        Import secondImport = buildImport(DEFAULT_IMPORT);
        assertEquals(firstImport, secondImport);
        assertEquals(secondImport, firstImport);
    }
    
    @Test
    public void testEqualsReturnsFalseForDifferentImport() throws Exception {
        Import classImport = buildImport(DEFAULT_IMPORT);
        Import interfaceImport = buildImport("org.jboss.forge.parser.java.impl.ImportImpl");
        assertFalse(classImport.equals(interfaceImport));
    }
    
    @Test
    public void testEqualsIsReflexive() throws Exception {
        Import reflexiveImport = buildImport(DEFAULT_IMPORT);
        assertEquals(reflexiveImport, reflexiveImport);
    }
    
    @Test
    public void testEqualsIsTransitive() throws Exception {
        Import firstImport = buildImport(DEFAULT_IMPORT);
        Import secondImport = buildImport(DEFAULT_IMPORT);
        Import thirdImport = buildImport(DEFAULT_IMPORT);
        assertEquals(firstImport, secondImport);
        assertEquals(secondImport, thirdImport);
        assertEquals(firstImport, thirdImport);
    }
    
    @Test
    public void testNotEqualToNull() throws Exception {
        assertFalse(buildImport(DEFAULT_IMPORT).equals(null));
    }
    
    @Test
    public void testNotEqualsToDifferentClass() throws Exception {
        Import myImport = buildImport(DEFAULT_IMPORT);
        assertFalse(myImport.equals(DEFAULT_IMPORT));
    }
    
    @Test
    public void testEqualsToDifferentImportImplementation() throws Exception {
        Import myImport = buildImport(DEFAULT_IMPORT);
        Import mockImport = new MockImportImpl(DEFAULT_IMPORT_PACKAGE, DEFAULT_IMPORT_CLASS);
        assertEquals(myImport, mockImport);
    }
    
    private class MockImportImpl implements Import {

        private String packageName;
        private String simpleName;
        
        private MockImportImpl(String packageName, String simpleName) {
            this.packageName = packageName;
            this.simpleName = simpleName;
        }

        @Override
        public Object getInternal() { return null; }

        @Override
        public String getPackage() { return packageName; }

        @Override
        public String getSimpleName() { return simpleName; }

        @Override
        public String getQualifiedName() { return packageName + "." + simpleName; }

        @Override
        public Import setName(String name) { 
            this.packageName = name.substring(0, name.lastIndexOf("."));
            this.simpleName = name.substring(name.lastIndexOf(".") + 1);
            return this; 
        }

        @Override
        public boolean isStatic() { return false; }

        @Override
        public Import setStatic(boolean value) { return this; }

        @Override
        public boolean isWildcard() { return false; }
        
    }

    private Import buildImport(String importName) {
        return new ImportImpl(JavaParser.parse("public class MockClass {}")).setName(importName);
    }
}
