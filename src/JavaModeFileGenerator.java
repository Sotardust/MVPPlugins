import com.intellij.ide.fileTemplates.JavaTemplateUtil;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;

/**
 * The file generator works in Contract/Presenter Mode.
 *
 * @author DengChao
 * @since 2017/4/10
 */
@SuppressWarnings("ConstantConditions")
public class JavaModeFileGenerator extends BaseFileGenerator {

    public JavaModeFileGenerator(Project project, PsiDirectory contractDir,  String prefix) {
        super(project, contractDir, prefix);
    }

    @Override
    public void start() {
        generateFile(myContractDir, myPrefix + "Contract", JavaTemplateUtil.INTERNAL_INTERFACE_TEMPLATE_NAME, (javaFile, psiClass) -> {

            PsiClass presenter = myFactory.createInterface("Presenter");

            presenter.getModifierList().setModifierProperty("public", false);
            psiClass.add(presenter);
            psiClass.getModifierList().setModifierProperty("public", true);//force 'public interface myPrefixContract'

            FileEditorManager fileEditorManager = FileEditorManager.getInstance(myProject);
            OpenFileDescriptor fileDescriptor = new OpenFileDescriptor(myProject, javaFile.getVirtualFile());
            fileEditorManager.openTextEditor(fileDescriptor, true);//Open the Contract

            generateModel();
            generatePresenter();
        });
    }

    private void generatePresenter() {
        generateFile(myPresenterDir, myPrefix + "Presenter", JavaTemplateUtil.INTERNAL_CLASS_TEMPLATE_NAME, (javaFile, psiClass) -> {
            PsiClass contractClass = myShortNamesCache.getClassesByName(myPrefix + "Contract", myProjectScope)[0];
            PsiClass presenter = contractClass.findInnerClassByName("Presenter", false);//don't need to search base
            psiClass.getImplementsList().add(myFactory.createClassReferenceElement(presenter));
            psiClass.getModifierList().setModifierProperty("public", true);//force 'public interface myPrefixContract'
        });
    }

    private void generateModel() {
        generateFile(myModelDir, myPrefix + "Model", JavaTemplateUtil.INTERNAL_CLASS_TEMPLATE_NAME, (javaFile, psiClass) -> {
            PsiClass contractClass = myShortNamesCache.getClassesByName(myPrefix + "Contract", myProjectScope)[0];
            PsiClass model = contractClass.findInnerClassByName("Model", false);//don't need to search base
            psiClass.getImplementsList().add(myFactory.createClassReferenceElement(model));
            psiClass.getModifierList().setModifierProperty("public", true);//force 'public interface myPrefixContract'
        });
    }

}
