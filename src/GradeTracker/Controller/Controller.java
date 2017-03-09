package GradeTracker.Controller;

import GradeTracker.GTModel;
import GradeTracker.Overviews.MainDisplay;

/**
 * Created by michelsd on 3/9/17.
 */
public class Controller implements ControllerInterface {
    GTModel model;
    MainDisplay view;

    public Controller() {
        this.model = model;
        view = new MainDisplay();
        //view = new MainDisplay(this, model)
    }

    public void onCourseClicked() {
        ;
    }
}




//
//public class BeatController implements ControllerInterface {
//    BeatModelInterface model;
//    DJView view;
//
//    public BeatController(BeatModelInterface model) {
//        this.model = model;
//        view = new DJView(this, model);
//        view.createView();
//        view.createControls();
//        view.disableStopMenuItem();
//        view.enableStartMenuItem();
//        model.initialize();
//    }
//
//    public void start() {
//        model.on();
//        view.disableStartMenuItem();
//        view.enableStopMenuItem();
//    }
//
//    public void stop() {
//        model.off();
//        view.disableStopMenuItem();
//        view.enableStartMenuItem();
//    }
//
//    public void increaseBPM() {
//        int bpm = model.getBPM();
//        model.setBPM(bpm + 1);
//    }
//
//    public void decreaseBPM() {
//        int bpm = model.getBPM();
//        model.setBPM(bpm - 1);
//    }
//
//    public void setBPM(int bpm) {
//        model.setBPM(bpm);
//    }
//}
