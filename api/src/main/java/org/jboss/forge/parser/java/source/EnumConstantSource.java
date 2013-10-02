package org.jboss.forge.parser.java.source;

import org.jboss.forge.parser.java.EnumConstant;

/**
 * Represents one of the constant members of a {@link JavaEnumSource}.
 */
public interface EnumConstantSource extends EnumConstant<JavaEnumSource>, AnnotationTargetSource<JavaEnumSource, EnumConstantSource>,
         NamedSource<EnumConstantSource>
{
   /**
    * Represents the anonymous subclass "body" of an {@link EnumConstantSource}.
    */
   public interface Body extends EnumConstant.ReadBody<Body>, JavaSource<Body>, FieldHolderSource<Body>,
            MethodHolderSource<Body>
   {
   }

   /**
    * Set the constructor arguments for this enum constant.
    */
   EnumConstantSource setConstructorArguments(String... literalArguments);

   EnumConstantSource.Body getBody();

   /**
    * Remove the {@link Body} of this enum constant.
    */
   EnumConstantSource removeBody();
}