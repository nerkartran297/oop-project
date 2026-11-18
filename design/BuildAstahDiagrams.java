import com.change_vision.jude.api.inf.editor.*;
import com.change_vision.jude.api.inf.model.*;
import com.change_vision.jude.api.inf.presentation.*;
import com.change_vision.jude.api.inf.project.*;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.*;

/**
 * Dựng UseCase.asta, GeneralClass.asta, DetailClass.asta (+ png) bằng Astah API, không cần mở giao diện.
 * Chạy: xem design/build.sh
 */
public class BuildAstahDiagrams {

    static BasicModelEditor basic;
    static ProjectAccessor pa;

    static class Link { IElement model; String from, to; Link(IElement m, String f, String t) { model = m; from = f; to = t; } }

    static Map<String, IClass> cls = new LinkedHashMap<>();
    static List<Link> links = new ArrayList<>();

    // ------------------------------------------------------------ helpers
    /** Kiểu là lớp trong project (kể cả dạng mảng X[]) thì dùng IClass thật, tránh Astah tự sinh lớp trùng tên. */
    static IClass known(String type) { return cls.get(type); }

    static void attr(IClass c, String name, String type, String vis) throws Exception {
        IClass t = known(type);
        IAttribute a = t != null ? basic.createAttribute(c, name, t) : basic.createAttribute(c, name, type);
        a.setVisibility(vis);
    }

    static void op(IClass c, String name, String ret, String[][] params, String vis, boolean abs) throws Exception {
        IClass rt = known(ret);
        IOperation o = rt != null ? basic.createOperation(c, name, rt) : basic.createOperation(c, name, ret);
        for (String[] p : params) {
            IClass pt = known(p[1]);
            if (pt != null) basic.createParameter(o, p[0], pt); else basic.createParameter(o, p[0], p[1]);
        }
        o.setVisibility(vis);
        if (abs) o.setAbstract(true);
    }

    static final String PUB = INamedElement.PUBLIC_VISIBILITY, PRO = INamedElement.PROTECTED_VISIBILITY,
            PRI = INamedElement.PRIVATE_VISIBILITY;
    static final String[][] NONE = {};

    static void op(IClass c, String name, String ret, String[]... params) throws Exception {
        op(c, name, ret, params, PUB, false);
    }

    static String[] p(String n, String t) { return new String[]{n, t}; }

    // ------------------------------------------------------------ class model
    static void buildClassModel() throws Exception {
        cls.clear();
        links.clear();
        IModel root = pa.getProject();
        IPackage pModel = basic.createPackage(root, "model");
        IPackage pView = basic.createPackage(root, "view");
        IPackage pController = basic.createPackage(root, "controller");

        String[][] all = {
                {"model", "ArrayWrapper"}, {"model", "SortStep"}, {"model", "SortAlgorithm"},
                {"model", "SelectionSort"}, {"model", "MergeSort"}, {"model", "ShellSort"},
                {"view", "MainMenuView"}, {"view", "HelpView"}, {"view", "DemoView"}, {"view", "StepPanel"},
                {"controller", "AppController"}};
        for (String[] a : all) {
            IPackage pk = a[0].equals("model") ? pModel : a[0].equals("view") ? pView : pController;
            cls.put(a[1], basic.createClass(pk, a[1]));
        }

        IClass aw = cls.get("ArrayWrapper");
        attr(aw, "data", "int[]", PRI);
        attr(aw, "comparisons", "int", PRI);
        attr(aw, "writes", "int", PRI);
        op(aw, "ArrayWrapper", "", p("values", "int[]"));
        op(aw, "size", "int");
        op(aw, "get", "int", p("index", "int"));
        op(aw, "set", "void", p("index", "int"), p("value", "int"));
        op(aw, "compare", "int", p("i", "int"), p("j", "int"));
        op(aw, "compareValues", "int", p("a", "int"), p("b", "int"));
        op(aw, "getComparisons", "int");
        op(aw, "getWrites", "int");
        op(aw, "swap", "void", p("i", "int"), p("j", "int"));
        op(aw, "copy", "ArrayWrapper");
        op(aw, "toArray", "int[]");

        IClass st = cls.get("SortStep");
        attr(st, "values", "int[]", PRI);
        attr(st, "firstIndex", "int", PRI);
        attr(st, "secondIndex", "int", PRI);
        attr(st, "rangeLow", "int", PRI);
        attr(st, "rangeHigh", "int", PRI);
        attr(st, "action", "String", PRI);
        attr(st, "description", "String", PRI);
        attr(st, "auxValues", "int[]", PRI);
        attr(st, "auxOffset", "int", PRI);
        op(st, "getValues", "int[]");
        op(st, "getAuxValues", "int[]");
        op(st, "getFirstIndex", "int");
        op(st, "getSecondIndex", "int");
        op(st, "getRangeLow", "int");
        op(st, "getRangeHigh", "int");
        op(st, "getAction", "String");
        op(st, "getDescription", "String");

        IClass sa = cls.get("SortAlgorithm");
        sa.setAbstract(true);
        attr(sa, "name", "String", PRO);
        attr(sa, "array", "ArrayWrapper", PRO);
        attr(sa, "original", "ArrayWrapper", PRI);
        attr(sa, "steps", "SortStep[]", PRI);
        attr(sa, "stepCount", "int", PRI);
        op(sa, "setArray", "void", p("array", "ArrayWrapper"));
        op(sa, "run", "void");
        op(sa, "sort", "void", NONE, PUB, true);
        op(sa, "recordStep", "void", new String[][]{p("i", "int"), p("j", "int"), p("action", "String"),
                p("description", "String")}, PRO, false);
        op(sa, "reset", "void");
        op(sa, "getName", "String");
        op(sa, "getStepCount", "int");
        op(sa, "getStep", "SortStep", p("index", "int"));
        op(sa, "getComparisons", "int");
        op(sa, "getWrites", "int");

        IClass sel = cls.get("SelectionSort");
        op(sel, "sort", "void");
        IClass mer = cls.get("MergeSort");
        op(mer, "sort", "void");
        op(mer, "mergeSort", "void", new String[][]{p("left", "int"), p("right", "int")}, PRI, false);
        op(mer, "merge", "void", new String[][]{p("left", "int"), p("mid", "int"), p("right", "int")}, PRI, false);
        IClass she = cls.get("ShellSort");
        op(she, "sort", "void");

        IClass mm = cls.get("MainMenuView");
        attr(mm, "controller", "AppController", PRI);
        IClass hv = cls.get("HelpView");
        attr(hv, "controller", "AppController", PRI);
        IClass dv = cls.get("DemoView");
        attr(dv, "controller", "AppController", PRI);
        attr(dv, "stepPanel", "StepPanel", PRI);
        op(dv, "reset", "void", p("algorithmName", "String"));
        op(dv, "showArray", "void", p("values", "int[]"));
        op(dv, "showStep", "void", p("step", "SortStep"), p("index", "int"), p("total", "int"));
        op(dv, "showStatistics", "void", p("comparisons", "int"), p("writes", "int"));
        op(dv, "setInputText", "void", p("text", "String"));
        op(dv, "setPlaying", "void", p("playing", "boolean"));
        op(dv, "showError", "void", p("message", "String"));
        IClass sp = cls.get("StepPanel");
        attr(sp, "currentStep", "SortStep", PRI);
        op(sp, "setStep", "void", p("step", "SortStep"));
        op(sp, "setArray", "void", p("values", "int[]"));
        op(sp, "clear", "void");
        op(sp, "paintComponent", "void", new String[][]{p("g", "Graphics")}, PRO, false);

        IClass ac = cls.get("AppController");
        attr(ac, "algorithm", "SortAlgorithm", PRI);
        attr(ac, "currentArray", "ArrayWrapper", PRI);
        attr(ac, "currentStepIndex", "int", PRI);
        attr(ac, "running", "boolean", PRI);
        op(ac, "start", "void");
        op(ac, "selectAlgorithm", "void", p("type", "String"));
        op(ac, "createRandomArray", "void", p("size", "int"));
        op(ac, "createArrayFromInput", "void", p("text", "String"));
        op(ac, "startSort", "void");
        op(ac, "nextStep", "void");
        op(ac, "previousStep", "void");
        op(ac, "toggleAuto", "void");
        op(ac, "setSpeed", "void", p("level", "int"));
        op(ac, "backToMenu", "void");
        op(ac, "showHelp", "void");
        op(ac, "quit", "void");

        generalize("SelectionSort", "SortAlgorithm");
        generalize("MergeSort", "SortAlgorithm");
        generalize("ShellSort", "SortAlgorithm");
        associate("SortAlgorithm", "ArrayWrapper", true);
        associate("SortAlgorithm", "SortStep", true);
        associate("DemoView", "StepPanel", true);
        associate("AppController", "SortAlgorithm", false);
        associate("AppController", "MainMenuView", true);
        associate("AppController", "HelpView", true);
        associate("AppController", "DemoView", true);
        // StepPanel phụ thuộc SortStep: client = StepPanel, supplier = SortStep
        links.add(new Link(basic.createDependency(cls.get("SortStep"), cls.get("StepPanel"), ""),
                "StepPanel", "SortStep"));
    }

    static void generalize(String sub, String sup) throws Exception {
        links.add(new Link(basic.createGeneralization(cls.get(sub), cls.get(sup), ""), sub, sup));
    }

    /** whole ◆── part (composition) hoặc đường thẳng thường (association). */
    static void associate(String whole, String part, boolean composite) throws Exception {
        IAssociation as = basic.createAssociation(cls.get(whole), cls.get(part), "", "", "");
        if (composite) as.getMemberEnds()[0].setComposite();
        links.add(new Link(as, whole, part));
    }

    // ------------------------------------------------------------ class diagrams
    static final Object[][] LAYOUT = {
            {"MainMenuView", 30, 30}, {"HelpView", 30, 150}, {"DemoView", 30, 270}, {"StepPanel", 30, 600},
            {"AppController", 420, 150},
            {"SortAlgorithm", 900, 30}, {"ArrayWrapper", 1480, 30}, {"SortStep", 470, 620},
            {"SelectionSort", 900, 520}, {"ShellSort", 1080, 520}, {"MergeSort", 1260, 520},
    };

    static final Object[][] MODEL_LAYOUT = {
            {"SortAlgorithm", 30, 30}, {"ArrayWrapper", 620, 30}, {"SortStep", 1000, 480},
            {"SelectionSort", 30, 520}, {"ShellSort", 210, 520}, {"MergeSort", 390, 520},
    };
    static final Object[][] VIEW_LAYOUT = {
            {"MainMenuView", 30, 30}, {"HelpView", 30, 150}, {"DemoView", 330, 30},
            {"StepPanel", 330, 330}, {"SortStep", 760, 330},
    };
    static final Object[][] CONTROLLER_LAYOUT = {
            {"MainMenuView", 30, 30}, {"HelpView", 30, 150}, {"DemoView", 30, 270},
            {"AppController", 620, 30}, {"SortAlgorithm", 1130, 30},
    };

    static void drawClassDiagram(String diagramName, boolean namesOnly, double sx, double sy) throws Exception {
        drawClassDiagram(diagramName, LAYOUT, namesOnly, sx, sy);
    }

    static void drawClassDiagram(String diagramName, Object[][] layout, boolean namesOnly, double sx, double sy) throws Exception {
        ClassDiagramEditor ed = pa.getDiagramEditorFactory().getClassDiagramEditor();
        IClassDiagram d = ed.createClassDiagram(pa.getProject(), diagramName);
        Map<String, INodePresentation> nodes = new HashMap<>();
        for (Object[] l : layout) {
            String n = (String) l[0];
            INodePresentation np = ed.createNodePresentation(cls.get(n),
                    new Point2D.Double(((Integer) l[1]) * sx, ((Integer) l[2]) * sy));
            nodes.put(n, np);
            if (namesOnly) {
                np.setProperty("attribute_compartment_visibility", "false");
                np.setProperty("operation_compartment_visibility", "false");
            }
        }
        for (Link k : links) {
            if (nodes.containsKey(k.from) && nodes.containsKey(k.to)) {
                ed.createLinkPresentation(k.model, nodes.get(k.from), nodes.get(k.to));
            }
        }
    }

    // ------------------------------------------------------------ use case
    static void buildUseCase() throws Exception {
        UseCaseDiagramEditor ed = pa.getDiagramEditorFactory().getUseCaseDiagramEditor();
        UseCaseModelEditor um = ModelEditorFactory.getUseCaseModelEditor();
        IModel root = pa.getProject();
        ed.createUseCaseDiagram(root, "UseCase");

        IClass user = um.createActor(root, "User");
        INodePresentation userNode = ed.createNodePresentation(user, new Point2D.Double(60, 260));

        String[][] defs = {
                {"select", "Select sorting algorithm", "300", "40"},
                {"create", "Create array", "300", "140"},
                {"random", "Generate random array", "620", "100"},
                {"manual", "Input array manually", "620", "180"},
                {"run", "Run sorting demonstration", "300", "260"},
                {"steps", "View sorting steps", "620", "260"},
                {"back", "Back to main menu", "300", "360"},
                {"help", "View help", "300", "440"},
                {"quit", "Quit application", "300", "520"},
                {"confirm", "Confirm quit", "620", "520"},
        };
        Map<String, IUseCase> uc = new HashMap<>();
        Map<String, INodePresentation> node = new HashMap<>();
        for (String[] d : defs) {
            IUseCase u = um.createUseCase(root, d[1]);
            uc.put(d[0], u);
            node.put(d[0], ed.createNodePresentation(u,
                    new Point2D.Double(Integer.parseInt(d[2]), Integer.parseInt(d[3]))));
        }
        for (String k : new String[]{"select", "create", "run", "back", "help", "quit"}) {
            IAssociation a = basic.createAssociation(user, uc.get(k), "", "", "");
            ed.createLinkPresentation(a, userNode, node.get(k));
        }
        for (String[] g : new String[][]{{"random", "create"}, {"manual", "create"}}) {
            IGeneralization ge = basic.createGeneralization(uc.get(g[0]), uc.get(g[1]), "");
            ed.createLinkPresentation(ge, node.get(g[0]), node.get(g[1]));
        }
        for (String[] g : new String[][]{{"run", "steps"}, {"quit", "confirm"}}) {
            IInclude in = um.createInclude(uc.get(g[0]), uc.get(g[1]), "");
            ed.createLinkPresentation(in, node.get(g[0]), node.get(g[1]));
        }
    }

    // ------------------------------------------------------------ main
    interface Body { void run() throws Exception; }

    static void project(String outDir, String fileName, Body body) throws Exception {
        pa = ProjectAccessorFactory.getProjectAccessor();
        pa.create();
        basic = ModelEditorFactory.getBasicModelEditor();
        TransactionManager.beginTransaction();
        try {
            body.run();
            TransactionManager.endTransaction();
        } catch (Exception e) {
            TransactionManager.abortTransaction();
            throw e;
        }
        pa.saveAs(new File(outDir, fileName).getAbsolutePath());
        System.out.println("saved: " + fileName);
        pa.close();
    }

    static List<IDiagram> diagrams(INamedElement e) throws Exception {
        return new ArrayList<>(Arrays.asList(e.getDiagrams()));
    }

    public static void main(String[] args) {
        try { run(args[0]); } catch (Throwable t) { System.out.println("ERROR:"); t.printStackTrace(System.out); System.exit(2); }
    }

    static void run(String out) throws Exception {
        new File(out).mkdirs();
        project(out, "UseCase.asta", BuildAstahDiagrams::buildUseCase);
        project(out, "GeneralClass.asta", () -> { buildClassModel(); drawClassDiagram("GeneralClass", true, 0.7, 0.8); });
        project(out, "DetailClass.asta", () -> {
            buildClassModel();
            drawClassDiagram("DetailClass", false, 1, 1);
            drawClassDiagram("DetailModel", MODEL_LAYOUT, false, 1, 1);
            drawClassDiagram("DetailView", VIEW_LAYOUT, false, 1, 1);
            drawClassDiagram("DetailController", CONTROLLER_LAYOUT, false, 1, 1);
        });
    }
}
