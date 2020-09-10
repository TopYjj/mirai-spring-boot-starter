package cn.graydove.robot.mirai.exception;

public class CloseException extends RuntimeException {
    public CloseException() {
        super("机器人关闭");
    }
}
