import com.intellij.ide.fileTemplates.JavaTemplateUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;

/**
 * Created by dai on 2018/2/26.
 */
public class MvpPlugins extends AnAction {
    private Project project;
    private String prefix;
    private PsiElementFactory factory;
    private PsiJavaFile psiJavaFile;
    private VirtualFile virtualFile;
    private PsiClass psiClass;

    private PsiShortNamesCache psiShortNamesCache;
    private GlobalSearchScope projectScope;
    private JavaDirectoryService directoryService;
    private PsiDirectory psiDirectory;

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        System.out.println("----------------------------------------------------------------------------------------");
        try {
            psiJavaFile = (PsiJavaFile) e.getData(CommonDataKeys.PSI_FILE);
            project = e.getData(CommonDataKeys.PROJECT);
            virtualFile = (VirtualFile) e.getData(PlatformDataKeys.VIRTUAL_FILE);
            System.out.println("virtualFile = " + virtualFile.getName().split("\\.")[0]);
            prefix = virtualFile.getName().split("\\.")[0];
            System.out.println("prefix = " + prefix);
            factory = JavaPsiFacade.getElementFactory(project);

            WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                @Override
                public void run() {
                    psiShortNamesCache = PsiShortNamesCache.getInstance(project);
                    projectScope = GlobalSearchScope.projectScope(project);
                    directoryService = JavaDirectoryService.getInstance();
                    psiDirectory = psiJavaFile.getParent();

                    generateFragment();
                    generateModule();
                    generatePresenter();
                    generateContact();


                    FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
                    OpenFileDescriptor fileDescriptor = new OpenFileDescriptor(project, psiDirectory.getVirtualFile());
                    fileEditorManager.openTextEditor(fileDescriptor, true);//Open the Contract
                }
            });


        } catch (Exception e1) {
            System.out.println("e1.toString() = " + e1.toString());
            e1.printStackTrace();
        }
    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
    }


    private void generateContact() {

        WriteCommandAction.runWriteCommandAction(project, new Runnable() {
            @Override
            public void run() {
                PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
//                psiClass = factory.createInterface((prefix + "Contract"));
                psiClass = directoryService.createInterface(psiDirectory, (prefix + "Contract"));

                PsiClass view = factory.createInterface("View");
                PsiClass presenter = factory.createInterface("Presenter");

                PsiClass baseView = factory.createInterface("BaseView");
                PsiClass basePresenter = factory.createInterface("BasePresenter");

                view.getModifierList().setModifierProperty("public", false);
                presenter.getModifierList().setModifierProperty("public", false);

                view.getImplementsList().add(factory.createClassReferenceElement(baseView));
                presenter.getImplementsList().add(factory.createClassReferenceElement(basePresenter));

                psiClass.add(view);
                psiClass.add(presenter);
                psiClass.getModifierList().setModifierProperty("public", true);
            }
        });

    }

    private void generatePresenter() {
        WriteCommandAction.runWriteCommandAction(project, new Runnable() {
            @Override
            public void run() {
                psiClass = directoryService.createClass(psiDirectory, prefix + "Presenter", JavaTemplateUtil.INTERNAL_CLASS_TEMPLATE_NAME);

                psiClass.getModifierList().setModifierProperty("public", true);
            }
        });

    }

    private void generateFragment() {
        WriteCommandAction.runWriteCommandAction(project, new Runnable() {
            @Override
            public void run() {
                psiClass = directoryService.createClass(psiDirectory, prefix + "Fragment", JavaTemplateUtil.INTERNAL_CLASS_TEMPLATE_NAME);

                psiClass.getModifierList().setModifierProperty("public", true);
            }
        });
    }

    private void generateModule() {
        WriteCommandAction.runWriteCommandAction(project, new Runnable() {
            @Override
            public void run() {
                psiClass = directoryService.createClass(psiDirectory, prefix + "Module", JavaTemplateUtil.INTERNAL_CLASS_TEMPLATE_NAME);
                psiClass.getModifierList().setModifierProperty("public", true);
                psiClass.getModifierList().setModifierProperty("abstract", true);
            }
        });

    }
}
