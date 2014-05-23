package ru.spbau.mit.repl.error;

public class ReplLocationError {
    private final Location from;
    private final Location to;

    private final String msg;

    public ReplLocationError(Location from, Location to, String msg) {
        this.from = from;
        this.to = to != null ? to : from;
        this.msg = msg;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReplLocationError that = (ReplLocationError) o;

        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;

        return true;
    }
}
