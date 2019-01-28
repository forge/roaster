package org.jboss.forge.roaster.model.util;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class StringsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testAreEqual() {
        Assert.assertEquals(true, Strings.areEqual(null, null));
        Assert.assertEquals(false, Strings.areEqual(null, "$"));
        Assert.assertEquals(false, Strings.areEqual("$", null));
        Assert.assertEquals(false, Strings.areEqual("ede", "$$"));
        Assert.assertEquals(true, Strings.areEqual("mro9", "mro9"));
        Assert.assertEquals(false, Strings.areEqual("   mro9 ", "mro9    "));
    }

    @Test
    public void testAreEqualTrimmed() {
        Assert.assertEquals(true, Strings.areEqualTrimmed(null, null));
        Assert.assertEquals(false, Strings.areEqualTrimmed(null, "$"));
        Assert.assertEquals(false, Strings.areEqualTrimmed("$", null));
        Assert.assertEquals(false, Strings.areEqualTrimmed("ede", "$$"));
        Assert.assertEquals(true, Strings.areEqualTrimmed("mro9", "mro9"));
        Assert.assertEquals(true, Strings.areEqualTrimmed("   mro9 ", "mro9    "));
    }

    @Test
    public void testCapitalize() {
        Assert.assertNull(Strings.capitalize(null));
        Assert.assertEquals("Capitalization", Strings.capitalize("capitalization"));
        Assert.assertEquals("Capitalization", Strings.capitalize("Capitalization"));
        Assert.assertEquals("CapitaliZation", Strings.capitalize("CapitaliZation"));
    }

    @Test
    public void testCountNumberOfOccurrences() {
        Assert.assertEquals(0, Strings.countNumberOfOccurrences(null, ""));
        Assert.assertEquals(0, Strings.countNumberOfOccurrences("the quick brown fox", "dog"));
        Assert.assertEquals(5, Strings.countNumberOfOccurrences("1qw2qw3qw4qw5qw", "qw"));
    }

    @Test
    public void testGetLevenshteinDistanceNoThreshold(){
        Assert.assertEquals(1, Strings.getLevenshteinDistance("a", "b"));
        Assert.assertEquals(17, Strings.getLevenshteinDistance("Levenshtein Distance", "String Similarity"));
        Assert.assertEquals(8, Strings.getLevenshteinDistance("*cm3pl;'", ""));
        Assert.assertEquals(2, Strings.getLevenshteinDistance("", " >"));
    }

    @Test
    public void testGetLevenshteinDistanceWithThreshold(){
        Assert.assertEquals(0, Strings.getLevenshteinDistance("", "", 0));
        Assert.assertEquals(17, Strings.getLevenshteinDistance("Levenshtein Distance", "String Similarity", 50));
        Assert.assertEquals(17, Strings.getLevenshteinDistance("String Similarity", "Levenshtein Distance", 50));
        Assert.assertEquals(-1, Strings.getLevenshteinDistance("Levenshtein Distance", "String Similarity", 10));
        Assert.assertEquals(8, Strings.getLevenshteinDistance("*cm3pl;'", "", 21));
        Assert.assertEquals(-1, Strings.getLevenshteinDistance("", " >", 1));
        thrown.expect(IllegalArgumentException.class);
        Strings.getLevenshteinDistance("1!!!!", "1!!!", -2000);
    }

    @Test
    public void testIsBlank() {
        Assert.assertEquals(true, Strings.isBlank("     "));
        Assert.assertEquals(true, Strings.isBlank(""));
        Assert.assertEquals(false, Strings.isBlank(" non-empty  "));
    }

    @Test
    public void testIsTrue() {
        Assert.assertEquals(true, Strings.isTrue("   trUe  "));
        Assert.assertEquals(false, Strings.isTrue("   a<  "));
    }

    @Test
    public void testUnquote() {
        Assert.assertEquals("a$nfq5ei1", Strings.unquote("\"a$nfq5ei1\""));
        Assert.assertEquals("\'\'", Strings.unquote("\'\'"));
        Assert.assertEquals("&uel;", Strings.unquote("&uel;"));
    }

    @Test
    public void testStripQuotes() {
        Assert.assertEquals("a$nfq5ei", Strings.stripQuotes("\"a$nfq5ei1\""));
        Assert.assertEquals("\'\'", Strings.stripQuotes("\'\'"));
        Assert.assertEquals("&uel;", Strings.stripQuotes("&uel;"));
    }

    @Test
    public void testUncapitalize() {
        Assert.assertNull(Strings.uncapitalize(null));
        Assert.assertEquals("uncapitalize", Strings.uncapitalize("Uncapitalize"));
        Assert.assertEquals("uncapitalize", Strings.uncapitalize("uncapitalize"));
        Assert.assertEquals("uncapitaLizE", Strings.uncapitalize("uncapitaLizE"));
    }
}
