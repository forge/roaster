package org.jboss.forge.test.roaster.model;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class NonAsciiCharactersTest {


    String nonAsciiCharactersClass =
        "public class NonAsciiCharacters {" +

        "   public int punctuations_example_çüéâäàêëèïîìÄÉôöòûùÿöü = 0; " +

        "   public int japanese_example_現在のプロジェクトデータモデルが変更されています以下のように変更する前に保存し = 1; " +

        "   public int chinese_example_下面的数据对象是由外部的编辑器修改的当生成模型时对这些对象的本地修改会覆盖外部的修改 = 2; " +

        "}";

    String nonAsciiCharactersClass2 =
            "public class NonAsciiCharacters {" +

                    "   public int punctuations_example_á = 0; " +

                    "}";

    @Test
    public void testNonAsciiCharacters()
    {
        JavaClassSource javaClass = Roaster.parse( JavaClassSource.class, nonAsciiCharactersClass );
        Assert.assertTrue( !javaClass.hasSyntaxErrors() );
        Assert.assertEquals( "punctuations_example_çüéâäàêëèïîìÄÉôöòûùÿöü", javaClass.getFields().get( 0 ).getName() );
        Assert.assertEquals( "japanese_example_現在のプロジェクトデータモデルが変更されています以下のように変更する前に保存し", javaClass.getFields().get( 1 ).getName() );
        Assert.assertEquals( "chinese_example_下面的数据对象是由外部的编辑器修改的当生成模型时对这些对象的本地修改会覆盖外部的修改", javaClass.getFields().get( 2 ).getName() );
    }

    @Test
    public void testNonAsciiCharacters2()
    {
        JavaClassSource javaClass = Roaster.parse( JavaClassSource.class, nonAsciiCharactersClass2 );
        Assert.assertTrue( !javaClass.hasSyntaxErrors() );
        Assert.assertEquals( "punctuations_example_á", javaClass.getFields().get( 0 ).getName() );
    }



}
