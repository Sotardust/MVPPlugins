# MVPPlugins
根据Google官方给出的 MVP模式生成对应文件

## 插件使用方法
    1、下载MVPPlugins.jar 在AS中添加插件
    
    2、添加成功后，右键点击创建的xxx.Java文件或者在xxx.java类中右键点击类名->generate->MVPPlugins
## 生成四种文件
    1、xxxContract.java
    eg:
        public interface TestsContract {
        
            interface View implements BaseView {
            
            }
        
            interface Presenter implements BasePresenter {
            
            }
        }
    2、xxxFragment.java
    eg:
      public class TestsFragment {
      
      }
  
    3、XXXModule.java
    eg:
       public abstract class TestsModule {
       
       }
 
    4、xxxPresenter.java
    eg:
        public class TestsPresenter {
        
        }
### 待解决问题
    1、在xxxContract.java文件中未实现implements BaseView<Presenter>,BasePresenter<View>
    
    2、在xxxFragment.java文件中未实现implements xxxContract.View
    
### 待优化问题
    1、给接口或类添加注解
    
    2、创建构造器或生成方法