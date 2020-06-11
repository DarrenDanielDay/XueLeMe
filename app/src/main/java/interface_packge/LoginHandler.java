package interface_packge;

public interface LoginHandler {
    public void password_correct();
    public void connection_failed();
    public void password_wrong();
    public void account_isnull();
    public void password_isnull();
    public void JSON_error();
}
