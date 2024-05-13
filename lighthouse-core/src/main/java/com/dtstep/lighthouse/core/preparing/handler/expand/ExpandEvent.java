package com.dtstep.lighthouse.core.preparing.handler.expand;

import java.io.Serializable;

public class ExpandEvent implements Serializable {

    private int slot;

    private String message;

    private int repeat;

    public ExpandEvent(int slot,String message,int repeat){
        this.slot = slot;
        this.message = message;
        this.repeat = repeat;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
}
