package com.dtstep.lighthouse.common.util;

public class StopWatch {
    private static final long NANO_2_MILLIS = 1000000L;
    private StopWatch.State runningState;
    private StopWatch.SplitState splitState;
    private long startTime;
    private long startTimeMillis;
    private long stopTime;

    public StopWatch() {
        this.runningState = StopWatch.State.UNSTARTED;
        this.splitState = StopWatch.SplitState.UNSPLIT;
    }

    public void start() {
        if (this.runningState == StopWatch.State.STOPPED) {
            throw new IllegalStateException("Stopwatch must be reset before being restarted. ");
        } else if (this.runningState != StopWatch.State.UNSTARTED) {
            throw new IllegalStateException("Stopwatch already started. ");
        } else {
            this.startTime = System.nanoTime();
            this.startTimeMillis = System.currentTimeMillis();
            this.runningState = StopWatch.State.RUNNING;
        }
    }

    public void stop() {
        if (this.runningState != StopWatch.State.RUNNING && this.runningState != StopWatch.State.SUSPENDED) {
            throw new IllegalStateException("Stopwatch is not running. ");
        } else {
            if (this.runningState == StopWatch.State.RUNNING) {
                this.stopTime = System.nanoTime();
            }

            this.runningState = StopWatch.State.STOPPED;
        }
    }

    public void reset() {
        this.runningState = StopWatch.State.UNSTARTED;
        this.splitState = StopWatch.SplitState.UNSPLIT;
    }

    public void split() {
        if (this.runningState != StopWatch.State.RUNNING) {
            throw new IllegalStateException("Stopwatch is not running. ");
        } else {
            this.stopTime = System.nanoTime();
            this.splitState = StopWatch.SplitState.SPLIT;
        }
    }

    public void unsplit() {
        if (this.splitState != StopWatch.SplitState.SPLIT) {
            throw new IllegalStateException("Stopwatch has not been split. ");
        } else {
            this.splitState = StopWatch.SplitState.UNSPLIT;
        }
    }

    public void suspend() {
        if (this.runningState != StopWatch.State.RUNNING) {
            throw new IllegalStateException("Stopwatch must be running to suspend. ");
        } else {
            this.stopTime = System.nanoTime();
            this.runningState = StopWatch.State.SUSPENDED;
        }
    }

    public void resume() {
        if (this.runningState != StopWatch.State.SUSPENDED) {
            throw new IllegalStateException("Stopwatch must be suspended to resume. ");
        } else {
            this.startTime += System.nanoTime() - this.stopTime;
            this.runningState = StopWatch.State.RUNNING;
        }
    }

    public long getTime() {
        return this.getNanoTime() / 1000000L;
    }

    public long getNanoTime() {
        if (this.runningState != StopWatch.State.STOPPED && this.runningState != StopWatch.State.SUSPENDED) {
            if (this.runningState == StopWatch.State.UNSTARTED) {
                return 0L;
            } else if (this.runningState == StopWatch.State.RUNNING) {
                return System.nanoTime() - this.startTime;
            } else {
                throw new RuntimeException("Illegal running state has occurred.");
            }
        } else {
            return this.stopTime - this.startTime;
        }
    }

    public long getSplitTime() {
        return this.getSplitNanoTime() / 1000000L;
    }

    public long getSplitNanoTime() {
        if (this.splitState != StopWatch.SplitState.SPLIT) {
            throw new IllegalStateException("Stopwatch must be split to get the split time. ");
        } else {
            return this.stopTime - this.startTime;
        }
    }

    public long getStartTime() {
        if (this.runningState == StopWatch.State.UNSTARTED) {
            throw new IllegalStateException("Stopwatch has not been started");
        } else {
            return this.startTimeMillis;
        }
    }

    public boolean isStarted() {
        return this.runningState.isStarted();
    }

    public boolean isSuspended() {
        return this.runningState.isSuspended();
    }

    public boolean isStopped() {
        return this.runningState.isStopped();
    }

    private static enum SplitState {
        SPLIT,
        UNSPLIT;

        private SplitState() {
        }
    }

    private static enum State {
        UNSTARTED {
            boolean isStarted() {
                return false;
            }

            boolean isStopped() {
                return true;
            }

            boolean isSuspended() {
                return false;
            }
        },
        RUNNING {
            boolean isStarted() {
                return true;
            }

            boolean isStopped() {
                return false;
            }

            boolean isSuspended() {
                return false;
            }
        },
        STOPPED {
            boolean isStarted() {
                return false;
            }

            boolean isStopped() {
                return true;
            }

            boolean isSuspended() {
                return false;
            }
        },
        SUSPENDED {
            boolean isStarted() {
                return true;
            }

            boolean isStopped() {
                return false;
            }

            boolean isSuspended() {
                return true;
            }
        };

        private State() {
        }

        abstract boolean isStarted();

        abstract boolean isStopped();

        abstract boolean isSuspended();
    }
}
