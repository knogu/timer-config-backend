package timerConfig;

public class TimerConfig {
    public Integer focusLength;
    public Integer shortBreakLength;
    public Integer longBreakLength;
    public Integer focusCntBeforeLongBreak;

    public TimerConfig(Integer focusLength, Integer shortBreakLength, Integer longBreakLength,
                       Integer focusCntBeforeLongBreak) {
        this.focusLength = focusLength;
        this.shortBreakLength = shortBreakLength;
        this.longBreakLength = longBreakLength;
        this.focusCntBeforeLongBreak = focusCntBeforeLongBreak;
    }
}
