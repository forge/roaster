package org.jboss.forge.roaster.model.impl;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jface.text.Document;
import org.jboss.forge.roaster.model.JavaInterface;
import org.jboss.forge.roaster.model.Method;
import org.jboss.forge.roaster.model.ast.MethodFinderVisitor;
import org.jboss.forge.roaster.model.source.Import;
import org.jboss.forge.roaster.model.source.InterfaceCapableSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;
import org.jboss.forge.roaster.model.source.JavaSource;
import org.jboss.forge.roaster.model.source.MemberSource;
import org.jboss.forge.roaster.model.source.MethodHolderSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.jboss.forge.roaster.model.source.ParameterSource;
import org.jboss.forge.roaster.model.util.Methods;
import org.jboss.forge.roaster.model.util.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractInterfaceCapableJavaSource<O extends JavaSource<O>> extends AbstractJavaSource<O>
         implements InterfaceCapableSource<O>, MethodHolderSource<O>
{
   protected AbstractInterfaceCapableJavaSource(JavaSource<?> enclosingType,
            Document document, CompilationUnit unit,
            BodyDeclaration body)
   {
      super(enclosingType, document, unit, body);
   }

   @Override
   public List<String> getInterfaces()
   {
      List<String> result = new ArrayList<>();
      List<Type> superTypes = JDTHelper.getInterfaces(getDeclaration());
      for (Type type : superTypes)
      {
         String name = JDTHelper.getTypeName(type);
         String rawName = Types.stripGenerics(name);
         if (Types.isSimpleName(rawName))
         {
            String pkg;
            Import imprt = getImport(rawName);
            if (imprt == null)
            {
               imprt = getImport(resolveType(rawName));
            }
            if (imprt != null)
            {
               pkg = imprt.getPackage();
            }
            else
            {
               pkg = this.getPackage();
            }
            if (!StringUtils.isEmpty(pkg))
            {
               name = pkg + "." + name;
            }
         }
         result.add(name);
      }
      return result;
   }

   @SuppressWarnings("unchecked")
   @Override
   public O addInterface(final String type)
   {
      if (!hasInterface(type))
      {
         String typeName;
         if (addImport(type) != null)
         {
            typeName = Types.toSimpleName(type);
         }
         else
         {
            typeName = type;
         }
         AbstractTypeDeclaration declaration = getDeclaration();
         AST ast = getDeclaration().getAST();
         SimpleType simpleType = ast.newSimpleType(ast.newName(typeName));
         JDTHelper.getInterfaces(declaration).add(simpleType);
      }
      return (O) this;
   }

   @Override
   public O implementInterface(Class<?> type)
   {
      O obj = addInterface(type);
      for (MethodSource<?> methodSource : Methods.implementAbstractMethods(type, this))
      {
         methodSource.setPublic().addAnnotation(Override.class);
      }
      return obj;
   }

   @Override
   public O implementInterface(JavaInterface<?> type)
   {
      O obj = addInterface(type);

      if (type instanceof JavaInterfaceSource)
      {
         Set<Import> usedImports = new HashSet<>();

         JavaInterfaceSource interfaceSource = (JavaInterfaceSource) type;
         for (MethodSource<JavaInterfaceSource> method : interfaceSource.getMethods())
         {
            if (method.isDefault())
            {
               // Do not add default implementations
               continue;
            }

            if (!method.isReturnTypeVoid())
            {
               usedImports.add(interfaceSource.getImport(method.getReturnType().getQualifiedName()));
            }

            for (ParameterSource<JavaInterfaceSource> parameter : method.getParameters())
            {
               usedImports.add(interfaceSource.getImport(parameter.getType().getQualifiedName()));
            }
         }

         for (Import imprt : interfaceSource.getImports())
         {
            if (usedImports.contains(imprt))
            {
               addImport(imprt);
            }
         }
      }

      for (MethodSource<?> methodSource : Methods.implementAbstractMethods(type, this))
      {
         methodSource.setPublic().addAnnotation(Override.class);
      }
      return obj;
   }

   @Override
   public boolean hasInterface(final String type)
   {
      for (String name : getInterfaces())
      {
         if (Types.areEquivalent(name, type))
         {
            return true;
         }
      }
      return false;
   }

   @SuppressWarnings("unchecked")
   @Override
   public O removeInterface(final String type)
   {
      List<Type> interfaces = JDTHelper.getInterfaces(getDeclaration());
      for (Type i : interfaces)
      {
         if (Types.areEquivalent(i.toString(), type))
         {
            interfaces.remove(i);
            break;
         }
      }
      return (O) this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public O removeMethod(final Method<O, ?> method)
   {
      getDeclaration().bodyDeclarations().remove(method.getInternal());
      return (O) this;
   }

   @Override
   @SuppressWarnings("unchecked")
   public MethodSource<O> addMethod()
   {
      MethodSource<O> m = new MethodImpl<>((O) this);
      getDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public MethodSource<O> addMethod(final String method)
   {
      MethodSource<O> m = new MethodImpl<>((O) this, method);
      getDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public MethodSource<O> addMethod(java.lang.reflect.Method method)
   {
      MethodSource<O> m = new MethodImpl<>((O) this, method);
      getDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public MethodSource<O> addMethod(Method<?, ?> method)
   {
      MethodSource<O> m = new MethodImpl<>((O) this, method.toString());
      getDeclaration().bodyDeclarations().add(m.getInternal());
      return m;
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<MethodSource<O>> getMethods()
   {
      List<MethodSource<O>> result = new ArrayList<>();

      MethodFinderVisitor methodFinderVisitor = new MethodFinderVisitor();
      body.accept(methodFinderVisitor);

      List<MethodDeclaration> methods = methodFinderVisitor.getMethods();
      for (MethodDeclaration methodDeclaration : methods)
      {
         result.add(new MethodImpl<>((O) this, methodDeclaration));
      }
      return Collections.unmodifiableList(result);
   }

   @Override
   public List<MemberSource<O, ?>> getMembers()
   {
      List<MemberSource<O, ?>> result = new ArrayList<>();

      result.addAll(getMethods());

      return result;
   }
}
