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
        try {
            psiJavaFile = (PsiJavaFile) e.getData(CommonDataKeys.PSI_FILE);
            project = e.getData(CommonDataKeys.PROJECT);
            virtualFile = (VirtualFile) e.getData(PlatformDataKeys.VIRTUAL_FILE);
            if (virtualFile.getName().contains("Activity")) {
                prefix = virtualFile.getName().replace("Activity", "");
            } else {
                prefix = virtualFile.getName().split("\\.")[0];
            }
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
                //创建类
                psiClass = directoryService.createInterface(psiDirectory, (prefix + "Contract"));

                //创建接口
                PsiClass view = factory.createInterface("View");
                PsiClass presenter = factory.createInterface("Presenter");

                //使用表达式显示接口的泛型
                PsiExpression baseView = factory.createExpressionFromText("BaseView<Presenter>", view);
                PsiExpression basePresenter = factory.createExpressionFromText("BasePresenter<View>", presenter);

                //添加extends关键字
                PsiKeyword psiKeyword = factory.createKeyword("extends", view);

                //设置public属性是否显示
                view.getModifierList().setModifierProperty("public", false);
                presenter.getModifierList().setModifierProperty("public", false);

                view.getExtendsList().add(psiKeyword);
                view.getExtendsList().add(baseView);
                presenter.getExtendsList().add(psiKeyword);
                presenter.getExtendsList().add(basePresenter);

//                baseView.getModifierList().add(factory.createMethod("isActive", PsiType.BOOLEAN));

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
                psiClass = directoryService.createClass(psiDirectory, (prefix + "Presenter implements " + prefix + "Contract.Presenter "), JavaTemplateUtil.INTERNAL_CLASS_TEMPLATE_NAME);
                psiClass.getModifierList().setModifierProperty("public", true);
                psiClass.getModifierList().addAnnotation("ActivityScoped");
                PsiMethod psiMethod = factory.createConstructor();
                psiMethod.getModifierList().addAnnotation("Inject");
                psiClass.add(psiMethod);
            }
        });

    }

    private void generateFragment() {
        WriteCommandAction.runWriteCommandAction(project, new Runnable() {
            @Override
            public void run() {
                psiClass = directoryService.createClass(psiDirectory, (prefix + "Fragment implements " + prefix + "Contract.View "), JavaTemplateUtil.INTERNAL_CLASS_TEMPLATE_NAME);
                psiClass.getModifierList().setModifierProperty("public", true);
                psiClass.getModifierList().addAnnotation("ActivityScoped");
                PsiMethod psiMethod = factory.createConstructor();
                psiMethod.getModifierList().addAnnotation("Inject");
                psiClass.add(psiMethod);
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
                psiClass.getModifierList().addAnnotation("Module");
            }
        });

    }
}
