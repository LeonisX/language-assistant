package md.leonis.assistant.utils.fsm.state;

public interface IProgressState extends IState {

    String getDescription();

    void setDescription(final String description);

}
