package org.jboss.forge.test.roaster.model;

import java.util.ArrayList;
import java.util.List;

class MockLocalClass
{
   static interface ID{ String getId(); }
   static List<ID> x = new ArrayList<ID>();
   
   
   private static void methodStatic(){
      class LC implements ID{ String id = "SM_LC_1"; public String getId(){ return id; } }
      x.add( new LC() );
   }
   
   private void method(){
      class LC implements ID{ String id = "M1_LC_2"; public String getId(){ return id; } }
      x.add( new LC() );
   }
   
   void method2(){
      { class LC implements ID{ String id = "M2_LC1_3"; public String getId(){ return id; } } x.add( new LC() ); }
      { class LC implements ID{ String id = "M2_LC2_4"; public String getId(){ return id; } } x.add( new LC() ); }
      { class LC implements ID{ String id = "M2_LC3_5"; public String getId(){ return id; } } x.add( new LC() ); }
      { class LC implements ID{ String id = "M2_LC4_6"; public String getId(){ return id; } } x.add( new LC() ); }
      { class LC implements ID{ String id = "M2_LC5_7"; public String getId(){ return id; } } x.add( new LC() ); }
      { class LC implements ID{ String id = "M2_LC6_8"; public String getId(){ return id; } } x.add( new LC() ); }
      { class LC implements ID{ String id = "M2_LC7_9"; public String getId(){ return id; } } x.add( new LC() ); }
      { class LC implements ID{ String id = "M2_LC8_10"; public String getId(){ return id; } } x.add( new LC() ); }
      { class LC implements ID{ String id = "M2_LC9_11"; public String getId(){ return id; } } x.add( new LC() ); }
      { class LC implements ID{ String id = "M2_LC10_12"; public String getId(){ return id; } } x.add( new LC() ); }
      { class LC implements ID{ String id = "M2_LC11_13"; public String getId(){ return id; } } x.add( new LC() ); }
   }
   
   void method3(){
      class LC2{
         class LC implements ID{ String id = "M3_LC2_LC1"; public String getId(){ return id; } }
         void init(){
            x.add( new LC() );
            method4();
         }
         { class LC implements ID{ String id = "M3_LC2_CI_LC2_1"; public String getId(){ return id; } } x.add( new LC() ); }
//         static { class LC implements ID{ String id = "M3_LC2_SI_LC"; public String getId(){ return id; } } x.add( new LC() ); } // syntax error
         
         private void method4(){
            { class LC implements ID{ String id = "M3_LC2_M4_LC1_3"; public String getId(){ return id; } } x.add( new LC() ); }
            { class LC implements ID{ String id = "M3_LC2_M4_LC2_4"; public String getId(){ return id; } } x.add( new LC() ); }
         }
      }
      
      new LC2().init();
   }
   
   
   {
      //in initializer 
      class LC implements ID{ String id = "CI_LC"; public String getId(){ return id; } }
      x.add( new LC() );
      method();
      method2();
      methodStatic();
      method3();
   }
   
   static {
      //in static initializer 
      class LC implements ID{ String id = "SI_LC"; public String getId(){ return id; } }
      x.add( new LC() );
   }
   



}
