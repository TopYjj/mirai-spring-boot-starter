package cn.graydove.robot.mirai.exception;

public class NoSuchFileException extends RuntimeException {
    public NoSuchFileException() {
        super("Can not find file");
    }
}
