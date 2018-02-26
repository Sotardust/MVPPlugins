import com.intellij.ide.fileTemplates.JavaTemplateUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;

import java.io.File;
import java.util.ResourceBundle;

/**
 * Created by dai on 2018/2/26.
 */
public class MvpPluginsCopy extends AnAction {
    protected Project myProject;
    protected String myPackageName;//where the action happens
    protected PsiDirectory myCurrentDir;//the base line of dir generation
    protected PsiDirectory myContractDir;
    protected PsiDirectory myModelDir;
    protected PsiDirectory myPresenterDir;
    protected String myPrefix;

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        System.out.println("MvpPlugins.actionPerformed");
        System.out.println("e = " + e);
        System.out.println("----------------------------------------------------------------------------------------");
        try {
            Editor editor = e.getData(PlatformDataKeys.EDITOR);
            if (editor != null) {
                System.out.println("editor " + editor);
                System.out.println("editor.toString() = " + editor.toString());
            } else {
                System.out.println("editor 为空");
            }

//            PsiJavaFile javaFile2 = (PsiJavaFile) e.getData(CommonDataKeys.PSI_FILE);
            PsiJavaFile javaFile2 = (PsiJavaFile) e.getData(CommonDataKeys.PSI_FILE);
            if (javaFile2 != null) {

                System.out.println("javaFile2 = " + javaFile2);
                System.out.println("javaFile2.getPackageName() = " + javaFile2.getPackageName());
                System.out.println("javaFile2.getName() = " + javaFile2.getName());
                System.out.println("javaFile2.getVirtualFile().getPath() = " + javaFile2.getVirtualFile().getPath());
                locateRootDir(javaFile2.getContainingFile().getParent());
            }
            Project project = e.getData(CommonDataKeys.PROJECT);

            if (project != null) {
                System.out.println("project.getBasePath() = " + project.getBasePath());
                System.out.println("project.getName() = " + project.getName());
//                listFiles(project.getBasePath(), true);
            } else {
                System.out.println("project 为空");
            }    //Set visibility only in '*.java' files.
            VirtualFile virtualFile = (VirtualFile) e.getData(PlatformDataKeys.VIRTUAL_FILE);


//            File file = new File(virtualFile.getPath()) ;
//PsiDirectory directory =
            if (virtualFile != null) {
                System.out.println("virtualFile = " + virtualFile);
                System.out.println("virtualFile.getPath() = " + virtualFile.getPath());
//                locateRootDir(javaFile.getContainingFile().getParent());
//                System.out.println("javaFile.getVirtualFile().getPath()) = " + javaFile.getVirtualFile().getPath());
//                System.out.println("javaFile.getContainingDirectory().getName() = " + javaFile.getContainingDirectory().getName());
//                System.out.println("javaFile.getParent() = " + javaFile.getParent());
//                System.out.println("javaFile.getProject().getBasePath() = " + javaFile.getProject().getBasePath());
//                System.out.println("javaFile.getProject().getName() = " + javaFile.getProject().getName());
//                System.out.println("javaFile.getText() = " + javaFile.getText());
                System.out.println("virtualFile.getName() = " + virtualFile.getName());
            } else {
                System.out.println("virtualFile 为空");
            }

            myPrefix = virtualFile.getName();
//            new JavaModeFileGenerator(project, myContractDir, myContractDir, myContractDir, myPrefix).start();
            System.out.println("MvpPlugins.actionPerformed end");

            PsiShortNamesCache psiShortNamesCache = PsiShortNamesCache.getInstance(project);
            GlobalSearchScope projectScope = GlobalSearchScope.projectScope(project);
            JavaDirectoryService directoryService = JavaDirectoryService.getInstance();
            WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                @Override
                public void run() {
//                    PsiClass[] psiClasses = psiShortNamesCache.getClassesByName(virtualFile.getName() + "Presenter", projectScope);//NotNull
                    PsiClass psiClass;
                    PsiJavaFile javaFile;
//                    if (psiClasses.length!=0){
//                        for (PsiClass psiClass1:psiClasses){
//                            System.out.println("psiClass1.getText() = " + psiClass1.getText());
//                            System.out.println("psiClass1.getName() = " + psiClass1.getName());
//                        }
//
//                    }
//                    if (psiClasses.length != 0) {//if the class already exist.
//                        psiClass = psiClasses[0];
//                        javaFile = (PsiJavaFile) psiClass.getContainingFile();
//                        javaFile.delete();//then delete the old one
//                        System.out.println("javaFile.getName()+\"Presenter\" = " + virtualFile.getName() + "Presenter");
//                    }//and re-generate one
                    System.out.println("myCurrentDir1111 = " + myCurrentDir);
                    System.out.println("virtualFile.getPath() = " + virtualFile.getPath());
//                    System.out.println("myCurrentDir.createSubdirectory(virtualFile.getParent().getPath() = " + myCurrentDir.createSubdirectory(virtualFile.getParent().getPath()));
                    PsiDirectory directory = javaFile2.getParent();
//                    PsiDirectory directory = myCurrentDir.findSubdirectory(virtualFile.getPath());
                    System.out.println("directory = " + directory);

                    psiClass = directoryService.createClass(directory, ("TestPresenter"), JavaTemplateUtil.INTERNAL_CLASS_TEMPLATE_NAME);
//                    psiClass = directoryService.cre
//                    javaFile = (PsiJavaFile) psiClass.getContainingFile();
//                    PsiPackage psiPackage = directoryService.getPackage(directory);
//                    javaFile.setPackageName(psiPackage.getQualifiedName());
                    PsiElementFactory myFactory = JavaPsiFacade.getElementFactory(project);

                    PsiClass presenter = myFactory.createInterface( "InterPresenter");
                    presenter.getModifierList().setModifierProperty("public", false);
//                    psiClass.add(presenter);
//                    psiClass.getModifierList().setModifierProperty("public", true);//force 'public interface myPrefixContract'


                    FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
                    OpenFileDescriptor fileDescriptor = new OpenFileDescriptor(project, javaFile2.getVirtualFile());
                    fileEditorManager.openTextEditor(fileDescriptor, true);//Open the Contract


                }
            });


        } catch (Exception e1) {
            System.out.println("e1.toString() = " + e1.toString());
            e1.printStackTrace();
        }

    }

    private void listFiles(String path, boolean isIterative) {
        File[] files = new File(path).listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isFile()) {
                if (!path.contains("kotlin")) break;
                System.out.println("file.getPath() = " + file.getPath());
                System.out.println("file.getName() = " + file.getName());
                if (!isIterative) break;
            } else if (file.isDirectory() && !file.getPath().contains("/.")) {
                listFiles(file.getPath(), isIterative);
            }
        }
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
    }

    private void locateRootDir(PsiDirectory currentDir) {
        String currentDirName = currentDir.getName();
        if (currentDirName.equals("java") || currentDirName.equals("src")) {
            myCurrentDir = currentDir;
            System.out.println("myCurrentDir = " + myCurrentDir);
        } else {
            PsiDirectory parent = currentDir.getParent();
            if (parent != null) {
                System.out.println("parent.getName() = " + parent.getName());
                locateRootDir(parent);//if this folder is not the root, then try its parent.
            } else {
                //when there is no more parent, we reached the ROOT of a hard-disk...
                //if we still can't locate myCurrentDir by now...
                //I guess..not the plugin's fault.. =(
                Messages.showErrorDialog(
                        "I can't imagine what happens to your project," +
                                " technically, no project could reach here.\n" +
                                " For your project match the IDEA's 'Java Project' definition," +
                                " and it match our basic rule: 'Contract' under contract-package and 'Presenter' under presenter-package." +
                                " Since it does happened, report us the detail please:" +
                                " image of this dialog, image of your project structure tree, and your description\n" +
                                ResourceBundle.getBundle("string").getString("feedBackUrl"),
                        "Locate Root Dir Error");
                throw new RuntimeException("The plugin cannot find a root dir like \"java\" or \"src\"");
            }
        }
    }
}
