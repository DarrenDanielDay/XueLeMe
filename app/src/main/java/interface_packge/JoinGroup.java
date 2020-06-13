package interface_packge;

public interface JoinGroup extends RequestInterface {
    public void GroupNotExist();
    public void AlreadyInGroup();
    public void HasRequested();//已经申请过了
    public void JSON_ERROR();
}
