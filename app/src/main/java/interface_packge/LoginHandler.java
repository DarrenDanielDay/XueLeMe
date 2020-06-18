package interface_packge;

public interface LoginHandler {
    public void passwordCorrect();
    public void connectionFailed();
    public void passwordWrong();
    public void accountIsnull();
    public void passwordIsnull();
    public void JSONError();
}
