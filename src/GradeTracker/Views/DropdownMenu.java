//package GradeTracker.Views;
//
//import GradeTracker.GTModel;
//import javafx.scene.control.Menu;
//import javafx.scene.control.MenuBar;
//
//// todo No worries if we delete this, just one attempt at fixing the title (which failed...)
//public class DropdownMenu {
//    private MenuBar menuBar;
//    private Menu menuSave;
//    private Menu menuHelp;
//    private Menu menuAbout;
//
//    public DropdownMenu(GTModel model) {
//        makeSaveBtn(model);
//        menuHelp = new Menu();
//        menuHelp.setText("Help");
//        menuHelp.setId("Save");
//        menuAbout = new Menu();
//        menuAbout.setText("About");
//        menuAbout.setId("Save");
//
//        menuBar = new MenuBar();
//        menuBar.getMenus().addAll(menuSave, menuHelp, menuAbout);
//    }
//
//    private void makeSaveBtn(GTModel model) {
//        menuSave = new Menu();
//        menuSave.setId("Save");
//        menuSave.setText("Save");
//        menuSave.setOnAction(ActionEvent -> {
//            model.saveCouses();
//        });
//    }
//
//
//    public MenuBar getMenuBar() {
//        return menuBar;
//    }
//
//}
