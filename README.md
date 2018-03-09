# MVPPlugins
根据Google官方给出的 `MVP + Dragger2` 生成对应文件

## 插件使用方法
1、下载MVPPlugins.jar 在AS中添加插件
    
2、添加成功后，右键点击创建的xxx.Java文件或者在xxx.java类中右键点击类名->generate->MVPPlugins
## 生成四种文件
1、`xxxContract.java`

       public interface TestContract {
        
            interface View extends BaseView < Presenter > {
            
            }
        
            interface Presenter extends BasePresenter < View > {
            
            }
        }
2、`xxxFragment.java`
   
      @ActivityScoped
      public class TestFragment implements TestContract.View {
      
          @Inject
          public TestFragment() {
          
          }
      }
  
3、`XXXModule.java`

       @Module
       public abstract class TestModule {
       
       }

 
4、`xxxPresenter.java`

        @ActivityScoped
        public class TestPresenter implements TestContract.Presenter {
        
            @Inject
            public TestPresenter() {
            
            }
        }
        
问题反馈邮箱：dht170916@163.com
    
