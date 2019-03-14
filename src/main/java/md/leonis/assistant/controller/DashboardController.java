package md.leonis.assistant.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import md.leonis.assistant.view.FxmlView;
import md.leonis.assistant.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
public class DashboardController {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    private void initialize() {
    }

    public void wordBankShow(ActionEvent actionEvent) {
        stageManager.showNewWindow(FxmlView.WORD_BANK);
    }

    public void watchVideoShow(ActionEvent actionEvent) {
        stageManager.showNewWindow(FxmlView.WATCH_VIDEO);
    }
}
