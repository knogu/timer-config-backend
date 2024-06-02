package timerConfig;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class TimerConfig {
    public Integer focusLength;
    public Integer shortBreakLength;
    public Integer longBreakLength;
    public Integer focusCntBeforeLongBreak;
    public Integer goalMinutesPerDay;

    @JsonCreator
    public TimerConfig(@JsonProperty("focusLength") Integer focusLength,
                       @JsonProperty("shortBreakLength") Integer shortBreakLength,
                       @JsonProperty("longBreakLength") Integer longBreakLength,
                       @JsonProperty("focusCntBeforeLongBreak") Integer focusCntBeforeLongBreak,
                       @JsonProperty("goalMinutesPerDay") Integer goalMinutesPerDay) {
        this.focusLength = focusLength;
        this.shortBreakLength = shortBreakLength;
        this.longBreakLength = longBreakLength;
        this.focusCntBeforeLongBreak = focusCntBeforeLongBreak;
        this.goalMinutesPerDay = goalMinutesPerDay;
    }
}
